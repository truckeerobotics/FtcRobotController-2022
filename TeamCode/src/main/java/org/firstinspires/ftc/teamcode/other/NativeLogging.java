package org.firstinspires.ftc.teamcode.other;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class NativeLogging {
    native void registerLogger();

    private Telemetry telemetryToLog;

    public NativeLogging(Telemetry telemetry) {
        telemetryToLog = telemetry;
        registerLogger();
    }

    public void logNative(String toLog) {
        telemetryToLog.addData("Native-Log", toLog);
        telemetryToLog.update();
    };
}
