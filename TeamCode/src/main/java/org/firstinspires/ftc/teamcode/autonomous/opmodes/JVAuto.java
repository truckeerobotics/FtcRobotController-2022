package org.firstinspires.ftc.teamcode.autonomous.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Robot2;

@Autonomous(name = "JV Autonomous")
public class JVAuto extends LinearOpMode {
    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    public void runOpMode() throws InterruptedException{
        motorFrontLeft = hardwareMap.dcMotor.get("mFL");
        motorBackLeft = hardwareMap.dcMotor.get("mBL");
        motorFrontRight = hardwareMap.dcMotor.get("mFR");
        motorBackRight = hardwareMap.dcMotor.get("mBR");

        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        motorFrontLeft.setPower(-0.5);
        motorBackLeft.setPower(-0.5);
        motorFrontRight.setPower(0.5);
        motorBackRight.setPower(0.5);
        while (!isStopRequested() && getRuntime() < 3
        ) {

        }
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
    }
}
