package org.endercore.android.operator;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.endercore.android.EnderCore;
import org.endercore.android.exception.LauncherException;
import org.endercore.android.interf.IFileEnvironment;
import org.endercore.android.interf.IInitializationListener;
import org.endercore.android.interf.implemented.InitializationListener;
import org.endercore.android.nmod.NMod;
import org.endercore.android.utils.CPUArch;
import org.endercore.android.utils.FileUtils;
import org.endercore.android.utils.Patcher;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import dalvik.system.DexClassLoader;

public final class Launcher {
    private final EnderCore core;
    private IInitializationListener listener;
    private final ArrayList<String> patchAssetPath;
    private final ArrayList<String> patchDexPath;
    private final ArrayList<String> patchLibPath;
    private boolean initializedGame;

    private final static String ASSETS_MAIN_DIR = "endercore" + File.separator + "android";
    private final static String ASSETS_FILE_AGENT_DEX = ASSETS_MAIN_DIR + File.separator + "AgentMainActivity.dex";
    private final static String ASSETS_FILE_CRACKER_DEX = ASSETS_MAIN_DIR + File.separator + "CrackedLicense.dex";
    private final static String NAME_AGENT_DEX = "AgentMainActivity.dex";
    private final static String NAME_CRACKER_DEX = "CrackedLicense.dex";
    private final static String NAME_CPP_SHARED = "libc++_shared.so";
    private final static String NAME_YURAI = "libyurai.so";
    private final static String NAME_SUBSTRATE = "libsubstrate.so";
    private final static String NAME_XHOOK = "libxhook.so";
    private final static String NAME_FMOD = "libfmod.so";
    private final static String NAME_MINECRAFTPE = "libminecraftpe.so";
    private final static String NAME_ENDERCORE = "libendercore.so";
    private final static String LIB_CPP_SHARED = "c++_shared";
    private final static String LIB_YURAI = "yurai";
    private final static String LIB_SUBSTRATE = "substrate";
    private final static String LIB_XHOOK = "xhook";
    private final static String LIB_FMOD = "fmod";
    private final static String LIB_MINECRAFTPE = "minecraftpe";
    private final static String LIB_ENDERCORE = "endercore";
    private final static String DIR_LIB = "lib";

    public Launcher(EnderCore core) {
        initializedGame = false;
        patchAssetPath = new ArrayList<>();
        patchDexPath = new ArrayList<>();
        patchLibPath = new ArrayList<>();
        this.core = core;
        listener = new InitializationListener();
    }

