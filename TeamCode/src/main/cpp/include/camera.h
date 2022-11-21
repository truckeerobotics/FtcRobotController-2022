//
// Created by Alex on 10/12/22.
//

#ifndef FTCROBOTCONTROLLER_2022_CAMERA_H
#define FTCROBOTCONTROLLER_2022_CAMERA_H

#include "imageBuffers.h"
#include <jni.h>
#include "tensorflow/lite/c/common.h"
#include "tensorflow/lite/c/c_api.h"
#include "signalSleeveDetection.h"
#include <iostream>
#include <fstream>

#include <android/log.h>

//static SignalSleeveDetection signalSleeveObject = SignalSleeveDetection(0,0,1920,1080);

extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_passImageBuffers(JNIEnv *env, jobject obj, jbyteArray bufferY, jbyteArray bufferU, jbyteArray bufferV);

extern "C" JNIEXPORT jint JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_getSleeveLevel(JNIEnv *env, jobject obj);

#endif //FTCROBOTCONTROLLER_2022_CAMERA_H

