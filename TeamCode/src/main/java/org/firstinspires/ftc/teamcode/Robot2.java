package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.Autonomous;
import org.firstinspires.ftc.teamcode.driver.Driver;

public class Robot2 {
    /** TO LATER BE CONVERTED TO ROBOT.JAVA
     *
     * The 3 main files are:
     * driver/Driver.java (Driving code)
     * autonomous/Autonomous.java (For all the autonomous movement)
     * Robot.java (Robot2.java currently.)
     *
     * Smaller files:
     * movement/Movement.java (Movement for autonomous. it currently contains hardware code which will be removed later)
     * movement/Hardware.java (Currently has no code in it but will be used later)
     *
     * Folders:
     * other: i dont know what this is but it was made by Alex
     * sensor: encoders are the only thing setup currently
     * utils: vector2 and vector3
     * OpModes: The OpModes folder contains all the OpModes needed and just creates this object with parameters
     */

    String type = "";
    LinearOpMode opmode;
    public Robot2(String type, LinearOpMode opmode){
        this.type = type;
        this.opmode = opmode;
    }

    public void run(){
        if(type.equals("Driver")) {
            Driver driver = new Driver(opmode);
            driver.run();
        } else if(type.equals("DriverJV")) {
            Driver driver = new Driver(opmode);
            driver.runJV();
        } else {
            Autonomous auto = new Autonomous(opmode);
            if (type.equals("AutoBlueLeft")) {
                auto.blueLeft();
            } else if (type.equals("AutoBlueRight")) {
                auto.blueRight();
            } else if (type.equals("AutoRedLeft")) {
                auto.redLeft();
            } else if (type.equals("AutoRedRight")) {
                auto.redRight();
            }else if(type.equals("Camera")){
                auto.camera();
            }else if(type.equals("JVAuto")){
                auto.JVAuto();
            }
        }
    }
}
