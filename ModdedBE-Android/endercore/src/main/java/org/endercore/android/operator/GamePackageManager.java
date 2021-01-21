package org.endercore.android.operator;

import android.content.Context;
import android.content.pm.PackageManager;

public final class GamePackageManager {
    public static final String PACKAGE_NAME = "com.mojang.minecraftpe";

    private String versionName;
    private String packageResourcePath;
    private boolean gameInstalled;

    public GamePackageManager(Context context) {
        gameInstalled = true;
        try {
            Context gameContext = context.createPackageContext(PACKAGE_NAME, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
            versionName = context.getPackageManager().getPackageInfo(gameContext.getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName;
            packageResourcePath = gameContext.getPackageResourcePath();
        } catch (PackageManager.NameNotFoundException e) {
            gameInstalled = false;
            versionName = "";
            packageResourcePath = null;
        }
    }

    public String getVersionName() {
        return versionName;
    }

    public boolean isGameInstalled() {
        return gameInstalled;
    }

    public String getPackageResourcePath() {
        return packageResourcePath;
    }
}
