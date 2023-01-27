package org.firstinspires.ftc.teamcode.other;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;

public class Tester {
    private LinearOpMode opmode;

    public Tester(LinearOpMode opmode){
        this.opmode = opmode;
    }

    public void run(){

        Servo servoArm = opmode.hardwareMap.servo.get("sArm");

        double num;

        while(!opmode.isStopRequested()){
            num = opmode.gamepad1.left_stick_y/2;
            servoArm.setPosition(num);
            opmode.telemetry.addData("num", num);
    opmode.telemetry.update();
        }
    }
}
