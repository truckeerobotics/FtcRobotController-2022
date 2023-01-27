package org.firstinspires.ftc.teamcode.sensor;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;
import java.util.List;

public class Encoder {
    private DcMotor motor;
    private int startingPos;

    private static ArrayList<Encoder> encoderArray = new ArrayList<Encoder>();

    private static final double COUNTS_PER_MOTOR_REV = 4;
    private static final double DRIVE_GEAR_REDUCTION = 0.083;
    private static final double WHEEL_DIAMETER_INCHES = 3;
    public static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    
    public Encoder(DcMotor motor){
//        if(encoderArray.size() != 4){
//            encoderArray.add(this);
//        }
        this.motor = motor;
        this.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        startingPos = (int) (motor.getCurrentPosition() * COUNTS_PER_INCH);
    }

    public int getDifference(){
        return (int) (this.motor.getCurrentPosition() * COUNTS_PER_INCH - startingPos);
    }

    public void reset(){
        startingPos = (int) (motor.getCurrentPosition() * COUNTS_PER_INCH);
    }
}
