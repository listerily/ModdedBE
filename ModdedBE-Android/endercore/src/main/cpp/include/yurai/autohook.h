#ifndef YURAI_AUTOHOOK_H
#define YURAI_AUTOHOOK_H

#include <dlfcn.h>
#include "hook.h"

namespace yurai {

class AutoHook {

private:
    yurai_hook_t hook;

    template <typename T>
    static void *castToVoid(T hook) {
        union {
            T a;
            void *b;
        } hookUnion;
        hookUnion.a = hook;
        return hookUnion.b;
    }

public:
    AutoHook(void* lib, const char *sym, void *hook, void **orig) {
        this->hook = yurai_hook(lib, sym, hook, orig);
    }
    AutoHook(const char *sym, void *hook, void **orig) {
        static void* lib = dlopen("libminecraftpe.so", RTLD_LAZY);
        this->hook = yurai_hook(lib, sym, hook, orig);
    }

    ~AutoHook() {
        if (hook)
            yurai_delete_hook(hook);
    }


    // workaround for a warning
    template<typename T>
    AutoHook(const char *sym, T hook, void **orig) : AutoHook(sym, castToVoid(hook), orig) {
    }

};

}

#endif
