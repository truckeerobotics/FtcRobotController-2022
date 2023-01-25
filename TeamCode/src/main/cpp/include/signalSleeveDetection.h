//
// Created by Alex on 11/6/22.
//

#ifndef FTCROBOTCONTROLLER_2022_SIGNALSLEEVEDETECTION_H
#define FTCROBOTCONTROLLER_2022_SIGNALSLEEVEDETECTION_H

#include <numeric>
#include <jni.h>
#include <algorithm>

#include "imageBuffers.h"
#include "logger.h"
#include "opencv2/opencv.hpp"
#include "TagDetector.h"





struct Point {
    int x;
    int y;
    Point(int x, int y) {this->x = x; this->y = y;};
    Point() {x=0; y=0;};
};

struct Size {
    int x;
    int y;
    Size(int x, int y) {this->x = x; this->y = y;};
    Size() {x=0; y=0;};
};

// Color bounds for YUV (Y: Brightness, U & V: Color). xy = uv
struct ColorBox {
    int uStart;
    int uEnd;
    int vStart;
    int vEnd;
    int yMax;
    int yMin;

    ColorBox(Point start, Point end, int yMin, int yMax);
    ColorBox();
};



class SignalSleeveDetection {
private:
    JNIEnv* env;
    Size imageSize;

public:
    SignalSleeveDetection(Size imageSize, JNIEnv* env);

    int detectSignalSide(uInt8Buffer brightnessDataContainer, uInt8Buffer uColorDataContainer, uInt8Buffer vColorDataContainer);

};


#endif //FTCROBOTCONTROLLER_2022_SIGNALSLEEVEDETECTION_H
