//
// Created by Alex on 11/6/22.
//

#include "../include/signalSleeveDetection.h"

ColorBox::ColorBox(Point start, Point end, int yMin, int yMax) {
    this->uStart = start.x;
    this->vStart = start.y;
    this->uEnd = end.x;
    this->vEnd = end.y;
    this->yMax = yMax;
    this->yMin = yMin;
};

ColorBox::ColorBox() {
    this->uStart = 0; this->vStart = 0; this->uEnd = 0; this->vEnd = 0; this->yMax = 0; this->yMin = 0;
};



int SignalSleeveDetection::getColorType(uint8_t *y, uint8_t *u, uint8_t *v){
    // Cast to int, as to do operations you gotta promote to 32 bit int anyways.
    int yInt = (int)*y;
    int uInt = (int)*u;
    int vInt = (int)*v;

    for (int i = 0; i < 3; ++i) {
        ColorBox colorBox = *(colorBoxes+i);
        if (uInt > colorBox.uStart && uInt < colorBox.uEnd) {
            if (vInt > colorBox.vStart && vInt < colorBox.vEnd) {
                if (yInt < colorBox.yMax && yInt > colorBox.yMin) {
                    return i;
                }
            }
        }
    }
    return 4;
}

SleeveDetectionResult SignalSleeveDetection::detectSignalLevel(uInt8Buffer yBufferContainer, uInt8Buffer uBufferContainer, uInt8Buffer vBufferContainer){
    uint8_t* yBuffer = yBufferContainer.data;
    uint8_t* uBuffer = uBufferContainer.data;
    uint8_t* vBuffer = vBufferContainer.data;

    // Count up all the pixels within the bounds of each color
    // Any not in any color bound counts to #3
    int pixelCounts[4] = {0,0,0};

    for(int i=startBufferIndex; i<endBufferIndex; i++){
        int colorType = getColorType(yBuffer+i, uBuffer+i, vBuffer+i);
        if (colorType != 4) {
            pixelCounts[colorType]++;
        }
        if(i % imageSize.x == repeatRowBufferIndex){
            i += addToRepeatRow;
        }
    }

    const int arraySize = sizeof(pixelCounts) / sizeof(int);

    // Get percentage of confidence per level
    float levelConfidences[3] = {0,0,0};
    int sum; std::accumulate(pixelCounts, pixelCounts+arraySize, sum);
    if (sum == 0) { return SleeveDetectionResult(4,0.0f); };
    for (int i = 0; i < 3; i++) { levelConfidences[i] =  pixelCounts[i] / sum; }

    // Gets the index of the level with the most pixels
    int detectedLevel = std::distance(pixelCounts, std::max_element(pixelCounts, pixelCounts + arraySize));

    // Return the level (+1 so it is more human readable) and the confidence that it is that level
    return SleeveDetectionResult{detectedLevel, levelConfidences[detectedLevel]};
}

SignalSleeveDetection::SignalSleeveDetection(ColorBox *colorBoxes, DetectionZone detectionZone, Size imageSize, int colorBytePerPixel) {
    this->detectionZone = detectionZone;
    this->imageSize = imageSize;
    this->colorBytePerPixel = colorBytePerPixel;
    this->colorBoxes = colorBoxes;

    this->startBufferIndex = detectionZone.start.x + (detectionZone.start.y*imageSize.x);
    this->endBufferIndex = detectionZone.end.x + (detectionZone.end.y*imageSize.x);
    this->repeatRowBufferIndex = detectionZone.end.x;
    this->addToRepeatRow = (imageSize.x - detectionZone.end.x) + detectionZone.start.x;
}

SleeveDetectionResult::SleeveDetectionResult(int level, float confidence) {
    this->level = level; this->confidence = confidence;
}
