package org.firstinspires.ftc.teamcode.sensor;


import static android.content.Context.CAMERA_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Camera {

    // Callback for camera device stuff
    private final CameraDevice.StateCallback cameraStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            telemetry.addData("CameraOpened", "successfully opened camera!");
            telemetry.update();
            System.out.print("SUCCESSFULLY OPENED CAMERA!");
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            telemetry.addData("CameraDisconnected", "camera has been disconnected!");
            telemetry.update();
            System.out.print("CAMERA DISCONNECTED!");
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            telemetry.addData("CameraError", "failed to open camera!");
            telemetry.update();
            System.out.print("FAILED TO OPEN CAMERA!");
        }
    };

    private final Handler.Callback cameraHandlerCallback = new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            telemetry.addData("Handle Message!", msg);
            return true;
        };
    };

    // Callback for camera device stuff
    private Handler cameraHandler;

    CameraManager cameraManager;
    Context context;
    Telemetry telemetry;

    // Gets all the cameras and opens them
    public boolean init(Context appContext, Telemetry robotTelemetry) {
        cameraHandler = new Handler(Looper.getMainLooper(), cameraHandlerCallback);

        context = appContext;
        telemetry = robotTelemetry;
        cameraManager = (CameraManager) context.getSystemService(CAMERA_SERVICE);

        telemetry.addData("Camera", "init");
        telemetry.update();

        try {
            String[] cameraIdList = cameraManager.getCameraIdList();

            int cameraIdCount = cameraIdList.length;
            telemetry.addData("Camera Id Count", cameraIdCount);
            for (int cameraIndex = 0; cameraIndex < cameraIdCount; cameraIndex++) {
                String cameraId = cameraIdList[cameraIndex];
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                telemetry.addData("Camera", "Opening");
                if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    telemetry.addData("Camera Open - ID", cameraId);
                    cameraManager.openCamera(cameraId, cameraStateCallback, cameraHandler);
                } else {
                    // Does not have permission, weird? Should not happen, but just in case this check is here.
                    telemetry.addData("Camera" ,"lacks permission");
                    telemetry.update();
                    return false;
                }
            }

        } catch (CameraAccessException cameraException) {
            telemetry.addData("Failed come on", "Failed");
            cameraException.printStackTrace();
        } catch (Exception exception) {
            telemetry.addData("Failed bruh", "Failed");
            telemetry.addData("Exception", exception);
            exception.printStackTrace();
        }
        telemetry.addData("Camera", "finished init");
        telemetry.update();

        return true;
    }


}
