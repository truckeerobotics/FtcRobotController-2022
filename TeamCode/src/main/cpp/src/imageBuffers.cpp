//
// Created by Alex on 11/14/22.
//

#include "../include/imageBuffers.h"

uInt8Buffer::uInt8Buffer(uint8_t *data, uint64_t length) {
    this->data = data; this->length = length;
}
uInt8Buffer::uInt8Buffer() {
    this->data = nullptr; this->length = 0;
}

uInt8Buffer getBufferFromJavaByteBuffer(JNIEnv *env, jbyteArray javaByteArray) {
    jbyte *bufferJavaRef = NULL;
    bufferJavaRef = env->GetByteArrayElements(javaByteArray, NULL);
    return uInt8Buffer((uint8_t *)bufferJavaRef, (uint64_t)(env->GetArrayLength(javaByteArray)));
}