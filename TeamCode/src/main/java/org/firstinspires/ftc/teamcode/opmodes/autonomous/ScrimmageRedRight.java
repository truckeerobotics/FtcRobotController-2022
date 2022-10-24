package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Scrimmage;

@Autonomous(name = "ScrimmageRedRight")
public class ScrimmageRedRight extends LinearOpMode {
    public void runOpMode() throws InterruptedException{
        Scrimmage robot = new Scrimmage("AutoRedRight", this);
        robot.run();
    }
}