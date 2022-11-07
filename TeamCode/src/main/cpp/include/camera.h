//
// Created by Alex on 10/12/22.
//

#ifndef FTCROBOTCONTROLLER_2022_CAMERA_H
#define FTCROBOTCONTROLLER_2022_CAMERA_H

#include <jni.h>
#include "tensorflow/lite/c/common.h"
#include "tensorflow/lite/c/c_api.h"

float* imageBufferY;
float* imageBufferU;
float* imageBufferV;

extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_passImageBuffers(JNIEnv *env, jobject obj, jfloatArray bufferY, jfloatArray bufferU, jfloatArray bufferV);

extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_initSignalSleeveDetection(JNIEnv *env, jobject obj);

extern "C" JNIEXPORT jint JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_getSleeveLevel(JNIEnv *env, jobject obj);

#endif //FTCROBOTCONTROLLER_2022_CAMERA_H

