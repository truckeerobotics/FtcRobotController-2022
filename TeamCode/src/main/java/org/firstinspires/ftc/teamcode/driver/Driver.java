package org.firstinspires.ftc.teamcode.driver;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Driver {
    LinearOpMode opmode;
    public Driver(LinearOpMode opmode){
        this.opmode = opmode;
    }

    public void run(){
        DcMotor leftMotor = opmode.hardwareMap.dcMotor.get("left_drive");
        DcMotor rightMotor = opmode.hardwareMap.dcMotor.get("right_drive");

        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        opmode.telemetry.setAutoClear(false);
        Telemetry.Item debugItem = opmode.telemetry.addData("OpMode", "Waiting for Start");
        opmode.telemetry.update();

        opmode.waitForStart();

        debugItem.setValue("Main Loop is Running!");
        opmode.telemetry.update();

        while (!opmode.isStopRequested()) {
            double drive = -opmode.gamepad1.left_stick_y;
            double turn = opmode.gamepad1.right_stick_x;
            double leftPower = Range.clip(drive + turn, -1.0, 1.0);
            double rightPower = Range.clip(drive - turn, -1.0, 1.0);
            leftMotor.setPower(leftPower);
            rightMotor.setPower(rightPower);
        }
    }
}
