package org.firstinspires.ftc.teamcode.sensor;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
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

    @Override public Position getPosition() { return new Position(); }
    @Override public Velocity getVelocity() { return new Velocity(); }
    @Override public Acceleration getAcceleration() { return new Acceleration(); }

    @Override public void update(Acceleration linearAcceleration)
    {
    }
}
