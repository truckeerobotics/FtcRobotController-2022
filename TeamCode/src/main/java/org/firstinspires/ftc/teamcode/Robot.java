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
        System.loadLibrary("ftcrobotcontroller");
    }

    public native int main();

    public void runOpMode() throws InterruptedException {
        appContext = hardwareMap.appContext;

        telemetry.addData("main", main());

        telemetry.addData("before start", "prepare for death");
        telemetry.update();

        Camera cameraInstance = new Camera();

        waitForStart();

        telemetry.addData("Starting", "Camera Instance");
        telemetry.update();
        boolean result = cameraInstance.init(appContext, telemetry);
        while (!isStopRequested()) {

        }
        telemetry.addData("Finished", "Completed");
        telemetry.update();
    }
}