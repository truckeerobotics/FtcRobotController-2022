package org.firstinspires.ftc.teamcode.autonomous.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot2;

@Autonomous(name = "Red Right Side")
public class MainRedRight extends LinearOpMode {
    public void runOpMode() throws InterruptedException{
        Robot2 robot = new Robot2("AutoRedRight", this);
        robot.run();
    }
}
