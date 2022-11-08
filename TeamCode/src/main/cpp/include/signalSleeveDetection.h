//
// Created by Alex on 11/6/22.
//

#ifndef FTCROBOTCONTROLLER_2022_SIGNALSLEEVEDETECTION_H
#define FTCROBOTCONTROLLER_2022_SIGNALSLEEVEDETECTION_H

#include <numeric>
#include <jni.h>


struct SleeveDetectionResult{
    SleeveDetectionResult(int level, float confidence);

    int level = -1;
    float confidence = 0.0f;
};


class SignalSleeveDetection {
private:
    int x = 0;
    int y = 0;
    int width = 0;
    int height = 0;
    const float COLORS[3][2] = { {109, 150}, {138, 177}, {125, 91} }; //brown,pink,green
    const int MAX_WIDTH = 1920;
    const int MAX_HEIGHT = 1080;
    const int START = y * MAX_HEIGHT + x;
    const int END = height * MAX_HEIGHT + width;

public:
    SignalSleeveDetection(int x, int y, int width, int height);

    int checkBounds(float* x, float* y);

    SleeveDetectionResult detectSignalLevel(float* yBuffer[], float* uBuffer[], float* vBuffer[]);
};


#endif //FTCROBOTCONTROLLER_2022_SIGNALSLEEVEDETECTION_H
