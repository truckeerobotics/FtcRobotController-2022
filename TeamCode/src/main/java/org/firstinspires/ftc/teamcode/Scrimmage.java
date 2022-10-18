package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.driver.DriverInput;

@TeleOp(name = "Scrimmage")
public class Scrimmage extends LinearOpMode {
    public void runOpMode() throws InterruptedException{
        DcMotor leftMotor = hardwareMap.dcMotor.get("left_drive");
        DcMotor rightMotor = hardwareMap.dcMotor.get("right_drive");

        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        telemetry.setAutoClear(false);
        Telemetry.Item debugItem = telemetry.addData("OpMode>", "Waiting for Start");
        telemetry.update();

        waitForStart();

        debugItem.setValue("Main Loop is Running!");
        telemetry.update();

        while (!isStopRequested()) {
            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;
            double leftPower = Range.clip(drive + turn, -1.0, 1.0);
            double rightPower = Range.clip(drive - turn, -1.0, 1.0);
            leftMotor.setPower(leftPower);
            rightMotor.setPower(rightPower);
        }
    }
}
