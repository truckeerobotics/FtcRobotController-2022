#include "../include/camera.h"

extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_passImageBuffers(JNIEnv *env, jobject obj, jbyteArray bufferY, jbyteArray bufferU, jbyteArray bufferV) {
    imageBufferY = getBufferFromJavaByteBuffer(env, bufferY);
    imageBufferU = getBufferFromJavaByteBuffer(env, bufferU);
    imageBufferV = getBufferFromJavaByteBuffer(env, bufferV);
}

extern "C" JNIEXPORT jstring JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_getSleeveLevel(JNIEnv *env, jobject obj) {
    logJavaEnvironment = env;
    javaLog("LOG", true, true);
    SleeveDetectionResult detectionResult = signalSleeveObject.detectSignalLevel(imageBufferY, imageBufferU, imageBufferV);
    std::string outputString = "level: " + std::to_string(detectionResult.level) + ", conf: " +std::to_string(detectionResult.confidence);
    return env->NewStringUTF(outputString.c_str());
    //return 0;
}


