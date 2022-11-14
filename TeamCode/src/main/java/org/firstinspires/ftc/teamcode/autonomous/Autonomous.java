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
    Movement move = null;
    Encoder leftEncoder = null;
    Encoder rightEncoder = null;

    public Autonomous(LinearOpMode opmode){
        this.opmode = opmode;
        //this.move = new Movement(opmode);
        //this.leftEncoder = new Encoder(move.leftMotor);
        //this.rightEncoder = new Encoder(move.rightMotor);
    }

    final float TOLERANCE = 25;
    private boolean check(float value, float check){
            return value <= check + TOLERANCE && value >= check - TOLERANCE;
    }

    public void camera(){
            ColorSensor colorSensor;    // Hardware Device Object

            // hsvValues is an array that will hold the hue, saturation, and value information.
            float hsvValues[] = {0F,0F,0F};

            // values is a reference to the hsvValues array.
            final float values[] = hsvValues;

            // get a reference to the RelativeLayout so we can change the background
            // color of the Robot Controller app to match the hue detected by the RGB sensor.
            int relativeLayoutId = opmode.hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", opmode.hardwareMap.appContext.getPackageName());
            final View relativeLayout = ((Activity) opmode.hardwareMap.appContext).findViewById(relativeLayoutId);

            // bPrevState and bCurrState represent the previous and current state of the button.
            boolean bPrevState = false;
            boolean bCurrState = false;

            // bLedOn represents the state of the LED.
            boolean bLedOn = false;

            // get a reference to our ColorSensor object.
            colorSensor = opmode.hardwareMap.get(ColorSensor.class, "sensor_color");

            // Set the LED in the beginning
            colorSensor.enableLed(bLedOn);

            // wait for the start button to be pressed.
            opmode.waitForStart();

            // while the op mode is active, loop and read the RGB data.
            // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
            while (opmode.opModeIsActive()) {

                // check the status of the x button on either gamepad.
                bCurrState = opmode.gamepad1.x;

                // check for button state transitions.
                if (bCurrState && (bCurrState != bPrevState))  {

                    // button is transitioning to a pressed state. So Toggle LED
                    bLedOn = !bLedOn;
                    colorSensor.enableLed(bLedOn);
                }

                // update previous state variable.
                bPrevState = bCurrState;

                // convert the RGB values to HSV values.
                Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);

                // send the info back to driver station using telemetry function.
                opmode.telemetry.addData("LED", bLedOn ? "On" : "Off");
                opmode.telemetry.addData("Clear", colorSensor.alpha());
                opmode.telemetry.addData("Red  ", colorSensor.red());
                opmode.telemetry.addData("Green", colorSensor.green());
                opmode.telemetry.addData("Blue ", colorSensor.blue());
                opmode.telemetry.addData("Hue", hsvValues[0]);

                String color = "NO COLOR";
                //brown
                if(check(colorSensor.red(), 82) && check(colorSensor.green(), 200) && check(colorSensor.blue(), 127)) {
                    color = "brown";
                }else if(check(colorSensor.red(), 70) && check(colorSensor.green(), 70) && check(colorSensor.blue(), 70)){
                    color = "pink";
                }else if(check(colorSensor.red(), 55) && check(colorSensor.green(), 75) && check(colorSensor.blue(), 54)){
                    color = "green";
                }
                opmode.telemetry.addData("color", color);

                // change the background color to match the color detected by the RGB sensor.
                // pass a reference to the hue, saturation, and value array as an argument
                // to the HSVToColor method.
                relativeLayout.post(new Runnable() {
                    public void run() {
                        relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
                    }
                });

                opmode.telemetry.update();
            }

            // Set the panel back to the default color
            relativeLayout.post(new Runnable() {
                public void run() {
                    relativeLayout.setBackgroundColor(Color.WHITE);
                }
            });
        }

    public Boolean blueLeft(){
        move.driveInches(34, 0.3, leftEncoder, rightEncoder);
        return true;
    }

    public Boolean blueRight(){
        move.driveInches(25, 0.3, leftEncoder, rightEncoder);
        return true;
    }

    public Boolean redLeft(){
        move.driveInches(25, 0.3, leftEncoder, rightEncoder);
        return true;
    }

    public Boolean redRight(){
        move.driveInches(34, 0.3, leftEncoder, rightEncoder);
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
