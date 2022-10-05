package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;

import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.bosch.NaiveAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.driver.DriverInput;


@TeleOp(name = "Main")
public class Robot extends LinearOpMode {
    BNO055IMU controlHubIMU;

    public void runOpMode() throws InterruptedException {

        //DRIVER INPUT. CAN BE DELETED
        DriverInput input = new DriverInput(gamepad1, gamepad2);
        telemetry.addData("Log","Loading Program");
        telemetry.update();

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;

        controlHubIMU = hardwareMap.get(BNO055IMU.class, "imu");

        controlHubIMU.initialize(parameters);

        controlHubIMU.startAccelerationIntegration(null, null, 10);

        int acceleratorStatus = 0, gyroStatus = 0;
        while (!isStopRequested() && !(acceleratorStatus == 3 && gyroStatus == 3)) {
            BNO055IMU.CalibrationStatus status = controlHubIMU.getCalibrationStatus();
            acceleratorStatus = ((status.calibrationStatus >> 2) & 0x03);
            gyroStatus = ((status.calibrationStatus >> 4) & 0x03);
            telemetry.addData("Calibration data:", controlHubIMU.getCalibrationStatus());
            telemetry.addData("Calibration accel:", acceleratorStatus);
            telemetry.addData("Calibration gyro:", gyroStatus);
            telemetry.update();
        }
        telemetry.addData("Calibration:", "done");
        telemetry.update();

        waitForStart();

        while (!isStopRequested()) {
            telemetry.addData("ButtonX", input.getButton("controller1ButtonX"));
            telemetry.addData("IMU accel", controlHubIMU.getAcceleration());
            telemetry.addData("IMU position", controlHubIMU.getPosition());
            telemetry.addData("IMU linear accel", controlHubIMU.getLinearAcceleration());
            telemetry.addData("IMU calibrated", controlHubIMU.isAccelerometerCalibrated());
            telemetry.addData("IMU orientation", controlHubIMU.getAngularOrientation());

            telemetry.update();
        }
    }
}