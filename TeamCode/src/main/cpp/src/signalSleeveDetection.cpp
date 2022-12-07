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
            if (vInt < colorBox.vStart && vInt > colorBox.vEnd) {
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
    int pixelCounts[3] = {0,0,0};

    for(int i=startBufferIndex; i<endBufferIndex; i++){
        int colorType = getColorType(yBuffer+i*(bytePerPixel*2), uBuffer+i*bytePerPixel, vBuffer+i*bytePerPixel);
        if (colorType != 4) {
            pixelCounts[colorType]++;
        }
        if(i % imageSize.x == repeatRowBufferIndex){

            i += addToRepeatRow;
        }
    }

    // Get percentage of confidence per level
    float levelConfidences[3] = {0,0,0};
    int sum; for (int i = 0; i < 3; ++i) { sum += pixelCounts[i]; };
    if (sum == 0) { return SleeveDetectionResult(4,0.0f); };
    //javaLog("LOG", true, true);
    //javaLog("sum" + std::to_string(sum));
    // Get highest confidence level
    int max = 0;
    int detectedLevel = 0;
    for (int i = 0; i < 3; i++) {
        //javaLog("level(+1): " + std::to_string(i+1));
        //javaLog("pixel count: " + std::to_string(pixelCounts[i]));
        levelConfidences[i] = pixelCounts[i] / sum;
        //javaLog("level confidence: " + std::to_string(pixelCounts[i]));
        if (levelConfidences[i] > max) {
            max = levelConfidences[i];
            detectedLevel = i;
        }

    }

    // Return the level (+1 so it is more human readable) and the confidence that it is that level
    return SleeveDetectionResult{detectedLevel, levelConfidences[detectedLevel]};
}

SignalSleeveDetection::SignalSleeveDetection(ColorBox *colorBoxes, DetectionZone detectionZone, Size imageSize, int bytePerPixel) {
    this->detectionZone = detectionZone;
    this->imageSize = imageSize;
    this->bytePerPixel = bytePerPixel;
    this->colorBoxes = colorBoxes;

    this->startBufferIndex = (detectionZone.start.x + (detectionZone.start.y*imageSize.x))/(bytePerPixel);
    this->endBufferIndex = (detectionZone.end.x + (detectionZone.end.y*imageSize.x))/(bytePerPixel);
    this->repeatRowBufferIndex = (detectionZone.end.x)/(bytePerPixel);
    this->addToRepeatRow = ((imageSize.x - detectionZone.end.x) + detectionZone.start.x)/(bytePerPixel);
}

SleeveDetectionResult::SleeveDetectionResult(int level, float confidence) {
    this->level = level; this->confidence = confidence;
}
