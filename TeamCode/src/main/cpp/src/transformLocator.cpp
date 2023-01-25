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

    javaLog("Begin Pole Detection", env);

    cv::Mat yuvMat = createYUVImage(yBuffer, uBuffer, vBuffer, imageSize.x, imageSize.y, 2);

    imwrite("/storage/emulated/0/FIRST/YUV_RAW.jpg", yuvMat);

    cv::Mat preprocessedImage;
    preprocessImage(yuvMat, preprocessedImage);
    imwrite("/storage/emulated/0/FIRST/origin_image3.jpg", preprocessedImage);
    std::vector<cv::RotatedRect> poleRectangles = getPoleRectanglesOpenCV(preprocessedImage, yBuffer, uBuffer, vBuffer);

    javaLog("Finished Getting Pole Rectangles", env);

    std::vector<Pole> poleStates = getPoleStates(poleRectangles, preprocessedImage);

    javaLog("Finished Pole Detection", env);

    return poleStates;
}

void TransformLocator::preprocessImage(const cv::Mat& yuvImage, cv::Mat& outPreprocessed) {
    cv::Mat brgImage(yuvImage.size(), CV_8UC3);
    cv::cvtColor(yuvImage, brgImage, cv::COLOR_YUV2BGR);

    // Calibrated using a python program to take images on a chess board, then using opencv in python to find the distortion & generate matrix + dist coeffecients
    cv::Mat cameraMatrix = (cv::Mat_<double>(3,3) << 6225.7109, 0, 449.1818, 0, 5631.2393, 358.5682, 0, 0, 1);
    cv::Mat distCoeffs = (cv::Mat_<double>(5,1) << 1.4039, 14.6769, -0.0071, -0.1362, -123.2744);

    cv::Mat rgbUndistored;
    cv::undistort(brgImage, rgbUndistored, cameraMatrix, distCoeffs);

    // After undistortion some pixels become invalid as the image has been changed, this compensates.
    // These constants are from the same python code used to create the matrix & dist constants.
    cv::Rect regionOfInterest(22, 19, 1876/2, 1030/2);
    cv::Mat brgUndistortedROI = rgbUndistored(regionOfInterest);

    cv::Mat testPreprocess;
    outPreprocessed = cv::Mat(brgUndistortedROI.size(), CV_8UC3);
    // Convert the BGR image to a HSV image
    cv::cvtColor(brgUndistortedROI, outPreprocessed, cv::COLOR_BGR2HSV);

    /// OPTIONAL ///

    // Balance the brightness, replace the original hsv image
//    std::vector<cv::Mat> channels;
//    cv::split(hsv_image, channels);
//
//    cv::equalizeHist(channels[2], channels[2]);
//
//    cv::merge(channels, hsv_image);

    // Apply Gaussian blur to the image
//    cv::GaussianBlur(hsv_image, hsv_image, cv::Size(5, 5), 0);

    /// OPTIONAL ///

    // Erode the image
    cv::Mat kernel = cv::getStructuringElement(cv::MORPH_ELLIPSE, cv::Size(5, 5));
    cv::erode(outPreprocessed, outPreprocessed, kernel);

    // Dilate the image
    cv::dilate(outPreprocessed, outPreprocessed, kernel);
}

