#include "../include/camera.h"

extern "C" JNIEXPORT jint JNICALL
// Returns failure or success state
Java_org_firstinspires_ftc_teamcode_Robot_initCameraAwareness(JNIEnv *env, jobject obj) {
    CameraController cameraController = CameraController();
    return cameraController.initCameraSurfaces();
}
