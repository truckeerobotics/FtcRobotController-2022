package org.firstinspires.ftc.teamcode.other;

import com.qualcomm.hardware.bosch.BNO055IMU;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.teamcode.sensor.SmartAccelerationIntegrator;

@TeleOp(name = "Testing IMU")
public class TestingIMU extends LinearOpMode {
    BNO055IMU controlHubIMU;
    Datalog datalog;

    public void runOpMode() throws InterruptedException {
        telemetry.addData("Log","Loading Program");
        telemetry.update();

        datalog = new Datalog("datalog_01");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;
        parameters.accelerationIntegrationAlgorithm = new SmartAccelerationIntegrator();

        controlHubIMU = hardwareMap.get(BNO055IMU.class, "imu");


        controlHubIMU.initialize(parameters);

        controlHubIMU.startAccelerationIntegration(null, null, 1);

//        int acceleratorStatus = 0, gyroStatus = 0;
//        while (!isStopRequested() && !(acceleratorStatus == 3 && gyroStatus == 3)) {
//            BNO055IMU.CalibrationStatus status = controlHubIMU.getCalibrationStatus();
//            acceleratorStatus = ((status.calibrationStatus >> 2) & 0x03);
//            gyroStatus = ((status.calibrationStatus >> 4) & 0x03);
//            telemetry.addData("Calibration data:", controlHubIMU.getCalibrationStatus());
//            telemetry.addData("Calibration accel:", acceleratorStatus);
//            telemetry.addData("Calibration gyro:", gyroStatus);
//            telemetry.update();
//        }
        telemetry.addData("Calibration:", "done");
        telemetry.update();

        waitForStart();



        while (!isStopRequested()) {

            // Logging
            telemetry.addData("IMU accel", controlHubIMU.getAcceleration());
            telemetry.addData("IMU linear accel", controlHubIMU.getLinearAcceleration());
            telemetry.addData("-------", "-------");
            telemetry.addData("IMU velocity", controlHubIMU.getVelocity());
            telemetry.addData("-------", "-------");
            telemetry.addData("IMU position", controlHubIMU.getPosition());
            telemetry.addData("-------", "-------");
            telemetry.addData("IMU calibrated", controlHubIMU.isAccelerometerCalibrated());
            telemetry.addData("-------", "-------");
            telemetry.addData("IMU orientation", controlHubIMU.getAngularOrientation());

            // Data logging
            Acceleration linearAcceleration = controlHubIMU.getLinearAcceleration();
            datalog.accelerationX.set(linearAcceleration.xAccel);
            datalog.accelerationY.set(linearAcceleration.yAccel);

            // Update telemetry
            telemetry.update();
        }
    }

    /*
     * This class encapsulates all the fields that will go into the datalog.
     */
    public static class Datalog
    {
        // The underlying datalogger object - it cares only about an array of loggable fields
        private final Datalogger datalogger;

        // These are all of the fields that we want in the datalog.
        // Note that order here is NOT important. The order is important in the setFields() call below
        public Datalogger.GenericField accelerationX = new Datalogger.GenericField("accelerationX");
        public Datalogger.GenericField accelerationY = new Datalogger.GenericField("accelerationY");


        public Datalog(String name)
        {
            // Build the underlying datalog object
            datalogger = new Datalogger.Builder()

                    // Pass through the filename
                    .setFilename(name)

                    // Request an automatic timestamp field
                    .setAutoTimestamp(Datalogger.AutoTimestamp.DECIMAL_SECONDS)

                    // Tell it about the fields we care to log.
                    // Note that order *IS* important here! The order in which we list
                    // the fields is the order in which they will appear in the log.
                    .setFields(
                            accelerationX,
                            accelerationY
                    )
                    .build();
        }

        // Tell the datalogger to gather the values of the fields
        // and write a new line in the log.
        public void writeLine()
        {
            datalogger.writeLine();
        }
    }
}


