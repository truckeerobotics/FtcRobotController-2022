//
// Created by Alex on 11/6/22.
//

#ifndef FTCROBOTCONTROLLER_2022_SIGNALSLEEVEDETECTION_H
#define FTCROBOTCONTROLLER_2022_SIGNALSLEEVEDETECTION_H

#include <numeric>

struct YUV{
    int y;
    int u;
    int v;
};

struct bound{
    float x = 0.0f;
    float y = 0.0f;
    float width = 0.0f;
    float height = 0.0f;
};

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
    int scoreBrown = 0;
    int scoreGreen = 0;
    int scorePink = 0;
    const bound BROWN_BOUND = bound{0,0,0, 0};
    const bound PINK_BOUND = bound{0,0,0, 0};
    const bound GREEN_BOUND = bound{0,0,0, 0};
    const int MAX_WIDTH = 1920;
    const int MAX_HEIGHT = 1080;
    const int START = y * MAX_HEIGHT + x;
    const int END = height * MAX_HEIGHT + width;

public:
    SignalSleeveDetection(int x, int y, int width, int height);

    bool checkBounds(float x, float y, bound b);

    SleeveDetectionResult detectSignalLevel(float yBuffer[], float uBuffer[], float vBuffer[]);
};


#endif //FTCROBOTCONTROLLER_2022_SIGNALSLEEVEDETECTION_H
