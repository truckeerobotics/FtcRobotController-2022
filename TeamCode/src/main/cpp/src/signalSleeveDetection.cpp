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

    imwrite("/storage/emulated/0/FIRST/qr_code_image.jpg", bgrMat);

    // test
    //cv::Mat testImage = imread("/storage/emulated/0/FIRST/Test_QRcode.png", cv::IMREAD_COLOR);

    // Default tag family (type)
    TagFamily tagFamily("Tag36h11");

    TagDetector tagDetector(tagFamily);

    cv::Point2d opticalCenter(bgrMat.rows, bgrMat.cols);

    TagDetectionArray tagDetections;
    tagDetector.process(bgrMat, opticalCenter, tagDetections);


    javaLog("Detection START", env);

    for (size_t i=0; i<tagDetections.size(); ++i) {
        const TagDetection& tagDetection = tagDetections[i];

        if (tagDetection.good == true) {
            at::code_t tagId = tagDetection.id;
            if (tagId == 1) {
                return 1;
            } else if (tagId == 2) {
                return 2;
            } else if (tagId == 3) {
                return 3;
            }
        };
    }
    
    javaLog("Detection END", env);

    return 0;

}

SignalSleeveDetection::SignalSleeveDetection(Size imageSize, JNIEnv* env) {
    this->env = env;
    this->imageSize = imageSize;
}
