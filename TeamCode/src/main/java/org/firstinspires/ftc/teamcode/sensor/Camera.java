package org.firstinspires.ftc.teamcode.sensor;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.SessionConfiguration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



/// External Methods: addCallbacks and getLatestImage

public class Camera {

    int IMAGE_FORMAT = ImageFormat.YUV_420_888;

    private CameraCharacteristics characteristics;
    private StreamConfigurationMap streamConfigurationMap;
    private ImageReader imageReader;
    private Surface imageSurface;
    private Size maxImageSize;
    private String name;
    private CameraDevice cameraDevice;
    private CaptureRequest.Builder captureSessionRequestBuilder;

    private Telemetry telemetry;
    private CameraManager cameraManager;
    private Handler threadHandler;

    public Camera(String cameraId, String name, CameraManager cameraManager, Telemetry opmodeTelemetry, Context appContext, Handler threadHandler) {
        this.telemetry = opmodeTelemetry;
        this.cameraManager = cameraManager;
        this.threadHandler = threadHandler;
        this.name = name;

        try {
            this.characteristics = cameraManager.getCameraCharacteristics(cameraId);
        } catch (CameraAccessException exception) {
            telemetry.addData("Failed", "CameraAccessException Exception");
            telemetry.addData("Failed Getting Camera Characteristics", exception);
            telemetry.update();
            exception.printStackTrace();
            return;
        }

        //-------------------------------------------------//

        StreamConfigurationMap cameraStreamConfigurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        telemetry.addData("Log", "Getting biggest output size");
        telemetry.update();

        // Compares sizes and get biggest camera can output (by area)
        Size cameraMaxOutputSize = Collections.max(Arrays.asList(cameraStreamConfigurationMap.getOutputSizes(IMAGE_FORMAT)), new CompareSizesByArea());

        telemetry.addData("Log", "Got biggest output size");
        telemetry.update();

        ImageReader imageReader = ImageReader.newInstance(cameraMaxOutputSize.getWidth(), cameraMaxOutputSize.getHeight(), IMAGE_FORMAT, 1);
        imageReader.setOnImageAvailableListener(imageReaderListener, threadHandler);

        Surface imageSurface = imageReader.getSurface();

        telemetry.addData("Is NULL?!", imageSurface == null);
        telemetry.update();

        this.imageSurface = imageSurface;
        this.maxImageSize = cameraMaxOutputSize;
        this.imageReader = imageReader;
        this.streamConfigurationMap = cameraStreamConfigurationMap;

        //-------------------------------------------------//

        // Check permissions, open the camera and handle any errors
        if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            telemetry.addData("Opening Camera - ID", cameraId);
            telemetry.update();
            try {
                cameraManager.openCamera(cameraId, stateCallback, threadHandler);
            } catch (CameraAccessException exception) {
                telemetry.addData("Failed", "CameraAccessException Exception");
                telemetry.addData("Failed Opening Camera", exception);
                telemetry.update();
                exception.printStackTrace();
                return;
            }

        } else {
            // Does not have permission, weird? Should not happen, but just in case this check is here.
            telemetry.addData("Camera" ,"Lacks Permission");
            telemetry.update();
            return;
        }

        //-------------------------------------------------//

