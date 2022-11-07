package org.firstinspires.ftc.teamcode.sensor;


import static android.content.Context.CAMERA_SERVICE;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;
import java.util.Map;

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

    // Since we are not in java 9 creating the map is a bit more ugly
    private static HashMap<String, String> createNameMap() {
        HashMap<String,String> cameraNameMap = new HashMap<String,String>();
        cameraNameMap.put("1", "Back Camera");
        cameraNameMap.put("0", "Front Camera");
        return cameraNameMap;
    }

    // Camera Names
    HashMap<String, String> cameraNames = createNameMap();

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
                String cameraName = "";
                if (cameraNames.containsKey(cameraId)) {
                    cameraName = cameraNames.get(cameraId);
                } else {
                    cameraName = "Unknown" + cameraId;
                }
                telemetry.addData("Name", cameraName);
                Camera cameraObject = new Camera(cameraId, cameraName, cameraManager, telemetry, context, cameraThreadHandler);
                telemetry.addData("camera is null?", cameraObject == null);
                telemetry.update();
                cameras.put(cameraId, cameraObject);
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



}