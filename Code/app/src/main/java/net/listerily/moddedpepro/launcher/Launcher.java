package net.listerily.moddedpepro.launcher;

import android.app.Application;
import android.content.Context;

import net.listerily.moddedpepro.launcher.nmod.NMod;

public class Launcher {
    public static Launcher mInstance = new Launcher();

    public NModManager nmodManager;
    public ResourceManager resourceManager;
    public GameManager gameManager;
    public LibraryManager libraryManager;
    public LauncherOptions launcherOptions;

    public final String DIR_ROOT = "launcher";
    public final String DIR_NMODS = "nmods";
    public final String DIR_OPTIONS = "options.json";
    public final String DIR_NMODS_DATA = "nmods.json";
    public final String DIR_LIBS = "libs";
    public final String DIR_CACHE = "launcher_cache";
    public final String DIR_RES = "resources";

    private Application context;
    private LauncherListener listener;

    private Launcher()
    {
        context = null;
        listener = new LauncherListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onLoadGameFilesStart() {

            }

            @Override
            public void onLoadNativeLibrariesStart() {

            }

            @Override
            public void onLoadNativeLibrary(String name) {

            }

            @Override
            public void onLoadNativeLibrariesFinish() {

            }

            @Override
            public void onLoadJavaLibrariesStart() {

            }

            @Override
            public void onLoadJavaLibrary(String name) {

            }

            @Override
            public void onLoadJavaLibrariesFinish() {

            }

            @Override
            public void onLoadResourcesStart() {

            }

            @Override
            public void onLoadAppAssest(String name) {

            }

            @Override
            public void onLoadAppResource(String name) {

            }

            @Override
            public void onLoadResourcesFinish() {

            }

            @Override
            public void onLoadGameFilesFinish() {

            }

            @Override
            public void onLoadNModsStart() {

            }

            @Override
            public void onLoadNMod(NMod nmod) {

            }

            @Override
            public void onLoadNModNativeLibrary(NMod nmod, String name) {

            }

            @Override
            public void onLoadNModAsset(String name) {

            }

            @Override
            public void onLoadNModsFinish() {

            }

            @Override
            public void onArrange() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onError(Error error) {

            }
        };
    }

    public void init(Application context)
    {
        this.context = context;
        launcherOptions = new LauncherOptions(this);
        gameManager = new GameManager(this);
        libraryManager = new LibraryManager(this);
        resourceManager = new ResourceManager(this);
        nmodManager = new NModManager(this);
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

    public Context getContext()
    {
        return context;
    }

    public void setLauncherListener(LauncherListener listener)
    {
        this.listener = listener;
    }


    public void onGameActivityCreate()
    {

    }

    public void onGameActivityFinish()
    {

    }

    public interface LauncherListener
    {
        void onStart();
        void onLoadGameFilesStart();
        void onLoadNativeLibrariesStart();
        void onLoadNativeLibrary(String name);
        void onLoadNativeLibrariesFinish();
        void onLoadJavaLibrariesStart();
        void onLoadJavaLibrary(String name);
        void onLoadJavaLibrariesFinish();
        void onLoadResourcesStart();
        void onLoadAppAssest(String name);
        void onLoadAppResource(String name);
        void onLoadResourcesFinish();
        void onLoadGameFilesFinish();
        void onLoadNModsStart();
        void onLoadNMod(NMod nmod);
        void onLoadNModNativeLibrary(NMod nmod,String name);
        void onLoadNModAsset(String name);
        void onLoadNModsFinish();
        void onArrange();
        void onFinish();
        void onError(Error error);
    }
}
