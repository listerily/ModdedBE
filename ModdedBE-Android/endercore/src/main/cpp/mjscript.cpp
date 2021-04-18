/**
 *  Unlock Mojang scripting
 *
 *  非常感谢 Zhuowei！此方法来源于他的 BlockLauncher (https://github.com/zhuowei/MCPELauncher)。事实
 *  上，Zhuowei 是第一个在安卓设备上解锁脚本引擎的人。
 *  由于 CydiaSubstrate 在 arm64-v8a 上不可用，此处使用的是 yurai
 *  (https://github.com/MCMrARM/yurai-api)。这个工具最初被用于 BDS Modloader，并带有源码和文档
 *  (https://github.com/minecraft-linux/server-modloader/wiki)。
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
static bool hScriptEngine_isScriptingEnabled( void *self )
{
    bool original = rScriptEngine_isScriptingEnabled( self );
    LOGI( "HOOK ScriptEngine::isScriptingEnabled( THIS ) : %s",
          original ? "true" : "false" );
    return original;
}

static std::string (*rI18n_get)( std::string );
static std::string hI18n_get( std::string str )
{
    return "XHOOK IS WORKING!!!";
}


class AppPlatform {};
class I18n {};
class ScriptEngine {};

TInstanceHook( bool, _ZNK11AppPlatform17supportsScriptingEv, AppPlatform )
{
    bool result = original(this);
    LOGI( "HOOK AppPlatform::supportsScripting( THIS ) : %s",
          result ? "true" : "false" );
    return result;
}

TInstanceHook( bool, _ZN12ScriptEngine18isScriptingEnabledEv, ScriptEngine )
{
    bool result = original(this);
    LOGI( "HOOK ScriptEngine::isScriptingEnabled( THIS ) : %s",
          result ? "true" : "false" );
    return result;
}

TStaticHook( std::string, _ZN4I18n3getERKNSt6__ndk112basic_stringIcNS0_11char_traitsIcEENS0_9allocatorIcEEEE, I18n, std::string str )
{
    return "YURAI IS WORKING!!!";
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
    xhook_register( ".*libminecraftpe\\.so$",
                    "_ZN4I18n3getERKNSt6__ndk112basic_stringIcNS0_11char_traitsIcEENS0_9allocatorIcEEEE",
                    (void* ) &hI18n_get,
                    (void**) &rI18n_get );
    xhook_refresh( 1 );
    return JNI_VERSION_1_6;
}
