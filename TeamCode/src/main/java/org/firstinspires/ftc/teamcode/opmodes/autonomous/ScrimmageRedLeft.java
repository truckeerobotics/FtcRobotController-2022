package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Scrimmage;

@TeleOp(name = "ScrimmageRedLeft")
public class ScrimmageRedLeft extends LinearOpMode {
    public void runOpMode() throws InterruptedException{
        Scrimmage robot = new Scrimmage("AutoRedLeft", this);
        robot.run();
    }
}