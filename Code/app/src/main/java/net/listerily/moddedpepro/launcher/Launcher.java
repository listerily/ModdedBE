package net.listerily.moddedpepro.launcher;

import android.app.Application;

public class Launcher {
    public static Launcher mInstance;

    public NModManager nmodManager;
    public ResourceManager resourceManager;
    public GameManager gameManager;
    public LibraryManager libraryManager;
    public LauncherOptions launcherOptions;

    public final String DIR_ROOT = "launcher";
    public final String DIR_NMODS = "nmods";
    public final String DIR_OPTIONS = "options.json";
    public final String DIR_LIBS = "libs";
    public final String DIR_CACHE = "launcher_cache";
    public final String DIR_RES = "resources";

    private Application context;

    public void init(Application context)
    {
        this.context = context;

        nmodManager = new NModManager(this);
        resourceManager = new ResourceManager(this);
        gameManager = new GameManager(this);
        libraryManager = new LibraryManager(this);
        launcherOptions = new LauncherOptions(this);
    }

    public ResourceManager getResourceManager()
    {
        return resourceManager;
    }

    public NModManager getNModManager() {
        return nmodManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public LauncherOptions getLauncherOptions() {
        return launcherOptions;
    }

    public LibraryManager getLibraryManager() {
        return libraryManager;
    }

    public void launchGame()
    {

    }

    public void onGameActivityCreate()
    {

    }

    public void onGameActivityFinish()
    {

    }

}
