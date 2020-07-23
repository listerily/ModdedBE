package net.listerily.moddedpepro;

import android.app.Application;
import android.content.res.AssetManager;

import androidx.multidex.MultiDex;

import net.listerily.moddedpepro.launcher.Launcher;

public class MApplication extends Application
{
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        Launcher.mInstance.init(this);
    }

    @Override
    public AssetManager getAssets() {
        return super.getAssets();
    }
}