        telemetry.addData("Finished Camera Init", "Success?");
        telemetry.update();
    }

    /// --------------------------------------------------------------------------- ///
    /// Functions ///
    /// --------------------------------------------------------------------------- ///

    public Image getLatestImage() {
        return imageReader.acquireLatestImage();
    }

    /// --------------------------------------------------------------------------- ///
    /// Callbacks ///
    /// --------------------------------------------------------------------------- ///

    // Implement this for a callback
    interface CameraCallback {
        void openCallback();
        void failedCallback(int error);
        void imageReadyCallback(Image latestImage);
    }

    private ArrayList<CameraCallback> cameraCallbacks = new ArrayList<CameraCallback>();

    // Add callbacks
    public void addCallbacks(CameraCallback callback) {
        cameraCallbacks.add(callback);
    }

    private Telemetry.Item ImageAcquired;

    // Listener for when new images are ready
    private final ImageReader.OnImageAvailableListener imageReaderListener = new ImageReader.OnImageAvailableListener() {
        public void onImageAvailable(ImageReader imageReader) {
            Image latestImage = imageReader.acquireLatestImage();
            if (ImageAcquired == null){
                ImageAcquired = telemetry.addData("New Image Acquired At","NULL");
            }
            ImageAcquired.setValue(latestImage.getTimestamp());
            telemetry.addData("Image", "New Image has been collected");
            telemetry.update();

            for (CameraCallback callback: cameraCallbacks) {
                callback.imageReadyCallback(latestImage);
            }
            //latestImage.close();
        }
    };

    private CameraCaptureSession.CaptureCallback imageCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            telemetry.addData("Capture", "Completed Capture");
            telemetry.update();
        }
    };

    private CameraCaptureSession.StateCallback captureStateCallback = new CameraCaptureSession.StateCallback(){
        @Override
        public void onConfigureFailed(CameraCaptureSession captureSession) {
            telemetry.addData("Capture", "Session Configure Failed!");
            telemetry.update();
            Log.i("Capture", "Session Configure Failed!");
        }
        @Override public void onConfigured(CameraCaptureSession captureSession) {
            telemetry.addData("Capture", "Session Configured!");
            telemetry.update();
            try {
                captureSession.setRepeatingRequest(captureSessionRequestBuilder.build(), imageCaptureCallback, threadHandler);
            } catch (CameraAccessException exception) {
                telemetry.addData("Failed", "CameraAccessException Exception");
                telemetry.addData("Failed to Start Capture!", exception);
                telemetry.update();
                exception.printStackTrace();
            }

        }
    };

    /// --------------------------------------------------------------------------- ///

    // Callback for camera open
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice openCameraDevice) {
            cameraDevice = openCameraDevice;
            telemetry.addData("CameraOpened", "successfully opened camera! " + cameraDevice.getId());
            telemetry.update();
            System.out.print("Successfully Opened Camera. ID: " + cameraDevice.getId());

            try {
                CaptureRequest.Builder captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                captureRequestBuilder.addTarget(imageSurface);
                List<Surface> surfaceList = new ArrayList<Surface>();
                surfaceList.add(imageSurface);
                Log.i("Image Surface", imageSurface.toString());
                cameraDevice.createCaptureSession(surfaceList, captureStateCallback, threadHandler);
                captureSessionRequestBuilder = captureRequestBuilder;

                telemetry.addData("Log", "Created capture session");
                telemetry.update();
            } catch (CameraAccessException exception) {
                telemetry.addData("Failed", "CameraAccessException Exception");
                telemetry.addData("Failed Creating Capture Session or Capture Session Builder", exception);
                telemetry.update();
                exception.printStackTrace();
                return;
            }

            for (CameraCallback callback: cameraCallbacks) {
                callback.openCallback();
            }
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            telemetry.addData("CameraDisconnected", "camera has been disconnected! " + cameraDevice.getId());
            telemetry.update();
            System.out.print("CAMERA DISCONNECTED! ID: " + cameraDevice.getId());
            // TODO: Implement reconnection!

            for (CameraCallback callback: cameraCallbacks) {
                callback.failedCallback(-1);
            }
        }

        @Override
        public void onError(CameraDevice cameraDevice, int error) {
            telemetry.addData("CameraError", "Error has occurred on camera! " + cameraDevice.getId());
            telemetry.addData("CameraError Code", error);
            telemetry.update();
            System.out.print("ERROR ON CAMERA! ID: " + cameraDevice.getId());
            for (CameraCallback callback: cameraCallbacks) {
                callback.failedCallback(error);
            }
        }
    };

    /// --------------------------------------------------------------------------- ///
    /// Utils ///
    /// --------------------------------------------------------------------------- ///

    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

}
