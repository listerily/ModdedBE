package com.mojang.minecraftpe;

import android.app.NativeActivity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;

public abstract class MainActivity extends NativeActivity {
    public native void nativeInitializeXboxLive(long xalInitArgs, long xblInitArgs);

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
        FirebaseApp.initializeApp(MainActivity.this.getApplicationContext());
        nativeInitializeXboxLive(xalInitArgs, xblInitArgs);
    }
}
