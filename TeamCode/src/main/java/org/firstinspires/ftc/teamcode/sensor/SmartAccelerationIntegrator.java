package org.firstinspires.ftc.teamcode.sensor;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

public class SmartAccelerationIntegrator implements BNO055IMU.AccelerationIntegrator {
    BNO055IMU.Parameters parameters;
    Acceleration acceleration;
    Position position;
    Velocity velocity;

    @Override public void initialize(BNO055IMU.Parameters parameters, Position initialPosition, Velocity initialVelocity)
    {

    }

    @Override public Position getPosition() { return position; }
    @Override public Velocity getVelocity() { return velocity; }
    @Override public Acceleration getAcceleration() { return acceleration; }

    @Override public void update(Acceleration linearAcceleration)
    {
        if (linearAcceleration != null) {
            double timeToAcquire = linearAcceleration.acquisitionTime;
            acceleration = linearAcceleration;
            double xAccelTimeAdjusted = linearAcceleration.xAccel * timeToAcquire;
            double yAccelTimeAdjusted = linearAcceleration.yAccel * timeToAcquire;
            if (velocity == null) { velocity = new Velocity(); }
            if (position == null) { position = new Position(); }
            velocity.xVeloc += xAccelTimeAdjusted;
            velocity.yVeloc += yAccelTimeAdjusted;
            position.x += velocity.xVeloc * timeToAcquire;
            position.y += velocity.yVeloc * timeToAcquire;
        }
    }
}
