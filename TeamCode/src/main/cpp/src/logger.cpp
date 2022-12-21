//
// Created by Alex on 12/5/22.
//

#include "../include/logger.h"



int javaLog(const char* toLog, JNIEnv* env, bool telemetry, bool update) {
    jstring javaStringToLog = env->NewStringUTF(toLog);

    jclass nativeLoggerClass = env->FindClass("org/firstinspires/ftc/teamcode/other/NativeLogging");
    nativeLogMethodId = env->GetStaticMethodID(nativeLoggerClass, "logNative", "(Ljava/lang/String;)V");

    env->CallStaticVoidMethod(nativeLoggerClass, nativeLogMethodId, javaStringToLog);
    return 1;
}
