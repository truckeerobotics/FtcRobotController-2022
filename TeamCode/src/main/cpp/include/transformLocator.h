//
// Created by Alex on 12/13/22.
//



#ifndef FTCROBOTCONTROLLER_2022_TRANSFORMLOCATOR_H
#define FTCROBOTCONTROLLER_2022_TRANSFORMLOCATOR_H

#include "signalSleeveDetection.h"
#include "logger.h"
#include <vector>

//ColorBox(Point(50,180), Point(102, 150), 10, 240);
static ColorBox poleColorBox = ColorBox(Point(25,200), Point(105, 130), 10, 240);
static int bytePerPixel = 2;
// How many pixels before a pole is considered finished
static int poleContinuityTolerance = 4;

struct Transform {

};

struct PolePixelLocation {
    int pixelStart;
    int pixelEnd;

    PolePixelLocation(int pixelStart, int pixelEnd)
    {this->pixelStart = pixelStart; this->pixelEnd = pixelEnd;};
};

struct Pole {
    double distance;
    double heading;
};

class TransformLocator{
private:
    JNIEnv *env;
    Size imageSize;

    std::vector<double> invertDistanceCoefficients(std::vector<double> coeffecients);
    Point correctDistortion(Point distortedPoint);



    bool isPoleColor(uint8_t *y, uint8_t *u, uint8_t *v);
    std::vector<PolePixelLocation> getPolePixelLocationsInRow(uint8_t* yBuffer, uint8_t* uBuffer, uint8_t* vBuffer, int row);
    std::vector<Pole> getPoles(uInt8Buffer yBufferContainer, uInt8Buffer uBufferContainer, uInt8Buffer vBufferContainer);

    std::vector<Point> getIntercepts(double poleDistances[]);
    Transform getTransform(std::vector<Pole> poles);

public:
    TransformLocator(Size imageSize);
    Transform locateTransform(uInt8Buffer yBufferContainer, uInt8Buffer uBufferContainer, uInt8Buffer vBufferContainer, JNIEnv *env);
};

#endif //FTCROBOTCONTROLLER_2022_TRANSFORMLOCATOR_H

