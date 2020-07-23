package net.listerily.moddedpepro;

import android.app.Application;
import android.content.res.AssetManager;

import androidx.multidex.MultiDex;

public class MApplication extends Application
{
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }

    @Override
    public AssetManager getAssets() {
        return super.getAssets();
    }
}