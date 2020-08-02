#include <jni.h>
#include <string>

JNIEXPORT jint JNI_OnLoad(JavaVM* vm,void* reserved)
{
    return JNI_VERSION_1_6;
}