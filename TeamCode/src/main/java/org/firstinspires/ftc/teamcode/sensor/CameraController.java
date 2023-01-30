package org.firstinspires.ftc.teamcode.sensor;


import static android.content.Context.CAMERA_SERVICE;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class CameraController {

    private final Handler.Callback cameraHandlerCallback = new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            telemetry.addData("Handle Message!", msg);
            return true;
        }
    };

    // Callback for camera device stuff
    private Handler cameraThreadHandler;

    CameraManager cameraManager;
    Context context;
    Telemetry telemetry;

    // Cameras
    HashMap<String, Camera> cameras = new HashMap<String, Camera>();

    // Telemetry Objects
    Telemetry.Item CameraStatus;

    // Gets all the cameras and opens them
    public boolean init(Context appContext, Telemetry robotTelemetry) {

        cameraThreadHandler = new Handler(Looper.getMainLooper(), cameraHandlerCallback);

        context = appContext;
        telemetry = robotTelemetry;
        cameraManager = (CameraManager) context.getSystemService(CAMERA_SERVICE);

        CameraStatus = telemetry.addData("Camera", "init");
        telemetry.update();
        try {
            String[] cameraIdList = cameraManager.getCameraIdList();


            int cameraIdCount = cameraIdList.length;
            telemetry.addData("Camera Count", cameraIdCount);
            telemetry.update();

            for (int cameraIndex = 0; cameraIndex < cameraIdCount; cameraIndex++) {
                String cameraId = cameraIdList[cameraIndex];

                Integer lensFacing = cameraManager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.LENS_FACING);
                telemetry.addData("Lens Facing", lensFacing);
                telemetry.addData("Camera:", "EXTERNAL, CONTINUING");

                createNewCameraObject(cameraId);
                telemetry.update();
            }
        } catch (CameraAccessException cameraException) {
            telemetry.addData("Failed", "Camera Exception");
            telemetry.addData("Exception", cameraException);
            cameraException.printStackTrace();
            telemetry.update();
        } catch (Exception exception) {
            telemetry.addData("Failed", "General Exception");
            telemetry.addData("Exception", exception);
            exception.printStackTrace();
            telemetry.update();
        }
        CameraStatus.setValue("Finished Init");
        telemetry.update();

        return true;
    }

    public Camera getCamera(@NonNull String cameraId) {
        return cameras.get(cameraId);
    }

    /// --------------------------------------------------------------------------- ///
    /// Functions ///
    /// --------------------------------------------------------------------------- ///

    public void createNewCameraObject(String cameraId) {
        Camera cameraObject = new Camera(cameraId, cameraManager, telemetry, context, cameraThreadHandler);
        cameras.put(cameraId, cameraObject);
        telemetry.addData("Created Camera of ID", cameraId);
        telemetry.update();
    }

    /// --------------------------------------------------------------------------- ///
    /// Callbacks ///
    /// --------------------------------------------------------------------------- ///


}