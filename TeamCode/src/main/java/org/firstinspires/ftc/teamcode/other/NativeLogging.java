package org.firstinspires.ftc.teamcode.other;

import android.util.Log;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class NativeLogging {

    private static Telemetry telemetryToLog;

    public static void initNativeLogging(Telemetry telemetry) {
        telemetryToLog = telemetry;
    }

    public static void logNative(String toLog) {
        Log.i("NativeLog", toLog);
        telemetryToLog.addData("Native-Log", toLog);
        telemetryToLog.update();
    };
}
