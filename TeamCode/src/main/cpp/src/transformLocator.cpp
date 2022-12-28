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

    getPolePixelLocationsInRow(yBuffer, uBuffer, vBuffer, 540);

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

    size_t rowStartIndex = row*imageSize.x/2;

    int pixelPoleStart = -1;
    int pixelPoleLast = -1;

    int poleColorDetectedCount = 0;

    for(int pixel=0; pixel<imageSize.x; pixel+=bytePerPixel){
        size_t index = pixel+rowStartIndex;
        bool isPixelPoleColor = isPoleColor(yBuffer+index*2, uBuffer+index, vBuffer+index);

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

            size_t index2 = pixelPoleStart+rowStartIndex;
            javaLog("START: Y: " + std::to_string(*(yBuffer+index2*(2))) + " U: " + std::to_string(*(uBuffer+index2)) + " V: " + std::to_string(*(vBuffer+index2)), env);
            javaLog("END: Y: " + std::to_string(*(yBuffer+index*(2))) + " U: " + std::to_string(*(uBuffer+index)) + " V: " + std::to_string(*(vBuffer+index)), env);
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

//------------------------------------------------------------------------------------------------------------------------------
//
//------------------------------------------------------------------------------------------------------------------------------

Point TransformLocator::correctDistortion(Point distortedPoint) {
//    // Loop over the pixels in the image
//    // Compute the normalized coordinates of the pixel
//    float nx = (distortedPoint.x - cx) / fx;
//    float ny = (distortedPoint.y - cy) / fy;
//    // Compute the distorted coordinates of the pixel
//    float dx = nx;
//    float dy = ny;
//    for (size_t i = 0; i < distCoeffs.size(); i++) {
//        float r2 = dx * dx + dy * dy;
//        float k = distCoeffs[i] * r2;
//        dx += k * dx; dy += k * dy;
//    }
//    // Compute the undistorted coordinates of the pixel
//    float ux = dx;
//    float uy = dy;
//    for (size_t i = 0; i < invDistCoeffs.size(); i++) {
//        float r2 = ux * ux + uy * uy; float k = invDistCoeffs[i] * r2; ux += k * ux; uy += k * uy;
//    }
//    // Compute the pixel coordinates in the undistorted image
//    int ux0 = std::round(ux * fx + cx);
//    int uy0 = std::round(uy * fy + cy);
//    // Check if the pixel is within the bounds of the undistorted image
//    if (ux0 >= 0 && ux0 < undistorted.width && uy0 >= 0 && uy0 < undistorted.height) {
//        // Copy the pixel data
//        size_t offset = (y * image.width + x) * 3;
//        size_t uoffset = (uy0 * undistorted.width + ux0) * 3;
//        undistorted.pixels[uoffset] = image.pixels[offset];
//        undistorted.pixels[uoffset + 1] = image.pixels[offset + 1];
//        undistorted.pixels[uoffset + 2] = image.pixels[offset + 2];
//    }
    return Point();
}

std::vector<double> TransformLocator::invertDistanceCoefficients(std::vector<double> distanceCoeffecients) {
    // Compute the inverse distortion coefficients
    std::vector<double> inverted = distanceCoeffecients;
    for (size_t i = 0; i < inverted.size(); i++) { inverted[i] = -inverted[i] / (i + 1); }
    return inverted;
}

