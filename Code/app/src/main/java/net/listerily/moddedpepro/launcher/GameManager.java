package net.listerily.moddedpepro.launcher;

import android.content.Context;
import android.content.pm.PackageManager;

public class GameManager {
    public static final String PACKAGE_NAME = "com.mojang.minecraftpe";

    private Launcher launcher;
    private String packageName;
    private Context gameContext;
    private boolean gameInstalled;
    private String versionName;
    private String nativeLibraryDir;

    public GameManager(Launcher launcher)
    {
        this.launcher = launcher;

        packageName = PACKAGE_NAME;
        gameInstalled = true;
        if (!launcher.getLauncherOptions().getPackageName().equals(PACKAGE_NAME))
            packageName = launcher.getLauncherOptions().getPackageName();
        try {
            gameContext = launcher.getContext().createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
            versionName = launcher.getContext().getPackageManager().getPackageInfo(gameContext.getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName;
            nativeLibraryDir = gameContext.getApplicationInfo().nativeLibraryDir;
        } catch (PackageManager.NameNotFoundException e) {
            gameInstalled = false;
            versionName = "";
            nativeLibraryDir = null;
        }
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

    public Context getGameContext() {
        return gameContext;
    }
}
