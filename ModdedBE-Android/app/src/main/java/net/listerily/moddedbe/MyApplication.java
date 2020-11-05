package net.listerily.moddedbe;

import android.app.Application;

import net.listerily.endercore.android.EnderCore;

public class MyApplication extends Application
{
    public void onCreate() {
        super.onCreate();
        EnderCore.instance.initialize(this);
    }
}