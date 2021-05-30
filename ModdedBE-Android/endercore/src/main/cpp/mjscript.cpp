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
static bool hAppPlatform_supportsScripting( void *_this )
{
    bool original = rAppPlatform_supportsScripting( _this );
    LOGI( "HOOK AppPlatform::supportsScripting( THIS ) : %s",
          original ? "true" : "false" );
    return original;
}

static bool (*rAppPlatform_android_supportsScripting)( void* );
static bool hAppPlatform_android_supportsScripting( void *_this )
{
    bool original = rAppPlatform_android_supportsScripting( _this );
    LOGI( "HOOK AppPlatform_android::supportsScripting( THIS ) : %s -> true",
          original ? "true" : "false" );
    return true;
}

static bool (*rClientInstance_isScriptingEnabled)( void* );
static bool hClientInstance_isScriptingEnabled( void *_this )
{
    bool original = rClientInstance_isScriptingEnabled( _this );
    LOGI( "HOOK ClientInstance::isScriptingEnabled( THIS ) : %s",
          original ? "true" : "false" );
    return original;
}

static bool (*rExperiments_Scripting)( void* );
static bool hExperiments_Scripting( void *_this )
{
    bool original = rExperiments_Scripting( _this );
    LOGI( "HOOK Experiments::Scripting( THIS ) : %s",
          original ? "true" : "false" );
    return original;
}

static bool (*rFeatureToggles_isEnabled)( void*, int );
static bool hFeatureToggles_isEnabled( void *_this, int id )
{
    bool original = rFeatureToggles_isEnabled( _this, id );
//    LOGI( "HOOK FeatureToggles::isEnabled( THIS, %d ) : %s -> true",
//          id, original ? "true" : "false" );
    return true;
}

static bool (*rScriptEngine_isScriptingEnabled)( void* );
static bool hScriptEngine_isScriptingEnabled( void *_this )
{
    bool original = rScriptEngine_isScriptingEnabled( _this );
    LOGI( "HOOK ScriptEngine::isScriptingEnabled( THIS ) : %s -> true",
          original ? "true" : "false" );
    return true;
}

static bool (*rhbui_Feature_isEnabled)( void* );
static bool hhbui_Feature_isEnabled( void *_this )
{
    bool original = rhbui_Feature_isEnabled( _this );
    LOGI( "HOOK hbui::Feature::isEnabled( THIS ) : %s -> true",
          original ? "true" : "false" );
    return true;
}

//static std::string (*rI18n_get)( std::string const& );
//static std::string hI18n_get( std::string const &str )
//{
//    return "XHOOK IS WORKING!!!";
//}


JNIEXPORT jint JNI_OnLoad( JavaVM *vm, void *reserved )
{
    xhook_enable_debug( 1 );
                    // Match WHOLE LINE that ends with "libminecraftpe.so"
    xhook_register( ".*libminecraftpe\\.so$",
                    "_ZNK11AppPlatform17supportsScriptingEv",
                    (void* ) &hAppPlatform_supportsScripting,
                    (void**) &rAppPlatform_supportsScripting );
    xhook_register( ".*libminecraftpe\\.so$",
                    "_ZNK19AppPlatform_android17supportsScriptingEv",
                    (void* ) &hAppPlatform_android_supportsScripting,
                    (void**) &rAppPlatform_android_supportsScripting );
    xhook_register( ".*libminecraftpe\\.so$",
                    "_ZNK14ClientInstance18isScriptingEnabledEv",
                    (void* ) &hClientInstance_isScriptingEnabled,
                    (void**) &rClientInstance_isScriptingEnabled );
    xhook_register( ".*libminecraftpe\\.so$",
                    "_ZNK11Experiments9ScriptingEv",
                    (void* ) &hExperiments_Scripting,
                    (void**) &rExperiments_Scripting );
    xhook_register( ".*libminecraftpe\\.so$",
                    "_ZNK14FeatureToggles9isEnabledE15FeatureOptionID",
                    (void* ) &hFeatureToggles_isEnabled,
                    (void**) &rFeatureToggles_isEnabled );
    xhook_register( ".*libminecraftpe\\.so$",
                    "_ZN12ScriptEngine18isScriptingEnabledEv",
                    (void* ) &hScriptEngine_isScriptingEnabled,
                    (void**) &rScriptEngine_isScriptingEnabled );
    xhook_register( ".*libminecraftpe\\.so$",
                    "_ZNK4hbui7Feature9isEnabledEv",
                    (void* ) &hhbui_Feature_isEnabled,
                    (void**) &rhbui_Feature_isEnabled );
//    xhook_register( ".*libminecraftpe\\.so$",
//                    "_ZN4I18n3getERKNSt6__ndk112basic_stringIcNS0_11char_traitsIcEENS0_9allocatorIcEEEE",
//                    (void* ) &hI18n_get,
//                    (void**) &rI18n_get );
    xhook_refresh( 1 );
    return JNI_VERSION_1_6;
}
