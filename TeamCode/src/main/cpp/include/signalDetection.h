//
// Created by Alex on 11/6/22.
//

#ifndef FTCROBOTCONTROLLER_2022_SIGNALDETECTION_H
#define FTCROBOTCONTROLLER_2022_SIGNALDETECTION_H

struct YUV{
    int y;
    int u;
    int v;
};

struct bound{
    float x = 0.0f;
    float y = 0.0f;
    float width = 0.0f;
    float height = 0.0f;
};

struct result{
    int level = -1;
    float score = 0.0f;
};


class signalDetection {
private:
    int x = 0;
    int y = 0;
    int width = 0;
    int height = 0;
    int scoreBrown = 0;
    int scoreGreen = 0;
    int scorePink = 0;
    const bound BROWN_BOUND = bound{0,0,0, 0};
    const bound PINK_BOUND = bound{0,0,0, 0};
    const bound GREEN_BOUND = bound{0,0,0, 0};
    const int MAX_WIDTH = 1920;
    const int MAX_HEIGHT = 1080;
    const int START = y * MAX_HEIGHT + x;
    const int END = height * MAX_HEIGHT + width;

public:
    signalDetection(int x, int y, int width, int height){
        this->x = x;
        this->y = y;
        this->width = width;
        this->height = height;
    }

    bool checkBounds(float x, float y, bound b){
        if (x >= b.x && x <= b.x + b.width && y >= b.y && y <= b.y + b.height) {
            return true;
        }
        return false;
    }

    result detectSignal(float yBuffer[], float uBuffer[], float vBuffer[]){
        int row = 0;
        for(int i=START; i<END; i++){
            if(checkBounds(uBuffer[i], vBuffer[i], BROWN_BOUND)){
                scoreBrown++;
            }else if(checkBounds(uBuffer[i], vBuffer[i], PINK_BOUND)){
                scorePink++;
            }else if(checkBounds(uBuffer[i], vBuffer[i], GREEN_BOUND)){
                scoreGreen++;
            }
            if(i % width == 0){
                i += MAX_WIDTH - width + x;
            }
        }
        float averageCount = (scoreGreen + scoreBrown + scorePink) / 3;

        if(scoreBrown > scorePink && scoreBrown > scoreGreen){
            return result{1, scoreBrown/averageCount};
        }else if(scorePink > scoreBrown && scorePink > scoreGreen){
            return result{2, scorePink/averageCount};
        }else if(scoreGreen > scoreBrown && scoreGreen > scorePink){
            return result{3, scorePink/averageCount};
        }else{
            return result{0, 100.0};
        }
    }
};


#endif //FTCROBOTCONTROLLER_2022_SIGNALDETECTION_H
