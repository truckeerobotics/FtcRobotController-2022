package org.firstinspires.ftc.teamcode;

import android.content.Context;
import android.media.Image;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.other.NativeLogging;
import org.firstinspires.ftc.teamcode.sensor.Camera;
import org.firstinspires.ftc.teamcode.sensor.CameraController;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@TeleOp(name = "Camera Main")
public class Robot extends LinearOpMode {



    Context appContext;

    static {
        System.loadLibrary("Nova");
    }
//
    public native String getSleeveLevel();
    public native void passImageBuffers(byte[] bufferY, byte[] bufferU, byte[] bufferV);

    private CameraController cameraController;

    public void runOpMode() throws InterruptedException {
        String cameraNameFront = hardwareMap.get("FrontCamera").getDeviceName();
        telemetry.addData("Device Name", cameraNameFront);
        telemetry.setAutoClear(false);
        appContext = hardwareMap.appContext;

        // Setup native logger
        new NativeLogging(telemetry);

        telemetry.addData("Init", "Init Successful - Prepare for Failure!");
        telemetry.update();


        waitForStart();
        telemetry.clear();



        telemetry.addData("Camera", "Setting up instance");
        telemetry.update();

        cameraController = new CameraController();
        cameraController.init(appContext, telemetry);
        Camera frontCamera = cameraController.getCamera("0");
        frontCamera.addCallbacks(cameraCallback);

        telemetry.addData("Camera", "Finished instance setup");
        telemetry.update();

        while (!isStopRequested()) {

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
            passImageBuffers(yBuffer,uBuffer,vBuffer);
            String sleeveLevel = getSleeveLevel();
            telemetry.addData("Sleeve Level", sleeveLevel);
            telemetry.update();

            String bufferUnsignedInt8 = "";
            String bufferUnsignedInt8RawY = "";
            String bufferUnsignedInt8RawU = "";
            String bufferUnsignedInt8RawV = "";
            for (int i = 0; i < 1000; i++) {
                int unsignedInt8y = yBuffer[i*4] & 0xFF;
                int unsignedInt8u = uBuffer[i*2] & 0xFF;
                int unsignedInt8v = vBuffer[i*2] & 0xFF;
                bufferUnsignedInt8 += "(" + Integer.toString(unsignedInt8y) + ";" + Integer.toString(unsignedInt8u) + ";" + Integer.toString(unsignedInt8v) + ";),";
                bufferUnsignedInt8RawY += unsignedInt8y + ",";
                bufferUnsignedInt8RawU += unsignedInt8u + ",";
                bufferUnsignedInt8RawV += unsignedInt8v + ",";
            }
            String fullDataString = bufferUnsignedInt8RawY + "|||" + bufferUnsignedInt8RawU + "|||" + bufferUnsignedInt8RawV + "###" + bufferUnsignedInt8;
            telemetry.addData("Camera Processor", "Writing Data File");
            telemetry.update();
            String filename = "BRIGHTNESS_TEST_FILE.txt";
            File file = AppUtil.getInstance().getSettingsFile(filename);
            ReadWriteFile.writeFile(file, fullDataString);
        };
    };
}