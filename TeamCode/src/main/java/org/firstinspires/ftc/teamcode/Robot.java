package org.firstinspires.ftc.teamcode;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.sensor.CameraController;

@TeleOp(name = "Main")
public class Robot extends LinearOpMode {



    Context appContext;

    static {
        System.loadLibrary("cameraAwareness");
    }
//
    public native int initCameraAwareness();

    public void runOpMode() throws InterruptedException {

        telemetry.setAutoClear(false);
        appContext = hardwareMap.appContext;



        telemetry.addData("before start", "prepare for death");
        telemetry.update();

        telemetry.addData("cameraAwareness: ", initCameraAwareness());
        telemetry.update();

        waitForStart();

        telemetry.clear();

        telemetry.addData("Starting", "Camera Instance");
        telemetry.update();

        CameraController cameraController = new CameraController();
        boolean result = cameraController.init(appContext, telemetry);
        telemetry.addData("Result", result);
        telemetry.update();



        while (!isStopRequested()) {

        }
        telemetry.addData("Finished", "Completed");
        telemetry.update();
    }
}