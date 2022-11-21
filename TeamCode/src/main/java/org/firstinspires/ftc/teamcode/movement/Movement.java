package org.firstinspires.ftc.teamcode.movement;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.sensor.Encoder;
import org.firstinspires.ftc.teamcode.other.Vector2;

public class Movement {

    private HardwareMap hardware;
    private LinearOpMode opmode;
    public DcMotor motorFrontLeft;
    public DcMotor motorBackLeft;
    public DcMotor motorFrontRight;
    public DcMotor motorBackRight;
    public DcMotor armLeft;
    public DcMotor armRight;
    public Servo coneHook;
    public Servo armSwing;

    public Movement(LinearOpMode opmode){
        this.hardware = opmode.hardwareMap;
        this.opmode = opmode;
        motorFrontLeft = opmode.hardwareMap.dcMotor.get("motorFrontLeft");
        motorBackLeft = opmode.hardwareMap.dcMotor.get("motorBackLeft");
        motorFrontRight = opmode.hardwareMap.dcMotor.get("motorFrontRight");
        motorBackRight = opmode.hardwareMap.dcMotor.get("motorBackRight");
        armLeft = opmode.hardwareMap.dcMotor.get("armLeft");
        armRight = opmode.hardwareMap.dcMotor.get("armRight");
        coneHook = opmode.hardwareMap.servo.get("coneHook");
        armSwing = opmode.hardwareMap.servo.get("armSwing");

        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
    }

    public void driveForward(double speed){
        motorBackLeft.setPower(speed);
        motorBackRight.setPower(speed);
        motorFrontLeft.setPower(speed);
        motorFrontRight.setPower(speed);
    }

    public void strafeLeft(double speed){
        motorBackLeft.setPower(speed*-1);
        motorBackRight.setPower(speed);
        motorFrontLeft.setPower(speed);
        motorFrontRight.setPower(speed*-1);
    }

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public void turn(int direction, double speed){
        if(direction == LEFT){
            //turn here
        }else{
            //turn here
        }
    }

    public void stop(){
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);
    }

    private Boolean checkEncoders(Encoder[] encoders, int inches){
        Boolean works = true;
        for(int i=0; i<encoders.length; i++){
            if(encoders[i].getDifference() > inches){
                works = false;
            }
        }
        return works;
    }

    public void driveInches(int inches, double speed, Encoder[] encoders){
        while(checkEncoders(encoders, inches) && !opmode.isStopRequested()) {
            driveForward(speed);
        }
    }

    public void strafeLeftInches(int inches, double speed, Encoder[] encoders){
        while(checkEncoders(encoders, inches) && !opmode.isStopRequested()) {
            strafeLeft(speed);
        }
    }

    /**
     * This method moves the robot to a certain position created with the Vector2 class
     * @param pos Inputted position from Vector2 that is the target point to end at
     * @param speed Target speed for robot to move to target position
     */
    public void goToPoint(Vector2 pos, double speed){

    }

    /**
     * This method moves the robot to certain set positions inside an array with Vector2 points
     * @param points Array of points for robot to move to
     * @param speed Target speed of robot to follow selected path
     */
    public void path(Vector2[] points, double speed){
        for(int i=0; i<points.length; i++){
            goToPoint(points[i], speed);
        }
    }


}
