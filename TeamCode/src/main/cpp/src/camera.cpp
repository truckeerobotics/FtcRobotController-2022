#include "../include/camera.h"

 float* convertJavaFloatArray(jfloatArray javaFloatArray, JNIEnv *env) {
    //jfloat* native_input_frame = (*env).GetFloatArrayElements(env, javaFloatArray, NULL);
}

extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_passImageBuffers(JNIEnv *env, jobject obj, jfloatArray bufferY, jfloatArray bufferU, jfloatArray bufferV) {

}

extern "C" JNIEXPORT void JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_initSignalSleeveDetection(JNIEnv *env, jobject obj) {

}

extern "C" JNIEXPORT jint JNICALL
Java_org_firstinspires_ftc_teamcode_Robot_getSleeveLevel(JNIEnv *env, jobject obj) {
    return 0;
}


