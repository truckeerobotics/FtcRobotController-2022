//
// Created by Alex on 12/5/22.
//

#include "../include/logger.h"


extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_other_NativeLogging_registerLogger(JNIEnv *env, jobject obj) {
    logJavaEnvironment = env;
    logJavaObject = obj;

    jclass nativeLoggerClass = env->FindClass("org/firstinspires/ftc/teamcode/other/NativeLogging");
    nativeLogMethodId = logJavaEnvironment->GetMethodID(nativeLoggerClass, "logNative", "(Ljava/lang/String;)V");
}

int javaLog(const char* toLog, bool telemetry, bool update) {
    if (logJavaEnvironment == nullptr) { return -1; }
    if (logJavaObject == nullptr) { return -2; }
    if (nativeLogMethodId == nullptr) { return -3; }
    jstring javaStringToLog = logJavaEnvironment->NewStringUTF(toLog);
    logJavaEnvironment->CallVoidMethod(logJavaObject, nativeLogMethodId, javaStringToLog);
    return 1;
}
