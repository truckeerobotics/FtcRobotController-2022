//
// Created by Alex on 10/16/22.
//

#ifndef FTCROBOTCONTROLLER_2022_CAMERACONTROLLER_H
#define FTCROBOTCONTROLLER_2022_CAMERACONTROLLER_H

#include <camera/NdkCameraMetadata.h>
#include <camera/NdkCameraManager.h>
#include <camera/NdkCameraDevice.h>
#include <string>

class CameraController {
private:

public:
    int initCameraDevices();
    int initCameraSurfaces();
    CameraController();

};


#endif //FTCROBOTCONTROLLER_2022_CAMERACONTROLLER_H
