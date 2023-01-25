//
// Created by Alex on 10/12/22.
//

#ifndef FTCROBOTCONTROLLER_2022_CAMERA_H
#define FTCROBOTCONTROLLER_2022_CAMERA_H


#include <jni.h>

#include "imageBuffers.h"
#include "signalSleeveDetection.h"
#include "logger.h"
#include "transformLocator.h"

#include <iostream>
#include <fstream>
#include <android/log.h>
#include <string>



extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_passImageBuffers(JNIEnv *env, jclass obj, jbyteArray bufferY, jbyteArray bufferU, jbyteArray bufferV);

extern "C" JNIEXPORT jint JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_getSleeveLevel(JNIEnv *env, jclass obj);

extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_getTransform(JNIEnv *env, jclass obj);

#endif //FTCROBOTCONTROLLER_2022_CAMERA_H

