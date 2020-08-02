package net.listerily.moddedpepro;

import android.app.Application;
import android.content.res.AssetManager;

import net.listerily.nmodder_android.launcher.Launcher;

import java.io.IOException;

public class MApplication extends Application
{
    public void onCreate() {
        super.onCreate();
        try {
            Launcher.mInstance.init(this);
        } catch (IOException e) {
            //todo init failed
        }
    }

    @Override
    public AssetManager getAssets() {
        return super.getAssets();
    }
}