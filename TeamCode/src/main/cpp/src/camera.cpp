#include "../include/camera.h"

extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_passImageBuffers(JNIEnv *env, jobject obj, jbyteArray bufferY, jbyteArray bufferU, jbyteArray bufferV) {
    imageBufferY = getBufferFromJavaByteBuffer(env, bufferY);
    imageBufferU = getBufferFromJavaByteBuffer(env, bufferU);
    imageBufferV = getBufferFromJavaByteBuffer(env, bufferV);
}

extern "C" JNIEXPORT jint JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_getSleeveLevel(JNIEnv *env, jobject obj) {
    SleeveDetectionResult detectionResult = signalSleeveObject.detectSignalLevel(imageBufferY, imageBufferU, imageBufferV);
    return detectionResult.level;
    //return 0;
}


