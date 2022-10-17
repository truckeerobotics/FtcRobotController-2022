package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "Scrimmage")
public class Scrimmage extends LinearOpMode {
    public void runOpMode() throws InterruptedException{
        DcMotor leftMotor = hardwareMap.dcMotor.get("left_drive");
        DcMotor rightMotor = hardwareMap.dcMotor.get("right_drive");

        Boolean tankMode = false;

        rightMotor.setDirection(DcMotor.Direction.REVERSE);
        telemetry.setAutoClear(false);
        Telemetry.Item modeItem = telemetry.addData("Mode", "Tank");

        waitForStart();

        while (!isStopRequested()) {
            if(tankMode){
                modeItem.setValue("Tank");
                telemetry.update();
                leftMotor.setPower(gamepad1.left_stick_y);
                rightMotor.setPower(gamepad2.left_stick_y);
            }else{
                //No turning yet
                modeItem.setValue("Normal");
                telemetry.update();
                double power = gamepad1.left_stick_y;
                leftMotor.setPower(power);
                rightMotor.setPower(power);
            }
        }
    }
}
