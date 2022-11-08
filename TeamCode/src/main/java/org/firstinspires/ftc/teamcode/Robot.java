package org.firstinspires.ftc.teamcode;

import android.content.Context;
import android.media.Image;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.sensor.Camera;
import org.firstinspires.ftc.teamcode.sensor.CameraController;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

@TeleOp(name = "Main")
public class Robot extends LinearOpMode {



    Context appContext;

    static {
        System.loadLibrary("Nova");
    }
//
    public native void initSignalSleeveDetection();
    public native int getSleeveLevel();
    public native void passImageBuffers(float[] bufferY, float[] bufferU, float[] bufferV);

    public void runOpMode() throws InterruptedException {

        telemetry.setAutoClear(false);
        appContext = hardwareMap.appContext;



        telemetry.addData("before start", "prepare for death");
        telemetry.update();

        waitForStart();

        telemetry.clear();

        telemetry.addData("Starting", "Camera Instance");
        telemetry.update();

        CameraController cameraController = new CameraController();
        boolean result = cameraController.init(appContext, telemetry);

        Camera mainCamera = cameraController.getCameraByName("Front Camera");
        mainCamera.addCallbacks(cameraCallback);
        telemetry.addData("Result", result);
        telemetry.update();



        while (!isStopRequested()) {

        }
        telemetry.addData("Finished", "Completed");
        telemetry.update();
    }

    Camera.CameraCallback cameraCallback = new Camera.CameraCallback() {
        public void openCallback() {

        };
        public void failedCallback(int error) {

        };
        public void imageReadyCallback(Image latestImage) {
            Image.Plane[] imagePlanes = latestImage.getPlanes();
            ByteBuffer byteBuffer = imagePlanes[0].getBuffer();
            FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
            float[] floatArray = floatBuffer.array();
            telemetry.addData("Float Values", floatArray[0]);
            telemetry.addData("Float Value", floatArray[1]);
            telemetry.addData("Float Value", floatArray[2]);
            telemetry.addData("Float Value", floatArray[3]);
            telemetry.addData("Float Value", floatArray[4]);
            telemetry.update();
        };
    };
}