    public void initializeGame(Context context) throws LauncherException {
        listener.onStart();
        try {
            // Check Availability
            if (!core.getGamePackageManager().isGameInstalled())
                throw new LauncherException("Minecraft Game is not installed.Please install game.");

            // Set Variants
            IFileEnvironment fileEnvironment = core.getFileEnvironment();
            NModManager nModManager = core.getNModManager();
            OptionsManager optionsManager = core.getOptionsManager();
            String targetArch = null;
            boolean[] dexExists = new boolean[10];
            for (int i = 0; i < 9; ++i)
                dexExists[i] = false;

            listener.onLoadGameFilesStart();

            // Copy Game Files
            try {
                File resPath = new File(core.getGamePackageManager().getPackageResourcePath());
                if(resPath.getParentFile() == null)
                    throw new IOException("Invalid file path.");
                File[] allApkFiles = resPath.getParentFile().listFiles();
                if(allApkFiles == null)
                    throw new IOException("Failed to list all apk files.");
                //Copy native libraries
                String[] supportedAbis = CPUArch.getSystemSupportedAbis();
                String[] requiredLibs = {NAME_FMOD, NAME_MINECRAFTPE};
                boolean[] libsCopied = new boolean[requiredLibs.length];
                for (int i = 0; i < requiredLibs.length; ++i)
                    libsCopied[i] = false;

                // find target arch
                for(String abiItem : supportedAbis){
                    for(File apkFile : allApkFiles){
                        if(!apkFile.isFile())
                            continue;

                        try{
                            ZipFile zipFileOfApk;
                            zipFileOfApk = new ZipFile(apkFile);
                            Enumeration<? extends ZipEntry> enumeration = zipFileOfApk.entries();
                            while(enumeration.hasMoreElements()){
                                ZipEntry zipEntry = enumeration.nextElement();
                                if(zipEntry.getName().startsWith(DIR_LIB + File.separator + abiItem) && CPUArch.isEnderCoreSupportedAbi(abiItem))
                                    targetArch = abiItem;
                            }
                        } catch (IOException ignored) {
                        }

                    }
                    if(targetArch != null)
                        break;
                }
                if(targetArch == null)
                    throw new LauncherException("Abis are not supported by EnderCore.");

                // copy game native libraries
                for (int i = 0; i < requiredLibs.length; ++i) {
                    String libName = requiredLibs[i];
                    if (!libsCopied[i]) {
                        for (File apk : allApkFiles) {
                            if (!apk.isFile())
                                continue;
                            ZipEntry targetEntry;
                            ZipFile apkFile;
                            try {
                                apkFile = new ZipFile(apk);
                                targetEntry = apkFile.getEntry(DIR_LIB + File.separator + targetArch + File.separator + libName);
                            } catch (IOException ignored) {
                                continue;
                            }

                            if (targetEntry != null) {
                                listener.onCopyGameFile(libName);
                                FileUtils.copy(apkFile.getInputStream(targetEntry), new File(fileEnvironment.getCodeCacheDirPathForNativeLib(), libName));
                                libsCopied[i] = true;
                            }
                        }
                    }
                }
                boolean allLibsCopied = true;
                int notCopiedLibId = -1;
                for(int i = 0;i < libsCopied.length;++i) {
                    boolean copied = libsCopied[i];
                    if (!copied) {
                        allLibsCopied = false;
                        notCopiedLibId = i;
                        break;
                    }
                }
                if(!allLibsCopied)
                    throw new LauncherException("Not all required libs are found int the minecraft game package. Lib " + requiredLibs[notCopiedLibId] + " of arch " + targetArch + " not found.");

                //Copy Dex files
                for (int i = 9; i >= 0; --i) {
                    String libName = "classes" + (i == 0 ? "" : i) + ".dex";
                    for (File apk : allApkFiles) {
                        if (!apk.isFile())
                            continue;

                        ZipEntry targetEntry;
                        ZipFile apkFile;
                        try {
                            apkFile = new ZipFile(apk);
                            targetEntry = apkFile.getEntry(libName);
                        } catch (IOException e) {
                            continue;
                        }

                        if (targetEntry != null) {
                            listener.onCopyGameFile(libName);
                            FileUtils.copy(apkFile.getInputStream(targetEntry), new File(fileEnvironment.getCodeCacheDirPathForDex(), libName));
                            dexExists[i] = true;
                        }
                    }
                }
            } catch (IOException ioexception) {
                throw new LauncherException("Extract game libraries failed.", ioexception);
            }
            Log.d("EnderCore-Launcher", "Game Files Copied");

            listener.onLoadJavaLibrariesStart();
            try {
                for (int i = 9; i >= 0; --i) {
                    String dexLibName = "classes" + (i == 0 ? "" : i) + ".dex";
                    File path = new File(fileEnvironment.getCodeCacheDirPathForDex(), dexLibName);
                    if (dexExists[i]) {
                        listener.onLoadJavaLibrary(dexLibName);
                        Patcher.patchDexFile(context.getClassLoader(), path.getAbsolutePath(), fileEnvironment.getCodeCacheDirPathForDexOpt());
                        patchDexPath.add(path.getAbsolutePath());
                    }
                }

                if (optionsManager.getAutoLicense()) {
                    //Crack License Checker
                    File licenseCrackerDex = new File(fileEnvironment.getCodeCacheDirPathForDex(), NAME_CRACKER_DEX);
                    FileUtils.copy(context.getAssets().open(ASSETS_FILE_CRACKER_DEX), licenseCrackerDex);
                    listener.onLoadJavaLibrary(NAME_CRACKER_DEX);
                    Patcher.patchDexFile(context.getClassLoader(), licenseCrackerDex.getAbsolutePath(), fileEnvironment.getCodeCacheDirPathForDexOpt());
                    patchDexPath.add(licenseCrackerDex.getAbsolutePath());
                }
            } catch (IllegalAccessException | IOException | NoSuchFieldException e) {
                throw new LauncherException("Exception occurred while loading *.dex file.", e);
            }
            listener.onLoadJavaLibrariesFinish();
            Log.d("EnderCore-Launcher", "Dex File Loaded.");

            // Load Native Libs
            listener.onLoadNativeLibrariesStart();
            try {
                Patcher.patchNativeLibraryDir(context.getClassLoader(), fileEnvironment.getCodeCacheDirPathForNativeLib());
                patchLibPath.add(fileEnvironment.getCodeCacheDirPathForNativeLib());
            } catch (IllegalAccessException | ClassNotFoundException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
                throw new LauncherException("Exception occurred while loading *.so file.", e);
            }
            try {
                listener.onLoadNativeLibrary(NAME_CPP_SHARED);
                System.loadLibrary(LIB_CPP_SHARED);
                listener.onLoadNativeLibrary(NAME_FMOD);
                System.loadLibrary(LIB_FMOD);
                listener.onLoadNativeLibrary(NAME_MINECRAFTPE);
                System.loadLibrary(LIB_MINECRAFTPE);
                listener.onLoadNativeLibrary(NAME_YURAI);
                System.loadLibrary(LIB_YURAI);
                listener.onLoadNativeLibrary(NAME_SUBSTRATE);
                System.loadLibrary(LIB_SUBSTRATE);
                listener.onLoadNativeLibrary(NAME_XHOOK);
                System.loadLibrary(LIB_XHOOK);
                listener.onLoadNativeLibrary(NAME_ENDERCORE);
                System.loadLibrary(LIB_ENDERCORE);
            } catch (Error error) {
                throw new LauncherException("Load game libraries failed.", error);
            }
            listener.onLoadNativeLibrariesFinish();

            Log.d("EnderCore-Launcher", "Native Libs Loaded.");

            // Load Resources
            listener.onLoadResourcesStart();
            patchAssetPath.add(context.getPackageResourcePath());
            patchAssetPath.add(core.getGamePackageManager().getPackageResourcePath());
            listener.onLoadResourcesFinish();

            listener.onLoadGameFilesFinish();

            // Load NMods
            if (optionsManager.getUseNMods()) {
                listener.onLoadNModsStart();

                // Extract all assets from game apk
                File assetsDir = new File(fileEnvironment.getCodeCacheDirPathForAssets());
                boolean mkdirsResult = assetsDir.mkdirs();
                if(!mkdirsResult)
                    throw new LauncherException("Failed to mkdirs: " + assetsDir.getAbsolutePath() + ".");

                File resPath = new File(core.getGamePackageManager().getPackageResourcePath());
                if(resPath.getParentFile() == null)
                    throw new IOException("Invalid file path.");
                File[] allApkFiles = resPath.getParentFile().listFiles();
                if(allApkFiles == null)
                    throw new IOException("Failed to list all apk files.");
                for(File apkFile : allApkFiles)
                {
                    try{
                        ZipFile zipFile = new ZipFile(apkFile);
                        Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
                        while(enumeration.hasMoreElements())
                        {
                            ZipEntry entry = enumeration.nextElement();
                            if(entry.getName().startsWith("assets/")){
                                FileUtils.copy(zipFile.getInputStream(entry),new File(assetsDir,entry.getName()));
                            }
                        }
                    } catch (IOException ignored){
                    }
                }

                ArrayList<NMod> enabledNMods = nModManager.getEnabledNMods();

                listener.onLoadNModsFinish();
            }

            // Arrange
            listener.onArrange();
            //todo arrange
        } catch (Throwable e) {
            listener.onSuspend();
            throw new LauncherException("Unexpected fatal error caused in game initialization.", e);
        }
        initializedGame = true;
        listener.onFinish();
    }

