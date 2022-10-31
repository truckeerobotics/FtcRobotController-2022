package org.firstinspires.ftc.teamcode.autonomous.opmodes.scrimmage;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Scrimmage;

@Disabled

@Autonomous(name = "ScrimmageBlueRight")
public class ScrimmageBlueRight extends LinearOpMode {
    public void runOpMode() throws InterruptedException{
        Scrimmage robot = new Scrimmage("AutoBlueRight", this);
        robot.run();
    }
}