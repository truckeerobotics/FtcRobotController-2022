package org.firstinspires.ftc.teamcode.sensor;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class IMU {
    BNO055IMU controlHubIMU;
    LinearOpMode opmode;

    public IMU(LinearOpMode opmode, String IMUName){
        this.opmode = opmode;

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;
        parameters.accelerationIntegrationAlgorithm = new SmartAccelerationIntegrator();

        controlHubIMU = opmode.hardwareMap.get(BNO055IMU.class, IMUName);

        controlHubIMU.initialize(parameters);

        controlHubIMU.startAccelerationIntegration(null, null, 1);
    }

    public Orientation getOrientation(){
        return controlHubIMU.getAngularOrientation();
    }
}
