package net.listerily.endercore.android.operator;

import android.content.Context;

import net.listerily.endercore.android.nmod.NMod;
import net.listerily.endercore.android.exception.NModException;
import net.listerily.endercore.android.nmod.NModPackage;
import net.listerily.endercore.android.utils.FileUtils;
import net.listerily.endercore.android.utils.NModData;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class NModManager {
    private Context appContext;
    public NModManager(Context context)
    {
        appContext = context;
    }

    public NMod installNMod(NModPackage nModPackage) throws NModException
    {
        try
        {
            //Copy package to internal dir
            FileManager fileManager = new FileManager(appContext);
            File installationDir = fileManager.getNModDirFor(nModPackage.getUUID());
            FileUtils.copyFile(nModPackage.getPackagePath(),new File(installationDir,"base.zip"));

            //Open manifest
            ZipFile zipFile = new ZipFile(nModPackage.getPackagePath());
            ZipEntry entry = zipFile.getEntry(NMod.MANIFEST_NAME);
            FileUtils.copyFile(zipFile.getInputStream(entry),new File(installationDir,NMod.MANIFEST_NAME));
            NModData.NModManifest manifest = nModPackage.getManifest();
            NModData.NModInfoAndroid nmodInfo = nModPackage.getNModInfo();

            //Unpack all files
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while(entries.hasMoreElements())
            {
                ZipEntry thisEntry = entries.nextElement();
                FileUtils.copyFile(zipFile.getInputStream(thisEntry),new File(installationDir,thisEntry.getName()));
            }

            //

        }catch(IOException e) {
            throw new NModException("IO failed during installing nmod.",e);
        }catch (Throwable t){
            throw new NModException("An unexpected error detected while installing nmod.",t);
        }

        return null;
    }

    public NMod loadNModFromInstalled(String uuid)
    {

        return null;
    }

    public void uninstallNMod(String UUID)
    {

    }
}