std::vector<Pole> TransformLocator::getPoleStates(std::vector<cv::RotatedRect> poleRectangles, const cv::Mat& image) {
    for (int i = 0; i < poleRectangles.size(); i++) {
        cv::RotatedRect poleRectangle = poleRectangles[i];
        cv::Point2f polePoints[4];
        poleRectangle.points(polePoints);

        std::vector<cv::Point2f> polePointVector(polePoints, polePoints + sizeof(polePoints) / sizeof(cv::Point2f));

        std::sort(polePointVector.begin(), polePointVector.end(), [](const cv::Point2f& p1, const cv::Point2f& p2) {
            return p1.y < p2.y;
        });

        // The points are ordered clockwise, so bottom two
        cv::Point2f botRightPoint = polePointVector[2];
        cv::Point2f botLeftPoint = polePointVector[3];
        cv::Point2f botPoint((botRightPoint.x + botLeftPoint.x) / 2.0, (botRightPoint.y + botLeftPoint.y) / 2.0);

        //circle(image, botRightPoint, 4, cv::Scalar(50, 100, 255), 0.15);
        //circle(image, botLeftPoint, 4, cv::Scalar(50, 100, 255), 0.15);

        cv::Size imageSize = image.size();
        cv::Point2f centerPoint = cv::Point2f(imageSize.width/2, imageSize.height/2);
        double poleAngle = getAngleOfPoint(botPoint, centerPoint);
        circle(image, botPoint, 4, cv::Scalar(0, 0, 255), 0.15);
        //circle(image, centerPoint, 7, cv::Scalar(0, 255, 255), 0.15);
        double poleWidth = abs(getAngleOfPoint(botRightPoint, centerPoint)-getAngleOfPoint(botLeftPoint, centerPoint))/2;
        double poleWidthRadians = poleWidth * (M_PI / 180);
        double poleDistance = 0.5/sin(poleWidthRadians/2);

        cv::Point textAnglePoint(botPoint.x, botPoint.y);
        cv::Scalar textColor(0, 0, 255);
        cv::putText(image, std::to_string(poleAngle),
                    textAnglePoint,
                    cv::FONT_HERSHEY_SIMPLEX, 0.5, textColor, 1, cv::LINE_8);

        cv::Point textWidthPoint(botPoint.x, botPoint.y-30);
        cv::Scalar textColor2(0, 255, 255);
        cv::putText(image, std::to_string(poleWidth),
                    textWidthPoint,
                    cv::FONT_HERSHEY_SIMPLEX, 0.5, textColor2, 1, cv::LINE_8);

        cv::Point textWidthPoint2(botPoint.x, botPoint.y-60);
        cv::Scalar textColor3(255, 255, 255);
        cv::putText(image, std::to_string(poleDistance),
                    textWidthPoint2,
                    cv::FONT_HERSHEY_SIMPLEX, 0.5, textColor3, 1, cv::LINE_8);

    }

    cv::Mat debug_image;
    cv::cvtColor(image, debug_image, cv::COLOR_HSV2BGR);

    imwrite("/storage/emulated/0/FIRST/pole_debug_image.jpg", debug_image);
    return std::vector<Pole>();
}

