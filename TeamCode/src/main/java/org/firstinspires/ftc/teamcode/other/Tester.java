package org.firstinspires.ftc.teamcode.other;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Tester {
    private LinearOpMode opmode;

    public Tester(LinearOpMode opmode){
        this.opmode = opmode;
    }

    public void run(){
        View relativeLayout;

        int relativeLayoutId = opmode.hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", opmode.hardwareMap.appContext.getPackageName());
        relativeLayout = ((Activity)opmode.hardwareMap.appContext).findViewById(relativeLayoutId);

        opmode.waitForStart();

        while(!opmode.isStopRequested()){
            relativeLayout.setBackgroundColor(Color.RED);
        }
    }
}
