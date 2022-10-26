package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.movement.Movement;
import org.firstinspires.ftc.teamcode.sensor.Encoder;

public class Autonomous {
    LinearOpMode opmode;
    Movement move = null;
    Encoder leftEncoder = null;
    Encoder rightEncoder = null;

    public Autonomous(LinearOpMode opmode){
        this.opmode = opmode;
        this.move = new Movement(opmode);
        this.leftEncoder = new Encoder(move.leftMotor);
        this.rightEncoder = new Encoder(move.rightMotor);
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
