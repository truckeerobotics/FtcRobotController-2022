package org.firstinspires.ftc.teamcode.sensor;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Encoder {
    private DcMotor motor = null;
    private int startingPos = 0;

    private static final double COUNTS_PER_MOTOR_REV = 1440;
    private static final double DRIVE_GEAR_REDUCTION = 1.0;
    private static final double WHEEL_DIAMETER_INCHES = 4.0;
    public static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    
    public Encoder(DcMotor motor){
        this.motor = motor;
        this.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        startingPos = motor.getCurrentPosition() * (int)COUNTS_PER_INCH;
    }

    public int getDiffrence(){
        return this.motor.getCurrentPosition() * (int)COUNTS_PER_INCH - startingPos;
    }
}
