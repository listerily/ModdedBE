package net.listerily.endercore.android.operator;

import android.content.Context;

import net.listerily.endercore.android.nmod.NMod;
import net.listerily.endercore.android.exception.NModException;
import net.listerily.endercore.android.nmod.NModOptions;
import net.listerily.endercore.android.nmod.NModPackage;
import net.listerily.endercore.android.utils.FileUtils;
import net.listerily.endercore.android.utils.NModData;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class NModManager {
    private NModOptions nmodOptions;
    public NModManager(Context context) throws IOException {
        nmodOptions = new FileManager(context).loadNModOptionsFile();
    }

    public NMod installNMod(Context context,NModPackage nmodPackage) throws NModException
    {
        try
        {
            //Copy package to internal dir
            FileManager fileManager = new FileManager(context);
            File installationDir = fileManager.getNModDirFor(nmodPackage.getUUID());
            FileUtils.copyFile(nmodPackage.getPackagePath(),new File(installationDir,"base.zip"));

            //Open manifest
            ZipFile zipFile = new ZipFile(nmodPackage.getPackagePath());
            ZipEntry entry = zipFile.getEntry(NMod.MANIFEST_NAME);
            FileUtils.copyFile(zipFile.getInputStream(entry),new File(installationDir,NMod.MANIFEST_NAME));
            NModData.NModManifest manifest = nmodPackage.getManifest();
            NModData.NModInfoAndroid nmodInfo = nmodPackage.getNModInfo();

            //Unpack all files
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while(entries.hasMoreElements())
            {
                ZipEntry thisEntry = entries.nextElement();
                FileUtils.copyFile(zipFile.getInputStream(thisEntry),new File(installationDir,thisEntry.getName()));
            }

            //Modify options
            nmodOptions.addNewInstalledNModElement(nmodPackage.getUUID());
            new FileManager(context).saveNModOptionsFile(nmodOptions);

            return new NMod(context,nmodPackage.getUUID());
        }catch(IOException e) {
            throw new NModException("IO failed during installing nmod.",e);
        }catch (Throwable t){
            throw new NModException("An unexpected error detected while installing nmod.",t);
        }
    }

    public NMod loadNModFromInstalled(Context context,String uuid) throws NModException
    {
        return new NMod(context,uuid);
    }

    public void uninstallNMod(Context context,String uuid) throws NModException
    {
        if(nmodOptions.findIfExists(uuid))
            throw new NModException("This nmod does not exists, uuid = " + uuid + ".");
        try {
            FileManager fileManager = new FileManager(context);
            File installationPath = fileManager.getNModDirFor(uuid);
            FileUtils.removeFiles(installationPath);
        } catch (IOException e) {
            throw new NModException("Failed to remove installed files.",e);
        }
        nmodOptions.removeInstalledNModElement(uuid);
    }
}
