package net.listerily.endercore.android.operator;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.listerily.endercore.android.EnderCore;
import net.listerily.endercore.android.exception.LauncherException;
import net.listerily.endercore.android.interf.IFileEnvironment;
import net.listerily.endercore.android.interf.IInitializationListener;
import net.listerily.endercore.android.interf.implemented.InitializationListener;
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

public final class Launcher {
    private final EnderCore core;
    private IInitializationListener listener;
    private final ArrayList<String> patchAssetPath;
    private boolean initializedGame;

    private final static String ASSETS_FILE_AGENT_DEX = "endercore/android/AgentMainActivity.dex";
    private final static String ASSETS_FILE_CRACKER_DEX = "endercore/android/CrackedLicense.dex";
    private final static String ASSETS_NAME_AGENT_DEX = "AgentMainActivity.dex";
    private final static String ASSETS_NAME_CRACKER_DEX = "CrackedLicense.dex";

    public Launcher(EnderCore core)
    {
        initializedGame = false;
        patchAssetPath = new ArrayList<>();
        this.core = core;
        listener = new InitializationListener();
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
            IFileEnvironment fileEnvironment = core.getFileEnvironment();
            NModManager nModManager = core.getNModManager();
            OptionsManager optionsManager = core.getOptionsManager();
            boolean[] dexExists = new boolean[10];
            for(int i = 0;i < 9;++i)
                dexExists[i] = false;

            listener.onLoadGameFilesStart();

            // Copy Game Files
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
                                    targetEntry = apkFile.getEntry("lib" + File.separator + thisAbi + File.separator + libName);
                                }catch(IOException e) {
                                    continue;
                                }

                                if(targetEntry != null)
                                {
                                    listener.onCopyGameFile(libName);
                                    FileUtils.copy(apkFile.getInputStream(targetEntry),new File(fileEnvironment.getCodeCacheDirPathForNativeLib(),libName));
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
                            listener.onCopyGameFile(libName);
                            FileUtils.copy(apkFile.getInputStream(targetEntry),new File(fileEnvironment.getCodeCacheDirPathForDex(),libName));
                            dexExists[i] = true;
                        }
                    }
                }
            }
            catch(IOException ioexception) {
                throw new LauncherException("Extract game libraries failed.",ioexception);
            }
            Log.d("EnderCore-Launcher","Game Files Copied");

            listener.onLoadJavaLibrariesStart();
            try {
                for(int i = 9;i >= 0;--i)
                {
                    String dexLibName = "classes" + (i == 0?"":i) + ".dex";
                    File path = new File(fileEnvironment.getCodeCacheDirPathForDex(),dexLibName);
                    if(dexExists[i])
                    {
                        listener.onLoadJavaLibrary(dexLibName);
                        Patcher.patchDexFile(context.getClassLoader(),path.getAbsolutePath(),path.getParent());
                    }
                }

                if(optionsManager.getAutoLicense())
                {
                    //Crack License Checker
                    File licenseCrackerDex = new File(fileEnvironment.getCodeCacheDirPathForDex(),ASSETS_NAME_CRACKER_DEX);
                    FileUtils.copy(context.getAssets().open(ASSETS_FILE_CRACKER_DEX),licenseCrackerDex);
                    listener.onLoadJavaLibrary(ASSETS_NAME_CRACKER_DEX);
                    Patcher.patchDexFile(context.getClassLoader(),licenseCrackerDex.getAbsolutePath(),licenseCrackerDex.getParent());
                }
            }
            catch (IllegalAccessException | IOException | NoSuchFieldException e) {
                throw new LauncherException("Exception occurred while loading *.dex file.",e);
            }
            listener.onLoadJavaLibrariesFinish();
            Log.d("EnderCore-Launcher","Dex File Loaded.");

            // Load Native Libs
            listener.onLoadNativeLibrariesStart();
            try
            {
                Patcher.patchNativeLibraryDir(context.getClassLoader(),fileEnvironment.getCodeCacheDirPathForNativeLib());
            } catch (IllegalAccessException | ClassNotFoundException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
                throw new LauncherException("Exception occurred while loading *.so file.", e);
            }
            try
            {
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
                listener.onLoadNativeLibrary("libxhook.so");
                System.loadLibrary("xhook");
                listener.onLoadNativeLibrary("libendercore.so");
                System.loadLibrary("endercore");
            }
            catch(Error error)
            {
                throw new LauncherException("Load game libraries failed.",error);
            }
            listener.onLoadNativeLibrariesFinish();

            Log.d("EnderCore-Launcher","Native Libs Loaded.");

            // Load Resources
            listener.onLoadResourcesStart();
            patchAssetPath.add(context.getPackageResourcePath());
            patchAssetPath.add(core.getGamePackageManager().getPackageResourcePath());
            listener.onLoadResourcesFinish();

            listener.onLoadGameFilesFinish();

            // Load NMods
            if(optionsManager.getUseNMods())
            {
                listener.onLoadNModsStart();

                listener.onLoadNModsFinish();
            }

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
            IFileEnvironment fileEnvironment = core.getFileEnvironment();
            File dir = new File(fileEnvironment.getCodeCacheDirPathForDex());
            FileUtils.copy(context.getAssets().open(ASSETS_FILE_AGENT_DEX),new File(dir,ASSETS_NAME_AGENT_DEX));
            Patcher.patchDexFile(context.getClassLoader(),new File(dir,ASSETS_NAME_AGENT_DEX).getAbsolutePath(),dir.getAbsolutePath());
            DexClassLoader dexClassLoader = new DexClassLoader(new File(dir,ASSETS_NAME_AGENT_DEX).getAbsolutePath(),dir.getAbsolutePath(),null,context.getClass().getClassLoader());
            Class<?> activityClass = dexClassLoader.loadClass("com.mojang.minecraftpe.AgentMainActivity");
            Intent launchIntent = new Intent(context,activityClass);
            launchIntent.putExtra("ENDERCORE-PATCH-ASSETS",patchAssetPath);
            context.startActivity(launchIntent);
        } catch(IOException | NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
            throw new LauncherException("Start game failed.",e);
        }
    }

    public void setGameInitializationListener(IInitializationListener listener)
    {
        this.listener = listener;
    }
}
