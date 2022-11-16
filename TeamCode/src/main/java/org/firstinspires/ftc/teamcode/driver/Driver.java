package org.firstinspires.ftc.teamcode.driver;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Driver {
    LinearOpMode opmode;

    private final double LateralSpeed = 0.6;
    private final double RotationalSpeed = 0.7;
    private final double StrafeSpeed = 1;

    private final double drivingPowerForwardDelta = 0.02;
    private final double drivingPowerBackwardDelta = 0.0175;
    private final double drivingPowerDifferenceCutoff = 0.1;
    private final double drivingPowerStrafeDelta = 0.02;
    private final double drivingPowerRotationDelta = 0.04;

    private double yCurrent = 0;
    private double xCurrent = 0;
    private double rxCurrent = 0;

    private double yTarget = 0;
    private double xTarget = 0;
    private double rxTarget = 0;

    public Driver(LinearOpMode opmode){
        this.opmode = opmode;
    }

    public void run(){
        DcMotor motorFrontLeft = opmode.hardwareMap.dcMotor.get("motorFrontLeft");
        DcMotor motorBackLeft = opmode.hardwareMap.dcMotor.get("motorBackLeft");
        DcMotor motorFrontRight = opmode.hardwareMap.dcMotor.get("motorFrontRight");
        DcMotor motorBackRight = opmode.hardwareMap.dcMotor.get("motorBackRight");
        DcMotor armLeft = opmode.hardwareMap.dcMotor.get("armLeft");
        DcMotor armRight = opmode.hardwareMap.dcMotor.get("armRight");

        Servo coneHook = opmode.hardwareMap.servo.get("coneHook");
        Servo armSwing = opmode.hardwareMap.servo.get("armSwing");

        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        armLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        coneHook.setPosition(0);
        armSwing.setPosition(0.5);



        opmode.waitForStart();

        DriverInput input = new DriverInput(opmode.gamepad1, opmode.gamepad2);
        Boolean toggle = false;
        Boolean toggle2 = false;
        double swingPos = 0;
        double hookPos = 0;
        while (!opmode.isStopRequested()) {
/*            opmode.telemetry.addData("toggle", toggle);
            opmode.telemetry.addData("gamepad1x", opmode.gamepad1.x);
            opmode.telemetry.update();
            */

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
                    swingPos = 0.05;
                }
            }



            if(!toggle2){
                armLeft.setPower(opmode.gamepad2.left_stick_y);
                armRight.setPower(opmode.gamepad2.left_stick_y);
            }

            double x = -opmode.gamepad1.left_stick_x;
            double y = -opmode.gamepad1.left_stick_y;
            double rx = opmode.gamepad1.right_stick_x;

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

            opmode.telemetry.addData("swingPos: ", swingPos);
            opmode.telemetry.addData("hookPos: ", hookPos);
            opmode.telemetry.addData("Hook: ", toggle);
            opmode.telemetry.addData("Swing: ", toggle2);

            opmode.telemetry.update();
        }
    }
}
