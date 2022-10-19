package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Scrimmage;

@TeleOp(name = "ScrimmageBlueRight")
public class ScrimmageBlueRight extends LinearOpMode {
    public void runOpMode() throws InterruptedException{
        Scrimmage robot = new Scrimmage("AutoBlueRight", this);
        robot.run();
    }
}