package net.listerily.endercore.android.interf.implemented;

import android.content.Context;

import net.listerily.endercore.android.interf.IFileEnvironment;

import java.io.File;

public class FileEnvironment implements IFileEnvironment {
    private final String codeCacheDirPath;
    private final String enderCoreDirPath;

    public final static String DIR_DATA_ROOT = "endercore_data";

    public final static String DIR_NMODS = "nmods";

    public final static String DIR_NATIVE_LIBS = "native_libs";
    public final static String DIR_DEX_LIBS = "dex_libs";
    public final static String DIR_DEX_OPT = "opt";

    public final static String DATA_FILE_ENDERCORE_OPTIONS = "options.json";

    public FileEnvironment(Context context) {
        codeCacheDirPath = context.getCodeCacheDir().getPath();
        enderCoreDirPath = context.getDir(DIR_DATA_ROOT,0).getPath();
    }


    @Override
    public String getCodeCacheDirPath() {
        return codeCacheDirPath;
    }

    @Override
    public String getEnderCoreDirPath() {
        return enderCoreDirPath;
    }

    @Override
    public String getOptionsFilePath() {
        return getEnderCoreDirPath() + File.separator + DATA_FILE_ENDERCORE_OPTIONS;
    }

    @Override
    public String getNModsDirPath() {
        return getEnderCoreDirPath() + File.separator + DIR_NMODS;
    }

    @Override
    public String getNModDirPathFor(String uuid) {
        return getEnderCoreDirPath() + File.separator + uuid;
    }

    @Override
    public String getCodeCacheDirPathForDex() {
        return getCodeCacheDirPath() + File.separator + DIR_DEX_LIBS;
    }

    @Override
    public String getCodeCacheDirPathForNativeLib() {
        return getCodeCacheDirPath() + File.separator + DIR_NATIVE_LIBS;
    }
}
