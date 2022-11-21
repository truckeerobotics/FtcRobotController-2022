package org.firstinspires.ftc.teamcode.autonomous;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.movement.Movement;
import org.firstinspires.ftc.teamcode.sensor.Encoder;

public class Autonomous {
    LinearOpMode opmode;
    Movement move;
    Encoder motorBackLeftEncoder;
    Encoder motorBackRightEncoder;
    Encoder motorFrontLeftEncoder;
    Encoder motorFrontRightEncoder;

    public Autonomous(LinearOpMode opmode){
        this.opmode = opmode;
        this.move = new Movement(opmode, true);
        this.motorBackLeftEncoder = new Encoder(move.motorBackLeft);
        this.motorBackRightEncoder = new Encoder(move.motorBackRight);
        this.motorFrontLeftEncoder = new Encoder(move.motorFrontLeft);
        this.motorFrontRightEncoder = new Encoder(move.motorFrontRight);
    }


    public void camera(){
        ColorSensor colorSensor;
        float hsvValues[] = {0F,0F,0F};

        colorSensor = opmode.hardwareMap.get(ColorSensor.class, "sensor_color");

        opmode.telemetry.addData("STATUS", "Waiting for start");
        opmode.telemetry.update();
        opmode.waitForStart();

        Encoder[] encoderArray = {motorBackLeftEncoder, motorBackRightEncoder, motorFrontLeftEncoder, motorFrontRightEncoder};
        move.driveInches(21, 0.50, encoderArray);
        move.stop();

        opmode.resetRuntime();
        int[] colors = new int[3];
        while(opmode.getRuntime() < 5.0 && !opmode.isStopRequested()) {
            Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);

            String color = "NO COLOR";

            if (colorSensor.blue() > colorSensor.green() && colorSensor.blue() > colorSensor.red()) {
                color = "blue";
                colors[0]++;
            } else if (colorSensor.green() > colorSensor.red() && colorSensor.green() > colorSensor.blue()) {
                color = "green";
                colors[1]++;
            } else if (colorSensor.red() > colorSensor.green() && colorSensor.red() > colorSensor.blue()) {
                color = "orange";
                colors[2]++;
            }

            opmode.telemetry.addData("STATUS", "Doing color scan");
            opmode.telemetry.addData("Red  ", colorSensor.red());
            opmode.telemetry.addData("Green", colorSensor.green());
            opmode.telemetry.addData("Blue ", colorSensor.blue());
            opmode.telemetry.addData("color", color);

            opmode.telemetry.update();
        }

        int highest = Integer.MIN_VALUE;
        int highestIndex = -1;
        for(int i=0; i<colors.length; i++){
            if(colors[i] > highest){
                highest = colors[i];
                highestIndex = i;
            }
        }

        move.driveInches(32, 0.75, encoderArray);
        move.stop();

        opmode.resetRuntime();
        while(opmode.getRuntime() < 3 && !opmode.isStopRequested()){
            opmode.telemetry.addData("STATUS", "waiting...");
            opmode.telemetry.update();
        }

        opmode.resetRuntime();
        while(opmode.getRuntime() < 4 && !opmode.isStopRequested()){
            if(highestIndex == 0){
                opmode.telemetry.addData("STATUS", "moving left");
                opmode.telemetry.addData("LEVEL", highestIndex);
                move.strafeLeft(0.5);
            }else if(highestIndex == 1){
                opmode.telemetry.addData("STATUS", "not moving");
                opmode.telemetry.addData("LEVEL", highestIndex);
                //we dont need to move lol
            }else if(highestIndex == 2){
                opmode.telemetry.addData("STATUS", "moving right");
                opmode.telemetry.addData("LEVEL", highestIndex);
                move.strafeLeft(-0.5);
            }
            opmode.telemetry.update();
        }
        move.stop();
    }

    public Boolean blueLeft(){
        opmode.waitForStart();
        Encoder[] encoderArray = {motorBackLeftEncoder, motorBackRightEncoder, motorFrontLeftEncoder, motorFrontRightEncoder};
        move.driveInches(12, 0.75, encoderArray);
        return true;
    }

    public Boolean blueRight(){
        opmode.waitForStart();
        Encoder[] encoderArray = {motorBackLeftEncoder, motorBackRightEncoder, motorFrontLeftEncoder, motorFrontRightEncoder};
        move.driveInches(25, 0.3, encoderArray);
        return true;
    }

    public Boolean redLeft(){
        opmode.waitForStart();
        Encoder[] encoderArray = {motorBackLeftEncoder, motorBackRightEncoder, motorFrontLeftEncoder, motorFrontRightEncoder};
        move.driveInches(25, 0.3, encoderArray);
        return true;
    }

    public Boolean redRight(){
        opmode.waitForStart();
        Encoder[] encoderArray = {motorBackLeftEncoder, motorBackRightEncoder, motorFrontLeftEncoder, motorFrontRightEncoder};
        move.driveInches(34, 0.3, encoderArray);
        return true;
    }

//    public Boolean blueLeft(){
//        opmode.resetRuntime();
//        while(opmode.getRuntime() < 1.25){
//            move.driveForward(1.0);
//
//        }
//        return true;
//    }
//
//    public Boolean blueRight(){
//        opmode.resetRuntime();
//        while(opmode.getRuntime() < 1.25){
//            move.driveForward(1.0);
//
//        }
//        return true;
//    }
//
//    public Boolean redLeft(){
//        opmode.resetRuntime();
//        while(opmode.getRuntime() < 1.25){
//            move.driveForward(1.0);
//
//        }
//        return true;
//    }
//
//    public Boolean redRight(){
//        opmode.resetRuntime();
//        while(opmode.getRuntime() < 1.25){
//            move.driveForward(1.0);
//
//        }
//        return true;
//    }
}
