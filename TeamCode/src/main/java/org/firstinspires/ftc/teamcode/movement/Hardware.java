package org.firstinspires.ftc.teamcode.movement;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Hardware {

    public static final int VA = 0;
    public static final int JV = 1;

    public DcMotor motorFrontLeft;
    public DcMotor motorFrontRight;
    public DcMotor motorBackLeft;
    public DcMotor motorBackRight;

    public DcMotor arm;

    public Servo armServo;
    public Servo coneHook; //Varsity Only

    public Hardware(LinearOpMode opmode, int id){
        if(id == VA){
            motorFrontLeft = opmode.hardwareMap.dcMotor.get("motorFrontLeft");
            motorBackLeft = opmode.hardwareMap.dcMotor.get("motorBackLeft");
            motorFrontRight = opmode.hardwareMap.dcMotor.get("motorFrontRight");
            motorBackRight = opmode.hardwareMap.dcMotor.get("motorBackRight");
            arm = opmode.hardwareMap.dcMotor.get("arm");
            coneHook = opmode.hardwareMap.servo.get("coneHook");
            armServo = opmode.hardwareMap.servo.get("armSwing");
        }else if(id ==  JV){
            motorFrontLeft = opmode.hardwareMap.dcMotor.get("mFL");
            motorBackLeft = opmode.hardwareMap.dcMotor.get("mBL");
            motorFrontRight = opmode.hardwareMap.dcMotor.get("mFR");
            motorBackRight = opmode.hardwareMap.dcMotor.get("mBR");
            arm = opmode.hardwareMap.dcMotor.get("arm");
            armServo = opmode.hardwareMap.servo.get("sArm");
        }

    }
}
