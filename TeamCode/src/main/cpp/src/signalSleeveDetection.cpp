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

// Gets which of the 3 possible side the signal sleeve is on.
int SignalSleeveDetection::getColorSignalSide(uint8_t *y, uint8_t *u, uint8_t *v){
    // Cast to int to avoid promoting an 8-bit int to a 32-bit int when performing operations many times.
    int yInt = static_cast<int>(*y);
    int uInt = static_cast<int>(*u);
    int vInt = static_cast<int>(*v);

    for (int signalSide = 0; signalSide < 3; ++signalSide) {
        // Check if the current pixel's color values fall within the bounds of the current signal side
        ColorBox colorBox = *(colorBoxes + signalSide);
        if (uInt > colorBox.uStart && uInt < colorBox.uEnd) {
            if (vInt < colorBox.vStart && vInt > colorBox.vEnd) {
                if (yInt < colorBox.yMax && yInt > colorBox.yMin) {
                    return signalSide;
                }
            }
        }
    }
    return 4;
}

/*
This function is used to detect the side of a signal sleeve based on the color of pixels in an image. It counts the number of
pixels that fall within the bounds of each of the 3 possible colors that the signal sleeve sides could be, and returns a level and
confidence level for the color with the highest number of pixels. If no pixels fall within any color bounds, it returns a level
of 4 and a confidence level of 0. The function uses the helper function getColorSignalSide to determine which color a pixel belongs to.
 */
SleeveDetectionResult SignalSleeveDetection::detectSignalSide(uInt8Buffer brightnessDataContainer, uInt8Buffer uColorDataContainer, uInt8Buffer vColorDataContainer){
    uint8_t* brightnessBuffer = brightnessDataContainer.data;
    uint8_t* colorUBuffer = uColorDataContainer.data;
    uint8_t* colorVBuffer = vColorDataContainer.data;

    // Count up all the pixels within the bounds of each color
    // Any not in any color bound counts to #3
    int pixelCounts[3] = {0,0,0};

    // startBufferIndex is the start index of the detection area, and endBufferIndex is the end.
    for(int pixelIndex=startBufferIndex; pixelIndex<endBufferIndex; pixelIndex++){
        // Gets the pixel color & brightness from the buffer, then uses them to get the signal side for the pixel.
        int signalSide = getColorSignalSide(brightnessBuffer+pixelIndex*(bytePerPixel*2), colorUBuffer +pixelIndex* bytePerPixel, colorVBuffer +pixelIndex* bytePerPixel);
        if (signalSide != 4) {
            pixelCounts[signalSide]++;
        }
        // This makes it loop around when it gets to the end of a row of the detection area.
        if(pixelIndex % imageSize.x == repeatRowBufferIndex){
            pixelIndex += addToRepeatRow;
        }
    }

    // Get the sum, if the total number of pixels of each side is 0, then return 4
    int sum = std::accumulate(std::begin(pixelCounts), std::end(pixelCounts), 0);
    if (sum == 0) { return SleeveDetectionResult(4,0.0f); };

    // Get highest confidence level
    float levelConfidences[3] = {0,0,0};

    for (int i = 0; i < 3; i++) {
        levelConfidences[i] = pixelCounts[i] / sum;
    }

    int detectedLevel = std::max_element(std::begin(levelConfidences), std::end(levelConfidences)) - std::begin(levelConfidences);

    // Return the level (+1 so it is more human readable) and the confidence that it is that level
    return SleeveDetectionResult{detectedLevel, levelConfidences[detectedLevel]};
}

SignalSleeveDetection::SignalSleeveDetection(ColorBox *colorBoxes, DetectionZone detectionZone, Size imageSize, int bytePerPixel, JNIEnv* env) {
    this->detectionZone = detectionZone;
    this->imageSize = imageSize;
    this->bytePerPixel = bytePerPixel;
    this->colorBoxes = colorBoxes;

    this->startBufferIndex = (detectionZone.start.x + (detectionZone.start.y*imageSize.x))/(bytePerPixel);
    this->endBufferIndex = (detectionZone.end.x + (detectionZone.end.y*imageSize.x))/(bytePerPixel);
    this->repeatRowBufferIndex = (detectionZone.end.x)/(bytePerPixel);
    this->addToRepeatRow = ((imageSize.x - detectionZone.end.x) + detectionZone.start.x)/(bytePerPixel);

    this->env = env;
}

SleeveDetectionResult::SleeveDetectionResult(int level, float confidence) {
    this->level = level; this->confidence = confidence;
}
