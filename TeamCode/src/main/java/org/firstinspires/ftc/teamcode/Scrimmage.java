package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.Autonomous;
import org.firstinspires.ftc.teamcode.driver.Driver;

public class Scrimmage {
    //Basically robot.java but for the scrimmage

    /** THIS IS A TEMPORARY FILE. ROBOT.JAVA WILL BE SIMILAR TO THIS LATER ON.
     *
     * The 3 main files are:
     * driver/Driver.java (Driving code)
     * autonomous/Autonomous.java (For all the autonomous movement)
     * Robot.java or Scrimmage.java (Robot.java is not setup yet but it will look similar to this file. Scrimmage.java will be deleted after the Scrimmage is over and Robot.java is setup)
     *
     * Smaller files:
     * movement/Movement.java (Movement for autonomous. it currently contains hardware code which will be removed later)
     * movement/Hardware.java (Currently has no code in it but will be used later)
     *
     * Folders:
     * other: i dont know what this is but it was made by Alex
     * sensor: all the code to read from the sensors but nothing is setup yet
     * utils: vector2 and vector3
     * opmodes: The opmodes folder contains all the opmodes needed and just creates this object with permatiers
     */
    String type = "";
    LinearOpMode opmode;
    public Scrimmage(String type, LinearOpMode opmode){
        this.type = type;
        this.opmode = opmode;
    }

    public void run(){
        if(type.equals("Driver")) {
            Driver driver = new Driver(opmode);
            driver.run();
        }else{
            opmode.waitForStart();
            Autonomous auto = new Autonomous(opmode);
            if(type.equals("AutoBlueLeft")){
                auto.blueLeft();
            }else if(type.equals("AutoBlueRight")){
                auto.blueRight();
            }else if(type.equals("AutoRedLeft")){
                auto.redLeft();
            }else if(type.equals("AutoRedRight")) {
                auto.redRight();
            }
        }
    }
}
