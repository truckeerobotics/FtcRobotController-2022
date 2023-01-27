package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.Robot.getSleeveLevel;
import static org.firstinspires.ftc.teamcode.Robot.passImageBuffers;
import static org.firstinspires.ftc.teamcode.Robot.yuvImageSaveJPEG;

import android.app.Activity;
import android.graphics.Color;
import android.media.Image;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.movement.Movement;
import org.firstinspires.ftc.teamcode.other.NativeLogging;
import org.firstinspires.ftc.teamcode.sensor.Camera;
import org.firstinspires.ftc.teamcode.sensor.CameraController;
import org.firstinspires.ftc.teamcode.sensor.Encoder;

import java.nio.ByteBuffer;

public class Autonomous {
    LinearOpMode opmode;
    Movement move;
    Encoder motorBackLeftEncoder;
    Encoder motorBackRightEncoder;
    Encoder motorFrontLeftEncoder;
    Encoder motorFrontRightEncoder;

    private CameraController cameraController;

    int sleeveLevel = 0;

    public Autonomous(LinearOpMode opmode){
        this.opmode = opmode;
        this.move = new Movement(opmode, true);
        this.motorBackLeftEncoder = new Encoder(move.motorBackLeft);
        this.motorBackRightEncoder = new Encoder(move.motorBackRight);
        this.motorFrontLeftEncoder = new Encoder(move.motorFrontLeft);
        this.motorFrontRightEncoder = new Encoder(move.motorFrontRight);
    }


    public void camera(){



        NativeLogging.initNativeLogging(opmode.telemetry);
        opmode.telemetry.setAutoClear(false);

        opmode.telemetry.addData("STATUS", "Waiting for start");
        opmode.telemetry.update();
        opmode.waitForStart();

        cameraController = new CameraController();
        cameraController.init(opmode.hardwareMap.appContext, opmode.telemetry);

        Camera frontCamera = cameraController.getCamera("0");
        frontCamera.addCallbacks(cameraCallback);

        while (!opmode.isStopRequested() && opmode.getRuntime() < 5) {

        }

        frontCamera.shutdown();

        Encoder[] encoderArray = {motorBackLeftEncoder, motorBackRightEncoder, motorFrontLeftEncoder, motorFrontRightEncoder};

        move.driveInches(50, 0.75, encoderArray);
        move.stop();

        opmode.resetRuntime();
        while(opmode.getRuntime() < 3 && !opmode.isStopRequested ()){
            opmode.telemetry.addData("STATUS", "waiting...");
            opmode.telemetry.update();
        }

        opmode.resetRuntime();
        while(opmode.getRuntime() < 4 && !opmode.isStopRequested()){
            opmode.telemetry.addData("LEVEL", sleeveLevel);

            if(sleeveLevel == 1){
                move.strafeLeft(-0.5);
            } else if(sleeveLevel == 3){
                move.strafeLeft(0.5);


            }
            opmode.telemetry.update();
        }
        move.stop();
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
            opmode.telemetry.addData("Camera Processor", "Running");
            opmode.telemetry.update();
            Image.Plane[] imagePlanes = latestImage.getPlanes();
            byte[] yBuffer = planeToByteBuffer(imagePlanes[0]);
            byte[] uBuffer = planeToByteBuffer(imagePlanes[1]);
            byte[] vBuffer = planeToByteBuffer(imagePlanes[2]);

            yuvImageSaveJPEG(latestImage);
            passImageBuffers(yBuffer,uBuffer,vBuffer);
            int detectedSleeveLevel = getSleeveLevel();
            if (detectedSleeveLevel != -1) {
                opmode.telemetry.addData("Log: ", "SLEEVE LEVEL DETECTED");
                opmode.telemetry.update();
                sleeveLevel = detectedSleeveLevel;
            }
        };
    };
}
