package net.listerily.nmodder_android.launcher;

import android.app.Application;
import android.content.Context;

import net.listerily.nmodder_android.nmod.NMod;
import net.listerily.nmodder_android.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Launcher {
    public static Launcher mInstance = new Launcher();

    public NModManager nmodManager;
    public ResourceManager resourceManager;
    public GameManager gameManager;
    public LibraryManager libraryManager;
    public LauncherOptions launcherOptions;

    public final static String DIR_ROOT = "nmodder-android-data";
    public final static String DIR_NMODS = "nmods";
    public final static String DIR_LIBS = "libs";
    public final static String DIR_CACHE = "nmodder-android-cache";
    public final static String DIR_RES = "resources";
    public final static String FILE_OPTIONS = "options.json";
    public final static String FILE_NMODS_DATA = "nmods.json";

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

    public void init(Application context) throws IOException
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

    public void launchGame() throws LauncherException,IOException
    {
        listener.onStart();
        // Variants
        File nativeDir = new File(gameManager.getNativeLibraryDir());
        File dataDir = context.getDir(DIR_ROOT,0);
        File libsDir = new File(dataDir,DIR_LIBS);
        File libMinecraftpe = new File(nativeDir,"libminecraftpe.so");
        File libFmod = new File(libsDir,"libfmod.so");
        String archName = libsDir.getName();
        // Copy Game Files
        listener.onLoadGameFilesStart();
        Utils.copy(new File(nativeDir,"libminecraftpe.so"),libMinecraftpe);
        Utils.copy(new File(nativeDir,"libfmod.so"),libFmod);
        // Load Native Libs
        listener.onLoadNativeLibrariesStart();
        listener.onLoadNativeLibrary("libfmod.so");
        System.load(libFmod.getAbsolutePath());
        listener.onLoadNativeLibrary("libminecraftpe.so");
        System.load(libMinecraftpe.getAbsolutePath());
        listener.onLoadNativeLibrary("libsubstrate.so");
        System.loadLibrary("substrate");
        listener.onLoadNativeLibrary("libnmodder.so");
        System.loadLibrary("nmodder");
        listener.onLoadNativeLibrariesFinish();
        // Load Java libraries
        listener.onLoadJavaLibrariesStart();
        // todo java libs
        listener.onLoadJavaLibrariesFinish();
        // Load Resources
        listener.onLoadResourcesStart();
        // todo load res
        listener.onLoadResourcesFinish();
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