std::vector<cv::RotatedRect>
TransformLocator::getPoleRectanglesOpenCV(const cv::Mat& preprocessed_image, uint8_t *yBuffer, uint8_t *uBuffer, uint8_t *vBuffer) {

    // Threshold the HSV image to only keep yellow pixels
    cv::Scalar lower_yellow(17, 100, 100);
    cv::Scalar upper_yellow(35, 255, 255);
    cv::Mat yellow_mask;
    cv::inRange(preprocessed_image, lower_yellow, upper_yellow, yellow_mask);

    imwrite("/storage/emulated/0/FIRST/hsv_image.jpg", preprocessed_image);

    cv::Mat debug_image;
    cv::cvtColor(preprocessed_image, debug_image, cv::COLOR_HSV2BGR);

    imwrite("/storage/emulated/0/FIRST/preprocessed_image.jpg", preprocessed_image);

    // Find contours in the yellow mask using a faster contour detection and approximation method
    std::vector<std::vector<cv::Point>> contours;
    cv::findContours(yellow_mask, contours, cv::RETR_TREE, cv::CHAIN_APPROX_TC89_L1);

    // Find rotated rectangles in the contours
    std::vector<cv::RotatedRect> rectangles;
    rectangles.reserve(contours.size());
    cv::parallel_for_(cv::Range(0, contours.size()), [&](const cv::Range& range) {
        for (int i = range.start; i < range.end; i++) {
//
//            cv::Rect bounding_rect = boundingRect(contours[i]);
//            float aspect_ratio = (float)bounding_rect.width / (float)bounding_rect.height;
//
//            if (aspect_ratio < 1) {
                // Check if the contour is rotated by more than 15 degrees
                cv::RotatedRect rotated_rect = cv::minAreaRect(contours[i]);
                float rotatedRectangleAspectRatio =
                        (float) rotated_rect.size.width / (float) rotated_rect.size.height;


                float angle = abs(rotated_rect.angle);

                if ((angle < 15.0) || (angle < 105.0 && angle > 75)) {
                    if (angle < 105.0 && angle > 75) {
                        rotatedRectangleAspectRatio = 1/rotatedRectangleAspectRatio;
                    }
                    if (rotatedRectangleAspectRatio > 0.025 && rotatedRectangleAspectRatio < 0.15 && rotated_rect.size.area()>400) {
//                        cv::Point bounding_rect_point(bounding_rect.x, bounding_rect.y);
//                        cv::Scalar textColor(0, 0, 255);
//                        cv::putText(debug_image, std::to_string(rotatedRectangleAspectRatio),
//                                    bounding_rect_point,
//                                    cv::FONT_HERSHEY_SIMPLEX, 0.5, textColor, 1, cv::LINE_8);

                        rectangles.push_back(rotated_rect);

                        // Get the corner points of the rectangle
                        cv::Point2f points[4];
                        rotated_rect.points(points);


                        // Draw lines between the corner points to form the rectangle
                        for (int i = 0; i < 4; i++) {
                            cv::line(debug_image, points[i], points[(i + 1) % 4],
                                     cv::Scalar(0, 255, 0), 2);
                        }
                    }
                }
            //}
        }
    });


    imwrite("/storage/emulated/0/FIRST/result_image.jpg", debug_image);
    imwrite("/storage/emulated/0/FIRST/yellow_masked.jpg", yellow_mask);


    return rectangles;
}

double TransformLocator::getAngleOfPoint(cv::Point2f point, cv::Point2f center){

    if (center.x == 0) {
        return 0;
    }

    double pixelsFromCenter = point.x - center.x;
    double fov = 70;

    // Center is half image resolution, hence we multiply it by 2.
    // We divide by 2 as we want the fov for half the screen, not the full fov.
    double degreeAngleConversion = (fov/2)/center.x;

    // Adjust the angle for the desired FOV
    double angle = pixelsFromCenter*degreeAngleConversion;
    return angle;
}

cv::Mat createYUVImage(uint8_t *yBuffer, uint8_t *uBuffer, uint8_t *vBuffer, int width, int height, int stride) {

    int uvRows = height / 2;
    int uvCols = width / 2;

    // Create a Mat object for the Y channel
    cv::Mat yMat(height, width, CV_8UC1, yBuffer);

    // Create a Mat object for the UV channels
    cv::Mat uMat(uvRows, uvCols, CV_8UC1);
    cv::Mat vMat(uvRows, uvCols, CV_8UC1);

    // Use OpenCV's parallel_for_ function to parallelize the loop over the rows of the uvMat
    cv::parallel_for_(cv::Range(0, uvRows), [&](const cv::Range& range) {
        for (int i = range.start; i < range.end; i++) {
            for (int j = 0; j < uvCols; j++) {
                std::memcpy(uMat.ptr(i)+j, (uBuffer + ((i * uvCols) + j) * stride), sizeof(uint8_t));
                std::memcpy(vMat.ptr(i)+j, (vBuffer + ((i * uvCols) + j) * stride), sizeof(uint8_t));
            }
        }
    });

    // Resize the image to half the size. We don't need so much brightness resolution.
    cv::Mat yMatResized(height/2, width/2, CV_8UC1);
    cv::resize(yMat, yMatResized, cv::Size(), 0.5, 0.5);


    // Create the YUV channels vector
    std::vector<cv::Mat> yuvChannels = {yMatResized, uMat, vMat};

    cv::Mat yuvMat(height/2, width/2, CV_8UC3);

    cv::merge(yuvChannels, yuvMat);

    return yuvMat;
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



