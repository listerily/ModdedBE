package org.endercore.android.nmod;

import com.google.gson.Gson;

import org.endercore.android.EnderCore;
import org.endercore.android.exception.NModException;
import org.endercore.android.interf.IFileEnvironment;
import org.endercore.android.utils.NModJsonBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class NMod {
    private final NModJsonBean.PackageManifest manifest;
    private final File installationDir;

    public static final String MANIFEST_PATH = "nmod_manifest.json";
    public static final String LIB_DIR_PATH = "lib";
    public static final String ASSETS_DIR_PATH = "assets";

    public static final String NMOD_PLATFORM = "android";

    public NMod(EnderCore enderCore, String uuid) throws NModException {
        try {
            IFileEnvironment fileEnvironment = enderCore.getFileEnvironment();
            installationDir = new File(fileEnvironment.getNModDirPathFor(uuid));
            manifest = new Gson().fromJson(new FileReader(new File(installationDir, MANIFEST_PATH)), NModJsonBean.PackageManifest.class);
        } catch (IOException e) {
            throw new NModException("IO failed while loading installed nmod, uuid = " + uuid + ".", e);
        }
    }

    public final String getUUID() {
        return manifest.uuid;
    }

    public final String getName() {
        return manifest.name;
    }

    public final String getVersionName() {
        return manifest.version_name;
    }

    public final int getVersionCode() {
        return manifest.version_code;
    }

    public NModJsonBean.PackageManifest getPackageManifest() {
        return manifest;
    }

    public InputStream openInFiles(String path) throws FileNotFoundException {
        return new FileInputStream(new File(installationDir,path));
    }

    public InputStream openInPlatformDir(String path) throws FileNotFoundException {
        return new FileInputStream(new File(installationDir,NMod.NMOD_PLATFORM + File.separator + path));
    }
}
