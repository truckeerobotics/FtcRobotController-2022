package org.firstinspires.ftc.teamcode;

import android.content.Context;
import android.os.Looper;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.sensor.Camera;

@TeleOp(name = "Main")
public class Robot extends LinearOpMode {



    Context appContext;

    static {
        System.loadLibrary("cameraAwareness");
    }
//
//    public native int initCameraAwareness();

    public void runOpMode() throws InterruptedException {
        appContext = hardwareMap.appContext;



        telemetry.addData("before start", "prepare for death");
        telemetry.update();

        Camera cameraInstance = new Camera();

        waitForStart();

        while(getRuntime()<5.00){

        }
//        telemetry.addData("cameraAwareness: ", initCameraAwareness());
        while(getRuntime()<5.00){

        }
        telemetry.addData("Starting", "Camera Instance");
        telemetry.update();
        boolean result = cameraInstance.init(appContext, telemetry);
        while (!isStopRequested()) {

        }
        telemetry.addData("Finished", "Completed");
        telemetry.update();
    }
}