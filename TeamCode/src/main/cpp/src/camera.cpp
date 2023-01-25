#include "../include/camera.h"
#include "opencv2/imgproc.hpp"
#include "TagDetector.h"

extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_passImageBuffers(JNIEnv *env, jclass obj, jbyteArray bufferY, jbyteArray bufferU, jbyteArray bufferV) {
    imageBufferY = getBufferFromJavaByteBuffer(env, bufferY);
    imageBufferU = getBufferFromJavaByteBuffer(env, bufferU);
    imageBufferV = getBufferFromJavaByteBuffer(env, bufferV);
}

extern "C" JNIEXPORT jint JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_getSleeveLevel(JNIEnv *env, jclass obj) {

    static SignalSleeveDetection signalSleeveObject = SignalSleeveDetection(Size(1920,1080), env);
    int detectionResult = signalSleeveObject.detectSignalSide(imageBufferY, imageBufferU, imageBufferV);
    return detectionResult;
}

extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_getTransform(JNIEnv *env, jclass obj) {
    TransformLocator locator = TransformLocator(Size(1920,1080));
    locator.locateTransform(imageBufferY,imageBufferU,imageBufferV,env);
}
