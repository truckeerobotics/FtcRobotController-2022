//
// Created by Alex on 11/6/22.
//

#ifndef FTCROBOTCONTROLLER_2022_SIGNALSLEEVEDETECTION_H
#define FTCROBOTCONTROLLER_2022_SIGNALSLEEVEDETECTION_H

#include <numeric>
#include <jni.h>
#include <algorithm>

#include "camera.h"


struct Point {
    int x;
    int y;
};

struct Size {
    int x;
    int y;
};

struct DetectionZone {
    Point start;
    Point end;
};

// Color bounds for YUV (Y: Brightness, U & V: Color). xy = uv
struct ColorBox {
    int uStart;
    int uEnd;
    int vStart;
    int vEnd;
    int yMax;
    int yMin;

    ColorBox(Point center, Size size, Size max);
    ColorBox();
};

struct SleeveDetectionResult{
    SleeveDetectionResult(int level, float confidence);

    int level = -1;
    float confidence = 0.0f;
};


class SignalSleeveDetection {
private:
    DetectionZone detectionZone;
    ColorBox colorBoxes; // 3 items
    Size imageSize;

public:
    SignalSleeveDetection(ColorBox colorBoxes[], DetectionZone detectionZone, Size imageSize, int colorBytePerPixel);

    int getColorType(uint8_t *u, uint8_t *v);

    SleeveDetectionResult detectSignalLevel(uInt8Buffer yBuffer, uInt8Buffer uBuffer, uInt8Buffer vBuffer);

    // 2 for yuv 420_888; 420 = 4:2, so 2 brightness pixels per color, _888 = 8 bits = 1 byte. So every 2 pixels is a new color byte.
    int colorBytePerPixel;

    // Variables computed in the constructor from detection zone and image sizes, used in primary color counter loop.
    int startBufferIndex;
    int endBufferIndex;
    int repeatRowBufferIndex;
    int addToRepeatRow;
};


#endif //FTCROBOTCONTROLLER_2022_SIGNALSLEEVEDETECTION_H
