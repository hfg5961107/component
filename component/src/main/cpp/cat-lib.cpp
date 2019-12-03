#include <jni.h>
#include <sys/ptrace.h>
#include <stdio.h>

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_easymi_component_cat_Cat_getSignByte(JNIEnv *env, jobject instance) {

    //instance表示该native方法所在类的实例，这里表示Cat类的实例
    jclass native_class = env->GetObjectClass(instance);

    //获取成员变量mContext
    jfieldID context_fieldId = env->GetFieldID(native_class, "mContext",
                                               "Landroid/content/Context;");
    jobject context = env->GetObjectField(instance, context_fieldId);

    //context class
    jclass context_class = env->GetObjectClass(context);

    //获取context的getPackageManager方法id
    jmethodID pm_id = env->GetMethodID(context_class, "getPackageManager",
                                       "()Landroid/content/pm/PackageManager;");
    //调用PackageManager.getPackageManager并返回PackageManager
    jobject pm_obj = env->CallObjectMethod(context, pm_id);

    //PackageManager class
    jclass pm_clazz = env->GetObjectClass(pm_obj);

    //获取context.getPackageName()方法id
    jmethodID pkgNameId = env->GetMethodID(context_class, "getPackageName", "()Ljava/lang/String;");
    //调用context.getPackageName()获取包名
    jstring pkg_str = static_cast<jstring>(env->CallObjectMethod(context, pkgNameId));


    // 获取PackageManager.getPackageInfo方法id
    jmethodID package_info_id = env->GetMethodID(pm_clazz, "getPackageInfo",
                                                 "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");

    //调用PackageManager.getPackageInfo方法，并传入参数id,获取PackageInfo
    jobject pi_obj = env->CallObjectMethod(pm_obj, package_info_id, pkg_str, 64);

    //获得PackageInfo类
    jclass pi_clazz = env->GetObjectClass(pi_obj);


    // 获得signatures ID,然后获取Field
    jfieldID signatures_fieldId = env->GetFieldID(pi_clazz, "signatures",
                                                  "[Landroid/content/pm/Signature;");
    jobject signatures_obj = env->GetObjectField(pi_obj, signatures_fieldId);

    //获取signs[0]
    jobjectArray signaturesArray = (jobjectArray) signatures_obj;

    jobject signature_obj = env->GetObjectArrayElement(signaturesArray, 0);
    jclass signature_class = env->GetObjectClass(signature_obj);

    //调用signs[0].toByteArray()
    jmethodID array_id = env->GetMethodID(signature_class, "toByteArray", "()[B");
    jbyteArray array = static_cast<jbyteArray >(env->CallObjectMethod(signature_obj, array_id));

    return array;
}

void anti_debug() {
    //ptrace 自己
    long res = ptrace(PTRACE_TRACEME, 0, 0, 0);
    if (res == -1) {
        printf("....");
    }
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    anti_debug();
    return JNI_VERSION_1_6;
}


