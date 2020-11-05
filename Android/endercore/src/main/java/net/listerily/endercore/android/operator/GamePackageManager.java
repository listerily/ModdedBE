package net.listerily.endercore.android.operator;

import android.content.Context;
import android.content.pm.PackageManager;

import net.listerily.endercore.android.EnderCore;

public class GamePackageManager {
    public static final String PACKAGE_NAME = "com.mojang.minecraftpe";

    private String packageName;
    private String versionName;
    private String packageResourcePath;
    private String nativeLibraryDir;
    private boolean gameInstalled;

    public GamePackageManager(Context context, EnderCore enderCore)
    {
        packageName = PACKAGE_NAME;
        gameInstalled = true;
        if (!enderCore.getEnderCoreOptions().getPackageName().equals(PACKAGE_NAME))
            packageName = enderCore.getEnderCoreOptions().getPackageName();
        try {
            Context gameContext = context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
            versionName = context.getPackageManager().getPackageInfo(gameContext.getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName;
            nativeLibraryDir = gameContext.getApplicationInfo().nativeLibraryDir;
            packageResourcePath = gameContext.getPackageResourcePath();
        } catch (PackageManager.NameNotFoundException e) {
            gameInstalled = false;
            versionName = "";
            nativeLibraryDir = null;
            packageResourcePath = null;
        }
    }

    public String getNativeLibraryDir() {
        return nativeLibraryDir;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean isGameInstalled() {
        return gameInstalled;
    }

    public String getPackageResourcePath()
    {
        return packageResourcePath;
    }
}
