package org.firstinspires.ftc.teamcode.movement;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.sensor.Encoder;
import org.firstinspires.ftc.teamcode.other.Vector2;

public class Movement {

    private LinearOpMode opmode;
    public Hardware h;

    private Boolean debug;

    public Movement(LinearOpMode opmode, Hardware h) {
        this.h = h;
        debug = false;
        init(opmode);
    }

    public Movement(LinearOpMode opmode, Hardware h, Boolean debug){
        this.h = h;
        this.debug = debug;
        init(opmode);
    }

    private void init(LinearOpMode opmode){
        this.opmode = opmode;

        h.motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        h.motorBackRight.setDirection(DcMotor.Direction.REVERSE);
    }

    public void driveForward(double speed){
        h.motorBackLeft.setPower(speed);
        h.motorBackRight.setPower(speed);
        h.motorFrontLeft.setPower(speed);
        h.motorFrontRight.setPower(speed);
    }

    public void strafeLeft(double speed){
        h.motorBackLeft.setPower(speed*-1);
        h.motorBackRight.setPower(speed);
        h.motorFrontLeft.setPower(speed);
        h.motorFrontRight.setPower(speed*-1);
    }


    public void stop(){
        h.motorBackLeft.setPower(0);
        h.motorFrontRight.setPower(0);
        h.motorFrontLeft.setPower(0);
        h.motorBackRight.setPower(0);
    }

    private Boolean checkEncoders(Encoder[] encoders, int inches){
        Boolean running = true;
        if(debug){
            opmode.telemetry.addData("STATUS", "Encoder debug");
            opmode.telemetry.addData("Target", inches);
        }
        for(int i=0; i<encoders.length; i++){
            if(encoders[i].getDifference() > inches){
                running = false;
            }
            if(debug){
                opmode.telemetry.addData(i + "", encoders[i].getDifference());
            }
        }
        opmode.telemetry.update();
        return running;
    }


    public void driveInches(int inches, double speed, Encoder[] encoders){
        for(int i=0; i<encoders.length; i++){
            encoders[i].reset();
        }
        while(checkEncoders(encoders, inches) && !opmode.isStopRequested()) {
            driveForward(speed);
        }
    }
}