    public void startGame(Context context) throws LauncherException {
        if (!initializedGame)
            throw new RuntimeException("Game isn't initialized.Please initialize game (Launcher.initializeGame) before start game.");
        try {
            IFileEnvironment fileEnvironment = core.getFileEnvironment();
            File dir = new File(fileEnvironment.getCodeCacheDirPathForDex());
            FileUtils.copy(context.getAssets().open(ASSETS_FILE_AGENT_DEX), new File(dir, NAME_AGENT_DEX));
            Patcher.patchDexFile(context.getClassLoader(), new File(dir, NAME_AGENT_DEX).getAbsolutePath(), fileEnvironment.getCodeCacheDirPathForDexOpt());
            patchDexPath.add(new File(dir, NAME_AGENT_DEX).getAbsolutePath());
            DexClassLoader dexClassLoader = new DexClassLoader(new File(dir, NAME_AGENT_DEX).getAbsolutePath(), dir.getAbsolutePath(), null, context.getClass().getClassLoader());
            Class<?> activityClass = dexClassLoader.loadClass("com.mojang.minecraftpe.AgentMainActivity");
            Intent launchIntent = new Intent(context, activityClass);
            launchIntent.putExtra("ENDERCORE-PATCH-ASSETS", patchAssetPath);
            launchIntent.putExtra("ENDERCORE-PATCH-DEX", patchDexPath);
            launchIntent.putExtra("ENDERCORE-PATCH-LIBS", patchLibPath);
            launchIntent.putExtra("ENDERCORE-PATCH-OPT",fileEnvironment.getCodeCacheDirPathForDexOpt());
            context.startActivity(launchIntent);
        } catch (IOException | NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
            throw new LauncherException("Start game failed.", e);
        }
    }

    public void setGameInitializationListener(IInitializationListener listener) {
        this.listener = listener;
    }
}
