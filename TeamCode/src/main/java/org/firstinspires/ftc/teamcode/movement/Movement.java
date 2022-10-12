package org.firstinspires.ftc.teamcode.movement;

import org.firstinspires.ftc.teamcode.utils.Vector2;

public class Movement {

    /**
     * @param pos Inputted position from Vector2 that is the target point to end at
     * @param speed Target speed for robot to move to target position
     *
     */
    public void goToPoint(Vector2 pos, double speed){

    }

    /**
     *
     * @param points Array of points for robot to move to
     * @param speed Target speed of robot to follow selected path
     */
    public void path(Vector2[] points, double speed){
        for(int i=0; i<points.length; i++){
            goToPoint(points[i], speed);
        }
    }


}
