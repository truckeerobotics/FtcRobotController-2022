package org.firstinspires.ftc.teamcode.driver;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.other.Vector2;
import org.firstinspires.ftc.teamcode.sensor.IMU;

public class Driver {
    LinearOpMode opmode;

    public Driver(LinearOpMode opmode){
        this.opmode = opmode;
    }

    // Constants for controlling the robot
    // Driving
    private final double LateralSpeed = -1;
    private final double StrafeSpeed = 1;
    private final double RotationalSpeed = 1;

    // Smart driving
    private final double drivingPowerForwardDelta = 0.025;
    private final double drivingPowerBackwardDelta = 0.02;
    private final double drivingPowerStrafeDelta = 0.05;
    private final double drivingPowerRotationDelta = 0.07;

    private final double drivingPowerDifferenceCutoff = 0.1;

    // Control variables used for making driving *smooth*.
    private double yTarget = 0;
    private double xTarget = 0;
    private double rxTarget = 0;

    private double yCurrent = 0;
    private double xCurrent = 0;
    private double rxCurrent = 0;

    public void run(){
        DcMotor motorFrontLeft = opmode.hardwareMap.dcMotor.get("motorFrontLeft");
        DcMotor motorBackLeft = opmode.hardwareMap.dcMotor.get("motorBackLeft");
        DcMotor motorFrontRight = opmode.hardwareMap.dcMotor.get("motorFrontRight");
        DcMotor motorBackRight = opmode.hardwareMap.dcMotor.get("motorBackRight");
        DcMotor arm = opmode.hardwareMap.dcMotor.get("arm");

        Servo coneHook = opmode.hardwareMap.servo.get("coneHook");
        Servo armSwing = opmode.hardwareMap.servo.get("armSwing");

        DriverInput input = new DriverInput(opmode.gamepad1, opmode.gamepad2);
        IMU imu = new IMU(opmode, "imu");

        boolean toggle = false, toggle2 = false, errorCorrection = true, debug = true;
        double swingPos = 0.25, hookPos = 0;

        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        coneHook.setPosition(hookPos);
        armSwing.setPosition(swingPos);

        opmode.telemetry.addData("STATUS", "Waiting for start");
        opmode.telemetry.update();

        opmode.waitForStart();

        while (!opmode.isStopRequested()) {

            opmode.telemetry.addData("debug", debug);

            if(input.onPush(opmode.gamepad1.x, "controller1ButtonX")) {
                errorCorrection = !errorCorrection;
            }

            if(input.onPush(opmode.gamepad1.y, "controller1ButtonY")) {
                debug  = !debug;
            }

            if(input.onPush(opmode.gamepad2.x, "controller2ButtonX")) {
                toggle = !toggle;
                if (toggle) {
                    hookPos = 0;
                } else {
                    hookPos = 0.3;
                }
            }

            if(input.onPush(opmode.gamepad2.y, "controller2ButtonY")){
                toggle2 = !toggle2;
                if (toggle2) {
                    swingPos = 0.75;
                } else {
                    swingPos = 0.25;
                }
            }

            arm.setPower(opmode.gamepad2.left_stick_y);

            yTarget = -opmode.gamepad1.left_stick_y * LateralSpeed; // Remember, this is reversed!
            xTarget = -opmode.gamepad1.left_stick_x * StrafeSpeed * 1.1; // Counteract imperfect strafing
            rxTarget = -opmode.gamepad1.right_stick_x * RotationalSpeed;


            if(debug) {
                opmode.telemetry.addData("x", xTarget);
                opmode.telemetry.addData("y", yTarget);
                opmode.telemetry.addData("rx", rxTarget);
            }

//            if(y != 0 && x == 0 && rx == 0){
//                motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
//                motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
//            }else{
//                motorFrontLeft.setDirection(DcMotor.Direction.FORWARD);
//                motorBackLeft.setDirection(DcMotor.Direction.FORWARD);
//            }

            if(errorCorrection) {
                Vector2 rot = imu.getHeadingCorrection(xTarget, yTarget);
                xTarget = rot.x;
                yTarget = rot.y;
                if (debug) {
                    opmode.telemetry.addData("rot", rot.toString());
                    opmode.telemetry.addData("new x", xTarget);
                    opmode.telemetry.addData("new y", yTarget);
                    opmode.telemetry.addData("new rx", rxTarget);
                }
            }





            // If difference cutoff is not reached, then move current/actual movement to target movement.
            if (!(Math.abs(yTarget - yCurrent) < drivingPowerDifferenceCutoff)) {
                if (yTarget > yCurrent) {
                    yCurrent += drivingPowerForwardDelta;
                } else {
                    yCurrent -= drivingPowerBackwardDelta;
                }
            } else {
                yCurrent = yTarget;
            }

            if (!(Math.abs(xTarget - xCurrent) < drivingPowerDifferenceCutoff)) {
                if (xTarget > xCurrent) {
                    xCurrent += drivingPowerStrafeDelta;
                } else {
                    xCurrent -= drivingPowerStrafeDelta;
                }
            } else {
                xCurrent = xTarget;
            }

            if (!(Math.abs(rxTarget - rxCurrent) < drivingPowerDifferenceCutoff)) {
                if (rxTarget > rxCurrent) {
                    rxCurrent += drivingPowerRotationDelta;
                } else {
                    rxCurrent -= drivingPowerRotationDelta;
                }
            } else {
                rxCurrent = rxTarget;
            }

            // Debug
            opmode.telemetry.addData("xTarget: ", xTarget);
            opmode.telemetry.addData("yTarget: ", yTarget);
            opmode.telemetry.addData("rxTarget: ", rxTarget);

            opmode.telemetry.addData("xCurrent: ", xCurrent);
            opmode.telemetry.addData("yCurrent: ", yCurrent);
            opmode.telemetry.addData("rxCurrent: ", rxCurrent);

            // Take things from smart/smooth driving and implement the intended axial changes.

            double y = yCurrent;
            double x = xCurrent;
            double rx = rxCurrent;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);

            coneHook.setPosition(hookPos);
            armSwing.setPosition(swingPos);

            if(debug){
                opmode.telemetry.addData("errorCorrection", errorCorrection);
                opmode.telemetry.addData("frontLeftPower", frontLeftPower);
                opmode.telemetry.addData("backLeftPower", backLeftPower);
                opmode.telemetry.addData("frontRightPower", frontRightPower);
                opmode.telemetry.addData("backRightPower", backRightPower);
                opmode.telemetry.addData("swingPos: ", swingPos);
                opmode.telemetry.addData("hookPos: ", hookPos);
                opmode.telemetry.addData("Hook: ", toggle);
                opmode.telemetry.addData("Swing: ", toggle2);
            }

            opmode.telemetry.update();
        }
    }




    public void runJV(){
        DcMotor motorFrontLeft = opmode.hardwareMap.dcMotor.get("mFL");
        DcMotor motorBackLeft = opmode.hardwareMap.dcMotor.get("mBL");
        DcMotor motorFrontRight = opmode.hardwareMap.dcMotor.get("mFR");
        DcMotor motorBackRight = opmode.hardwareMap.dcMotor.get("mBR");
        DcMotor motorArm = opmode.hardwareMap.dcMotor.get("arm");
        Servo servoArm = opmode.hardwareMap.servo.get("sArm");

        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);

        DriverInput input = new DriverInput(opmode.gamepad1, opmode.gamepad2);

        opmode.waitForStart();

        boolean speedToggle = false;

        while (!opmode.isStopRequested()) {


            if(input.onPush(opmode.gamepad1.x, "controller1ButtonX")) {
                speedToggle = !speedToggle;
            }


            double movementModifier = 0.55;
            if (speedToggle == true) {
                movementModifier = 1;
            }

            double x = opmode.gamepad1.left_stick_x*movementModifier;
            double y = opmode.gamepad1.left_stick_y*movementModifier;
            double rx = -opmode.gamepad1.right_stick_x*movementModifier;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);

            double armY = opmode.gamepad2.right_stick_y;
            motorArm.setPower(armY);

            double armX = opmode.gamepad2.left_stick_x;
            opmode.telemetry.addData("servoArm.getPosition()", servoArm.getPosition());
            opmode.telemetry.addData("armX", armX);
            if(armX > 0 && servoArm.getPosition() < 0.5) {
                servoArm.setPosition(servoArm.getPosition() + armX);
            }else if(armX < 0 && servoArm.getPosition() > 0.0){
                servoArm.setPosition(servoArm.getPosition() + armX);
            }

            opmode.telemetry.addData("ARM Y", armY);

            opmode.telemetry.addData("X", x);
            opmode.telemetry.addData("Y", y);
            opmode.telemetry.addData("RX", rx);

            opmode.telemetry.addData("Front Left Power", frontLeftPower);
            opmode.telemetry.addData("Back Left Power", backLeftPower);
            opmode.telemetry.addData("Front Right Power", frontRightPower);
            opmode.telemetry.addData("Back Right Power", backRightPower);

            opmode.telemetry.update();
        }
    }
}
