package org.firstinspires.ftc.teamcode.driver;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
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

        /*
        UNCOMMENT WHEN MECHANUM SWITCH IS MADE

        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");

        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);

        also delete the below
        */

        DcMotor leftMotor = opmode.hardwareMap.dcMotor.get("left_drive");
        DcMotor rightMotor = opmode.hardwareMap.dcMotor.get("right_drive");

        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        opmode.waitForStart();

        DriverInput input = new DriverInput(opmode.gamepad1, opmode.gamepad2);
        Boolean toggle = false;
        while (!opmode.isStopRequested()) {
            opmode.telemetry.addData("toggle", toggle);
            opmode.telemetry.addData("gamepad1x", opmode.gamepad1.x);
            opmode.telemetry.update();
            if(input.onPush(opmode.gamepad1.x, "controller1ButtonX")){
                toggle = !toggle;
            }

            double y = yCurrent;
            double x = xCurrent;
            double rx = rxCurrent;

            // Smart/Smooth driving logic

            yTarget = -opmode.gamepad1.left_stick_y * LateralSpeed; // Remember, this is reversed!
            rxTarget = -opmode.gamepad1.right_stick_x * RotationalSpeed;

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

            if (!(Math.abs(rxTarget - rxCurrent) < drivingPowerDifferenceCutoff)) {
                if (rxTarget > rxCurrent) {
                    rxCurrent += drivingPowerRotationDelta;
                } else {
                    rxCurrent -= drivingPowerRotationDelta;
                }
            } else {
                rxCurrent = rxTarget;
            }

            double drive = yCurrent;
            double turn = -rxCurrent;
            double denominator = Math.max(Math.abs(y) + Math.abs(rx), 1);
            double leftPower = Range.clip(drive + turn, -1.0, 1.0);
            double rightPower = Range.clip(drive - turn, -1.0, 1.0);
            leftMotor.setPower(leftPower/denominator);
            rightMotor.setPower(rightPower/denominator);

            //FUTURE DRIVE CODE

            //--------------------------------------------------------

            /*
            yTarget = -opmode.gamepad1.left_stick_y * LateralSpeed; // Remember, this is reversed!
            xTarget = -opmode.gamepad1.left_stick_x * StrafeSpeed * 1.1; // Counteract imperfect strafing
            rxTarget = -opmode.gamepad1.right_stick_x * RotationalSpeed;

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

            //uncomment later --------------------------------------------------------------------------------

//            motorFrontLeft.setPower(frontLeftPower);
//            motorBackLeft.setPower(backLeftPower);
//            motorFrontRight.setPower(frontRightPower);
//            motorBackRight.setPower(backRightPower);
             */
        }
    }
}
