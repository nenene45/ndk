#include <Calculator.h>

JNIEXPORT jint JNICALL Java_com_android_ndk_MainActivity_getSum

(JNIEnv *env, jobject thiz, jint num1, jint num2) {
    return num1 + num2;
}