// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("ftcrobotcontroller");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("ftcrobotcontroller")
//      }
//    }
#include "main.h"

JNIEXPORT jint Java_org_firstinspires_ftc_teamcode_Robot_main(JNIEnv *env, jobject obj) {
    std::cout << "Hello" << std::endl;
    return 1;
}
