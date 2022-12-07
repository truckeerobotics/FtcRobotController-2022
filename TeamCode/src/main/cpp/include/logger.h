//
// Created by Alex on 12/5/22.
//

#ifndef FTCROBOTCONTROLLER_2022_LOGGER_H
#define FTCROBOTCONTROLLER_2022_LOGGER_H

#include <jni.h>
#include <string>

static JNIEnv* logJavaEnvironment = nullptr;
static jobject logJavaObject = nullptr;
static jmethodID nativeLogMethodId = nullptr;

int javaLog(const char* toLog, bool telemetry = true, bool update = true);
inline int javaLog(std::string toLog, bool telemetry = true, bool update = true) {return javaLog(toLog.c_str(), telemetry, update);}

inline int javaLog(double toLog, bool telemetry = true, bool update = true) {return javaLog(std::to_string(toLog), telemetry, update);}
inline int javaLog(float toLog, bool telemetry = true, bool update = true) {return javaLog(std::to_string(toLog), telemetry, update);}
inline int javaLog(bool toLog, bool telemetry = true, bool update = true) {return javaLog(std::to_string(toLog), telemetry, update);}
inline int javaLog(int toLog, bool telemetry = true, bool update = true) {return javaLog(std::to_string(toLog), telemetry, update);}

extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_other_NativeLogging_registerLogger(JNIEnv *env, jobject obj);

#endif //FTCROBOTCONTROLLER_2022_LOGGER_H
