package net.listerily.endercore.android.nmod;

import com.google.gson.Gson;

import net.listerily.endercore.android.EnderCore;
import net.listerily.endercore.android.exception.NModException;
import net.listerily.endercore.android.interf.IFileEnvironment;
import net.listerily.endercore.android.utils.NModJsonBean;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class NMod
{
    private final NModJsonBean.NModManifest manifest;
    private final NModJsonBean.NModInfoAndroid info;
    private final File installationPath;

    public static final String MANIFEST_PATH = "nmod_manifest.json";
    public static final String ICON_PATH = "icon.png";
    public static final String BANNER_PATH = "banner.png";
    public static final String LIB_DIR_PATH = "lib";
    public static final String ASSETS_DIR_PATH = "assets";
    public static final String I18N_DIR_PATH = "i18n";

    public static final String NMOD_PLATFORM = "android";

    public NMod(EnderCore enderCore, String uuid) throws NModException
    {
        try{
            IFileEnvironment fileEnvironment = enderCore.getFileEnvironment();
            installationPath = new File(fileEnvironment.getNModDirPathFor(uuid));
            manifest = new Gson().fromJson(new FileReader(new File(installationPath, MANIFEST_PATH)), NModJsonBean.NModManifest.class);
            info = manifest.android;

        } catch (IOException e) {
            throw new NModException("IO failed while loading installed nmod, uuid = " + uuid + ".",e);
        }
    }

    public final String getName()
    {
        return info.name;
    }

    public final String getDescription()
    {
        return info.description;
    }

    public final String getAuthor()
    {
        return info.author;
    }

    public final String getVersionName()
    {
        return info.version_name;
    }

    public final int getVersionCode(){
        return info.version_code;
    }

    public NModJsonBean.NModManifest getManifest() {
        return manifest;
    }

    public NModJsonBean.NModInfoAndroid getInfo() {
        return info;
    }

    public String getChangeLog()
    {
        return info.change_log;
    }

    public File getInstallationPath() {
        return installationPath;
    }
}
