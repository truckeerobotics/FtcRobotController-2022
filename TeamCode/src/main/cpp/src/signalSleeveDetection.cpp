    //
// Created by Alex on 11/6/22.
//

#include "../include/signalSleeveDetection.h"
#include "../include/transformLocator.h"

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


int SignalSleeveDetection::detectSignalSide(uInt8Buffer brightnessDataContainer, uInt8Buffer uColorDataContainer, uInt8Buffer vColorDataContainer){
    uint8_t* yBuffer = brightnessDataContainer.data;
    uint8_t* uBuffer = uColorDataContainer.data;
    uint8_t* vBuffer = vColorDataContainer.data;

    javaLog("Begin Pole Detection", env);

    cv::Mat yuvMat = createYUVImage(yBuffer, uBuffer, vBuffer, imageSize.x, imageSize.y, 2);

    cv::Mat bgrMat(yuvMat.size(), CV_8UC3);
    cv::cvtColor(yuvMat, bgrMat, cv::COLOR_YUV2BGR);

    cv::QRCodeDetector qrDecoder = cv::QRCodeDetector();

    cv::Mat boundingBox, rectifiedImage;
    std::string data = qrDecoder.detectAndDecode(bgrMat, boundingBox, rectifiedImage);


    javaLog(data, env);
    if (data == "RED") {
        return 1;
    } else if(data == "Green") {
        return 2;
    } else if (data == "BLUE") {
        return 3;
    } else {
        return -1
    }

}

SignalSleeveDetection::SignalSleeveDetection(Size imageSize, JNIEnv* env) {
    this->env = env;
    this->imageSize = imageSize;
}
