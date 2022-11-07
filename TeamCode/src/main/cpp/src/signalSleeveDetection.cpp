//
// Created by Alex on 11/6/22.
//

#include <algorithm>
#include "../include/signalSleeveDetection.h"
SignalSleeveDetection::SignalSleeveDetection(int x, int y, int width, int height){
    this->x = x;
    this->y = y;
    this->width = width;
    this->height = height;
}

bool SignalSleeveDetection::checkBounds(float x, float y, bound b){
    if (x >= b.x && x <= b.x + b.width && y >= b.y && y <= b.y + b.height) {
        return true;
    }
    return false;
}

SleeveDetectionResult SignalSleeveDetection::detectSignalLevel(float yBuffer[], float uBuffer[], float vBuffer[]){
    // Count up all the pixels within the bounds
    int pixelCounts[3] = {0,0,0};
    for(int i=START; i<END; i++){
        if(checkBounds(uBuffer[i], vBuffer[i], BROWN_BOUND)){
            pixelCounts[0]++;
        }else if(checkBounds(uBuffer[i], vBuffer[i], PINK_BOUND)){
            pixelCounts[1]++;
        }else if(checkBounds(uBuffer[i], vBuffer[i], GREEN_BOUND)){
            pixelCounts[2]++;
        }
        if(i != 0 && i % width == 0){
            i += MAX_WIDTH - width + x;
        }
    }

    const int arraySize = sizeof(pixelCounts) / sizeof(int);
    // Get percentage of confidence per level
    float levelConfidences[3] = {0,0,0};
    int sum; std::accumulate(pixelCounts, pixelCounts+arraySize, sum);
    if (sum == 0) { return SleeveDetectionResult(1,0.0f); };
    for (int i = 0; i < 3; i++) { levelConfidences[i] =  pixelCounts[i] / sum; }

    // Gets the index of the level with the most pixels
    int detectedLevel = std::distance(pixelCounts, std::max_element(pixelCounts, pixelCounts + arraySize));

    // Return the level (+1 so it is more human readable) and the confidence that it is that level
    return SleeveDetectionResult{detectedLevel+1, levelConfidences[detectedLevel]};
}

SleeveDetectionResult::SleeveDetectionResult(int level, float confidence) {
    this->level = level; this->confidence = confidence;
}
