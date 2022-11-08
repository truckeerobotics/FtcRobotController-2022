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

int SignalSleeveDetection::checkBounds(float* x, float* y){
    float distance[3] = {0,0,0};
    for(int i=0; i<3; i++){
        distance[i] = ((pow((x - y), 2) + pow((COLORS[i][0], COLORS[i][1]), 2)));
    }
    return std::distance(distance, std::min_element(distance, distance + sizeof(distance) / sizeof(float)));
}

SleeveDetectionResult SignalSleeveDetection::detectSignalLevel(float* yBuffer[], float* uBuffer[], float* vBuffer[]){
    // Count up all the pixels within the bounds
    int pixelCounts[3] = {0,0,0};
    for(int i=START; i<END; i++){
        //thing
        if(yBuffer[i] > )
        pixelCounts[checkBounds(uBuffer[i], vBuffer[i])]++;
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
