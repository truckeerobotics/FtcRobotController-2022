//
// Created by Alex on 11/14/22.
//

#ifndef FTCROBOTCONTROLLER_2022_IMAGEBUFFERS_H
#define FTCROBOTCONTROLLER_2022_IMAGEBUFFERS_H

#include <jni.h>

struct uInt8Buffer {
    uint8_t *data;
    uint64_t length;
    uInt8Buffer(uint8_t *data, uint64_t length);
    uInt8Buffer();
};

uInt8Buffer getBufferFromJavaByteBuffer(JNIEnv *env, jbyteArray javaByteArray);

static uInt8Buffer imageBufferY;
static uInt8Buffer imageBufferU;
static uInt8Buffer imageBufferV;

#endif //FTCROBOTCONTROLLER_2022_IMAGEBUFFERS_H
