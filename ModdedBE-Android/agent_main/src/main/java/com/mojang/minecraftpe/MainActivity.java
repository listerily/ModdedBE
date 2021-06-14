package com.mojang.minecraftpe;

import android.app.NativeActivity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;

public abstract class MainActivity extends NativeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        throw new RuntimeException("Stub!");
    }

    public AssetManager getAssets() {
        throw new RuntimeException("Stub!");
    }

    public Resources getResources() {
        throw new RuntimeException("Stub!");
    }

    public void initializeXboxLive(long xalInitArgs, long xblInitArgs) {
        throw new RuntimeException("Stub!");
    }
}
