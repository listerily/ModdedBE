package net.listerily.endercore.android.nmod;

import android.content.Context;

import com.google.gson.Gson;

import net.listerily.endercore.android.exception.NModException;
import net.listerily.endercore.android.operator.FileManager;
import net.listerily.endercore.android.utils.NModData;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class NMod
{
    private final NModData.NModManifest manifest;
    private final NModData.NModInfoAndroid info;
    private final File installationPath;

    public static final String MANIFEST_NAME = "nmod_manifest.json";
    public static final String NMOD_PLATFORM = "android";

    public NMod(Context context,String uuid) throws NModException
    {
        try{
            FileManager fileManager = new FileManager(context);
            installationPath = fileManager.getNModDirFor(uuid);
            manifest = new Gson().fromJson(new FileReader(new File(installationPath,MANIFEST_NAME)),NModData.NModManifest.class);
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

    public NModData.NModManifest getManifest() {
        return manifest;
    }

    public NModData.NModInfoAndroid getInfo() {
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
