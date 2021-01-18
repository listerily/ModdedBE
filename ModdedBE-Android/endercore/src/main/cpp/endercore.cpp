#include <jni.h>
#include <string>
#include <dlfcn.h>

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    void *handle = dlopen("libminecraftpe.so", RTLD_LAZY);

    return JNI_VERSION_1_6;
}