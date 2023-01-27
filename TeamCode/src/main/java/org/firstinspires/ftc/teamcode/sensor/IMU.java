package org.firstinspires.ftc.teamcode.sensor;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.other.Vector2;

public class IMU {
    private BNO055IMU imu;
    private LinearOpMode opmode;

    public IMU(LinearOpMode opmode, String IMUName){
        this.opmode = opmode;
        // Retrieve the IMU from the hardware map
        BNO055IMU imu = opmode.hardwareMap.get(BNO055IMU.class, IMUName);
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        // Technically this is the default, however specifying it is clearer
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        // Without this, data retrieving from the IMU throws an exception
        imu.initialize(parameters);

        this.imu = imu;
    }

    public double getHeading() {
        return -imu.getAngularOrientation().firstAngle;
    }

    public Vector2 getHeadingCorrection(double x, double y) {
        double botHeading = getHeading();
        double rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
        double rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);
        return new Vector2(rotX, rotY);
    }
}
