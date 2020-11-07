package net.listerily.endercore.android.operator;

import android.content.Context;

import net.listerily.endercore.android.EnderCoreOptions;
import net.listerily.endercore.android.nmod.NModOptions;
import net.listerily.endercore.android.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {
    public final static String DIR_DATA_ROOT = "endercore_data";

    public final static String DIR_NMODS = "nmods";
    public final static String DIR_LAUNCHER = "endercore_launcher";

    public final static String DIR_NATIVE_LIBS = "native_libs";
    public final static String DIR_DEX_LIBS = "dex_libs";
    public final static String DIR_DEX_OPT = "opt";

    public final static String ASSETS_FILE_AGENT_DEX = "endercore/android/AgentMainActivity.dex";
    public final static String ASSETS_FILE_CRACKER_DEX = "endercore/android/CrackedLicense.dex";
    public final static String ASSETS_NAME_AGENT_DEX = "AgentMainActivity.dex";
    public final static String ASSETS_NAME_CRACKER_DEX = "CrackedLicense.dex";

    public final static String DATA_FILE_ENDERCORE_OPTIONS = "options.json";
    public final static String DATA_FILE_NMODS_OPTIONS = "nmods.json";

    private Context context;
    public FileManager(Context context)
    {
        this.context = context;
    }

    public void saveEnderCoreOptionsFile(EnderCoreOptions options) throws IOException
    {
        File dataDirRoot = context.getDir(DIR_DATA_ROOT,0);
        if(!dataDirRoot.exists())
            if(!dataDirRoot.mkdirs())
                throw new IOException("Failed to mkdirs " + dataDirRoot.getAbsolutePath() + ".Please wipe app data.");
        File optionsFile = new File(dataDirRoot, DATA_FILE_ENDERCORE_OPTIONS);
        optionsFile.createNewFile();
        new FileOutputStream(optionsFile).write(options.toJsonContent().getBytes());
    }

    public EnderCoreOptions loadEnderCoreOptionsFile() throws IOException
    {
        EnderCoreOptions optionsNew = new EnderCoreOptions();
        File dataDirRoot = context.getDir(DIR_DATA_ROOT,0);
        File optionsFile = new File(dataDirRoot, DATA_FILE_ENDERCORE_OPTIONS);
        if(!optionsFile.exists())
        {
            saveEnderCoreOptionsFile(optionsNew);
            return optionsNew;
        }
        boolean result = optionsNew.fromJsonContent(FileUtils.readJsonToString(optionsFile));
        if(!result)
        {
            optionsFile.createNewFile();
            saveEnderCoreOptionsFile(optionsNew);
            return optionsNew;
        }
        return optionsNew;
    }

    public void saveNModOptionsFile(NModOptions options) throws IOException
    {
        File dataDirRoot = context.getDir(DIR_DATA_ROOT,0);
        if(!dataDirRoot.exists())
            if(!dataDirRoot.mkdirs())
                throw new IOException("Failed to mkdirs " + dataDirRoot.getAbsolutePath() + ".Please wipe app data.");
        File optionsFile = new File(dataDirRoot, DATA_FILE_NMODS_OPTIONS);
        optionsFile.createNewFile();
        new FileOutputStream(optionsFile).write(options.toJsonContent().getBytes());
    }

    public NModOptions loadNModOptionsFile() throws IOException
    {
        NModOptions optionsNew = new NModOptions();
        File dataDirRoot = context.getDir(DIR_DATA_ROOT,0);
        File optionsFile = new File(dataDirRoot, DATA_FILE_NMODS_OPTIONS);
        if(!optionsFile.exists())
        {
            saveNModOptionsFile(optionsNew);
            return optionsNew;
        }
        boolean result = optionsNew.fromJsonContent(FileUtils.readJsonToString(optionsFile));
        if(!result)
        {
            optionsFile.createNewFile();
            saveNModOptionsFile(optionsNew);
            return optionsNew;
        }
        return optionsNew;
    }

    public File getNativeLibsSavedPath() throws IOException
    {
        File tempDir = context.getCodeCacheDir();
        File launcherDir = new File(tempDir,DIR_LAUNCHER);
        File nativeLibsDir = new File(launcherDir,DIR_NATIVE_LIBS);
        if(!nativeLibsDir.exists())
            if(!nativeLibsDir.mkdirs())
                throw new IOException("Failed to mkdirs " + nativeLibsDir.getAbsolutePath() + ".Please wipe app data.");
        return nativeLibsDir;
    }

    public File getNModNativeLibsSavedPath(String uuid) throws IOException
    {
        File tempDir = context.getCodeCacheDir();
        File nmodsDir = new File(tempDir,DIR_NMODS);
        File nmodDir = new File(nmodsDir,uuid);
        File nativeLibsDir = new File(nmodDir,DIR_NATIVE_LIBS);
        if(!nativeLibsDir.exists())
            if(!nativeLibsDir.mkdirs())
                throw new IOException("Failed to mkdirs " + nativeLibsDir.getAbsolutePath() + ".Please wipe app data.");
        return nativeLibsDir;
    }

    public File getDexLibsSavedPath() throws IOException
    {
        File tempDir = context.getCodeCacheDir();
        File launcherDir = new File(tempDir,DIR_LAUNCHER);
        File dexLibsDir = new File(launcherDir,DIR_DEX_LIBS);
        if(!dexLibsDir.exists())
            if(!dexLibsDir.mkdirs())
                throw new IOException("Failed to mkdirs " + dexLibsDir.getAbsolutePath() + ".Please wipe app data.");
        return dexLibsDir;
    }

    public File getNModDexLibsSavedPath(String uuid) throws IOException
    {
        File tempDir = context.getCodeCacheDir();
        File nmodsDir = new File(tempDir,DIR_NMODS);
        File nmodDir = new File(nmodsDir,uuid);
        File dexLibsDir = new File(nmodDir,DIR_DEX_LIBS);
        if(!dexLibsDir.exists())
            if(!dexLibsDir.mkdirs())
                throw new IOException("Failed to mkdirs " + dexLibsDir.getAbsolutePath() + ".Please wipe app data.");
        return dexLibsDir;
    }

    public static File getDexOptimizeDir(File dex) throws IOException
    {
        File opt = new File(dex.getParent(),DIR_DEX_OPT + dex.getName());
        if(!opt.exists())
            if(!opt.mkdirs())
                throw new IOException("Failed to mkdirs " + opt.getAbsolutePath() + ".Please wipe app data.");
        return opt;
    }

    public File getNModDir() throws IOException
    {
        File result = new File(context.getDir(DIR_DATA_ROOT,0),DIR_NMODS);
        if(!result.exists())
            if(!result.mkdirs())
                throw new IOException("Failed to mkdirs " + result.getAbsolutePath() + ".Please wipe app data.");
        return result;
    }

    public File getNModDirFor(String uuid) throws IOException
    {
        File result = new File(getNModDir(),uuid);
        if(!result.exists())
            if(!result.mkdirs())
                throw new IOException("Failed to mkdirs " + result.getAbsolutePath() + ".Please wipe app data.");
        return result;
    }

    public File getNModCertificateFile(String uuid) throws IOException
    {
        File nmodDir = getNModDir();
        File result = new File(nmodDir,uuid + ".json");
        result.createNewFile();
        return result;
    }


    public void removeEnderCoreData()
    {
        FileUtils.removeFiles(context.getCodeCacheDir());
        FileUtils.removeFiles(context.getDir(DIR_DATA_ROOT,0));
    }
}
