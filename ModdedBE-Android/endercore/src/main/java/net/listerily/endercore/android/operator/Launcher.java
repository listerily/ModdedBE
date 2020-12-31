package net.listerily.endercore.android.operator;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.listerily.endercore.android.EnderCore;
import net.listerily.endercore.android.exception.LauncherException;
import net.listerily.endercore.android.nmod.NMod;
import net.listerily.endercore.android.utils.CPUArch;
import net.listerily.endercore.android.utils.FileUtils;
import net.listerily.endercore.android.utils.Patcher;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import dalvik.system.DexClassLoader;

public class Launcher {
    private final EnderCore core;
    private GameInitializationListener listener;
    private ArrayList<String> patchAssetPath;
    private boolean initializedGame;

    public Launcher(EnderCore core)
    {
        initializedGame = false;
        patchAssetPath = new ArrayList<>();
        this.core = core;
        listener = new GameInitializationListener() {
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
            public void onLoadAppAsset(String name) {

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
            public void onLoadNModJavaLibrary(NMod nmod, String name) {

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
            public void onSuspend() {

            }
        };
    }

    public void initializeGame(Context context) throws LauncherException
    {
        listener.onStart();
        try
        {
            // Check Availability
            if(!core.getGamePackageManager().isGameInstalled())
                throw new LauncherException("Minecraft Game is not installed.Please install game.");

            // Set Variants
            FileManager fileManager = new FileManager(context);
            boolean[] dexExists = new boolean[10];
            for(int i = 0;i < 9;++i)
                dexExists[i] = false;

            // Copy Game Files
            listener.onLoadGameFilesStart();
            try {
                File resPath = new File(core.getGamePackageManager().getPackageResourcePath());
                File[] listApk = resPath.getParentFile().listFiles();
                //Copy native libraries
                String[] supportedAbis = CPUArch.getSupportedAbis();
                String[] targetLibs = {"libminecraftpe.so","libfmod.so"};
                boolean[] libsCopied = new boolean[targetLibs.length];
                for(int i = 0;i < targetLibs.length;++i)
                    libsCopied[i] = false;

                for(int i = 0;i < targetLibs.length;++i)
                {
                    String libName = targetLibs[i];
                    for(String thisAbi:supportedAbis)
                    {
                        if(!libsCopied[i])
                        {
                            for(File apk:listApk)
                            {
                                if(!apk.isFile())
                                    continue;

                                ZipEntry targetEntry;
                                ZipFile apkFile;
                                try {
                                    apkFile = new ZipFile(apk);
                                    targetEntry = apkFile.getEntry("lib/" + thisAbi + "/" + libName);
                                }catch(IOException e) {
                                    continue;
                                }

                                if(targetEntry != null)
                                {
                                    FileUtils.copyFile(apkFile.getInputStream(targetEntry),new File(fileManager.getNativeLibsSavedPath(),libName));
                                    libsCopied[i] = true;
                                }
                            }
                        }

                    }
                }
                //Copy Dex files
                for(int i = 9;i >= 0;--i)
                {
                    String libName = "classes" + (i == 0?"":i) + ".dex";
                    for(File apk:listApk)
                    {
                        if(!apk.isFile())
                            continue;

                        ZipEntry targetEntry;
                        ZipFile apkFile;
                        try {
                            apkFile = new ZipFile(apk);
                            targetEntry = apkFile.getEntry(libName);
                        }catch(IOException e) {
                            continue;
                        }

                        if(targetEntry != null)
                        {
                            FileUtils.copyFile(apkFile.getInputStream(targetEntry),new File(fileManager.getDexLibsSavedPath(),libName));
                            dexExists[i] = true;
                        }
                    }
                }
            }
            catch(IOException ioexception) {
                throw new LauncherException("Extract game libraries failed.",ioexception);
            }

            Log.d("EnderCore-Launcher","Game Files Copied");

            try {
                for(int i = 9;i >= 0;--i)
                {
                    String dexLibName = "classes" + (i == 0?"":i) + ".dex";
                    File path = new File(fileManager.getDexLibsSavedPath(),dexLibName);
                    if(dexExists[i])
                        Patcher.patchDexPath(context.getClassLoader(),path.getAbsolutePath(),FileManager.getDexOptimizeDir(path).getAbsolutePath());
                }

                //Crack License Checker
                File licenseCrackerDex = new File(fileManager.getDexLibsSavedPath(),FileManager.ASSETS_NAME_CRACKER_DEX);
                FileUtils.copyFile(context.getAssets().open(FileManager.ASSETS_FILE_CRACKER_DEX),licenseCrackerDex);
                Patcher.patchDexPath(context.getClassLoader(),licenseCrackerDex.getAbsolutePath(),FileManager.getDexOptimizeDir(licenseCrackerDex).getAbsolutePath());
            }
            catch (IllegalAccessException | IOException | NoSuchFieldException e) {
                throw new LauncherException("Exception occurred while loading *.dex file.",e);
            }

            Log.d("EnderCore-Launcher","Dex File Loaded.");

            // Load Native Libs
            try
            {
                Patcher.patchNativeLibraryDir(context.getClassLoader(),fileManager.getNativeLibsSavedPath().getAbsolutePath());
            } catch (IllegalAccessException | IOException | ClassNotFoundException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
                throw new LauncherException("Exception occurred while loading *.so file.", e);
            }

            try
            {
                listener.onLoadNativeLibrariesStart();
                listener.onLoadNativeLibrary("libc++_shared.so");
                System.loadLibrary("c++_shared");
                listener.onLoadNativeLibrary("libfmod.so");
                System.loadLibrary("fmod");
                listener.onLoadNativeLibrary("libminecraftpe.so");
                System.loadLibrary("minecraftpe");
                listener.onLoadNativeLibrary("libyurai.so");
                System.loadLibrary("yurai");
                listener.onLoadNativeLibrary("libsubstrate.so");
                System.loadLibrary("substrate");
                listener.onLoadNativeLibrary("libendercore.so");
                System.loadLibrary("endercore");
                listener.onLoadNativeLibrariesFinish();
            }
            catch(Error error)
            {
                throw new LauncherException("Load game libraries failed.",error);
            }

            Log.d("EnderCore-Launcher","Native Libs Loaded.");

            // Load Resources
            listener.onLoadResourcesStart();
            patchAssetPath.add(context.getPackageResourcePath());
            patchAssetPath.add(core.getGamePackageManager().getPackageResourcePath());
            listener.onLoadResourcesFinish();

            listener.onLoadGameFilesFinish();


            // Arrange
            listener.onArrange();
            //todo arrange
        }
        catch(Throwable e) {
            listener.onSuspend();
            throw new LauncherException("Unexpected fatal error caused in game initialization.", e);
        }
        initializedGame = true;
        listener.onFinish();
    }

    public void startGame(Context context) throws LauncherException {
        if(!initializedGame)
            throw new RuntimeException("Game isn't initialized.Please initialize game (Launcher.initializeGame) before start game.");
        try {
            FileManager fileManager = new FileManager(context);
            File dir = fileManager.getDexLibsSavedPath();
            FileUtils.copyFile(context.getAssets().open(FileManager.ASSETS_FILE_AGENT_DEX),new File(dir,FileManager.ASSETS_NAME_AGENT_DEX));
            Patcher.patchDexPath(context.getClassLoader(),new File(dir,FileManager.ASSETS_NAME_AGENT_DEX).getAbsolutePath(),dir.getAbsolutePath());
            DexClassLoader dexClassLoader = new DexClassLoader(new File(dir,FileManager.ASSETS_NAME_AGENT_DEX).getAbsolutePath(),dir.getAbsolutePath(),null,context.getClass().getClassLoader());
            Class<?> activityClass = dexClassLoader.loadClass("com.mojang.minecraftpe.AgentMainActivity");
            Intent launchIntent = new Intent(context,activityClass);
            launchIntent.putExtra("ENDERCORE-PATCH-ASSETS",patchAssetPath);
            context.startActivity(launchIntent);
        } catch(IOException | NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
            throw new LauncherException("Start game failed.",e);
        }
    }

    public void setGameInitializationListener(GameInitializationListener listener)
    {
        this.listener = listener;
    }

    public interface GameInitializationListener
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
        void onLoadAppAsset(String name);
        void onLoadAppResource(String name);
        void onLoadResourcesFinish();
        void onLoadGameFilesFinish();
        void onLoadNModsStart();
        void onLoadNMod(NMod nmod);
        void onLoadNModNativeLibrary(NMod nmod,String name);
        void onLoadNModJavaLibrary(NMod nmod,String name);
        void onLoadNModAsset(String name);
        void onLoadNModsFinish();
        void onArrange();
        void onFinish();
        void onSuspend();
    }
}
