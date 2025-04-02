#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_itsec_rndforge_crackme_nativelib_NativeLib_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "was_it_still_too_easy_for_you?";
    return env->NewStringUTF(hello.c_str());
}