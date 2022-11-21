package org.firstinspires.ftc.teamcode;

import android.content.Context;
import android.media.Image;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
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
    public native int getSleeveLevel();
    public native void passImageBuffers(byte[] bufferY, byte[] bufferU, byte[] bufferV);

    public void runOpMode() throws InterruptedException {

        telemetry.setAutoClear(false);
        appContext = hardwareMap.appContext;



        telemetry.addData("before start", "prepare for death!");
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
        //cameraController.onOpModeStopped();
        telemetry.addData("Finished", "Completed");
        telemetry.update();
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

            telemetry.addData("Getting buffers", "Indeed");
            telemetry.update();
            Image.Plane[] imagePlanes = latestImage.getPlanes();
            byte[] yBuffer = planeToByteBuffer(imagePlanes[0]);
            byte[] uBuffer = planeToByteBuffer(imagePlanes[1]);
            byte[] vBuffer = planeToByteBuffer(imagePlanes[2]);
            passImageBuffers(yBuffer,uBuffer,vBuffer);
            telemetry.addData("SLEVE LEVEL", getSleeveLevel());
            telemetry.addData("Reformating image y", "Quite");
            telemetry.update();
            String bufferUnsignedInt8 = "";
            for (int i = 0; i < 500; i++) {
                int unsignedInt8y = yBuffer[i] & 0xFF;
                int unsignedInt8u = uBuffer[i] & 0xFF;
                int unsignedInt8v = vBuffer[i] & 0xFF;
                bufferUnsignedInt8 += "(" + Integer.toString(unsignedInt8y) + ";" + Integer.toString(unsignedInt8u) + ";" + Integer.toString(unsignedInt8v) + ";),";

            }
            telemetry.addData("DOING FILE THINGS", "YES");
            telemetry.update();
            String filename = "BRIGHTNESS_TEST_FILE.txt";
            File file = AppUtil.getInstance().getSettingsFile(filename);
            ReadWriteFile.writeFile(file, bufferUnsignedInt8);
        };
    };
}