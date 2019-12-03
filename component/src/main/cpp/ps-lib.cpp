#include <jni.h>
#include <string>

void qS(unsigned char array[], int size);

void _qS(unsigned char array[], int low, int high);

int findPivotIndex(unsigned char array[], int low, int high);


extern "C"
JNIEXPORT jstring JNICALL
Java_com_easymi_component_utils_EncApi_en(JNIEnv *env, jobject instance, jstring aesPassword ,jstring content_) {
    //获取AesCore
    jclass aesCoreClass = env->FindClass("com/easymi/component/utils/AesUtil");
    //获取aesEncrypt静态方法
    jmethodID aesEncrypt = env->GetStaticMethodID(aesCoreClass, "aesEncrypt",
                                                  "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");

//    //vipara & password 都为 RWA7XBFXQBA2S00E
//    //为了防止测评直接看到明码，做点小手段让他肉眼看不出来
//
//    //A到Z字母乱序
//    unsigned char a[27] = {'K', 'L', 'M', 'Y', 'Z', 'D', 'E', 'N', 'O', 'P', 'Q', 'R', 'U', 'H',
//                           'A', 'F', 'G', 'V', 'W', 'X', 'I', 'J', 'S', 'T', 'B', 'C'};
//    //0到9数组乱序
//    unsigned char b[11] = {'9', '1', '3', '4', '0', '5', '6', '7', '8', '2'};
//    int is[] = {17, 22, 0, 7, 23, 1, 5, 23, 16, 1, 0, 2, 18, 0, 0, 4};
//
//    qS(a, 26);
//    qS(b, 10);
//    char r[17] = {0};
//    for (int i = 0; i < 16; i++) {
//        r[i] = a[is[i]];
//    }
//    r[3] = b[7];
//    r[11] = b[2];
//    r[13] = b[0];
//    r[14] = b[0];
//
////    jstring vipara = env->NewStringUTF("RWA7XBFXQBA2S00E");
////    jstring password = env->NewStringUTF("RWA7XBFXQBA2S00E");
//
//    jstring vipara = env->NewStringUTF(r);
//    jstring password = env->NewStringUTF(r);

    jstring  str=(jstring)env->CallStaticObjectMethod(aesCoreClass,aesEncrypt,aesPassword,content_,aesPassword);

    if (str != NULL) {
        const char *newStr = env->GetStringUTFChars(str, 0);
        return env->NewStringUTF(newStr);
    } else {
        return NULL;
    }

}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_easymi_component_utils_EncApi_dec(JNIEnv *env, jobject instance, jstring aesPassword , jstring content_) {
    //获取AesCore
    jclass aesCoreClass = env->FindClass("com/easymi/component/utils/AesUtil");
    //获取aesEncrypt静态方法
    jmethodID aesDecrypt = env->GetStaticMethodID(aesCoreClass, "aesDecrypt",
                                                  "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
//
//    //vipara & password 都为 RWA7XBFXQBA2S00E
//    //为了防止测评直接看到明码，做点小手段让他肉眼看不出来
//
//    //A到Z字母乱序
//    unsigned char a[27] = {'K', 'L', 'M', 'Y', 'Z', 'D', 'E', 'N', 'O', 'P', 'Q', 'R', 'U', 'H',
//                           'A', 'F', 'G', 'V', 'W', 'X', 'I', 'J', 'S', 'T', 'B', 'C'};
//    //0到9数组乱序
//    unsigned char b[11] = {'9', '1', '3', '4', '0', '5', '6', '7', '8', '2'};
//    int is[] = {17, 22, 0, 7, 23, 1, 5, 23, 16, 1, 0, 2, 18, 0, 0, 4};
//
//    qS(a, 26);
//    qS(b, 10);
//
//    char r[17] = {0};
//    for (int i = 0; i < 16; i++) {
//        r[i] = a[is[i]];
//    }
//
//    r[3] = b[7];
//    r[11] = b[2];
//    r[13] = b[0];
//    r[14] = b[0];
//
////    jstring vipara = env->NewStringUTF("RWA7XBFXQBA2S00E");
////    jstring password = env->NewStringUTF("RWA7XBFXQBA2S00E");
//
//    jstring vipara = env->NewStringUTF(r);
//    jstring password = env->NewStringUTF(r);

    jstring str = (jstring) env->CallStaticObjectMethod(aesCoreClass, aesDecrypt, aesPassword, content_,
                                                        aesPassword);

    if (str != NULL) {
        const char *newStr = env->GetStringUTFChars(str, 0);
        return env->NewStringUTF(newStr);
    } else {
        return NULL;
    }
}


void qS(unsigned char array[], int size) {
    _qS(array, 0, size - 1);
}

void _qS(unsigned char array[], int low, int high) {
    int pivotIndex = 0;
    if (low < high) {
        pivotIndex = findPivotIndex(array, low, high);
        _qS(array, low, pivotIndex - 1);
        _qS(array, pivotIndex + 1, high);
    }
}

int findPivotIndex(unsigned char array[], int low, int high) {
    unsigned char pivot = array[low];
    while (low < high) {
        while (low < high && array[high] >= pivot) { --high; }
        array[low] = array[high];
        while (low < high && array[low] <= pivot) { ++low; }
        array[high] = array[low];
    }
    array[low] = pivot;
    return low;
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_easymi_component_utils_EncApi_getPubKey(JNIEnv *env, jobject instance) {
    std::string value = "";
    return env->NewStringUTF(value.c_str());
}

