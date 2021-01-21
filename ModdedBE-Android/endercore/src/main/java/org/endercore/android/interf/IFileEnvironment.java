package org.endercore.android.interf;

public interface IFileEnvironment {
    String getCodeCacheDirPath();

    String getEnderCoreDirPath();

    String getOptionsFilePath();

    String getNModsDirPath();

    String getNModDirPathFor(String uuid);

    String getCodeCacheDirPathForDex();

    String getCodeCacheDirPathForNativeLib();

    String getRedirectedGameDir();

    String getCodeCacheDirPathForDexOpt();

    String getCodeCacheDirPathForNMods();

    String getCodeCacheDirPathForAssets();
}
