package org.firstinspires.ftc.teamcode.movement;

import org.firstinspires.ftc.teamcode.utils.Vector2;

public class Movement {
    public void goToPoint(Vector2 pos, double speed){
        //ok
    }

    public void path(Vector2[] points, double speed){
        for(int i=0; i<points.length; i++){
            goToPoint(points[i], speed);
        }
    }


}
