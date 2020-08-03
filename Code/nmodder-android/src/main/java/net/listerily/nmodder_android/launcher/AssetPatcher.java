package net.listerily.nmodder_android.launcher;

import android.content.res.AssetManager;

import java.lang.reflect.Method;

public class AssetPatcher {

    AssetManager rootMgr;
    public AssetPatcher(AssetManager rootMgr) {
        this.rootMgr = rootMgr;
    }

    public void patch(String packageResourcePath) throws Throwable
    {
        Method method = AssetManager.class.getMethod("addAssetPath", String.class);
        method.invoke(rootMgr, packageResourcePath);
    }

    public AssetManager getAssetManager() {
        return rootMgr;
    }
}
