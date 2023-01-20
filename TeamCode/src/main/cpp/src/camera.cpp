#include "../include/camera.h"
#include "opencv2/imgproc.hpp"

extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_passImageBuffers(JNIEnv *env, jobject obj, jbyteArray bufferY, jbyteArray bufferU, jbyteArray bufferV) {
    imageBufferY = getBufferFromJavaByteBuffer(env, bufferY);
    imageBufferU = getBufferFromJavaByteBuffer(env, bufferU);
    imageBufferV = getBufferFromJavaByteBuffer(env, bufferV);
}

extern "C" JNIEXPORT jstring JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_getSleeveLevel(JNIEnv *env, jobject obj) {

    static SignalSleeveDetection signalSleeveObject = SignalSleeveDetection(Size(1920,1080), env);
    int detectionResult = signalSleeveObject.detectSignalSide(imageBufferY, imageBufferU, imageBufferV);
    std::string outputString = "detected: " + std::to_string(detectionResult);
    return env->NewStringUTF(outputString.c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_getTransform(JNIEnv *env, jobject obj) {
    TransformLocator locator = TransformLocator(Size(1920,1080));
    locator.locateTransform(imageBufferY,imageBufferU,imageBufferV,env);
}
