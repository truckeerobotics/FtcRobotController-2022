package org.firstinspires.ftc.teamcode;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.other.NativeLogging;
import org.firstinspires.ftc.teamcode.sensor.Camera;
import org.firstinspires.ftc.teamcode.sensor.CameraController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
    public static native int getSleeveLevel();
    public static native void getTransform();
    public static native void passImageBuffers(byte[] bufferY, byte[] bufferU, byte[] bufferV);

    private CameraController cameraController;

    public void runOpMode() throws InterruptedException {
        String cameraNameFront = hardwareMap.get("FrontCamera").getDeviceName();
        telemetry.addData("Device Name", cameraNameFront);
        telemetry.setAutoClear(false);
        appContext = hardwareMap.appContext;

        // Setup native logger
        NativeLogging.initNativeLogging(telemetry);

        cameraController = new CameraController();
        cameraController.init(appContext, telemetry);

        telemetry.addData("Init", "Init Successful - Prepare for Failure!");
        telemetry.update();


        waitForStart();
        telemetry.clear();



        telemetry.addData("Camera", "Setting up instance");
        telemetry.update();


        Camera frontCamera = cameraController.getCamera("0");
        frontCamera.addCallbacks(cameraCallback);

        telemetry.addData("Camera", "Finished instance setup");
        telemetry.update();

        while (!isStopRequested()) {

        }
        frontCamera.shutdown();
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
            //telemetry.addData("Camera Processor", "Running");
            //telemetry.update();
            Image.Plane[] imagePlanes = latestImage.getPlanes();
            byte[] yBuffer = planeToByteBuffer(imagePlanes[0]);
            byte[] uBuffer = planeToByteBuffer(imagePlanes[1]);
            byte[] vBuffer = planeToByteBuffer(imagePlanes[2]);
            yuvImageSaveJPEG(latestImage);
            passImageBuffers(yBuffer,uBuffer,vBuffer);
            //getTransform();
            int sleeveLevel = getSleeveLevel();
            telemetry.addData("Sleeve Level", sleeveLevel);
//            telemetry.update();
//
//            String bufferUnsignedInt8 = "";
//            String bufferUnsignedInt8RawY = "";
//            String bufferUnsignedInt8RawU = "";
//            String bufferUnsignedInt8RawV = "";
//            for (int i = 0; i < 1000; i++) {
//                int unsignedInt8y = yBuffer[i*4] & 0xFF;
//                int unsignedInt8u = uBuffer[i*2] & 0xFF;
//                int unsignedInt8v = vBuffer[i*2] & 0xFF;
//                bufferUnsignedInt8 += "(" + Integer.toString(unsignedInt8y) + ";" + Integer.toString(unsignedInt8u) + ";" + Integer.toString(unsignedInt8v) + ";),";
//                bufferUnsignedInt8RawY += unsignedInt8y + ",";
//                bufferUnsignedInt8RawU += unsignedInt8u + ",";
//                bufferUnsignedInt8RawV += unsignedInt8v + ",";
//            }
//            String fullDataString = bufferUnsignedInt8RawY + "|||" + bufferUnsignedInt8RawU + "|||" + bufferUnsignedInt8RawV + "###" + bufferUnsignedInt8;
//            telemetry.addData("Camera Processor", "Writing Data File");
//            telemetry.update();
//            String filename = "BRIGHTNESS_TEST_FILE.txt";
//            File file = AppUtil.getInstance().getSettingsFile(filename);
//            ReadWriteFile.writeFile(file, fullDataString);




            //telemetry.addData("IMAGE", "Finished writing image to jpeg");
            //telemetry.update();
        };
    };

    public static void yuvImageSaveJPEG(Image newImage) {
        byte[] data = YUV420toNV21(newImage);
        byte[] RGBBytes = NV21toJPEG(data, newImage.getWidth(), newImage.getHeight(), 100);

        String path = Environment.getExternalStorageDirectory() + "/FIRST/JPEG_FROM_YUV.jpeg";

        FileOutputStream fileOutputStream = null;
        try
        {
            fileOutputStream = new FileOutputStream(path);
            //_path = '/storage/emulated/0/Android/data/com.example.camera2app/files/90529437.png'

            fileOutputStream.write(RGBBytes);// write from bytes to _path file !

            fileOutputStream.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(fileOutputStream != null)
            {
                try
                {
                    fileOutputStream.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        }
    }

    public static byte[] NV21toJPEG(byte[] nv21, int width, int height, int quality) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YuvImage yuv = new YuvImage(nv21, ImageFormat.NV21, width, height, null);
        yuv.compressToJpeg(new Rect(0, 0, width, height), quality, out);
        return out.toByteArray();
    }

    public static byte[] YUV420toNV21(Image image) {

        Rect crop = image.getCropRect();
        int format = image.getFormat();
        int width = crop.width();
        int height = crop.height();
        Image.Plane[] planes = image.getPlanes();
        byte[] data = new byte[width * height * ImageFormat.getBitsPerPixel(format) / 8];
        byte[] rowData = new byte[planes[0].getRowStride()];

        int channelOffset = 0;
        int outputStride = 1;
        for (int i = 0; i < planes.length; i++) {
            switch (i) {
                case 0:
                    channelOffset = 0;
                    outputStride = 1;
                    break;
                case 1:
                    channelOffset = width * height + 1;
                    outputStride = 2;
                    break;
                case 2:
                    channelOffset = width * height;
                    outputStride = 2;
                    break;
            }

            ByteBuffer buffer = planes[i].getBuffer();

            int rowStride = planes[i].getRowStride();
            int pixelStride = planes[i].getPixelStride();

            int shift = (i == 0) ? 0 : 1;
            int w = width >> shift;
            int h = height >> shift;
            buffer.position(rowStride * (crop.top >> shift) + pixelStride * (crop.left >> shift));
            for (int row = 0; row < h; row++) {
                int length;
                if (pixelStride == 1 && outputStride == 1) {
                    length = w;
                    buffer.get(data, channelOffset, length);
                    channelOffset += length;
                } else {
                    length = (w - 1) * pixelStride + 1;
                    buffer.get(rowData, 0, length);
                    for (int col = 0; col < w; col++) {
                        data[channelOffset] = rowData[col * pixelStride];
                        channelOffset += outputStride;
                    }
                }
                if (row < h - 1) {
                    buffer.position(buffer.position() + rowStride - length);
                }
            }
        }
        return data;
    }
}