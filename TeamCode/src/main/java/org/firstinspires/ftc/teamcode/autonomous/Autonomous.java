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
        this.move = new Movement(opmode, opmode.hardwareMap);
        this.leftEncoder = new Encoder(move.leftMotor);
        this.rightEncoder = new Encoder(move.rightMotor);
    }

    public Boolean blueLeft(){
        move.driveInches(5, 1.0, leftEncoder, rightEncoder);
        move.turn(Movement.LEFT, 1.0);
        return true;
    }

    public Boolean blueRight(){
        move.driveInches(5, 1.0, leftEncoder, rightEncoder);
        move.turn(Movement.LEFT, 1.0);
        return true;
    }

    public Boolean redLeft(){
        move.driveInches(5, 1.0, leftEncoder, rightEncoder);
        move.turn(Movement.LEFT, 1.0);
        return true;
    }

    public Boolean redRight(){
        move.driveInches(5, 1.0, leftEncoder, rightEncoder);
        move.turn(Movement.LEFT, 1.0);
        return true;
    }
}
