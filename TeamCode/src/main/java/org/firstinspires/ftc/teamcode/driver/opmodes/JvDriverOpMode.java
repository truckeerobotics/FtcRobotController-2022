package org.firstinspires.ftc.teamcode.driver.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot2;

@TeleOp(name = "JV Driver")
public class JvDriverOpMode extends LinearOpMode{
    public void runOpMode() throws InterruptedException{
        Robot2 robot = new Robot2("DriverJV", this);
        robot.run();
    }
}
