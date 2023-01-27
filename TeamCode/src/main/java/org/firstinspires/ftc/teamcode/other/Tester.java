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
        Servo coneHook = opmode.hardwareMap.servo.get("coneHook");
        Servo armSwing = opmode.hardwareMap.servo.get("armSwing");

        while(!opmode.isStopRequested()){
            opmode.telemetry.addData("cone", coneHook.getPosition());
            opmode.telemetry.addData("arm", armSwing.getPosition());
            opmode.telemetry.update();

            coneHook.setPosition(coneHook.getPosition() + opmode.gamepad1.left_stick_y * 0.3);
            armSwing.setPosition(armSwing.getPosition() + opmode.gamepad1.right_stick_y * 0.3);
        }
    }
}
