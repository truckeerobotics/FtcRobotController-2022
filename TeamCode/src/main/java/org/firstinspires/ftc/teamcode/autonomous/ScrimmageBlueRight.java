package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.movement.Movement;
import org.firstinspires.ftc.teamcode.sensor.Encoder;

@TeleOp(name = "ScrimmageBlueRight")
public class ScrimmageBlueRight extends LinearOpMode {
    public void runOpMode() throws InterruptedException{
        Movement move = new Movement(this, hardwareMap);
        Encoder leftEncoder = new Encoder(move.leftMotor);
        Encoder rightEncoder = new Encoder(move.rightMotor);

        move.driveInches(5, 1.0, leftEncoder, rightEncoder);
        move.turn(Movement.LEFT, 1.0);
    }
}