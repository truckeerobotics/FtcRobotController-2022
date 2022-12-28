

# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

# Declares and names the project.

project("Nova")

#add_subdirectory(
#        "third_party/tflite" EXCLUDE_FROM_ALL)

#add_subdirectory(third_party/tflite)

#include_directories(src/main/cpp/third_party/tflite/include/)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        Nova

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
        "include/camera.h" "src/camera.cpp" "include/signalSleeveDetection.h" "src/signalSleeveDetection.cpp" "include/imageBuffers.h" "src/imageBuffers.cpp" "include/logger.h" "src/logger.cpp" "include/transformLocator.h" "src/transformLocator.cpp")

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.





##set(OPENCV_DIRS ${CMAKE_SOURCE_DIR}/../../../../sdk/native/jni/${ANDROID_ABI}/OpenCVConfig.cmake)
#set(OPENCV_DIRS /Users/alex/StudioProjects/FtcRobotController-2022/sdk/native/jni/abi-${ANDROID_ABI}/OpenCVConfig.cmake)
##set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} ${CMAKE_SOURCE_DIR}/../../../../sdk/native/jni/${ANDROID_ABI}/OpenCVConfig.cmake)
#message("PATH: ${OPENCV_DIRS}")
#
#find_package(OpenCV 4.6 REQUIRED java)






set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")
include_directories(${CMAKE_SOURCE_DIR}/third_party/opencv/includes/)
message(${CMAKE_SOURCE_DIR}/third_party/opencv/includes)

message(${CMAKE_SOURCE_DIR}/third_party/opencv/libs/${ANDROID_ABI}/libopencv_java4.so)

add_library(lib_opencv SHARED IMPORTED)

set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/third_party/opencv/libs/${ANDROID_ABI}/libopencv_java4.so)





find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )


# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                        Nova

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} ${log-lib} lib_opencv) #tflite
#target_link_libraries(native-lib ${OpenCV_LIBRARIES})


