//
// Created by Alex on 12/5/22.
//

#ifndef FTCROBOTCONTROLLER_2022_LOGGER_H
#define FTCROBOTCONTROLLER_2022_LOGGER_H

#include <jni.h>
#include <string>

static JavaVM* logJVM = nullptr;
static jmethodID nativeLogMethodId = nullptr;

int javaLog(const char* toLog, JNIEnv* env, bool telemetry = true, bool update = true);
inline int javaLog(std::string toLog, JNIEnv* env, bool telemetry = true, bool update = true) {return javaLog(toLog.c_str(), env, telemetry, update);}

inline int javaLog(double toLog, JNIEnv* env, bool telemetry = true, bool update = true) {return javaLog(std::to_string(toLog), env, telemetry, update);}
inline int javaLog(float toLog, JNIEnv* env, bool telemetry = true, bool update = true) {return javaLog(std::to_string(toLog), env, telemetry, update);}
inline int javaLog(bool toLog, JNIEnv* env, bool telemetry = true, bool update = true) {return javaLog(std::to_string(toLog), env, telemetry, update);}
inline int javaLog(int toLog, JNIEnv* env, bool telemetry = true, bool update = true) {return javaLog(std::to_string(toLog), env, telemetry, update);}

#endif //FTCROBOTCONTROLLER_2022_LOGGER_H
