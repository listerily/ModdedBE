package net.listerily.endercore.android.operator;

import net.listerily.endercore.android.EnderCore;
import net.listerily.endercore.android.exception.NModException;
import net.listerily.endercore.android.interf.IFileEnvironment;
import net.listerily.endercore.android.nmod.NMod;
import net.listerily.endercore.android.nmod.NModPackage;
import net.listerily.endercore.android.utils.FileUtils;
import net.listerily.endercore.android.utils.NModJsonBean;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class NModManager {
    private final EnderCore core;
    public NModManager(EnderCore core) {
        this.core = core;
    }

    public NMod installNMod(NModPackage nmodPackage) throws NModException
    {
        try
        {
            //Check installation availability
            if(!isSDKSupported(nmodPackage))
                throw new NModException("NMod package sdk version is higher than EnderCore sdk version.Please update EnderCore.");
            if(hasNewerVersionInstalled(nmodPackage))
                throw new NModException("A newer version has already been installed.");

            IFileEnvironment fileEnvironment = core.getFileEnvironment();
            File installationDir = new File(fileEnvironment.getNModDirPathFor(nmodPackage.getUUID()));
            //Remove old files
            FileUtils.removeFiles(installationDir);
            boolean mkdirsResult = installationDir.mkdirs();
            if(!mkdirsResult)
                throw new IOException("Failed to mkdirs: " + installationDir.getAbsolutePath() + ".");

            //Copy package to internal dir
            FileUtils.copy(nmodPackage.getPackagePath(),new File(installationDir,"base.zip"));

            //Open manifest
            ZipFile zipFile = new ZipFile(nmodPackage.getPackagePath());
            ZipEntry entry = zipFile.getEntry(NMod.MANIFEST_PATH);
            FileUtils.copy(zipFile.getInputStream(entry),new File(installationDir,NMod.MANIFEST_PATH));
            NModJsonBean.NModManifest manifest = nmodPackage.getManifest();
            NModJsonBean.NModInfoAndroid nmodInfo = nmodPackage.getNModInfo();

            //Unpack all files
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while(entries.hasMoreElements())
            {
                ZipEntry thisEntry = entries.nextElement();
                FileUtils.copy(zipFile.getInputStream(thisEntry),new File(installationDir,thisEntry.getName()));
            }
            NMod result = new NMod(core,nmodPackage.getUUID());

            //Modify options
            core.getOptionsManager().addNewNModElement(nmodPackage.getUUID(), true);
            core.getOptionsManager().saveDataToFile();
            return result;
        }catch(IOException e) {
            throw new NModException("IO failed during installing nmod.",e);
        }catch (Throwable t){
            throw new NModException("An unexpected error detected while installing nmod.",t);
        }
    }

    public NMod loadNModFromInstalled(String uuid) throws NModException
    {
        if(core.getOptionsManager().isNModEnabled(uuid))
            throw new NModException("This nmod does not exists, uuid = " + uuid + ".");
        return new NMod(core,uuid);
    }

    public void uninstallNMod(String uuid) throws NModException
    {
        if(core.getOptionsManager().isNModEnabled(uuid))
            throw new NModException("This nmod does not exists, uuid = " + uuid + ".");
        IFileEnvironment fileEnvironment = core.getFileEnvironment();
        File installationPath = new File(fileEnvironment.getNModDirPathFor(uuid));
        FileUtils.removeFiles(installationPath);
        core.getOptionsManager().removeNModElement(uuid);
    }

    public boolean hasNewerVersionInstalled(NModPackage newPackage) throws NModException
    {
        NMod nmod = loadNModFromInstalled(newPackage.getUUID());
        return nmod.getVersionCode() > newPackage.getVersionCode();
    }

    public boolean isSDKSupported(NModPackage nmodPackage)
    {
        return EnderCore.SDK_INT >= nmodPackage.getManifest().min_sdk_version;
    }

    public boolean isValidPackage(NModPackage nmodPackage)
    {
        try {
            return isSDKSupported(nmodPackage) && !hasNewerVersionInstalled(nmodPackage);
        } catch (NModException e) {
            e.printStackTrace();
            return false;
        }
    }
}
