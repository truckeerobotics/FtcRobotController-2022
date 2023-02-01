package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.Robot.getSleeveLevel;
import static org.firstinspires.ftc.teamcode.Robot.passImageBuffers;
import static org.firstinspires.ftc.teamcode.Robot.yuvImageSaveJPEG;

import android.media.Image;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Robot2;
import org.firstinspires.ftc.teamcode.movement.Movement;
import org.firstinspires.ftc.teamcode.other.NativeLogging;
import org.firstinspires.ftc.teamcode.sensor.Camera;
import org.firstinspires.ftc.teamcode.sensor.CameraController;
import org.firstinspires.ftc.teamcode.sensor.Encoder;
import org.firstinspires.ftc.teamcode.sensor.SleeveDetection;

import java.nio.ByteBuffer;

@Autonomous(name = "JV Autonomous")
public class JVAuto extends LinearOpMode {
    private CameraController cameraController;

    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;
    DcMotor motorFrontRight;
    DcMotor motorBackRight;

    Encoder motorBackLeftEncoder;
    Encoder motorBackRightEncoder;
    Encoder motorFrontLeftEncoder;
    Encoder motorFrontRightEncoder;

    public boolean debug = false;



    public void runOpMode() throws InterruptedException{
        motorFrontLeft = hardwareMap.dcMotor.get("mFL");
        motorBackLeft = hardwareMap.dcMotor.get("mBL");
        motorFrontRight = hardwareMap.dcMotor.get("mFR");
        motorBackRight = hardwareMap.dcMotor.get("mBR");

        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        double modification_value = 0.025;

        this.motorBackLeftEncoder = new Encoder(motorBackLeft, modification_value);
        this.motorBackRightEncoder = new Encoder(motorBackRight, modification_value);
        this.motorFrontLeftEncoder = new Encoder(motorFrontLeft, modification_value);
        this.motorFrontRightEncoder = new Encoder(motorFrontRight, modification_value);

        Encoder[] encoderArray = {motorBackLeftEncoder, motorBackRightEncoder, motorFrontLeftEncoder, motorFrontRightEncoder};

        SleeveDetection s = new SleeveDetection(this);
        int sleeveLevel = s.camera();

        double[] forwardSpeedArray = new double[]{-1, -1, 1, 1};

        driveInches(24, encoderArray, forwardSpeedArray, 1);

        wait(0.5f);

        motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        if (sleeveLevel == 3) {
            telemetry.addData("3","3");
            telemetry.update();
            setMotorsStrafe(0.2f);
            wait(5f);
        } else if (sleeveLevel == 1) {
            telemetry.addData("1","1");
            telemetry.update();
            setMotorsStrafe(-0.2f);
            wait(5f);
        }

        telemetry.addData("ENDED",sleeveLevel);
        telemetry.update();


    }

    private void stopMotors() {
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
    }

    private void setMotorsForwards(float speed){
        motorFrontLeft.setPower(speed);
        motorBackLeft.setPower(speed);
        motorFrontRight.setPower(speed);
        motorBackRight.setPower(speed);
    }

    private void setMotorsStrafe(float speed) {
        motorFrontLeft.setPower(speed);
        motorBackLeft.setPower(speed);
        motorFrontRight.setPower(-speed);
        motorBackRight.setPower(-speed);
    }

    private void setMotorsTurn(float speed) {
        motorFrontLeft.setPower(speed);
        motorBackLeft.setPower(speed);
        motorFrontRight.setPower(speed);
        motorBackRight.setPower(speed);
    }

    private void wait(float time) {
        resetRuntime();
        while (!isStopRequested() && getRuntime() < time) {

        }
    }


    private Boolean checkEncoders(Encoder[] encoders, int inches){
        Boolean running = true;
        if(debug){
            telemetry.addData("STATUS", "Encoder debug");
            telemetry.addData("Target", inches);
        }
        for(int i=0; i<encoders.length; i++){
            if(encoders[i].getDifference() > inches){
                running = false;
            }
            if(debug){
                telemetry.addData(i + "", encoders[i].getDifference());
            }
        }
        telemetry.update();
        return running;
    }

    private void driveInches(int inches, Encoder[] encoders, double[] speeds, double speed){
        for(int i=0; i<encoders.length; i++){
            encoders[i].reset();
        }
        motorBackLeft.setPower(speeds[0]*speed);
        motorBackRight.setPower(speeds[1]*speed);
        motorFrontLeft.setPower(speeds[2]*speed);
        motorFrontRight.setPower(speeds[3]*speed);
        telemetry.addData("SPEED 0", speeds[0]*speed);
        telemetry.addData("SPEED 1", speeds[1]*speed);
        telemetry.addData("SPEED 2", speeds[2]*speed);
        telemetry.addData("SPEED 3", speeds[3]*speed);
        while(checkEncoders(encoders, inches) && !isStopRequested()) {

        }
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);
    }
}
