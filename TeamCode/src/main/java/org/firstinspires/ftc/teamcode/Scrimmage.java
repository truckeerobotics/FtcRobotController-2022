package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Scrimmage")
public class Scrimmage extends LinearOpMode {
    public void runOpMode() throws InterruptedException{
        DcMotor leftMotor = hardwareMap.dcMotor.get("left_drive");
        DcMotor rightMotor = hardwareMap.dcMotor.get("right_drive");
        waitForStart();
        while (!isStopRequested()) {
            leftMotor.setPower(1);
            rightMotor.setPower(-1);
        }
    }
}
