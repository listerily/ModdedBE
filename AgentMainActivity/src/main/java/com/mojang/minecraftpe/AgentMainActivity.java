package com.mojang.minecraftpe;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class AgentMainActivity extends com.mojang.minecraftpe.MainActivity {

    private AssetManager patchAssetManager = null;
    private Resources patchResources = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<String> patchAssetsPath = getIntent().getStringArrayListExtra("ENDERCORE-PATCH-ASSETS");

        if(patchAssetsPath == null)
        {
            Log.e("EnderCore-AgentMain","Value ENDERCORE-PATCH-ASSETS in Intent defines to be null.");
            Log.e("EnderCore-AgentMain","Force close AgentMainActivity.");
            finish();
        }
        else
        {
            Log.i("EnderCore-AgentMain","Start patching assets.");
            try {
                patchAssetManager = AssetManager.class.newInstance();
            } catch (IllegalAccessException e) {
                Log.e("EnderCore-AgentMain","Failed to create new instance of AssetManager.");
                e.printStackTrace();
                Log.e("EnderCore-AgentMain","Force close AgentMainActivity.");
                finish();
                return;
            } catch (InstantiationException e) {
                Log.e("EnderCore-AgentMain","Failed to create new instance of AssetManager.");
                e.printStackTrace();
                Log.e("EnderCore-AgentMain","Force close AgentMainActivity.");
                finish();
                return;
            }

            try {
                int arrayListSize = patchAssetsPath.size();
                Method method = AssetManager.class.getMethod("addAssetPath",String.class);
                for(int i = 0;i < arrayListSize;++i)
                {
                    String path = patchAssetsPath.get(i);
                    Log.i("EnderCore-AgentMain","Patched [" + path + "].");
                    method.invoke(patchAssetManager,path);
                }
            } catch(Throwable t) {
                Log.e("EnderCore","Failed to patch assets.");
                t.printStackTrace();
                Log.e("EnderCore-AgentMain","Force close AgentMainActivity.");
                finish();
                return;
            }
            Log.i("EnderCore-AgentMain","Assets patched successfully.");
            Log.i("EnderCore-AgentMain","Starting to patch Resources.");
            Resources resourcesOriginal = super.getResources();
            patchResources = new Resources(patchAssetManager,resourcesOriginal.getDisplayMetrics(),resourcesOriginal.getConfiguration());
            Log.i("EnderCore-AgentMain","Resources patching succeed.");
            Log.i("EnderCore-AgentMain","Patching finished.Activity creating.");
            super.onCreate(savedInstanceState);
        }
    }

    @Override
    public AssetManager getAssets() {
        if(patchAssetManager != null)
            return patchAssetManager;
        return super.getAssets();
    }

    @Override
    public Resources getResources() {
        if(patchResources != null)
            return patchResources;
        return super.getResources();
    }
}