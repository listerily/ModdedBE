package net.listerily.moddedpepro;

import android.app.Application;
import android.content.res.AssetManager;

import net.listerily.nmodder_android.launcher.Launcher;

public class MApplication extends Application
{
    public void onCreate() {
        super.onCreate();
        Launcher.mInstance.init(this);
    }

    @Override
    public AssetManager getAssets() {
        return super.getAssets();
    }
}