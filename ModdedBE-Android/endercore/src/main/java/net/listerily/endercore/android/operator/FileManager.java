package net.listerily.endercore.android.operator;

import android.content.Context;

import com.google.gson.Gson;

import net.listerily.endercore.android.EnderCoreOptions;
import net.listerily.endercore.android.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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

    public final static String DATA_FILE_OPTIONS = "options.json";
    public final static String DATA_FILE_NMODS_DATA = "nmods.json";

    private Context context;
    public FileManager(Context context)
    {
        this.context = context;
    }

    public void saveOptionsFile(EnderCoreOptions options) throws IOException
    {
        File dataDirRoot = context.getDir(DIR_DATA_ROOT,0);
        if(!dataDirRoot.exists())
            if(!dataDirRoot.mkdirs())
                throw new IOException("Failed to mkdirs " + dataDirRoot.getAbsolutePath() + ".Please wipe app data.");
        File optionsFile = new File(dataDirRoot,DATA_FILE_OPTIONS);
        optionsFile.createNewFile();
        new FileOutputStream(optionsFile).write(new Gson().toJson(options.getOptionsData()).getBytes());
    }

    public EnderCoreOptions loadOptionsFile() throws IOException
    {
        EnderCoreOptions optionsNew = new EnderCoreOptions();
        File dataDirRoot = context.getDir(DIR_DATA_ROOT,0);
        File optionsFile = new File(dataDirRoot,DATA_FILE_OPTIONS);
        if(!optionsFile.exists())
        {
            optionsFile.createNewFile();
            optionsNew.setOptionsData(new EnderCoreOptions.OptionsBean());
            saveOptionsFile(optionsNew);
            return optionsNew;
        }
        EnderCoreOptions.OptionsBean data = new Gson().fromJson(new InputStreamReader(new FileInputStream(optionsFile)), EnderCoreOptions.OptionsBean.class);
        if(data == null)
        {
            optionsFile.createNewFile();
            optionsNew.setOptionsData(new EnderCoreOptions.OptionsBean());
            saveOptionsFile(optionsNew);
            return optionsNew;
        }
        optionsNew.setOptionsData(data);
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

    public void removeEnderCoreData()
    {
        FileUtils.removeFiles(context.getCodeCacheDir());
        FileUtils.removeFiles(context.getDir(DIR_DATA_ROOT,0));
    }
}
