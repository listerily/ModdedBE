/**
 *  Unlock Mojang scripting
 *
 *  非常感谢 zhuowei (GitHub: zhuowei)！此方法来源于他的 BlockLauncher
 *  (GitHub: zhuowei/MCPELauncher)。事实上，zhuowei 是第一个在安卓设备上解锁脚本引擎的人。
 *  应 MiemieMethod (GitHub: MiemieMethod) 提议，现将其在最新版本中实现。
 *  由于 CydiaSubstrate 在 arm64-v8a 上不可用，此处使用的是 xHook (GitHub: iqiyi/xHook)。
 */

#include <jni.h>
#include <dlfcn.h>
#include <android/log.h>
#include <string>
#include "include/xhook.h"
#include "include/yurai/statichook.h"

#define LOG_TAG "EnderCore-mjscript"
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__))


static bool (*rAppPlatform_supportsScripting)( void* );
static bool hAppPlatform_supportsScripting( void *self )
{
    bool original = rAppPlatform_supportsScripting( self );
    LOGI( "HOOK AppPlatform::supportsScripting( THIS ) : %s",
          original ? "true" : "false" );
    return original;
}

static bool (*rScriptEngine_isScriptingEnabled)( void* );
static bool hScriptEngine_isScriptingEnabled( void *_this )
{
    bool original = rScriptEngine_isScriptingEnabled( _this );
    LOGI( "HOOK ScriptEngine::isScriptingEnabled( THIS ) : %s -> true",
          original ? "true" : "false" );
    return true;
}

static std::string (*rI18n_get)( std::string );
static std::string hI18n_get( std::string str )
{
    return "XHOOK IS WORKING!!!";
}


JNIEXPORT jint JNI_OnLoad( JavaVM *vm, void *reserved )
{
    xhook_enable_debug( 1 );
                    // Match WHOLE LINE that ends with "libminecraftpe.so"
    xhook_register( ".*libminecraftpe\\.so$",
                    "_ZNK11AppPlatform17supportsScriptingEv",
                    (void* ) &hAppPlatform_supportsScripting,
                    (void**) &rAppPlatform_supportsScripting );
    xhook_register( ".*libminecraftpe\\.so$",
                    "_ZN12ScriptEngine18isScriptingEnabledEv",
                    (void* ) &hScriptEngine_isScriptingEnabled,
                    (void**) &rScriptEngine_isScriptingEnabled );
//    xhook_register( ".*libminecraftpe\\.so$",
//                    "_ZN4I18n3getERKNSt6__ndk112basic_stringIcNS0_11char_traitsIcEENS0_9allocatorIcEEEE",
//                    (void* ) &hI18n_get,
//                    (void**) &rI18n_get );
    xhook_refresh( 1 );
    return JNI_VERSION_1_6;
}
