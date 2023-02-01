package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.Robot.getSleeveLevel;
import static org.firstinspires.ftc.teamcode.Robot.passImageBuffers;
import static org.firstinspires.ftc.teamcode.Robot.yuvImageSaveJPEG;

import android.media.Image;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.movement.Hardware;
import org.firstinspires.ftc.teamcode.movement.Movement;
import org.firstinspires.ftc.teamcode.other.NativeLogging;
import org.firstinspires.ftc.teamcode.sensor.Camera;
import org.firstinspires.ftc.teamcode.sensor.CameraController;
import org.firstinspires.ftc.teamcode.sensor.Encoder;
import org.firstinspires.ftc.teamcode.sensor.SleeveDetection;

import java.nio.ByteBuffer;

@Autonomous(name = "Camera Op Mode")
public class VAuto extends LinearOpMode {
    

    private CameraController cameraController;


    @Override
    public void runOpMode() throws InterruptedException {
        Hardware h = new Hardware(this, Hardware.VA);
        Movement move = new Movement(this, h);
        Encoder motorBackLeftEncoder = new Encoder(h.motorBackLeft);
        Encoder motorBackRightEncoder = new Encoder(h.motorBackRight);
        Encoder motorFrontLeftEncoder = new Encoder(h.motorFrontLeft);
        Encoder motorFrontRightEncoder = new Encoder(h.motorFrontRight);

        telemetry.setAutoClear(true);

        telemetry.addData("STATUS", "Waiting for start");
        telemetry.update();
        waitForStart();

        SleeveDetection s = new SleeveDetection(this);

        int sleeveLevel = s.camera();

        Encoder[] encoderArray = {motorBackLeftEncoder, motorBackRightEncoder, motorFrontLeftEncoder, motorFrontRightEncoder};

        resetRuntime();
        while(getRuntime() < 4 && !isStopRequested()){
            move.strafeLeft(-0.5);
            telemetry.update();
        }
        resetRuntime();
        while(getRuntime() < 1 && !isStopRequested()){
            move.stop();
            telemetry.update();
        }
        resetRuntime();
        while(getRuntime() < 4.15 && !isStopRequested()){
            move.strafeLeft(0.5);
            telemetry.update();
        }
        resetRuntime();
        while(getRuntime() < 1 && !isStopRequested()){
            move.stop();
            telemetry.update();
        }

        move.driveInches(50, 0.75, encoderArray);
        move.stop();

        resetRuntime();
        while(getRuntime() < 3 && !isStopRequested ()){
            telemetry.addData("STATUS", "waiting...");
            telemetry.update();
        }

        resetRuntime();
        while(getRuntime() < 4 && !isStopRequested()){
            telemetry.addData("LEVEL", sleeveLevel);

            if(sleeveLevel == 1){
                move.strafeLeft(-0.5);
            } else if(sleeveLevel == 3){
                move.strafeLeft(0.5);
            }
            telemetry.update();
        }
        move.stop();
    }
}
