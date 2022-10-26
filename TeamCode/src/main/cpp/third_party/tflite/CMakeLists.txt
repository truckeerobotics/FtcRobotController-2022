cmake_minimum_required(VERSION 3.5)
project(tflite)
add_library(tflite INTERFACE)

set(TFLITE_INCLUDE_DIRS "${CMAKE_CURRENT_SOURCE_DIR}/include")
set(TFLITE_THIRDPARTY_DIR "${TFLITE_INCLUDE_DIRS}/tensorflow/lite/tools/make/downloads")
set(TFLITE_LIBRARY_DIR "${CMAKE_CURRENT_SOURCE_DIR}/lib/${ANDROID_ABI}")

target_include_directories(tflite INTERFACE
        ${TFLITE_INCLUDE_DIRS}
        "${TFLITE_THIRDPARTY_DIR}/flatbuffers/include"
        "${TFLITE_THIRDPARTY_DIR}/absl"
        )

target_link_libraries(tflite INTERFACE
        ${TFLITE_LIBRARY_DIR}/libtensorflowlite.so
        ${TFLITE_LIBRARY_DIR}/libtensorflowlite_gpu_delegate.so
        ${TFLITE_LIBRARY_DIR}/libnnapi_implementation.so
        ${TFLITE_LIBRARY_DIR}/libnnapi_util.so
        ${TFLITE_LIBRARY_DIR}/libnnapi_delegate_no_nnapi_implementation.a
        )
