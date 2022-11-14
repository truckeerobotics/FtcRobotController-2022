package org.firstinspires.ftc.teamcode.autonomous.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot2;

@Autonomous(name = "Camera Op Mode")
public class CameraOpMode extends LinearOpMode {
    public void runOpMode() throws InterruptedException{
        Robot2 robot = new Robot2("Camera", this);
        robot.run();
    }
}
