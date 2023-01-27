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

            double x = -opmode.gamepad1.left_stick_x * 1.1;
            double y = opmode.gamepad1.left_stick_y;
            double rx = -opmode.gamepad1.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

            if(debug) {
                opmode.telemetry.addData("x", x);
                opmode.telemetry.addData("y", y);
                opmode.telemetry.addData("rx", rx);
            }

//            if(y != 0 && x == 0 && rx == 0){
//                motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
//                motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
//            }else{
//                motorFrontLeft.setDirection(DcMotor.Direction.FORWARD);
//                motorBackLeft.setDirection(DcMotor.Direction.FORWARD);
//            }

            if(errorCorrection) {
                Vector2 rot = imu.getHeadingCorrection(x, y);
                x = rot.x;
                y = rot.y;
                if (debug) {
                    opmode.telemetry.addData("rot", rot.toString());
                    opmode.telemetry.addData("new x", x);
                    opmode.telemetry.addData("new y", y);
                    opmode.telemetry.addData("new rx", rx);
                }
            }

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


            double movementModifier = 0.7;
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
