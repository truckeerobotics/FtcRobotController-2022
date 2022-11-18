#include "../include/camera.h"




extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_passImageBuffers(JNIEnv *env, jobject obj, jbyteArray bufferY, jbyteArray bufferU, jbyteArray bufferV) {
    imageBufferY = getBufferFromJavaByteBuffer(env, bufferY);
    imageBufferU = getBufferFromJavaByteBuffer(env, bufferU);
    imageBufferV = getBufferFromJavaByteBuffer(env, bufferV);

}

extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_initSignalSleeveDetection(JNIEnv *env, jobject obj) {

}

extern "C" JNIEXPORT jint JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_getSleeveLevel(JNIEnv *env, jobject obj) {
    SignalSleeveDetection detection(0,0,1920,1080);
    detection.detectSignalLevel(imageBufferY, imageBufferU, imageBufferV);
    return 0;
}


