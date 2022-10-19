package org.firstinspires.ftc.teamcode.opmodes.driver;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Scrimmage;

@TeleOp(name = "Scrimmage")
public class ScrimmageOpMode extends LinearOpMode {
    public void runOpMode() throws InterruptedException{
        Scrimmage robot = new Scrimmage("Driver", this);
        robot.run();
    }
}
