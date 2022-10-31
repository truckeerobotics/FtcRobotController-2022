package org.firstinspires.ftc.teamcode.driver;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
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


            yTarget = -opmode.gamepad1.left_stick_y * LateralSpeed;
            rxTarget = -opmode.gamepad1.right_stick_x * RotationalSpeed;

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
        }
    }
}
