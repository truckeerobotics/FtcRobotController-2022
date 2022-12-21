//
// Created by Alex on 12/13/22.
//

#include "../include/transformLocator.h"

//------------------------------------------------------------------------------------------------------------------------------
//
//------------------------------------------------------------------------------------------------------------------------------

Transform
TransformLocator::locateTransform(uInt8Buffer yBufferContainer, uInt8Buffer uBufferContainer,
                                  uInt8Buffer vBufferContainer, JNIEnv *env) {
    this->env = env;
    javaLog("Going in!", env);

    getPoles(yBufferContainer,uBufferContainer,vBufferContainer);

    javaLog("Finished!", env);
    return Transform();
}

//------------------------------------------------------------------------------------------------------------------------------
//
//------------------------------------------------------------------------------------------------------------------------------




std::vector<Pole>
TransformLocator::getPoles(uInt8Buffer yBufferContainer, uInt8Buffer uBufferContainer,
                           uInt8Buffer vBufferContainer) {
    uint8_t* yBuffer = yBufferContainer.data;
    uint8_t* uBuffer = uBufferContainer.data;
    uint8_t* vBuffer = vBufferContainer.data;

    getPolePixelLocationsInRow(yBuffer, uBuffer, vBuffer, 1);

    return std::vector<Pole>();
}



bool TransformLocator::isPoleColor(uint8_t *y, uint8_t *u, uint8_t *v){
    int yInt = (int)*y;
    int uInt = (int)*u;
    int vInt = (int)*v;
    if (uInt > poleColorBox.uStart && uInt < poleColorBox.uEnd) {
        if (vInt < poleColorBox.vStart && vInt > poleColorBox.vEnd) {
            if (yInt < poleColorBox.yMax && yInt > poleColorBox.yMin) {
                return true;
            }
        }
    }
    return false;

}

std::vector<PolePixelLocation>
TransformLocator::getPolePixelLocationsInRow(uint8_t *yBuffer, uint8_t *uBuffer, uint8_t *vBuffer, int row) {
    std::vector<PolePixelLocation> imagePoleLocations = std::vector<PolePixelLocation>();

    size_t rowStartIndex = row*imageSize.x;

    int pixelPoleStart = -1;
    int pixelPoleLast = -1;

    int poleColorDetectedCount = 0;

    for(int pixel=0; pixel<imageSize.x; pixel++){
        size_t index = pixel+rowStartIndex;
        bool isPixelPoleColor = isPoleColor(yBuffer+index*(bytePerPixel*2), uBuffer+index*bytePerPixel, vBuffer+index*bytePerPixel);

        if (isPixelPoleColor) {
            poleColorDetectedCount++;
            pixelPoleLast = pixel;
            if (pixelPoleStart == -1) {
                pixelPoleStart = pixel;
            }

        } else if (pixelPoleLast != -1 && pixelPoleStart != -1 && pixelPoleLast+poleContinuityTolerance<=pixel) {
            javaLog("Pole Detected:", env);
            javaLog(pixelPoleStart, env);
            javaLog(pixelPoleLast, env);

            size_t index2 = pixelPoleStart+imageSize.x;
            javaLog("START: Y: " + std::to_string(*(yBuffer+index2*(bytePerPixel*2))) + " U: " + std::to_string(*(uBuffer+index2*bytePerPixel)) + " V: " + std::to_string(*(vBuffer+index2*bytePerPixel)), env);
            javaLog("END: Y: " + std::to_string(*(yBuffer+index*(bytePerPixel*2))) + " U: " + std::to_string(*(uBuffer+index*bytePerPixel)) + " V: " + std::to_string(*(vBuffer+index*bytePerPixel)), env);
            imagePoleLocations.push_back(PolePixelLocation(pixelPoleStart, pixelPoleLast));
            pixelPoleLast = -1;
            pixelPoleStart = -1;
            continue;
        }
    }
    javaLog("Pole Color Detected Count: " + std::to_string(poleColorDetectedCount), env);
    javaLog("Image Pole Locations List: " + std::to_string(imagePoleLocations.size()), env);
    javaLog("Returning image pole location", env);
    return imagePoleLocations;
}


//------------------------------------------------------------------------------------------------------------------------------
//
//------------------------------------------------------------------------------------------------------------------------------

TransformLocator::TransformLocator(Size imageSize) {
    this->imageSize = imageSize;
}

Transform TransformLocator::getTransform(std::vector<Pole> poles) {
    return Transform();
}



std::vector<Point> TransformLocator::getIntercepts(double *poleDistances) {
    return std::vector<Point>();
}

