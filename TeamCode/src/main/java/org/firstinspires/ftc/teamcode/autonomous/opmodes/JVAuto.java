package org.firstinspires.ftc.teamcode.autonomous.opmodes;

import static org.firstinspires.ftc.teamcode.Robot.getSleeveLevel;
import static org.firstinspires.ftc.teamcode.Robot.passImageBuffers;
import static org.firstinspires.ftc.teamcode.Robot.yuvImageSaveJPEG;

import android.media.Image;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Robot2;
import org.firstinspires.ftc.teamcode.other.NativeLogging;
import org.firstinspires.ftc.teamcode.sensor.Camera;
import org.firstinspires.ftc.teamcode.sensor.CameraController;

import java.nio.ByteBuffer;

@Autonomous(name = "JV Autonomous")
public class JVAuto extends LinearOpMode {

    int sleeveLevel = 0;

    private CameraController cameraController;

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

        NativeLogging.initNativeLogging(telemetry);
        telemetry.setAutoClear(false);
        telemetry.addData("Log: ", "Init");
        telemetry.update();

        waitForStart();

        telemetry.addData("Log: ", "Start");
        telemetry.update();

        cameraController = new CameraController();
        cameraController.init(hardwareMap.appContext, telemetry);

        telemetry.addData("Log: ", "Camera Controller Init");
        telemetry.update();

        Camera frontCamera = cameraController.getCamera("0");
        frontCamera.addCallbacks(cameraCallback);

        setMotorsForwards(0.5f);

        telemetry.addData("Log: ", "Set Forward");
        telemetry.update();

        wait(0.97f);

        stopMotors();

        wait(5f);

        // turn -0.5
        setMotorsTurn(-0.5f);
        wait(0.25f);
        stopMotors();

        wait(5f);

        // turn 1
        setMotorsTurn(0.5f);
        wait(0.5f);
        stopMotors();

        wait(5f);

        //Turn -0.5
        setMotorsTurn(-0.5f);
        wait(0.25f);
        stopMotors();

        if (sleeveLevel == 0) {
            telemetry.addData("Log: ", "No result, waiting");
            telemetry.update();

            wait(4f);
        }

        setMotorsForwards(0.5f);
        wait(0.8f);
        stopMotors();



        telemetry.addData("Log: ", "Camera Shutdown");
        telemetry.update();

        frontCamera.shutdown();
        if (sleeveLevel == 3) {
            setMotorsStrafe(0.5f);
            wait(1.5f);
            stopMotors();
        } else if (sleeveLevel == 1) {
            setMotorsStrafe(-0.5f);
            wait(1.5f);
            stopMotors();
        }

    }

    private void stopMotors() {
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
    }

    private void setMotorsForwards(float speed){
        motorFrontLeft.setPower(-speed);
        motorBackLeft.setPower(-speed);
        motorFrontRight.setPower(speed);
        motorBackRight.setPower(speed);
    }

    private void setMotorsStrafe(float speed) {
        motorFrontLeft.setPower(speed);
        motorBackLeft.setPower(-speed);
        motorFrontRight.setPower(speed);
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

    Camera.CameraCallback cameraCallback = new Camera.CameraCallback() {
        public void openCallback() {

        };
        public void failedCallback(int error) {

        };
        private byte[] planeToByteBuffer(Image.Plane plane) {
            ByteBuffer byteBuffer = plane.getBuffer();
            byte[] byteBufferArray = new byte[byteBuffer.remaining()];
            byteBuffer.get(byteBufferArray);
            return byteBufferArray;
        }
        public void imageReadyCallback(Image latestImage) {
            telemetry.addData("Camera Processor", "Running");
            telemetry.update();
            Image.Plane[] imagePlanes = latestImage.getPlanes();
            byte[] yBuffer = planeToByteBuffer(imagePlanes[0]);
            byte[] uBuffer = planeToByteBuffer(imagePlanes[1]);
            byte[] vBuffer = planeToByteBuffer(imagePlanes[2]);

            yuvImageSaveJPEG(latestImage);
            passImageBuffers(yBuffer,uBuffer,vBuffer);
            int detectedSleeveLevel = getSleeveLevel();
            if (detectedSleeveLevel != -1) {
                telemetry.addData("Log: ", "SLEEVE LEVEL DETECTED");
                telemetry.update();
                sleeveLevel = detectedSleeveLevel;
            }
        };
    };
}
