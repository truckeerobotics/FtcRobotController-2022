package org.firstinspires.ftc.teamcode.sensor;

import static org.firstinspires.ftc.teamcode.Robot.getSleeveLevel;
import static org.firstinspires.ftc.teamcode.Robot.passImageBuffers;
import static org.firstinspires.ftc.teamcode.Robot.yuvImageSaveJPEG;

import android.media.Image;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.other.NativeLogging;

import java.nio.ByteBuffer;

public class SleeveDetection {

    private int sleeveLevel = 0;
    private CameraController cameraController;
    private LinearOpMode opmode;

    public SleeveDetection(LinearOpMode opmode){
        this.opmode = opmode;
    }


    public int camera(){
        NativeLogging.initNativeLogging(opmode.telemetry);

        cameraController = new CameraController();
        cameraController.init(opmode.hardwareMap.appContext, opmode.telemetry);

        Camera frontCamera = cameraController.getCamera("0");
        frontCamera.addCallbacks(cameraCallback);

        opmode.resetRuntime();
        while (!opmode.isStopRequested() && opmode.getRuntime() < 5 && sleeveLevel == 0) {

        }

        frontCamera.shutdown();
        return sleeveLevel;
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
