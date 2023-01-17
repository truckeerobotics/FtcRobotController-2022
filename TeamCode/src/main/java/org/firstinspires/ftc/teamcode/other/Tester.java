package org.firstinspires.ftc.teamcode.other;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;

public class Tester {
    private LinearOpMode opmode;

    public Tester(LinearOpMode opmode){
        this.opmode = opmode;
    }

    public void run(){

        DcMotor armTop = opmode.hardwareMap.dcMotor.get("armTop");

        while(!opmode.isStopRequested()){
            armTop.setPower(opmode.gamepad1.left_stick_y);
        }
    }
}
