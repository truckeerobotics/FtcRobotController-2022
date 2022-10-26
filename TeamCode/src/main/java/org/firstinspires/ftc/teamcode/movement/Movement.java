package org.firstinspires.ftc.teamcode.movement;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.sensor.Encoder;
import org.firstinspires.ftc.teamcode.utils.Vector2;

public class Movement {

    private HardwareMap hardware;
    private LinearOpMode opmode;
    public DcMotor leftMotor;
    public DcMotor rightMotor;

    public Movement(LinearOpMode opmode){
        this.hardware = opmode.hardwareMap;
        this.opmode = opmode;
        leftMotor = hardware.dcMotor.get("left_drive");
        rightMotor = hardware.dcMotor.get("right_drive");
    }

    /**
     *  TEMPORARY CODE FOR SCRIMMAGE
     */

    public void driveForward(double speed){
        leftMotor.setPower(speed);
        rightMotor.setPower(-speed);
    }

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public void turn(int direction, double speed){
        if(direction == LEFT){
            rightMotor.setPower(0);
            leftMotor.setPower(speed);
        }else{
            rightMotor.setPower(speed);
            leftMotor.setPower(0);
        }
    }

    public void stop(){
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }

    public void driveInches(int inches, double speed, Encoder leftEncoder, Encoder rightEncoder){
        while(leftEncoder.getDiffrence() < inches && rightEncoder.getDiffrence() < inches && !opmode.isStopRequested()){
            driveForward(speed);
        }
    }

    /**
     *  END TEMPORARY CODE FOR SCRIMMAGE
     */




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
