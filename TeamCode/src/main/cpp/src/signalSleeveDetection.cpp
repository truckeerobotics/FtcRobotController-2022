//
// Created by Alex on 11/6/22.
//

#include "../include/signalSleeveDetection.h"
#include "../include/imageBuffers.h"

ColorBox::ColorBox(Point center, Size size, Size max) {

};

ColorBox::ColorBox() {};


int SignalSleeveDetection::getColorType(uint8_t *u, uint8_t *v){
    return 0;
}

SleeveDetectionResult SignalSleeveDetection::detectSignalLevel(uInt8Buffer yBufferContainer, uInt8Buffer uBufferContainer, uInt8Buffer vBufferContainer){
    uint8_t* yBuffer = yBufferContainer.data;
    uint8_t* uBuffer = uBufferContainer.data;
    uint8_t* vBuffer = vBufferContainer.data;

    // Count up all the pixels within the bounds of each color
    // Any not in any color bound counts to #3
    int pixelCounts[4] = {0,0,0, 0};

    for(int i=startBufferIndex; i<endBufferIndex; i++){
        int colorType = getColorType(uBuffer+i, vBuffer+i);
        pixelCounts[colorType]++;
        if(i != 0 && i % repeatRowBufferIndex == 0){
            i += addToRepeatRow;
        }
    }

    const int arraySize = sizeof(pixelCounts) / sizeof(int);
    // Get percentage of confidence per level
    float levelConfidences[3] = {0,0,0};
    int sum; std::accumulate(pixelCounts, pixelCounts+arraySize, sum);
    if (sum == 0) { return SleeveDetectionResult(0,0.0f); };
    for (int i = 0; i < 3; i++) { levelConfidences[i] =  pixelCounts[i] / sum; }

    // Gets the index of the level with the most pixels
    int detectedLevel = std::distance(pixelCounts, std::max_element(pixelCounts, pixelCounts + arraySize));

    // Return the level (+1 so it is more human readable) and the confidence that it is that level
    return SleeveDetectionResult{pixelCounts[2], levelConfidences[detectedLevel]};
}

SignalSleeveDetection::SignalSleeveDetection(ColorBox *colorBoxes, DetectionZone detectionZone, Size imageSize, int colorBytePerPixel) {

}

SleeveDetectionResult::SleeveDetectionResult(int level, float confidence) {
    this->level = level; this->confidence = confidence;
}
