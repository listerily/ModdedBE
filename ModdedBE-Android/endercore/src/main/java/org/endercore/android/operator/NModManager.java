package org.endercore.android.operator;

import org.endercore.android.EnderCore;
import org.endercore.android.exception.NModException;
import org.endercore.android.interf.IFileEnvironment;
import org.endercore.android.nmod.NMod;
import org.endercore.android.nmod.NModPackage;
import org.endercore.android.utils.FileUtils;
import org.endercore.android.utils.NModJsonBean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class NModManager {
    private final EnderCore core;
    private final ArrayList<NMod> allNMods;

    public NModManager(EnderCore core){
        this.core = core;
        allNMods = new ArrayList<>();
        final OptionsManager.NModOptionsElement[] installedNMods = core.getOptionsManager().getInstalledNMods();
        for(OptionsManager.NModOptionsElement nmodElement : installedNMods){
            try{
                allNMods.add(loadNModFromInstalled(nmodElement.uuid));
            } catch (NModException e){
                e.printStackTrace();
                core.getOptionsManager().removeNModElement(nmodElement.uuid);
            }
        }
    }

    public NMod installNMod(NModPackage nmodPackage) throws NModException {
        try {
            if(nmodPackage == null)
                throw new NullPointerException("nmodPackage");

            //Check installation availability
            if (!isSDKSupported(nmodPackage))
                throw new NModException("NMod package sdk version is higher than EnderCore sdk version.Please update EnderCore.");
            if (hasNewerVersionInstalled(nmodPackage))
                throw new NModException("A newer version has already been installed.");

            IFileEnvironment fileEnvironment = core.getFileEnvironment();
            File installationDir = new File(fileEnvironment.getNModDirPathFor(nmodPackage.getUUID()));
            boolean isUpdateInstall = isNModExists(nmodPackage.getUUID());

            //Remove old files and create dir
            FileUtils.removeFiles(installationDir);
            boolean mkdirsResult = installationDir.mkdirs();
            if (!mkdirsResult)
                throw new IOException("Failed to mkdirs: " + installationDir.getAbsolutePath() + ".");

            //Copy package to internal dir
            FileUtils.copy(nmodPackage.getPackageFilePath(), new File(installationDir, "base.zip"));

            //Open manifest
            ZipFile zipFile = new ZipFile(nmodPackage.getPackageFilePath());
            ZipEntry entry = zipFile.getEntry(NMod.MANIFEST_PATH);
            FileUtils.copy(zipFile.getInputStream(entry), new File(installationDir, NMod.MANIFEST_PATH));
            NModJsonBean.PackageManifest manifest = nmodPackage.getPackageManifest();

            //Unpack all files
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry thisEntry = entries.nextElement();
                if(thisEntry.isDirectory()){
                    boolean mkdirsResult2 = new File(installationDir,thisEntry.getName()).mkdirs();
                    if(!mkdirsResult2)
                        throw new NModException("Failed to mkdirs: " + installationDir.getAbsolutePath() + File.separator + thisEntry.getName());
                }
                else
                    FileUtils.copy(zipFile.getInputStream(thisEntry), new File(installationDir, thisEntry.getName()));
            }
            NMod result = new NMod(core, nmodPackage.getUUID());

            //Modify options
            core.getOptionsManager().addNewNModElement(nmodPackage.getUUID(), true);
            core.getOptionsManager().saveDataToFile();
            if(!isUpdateInstall)
                allNMods.add(result);
            return result;
        } catch (IOException e) {
            throw new NModException("IO failed during installing nmod.", e);
        } catch (Throwable t) {
            throw new NModException("An unexpected error detected while installing nmod.", t);
        }
    }

    private NMod loadNModFromInstalled(String uuid) throws NModException {
        return new NMod(core, uuid);
    }

    public void uninstallNMod(String uuid) {
        IFileEnvironment fileEnvironment = core.getFileEnvironment();
        File installationPath = new File(fileEnvironment.getNModDirPathFor(uuid));
        FileUtils.removeFiles(installationPath);
        core.getOptionsManager().removeNModElement(uuid);
        for(NMod nmod : allNMods)
        {
            if(nmod.getUUID().equals(uuid))
                allNMods.remove(nmod);
        }
    }

    public boolean hasNewerVersionInstalled(NModPackage newPackage) {
        NMod nmod;
        try{
            nmod = loadNModFromInstalled(newPackage.getUUID());
        } catch (NModException ignored){
            return false;
        }
        return nmod.getVersionCode() > newPackage.getVersionCode();
    }

    public boolean isNModExists(String uuid){
        for(NMod nmod : allNMods){
            if(nmod.getUUID().equals(uuid))
                return true;
        }
        return false;
    }

    public boolean isSDKSupported(NModPackage nmodPackage) {
        return EnderCore.SDK_INT >= nmodPackage.getPackageManifest().min_sdk_version;
    }

    public boolean isValidPackage(NModPackage nmodPackage) {
        return isSDKSupported(nmodPackage) && !hasNewerVersionInstalled(nmodPackage);
    }

    public boolean isNModEnabled(NMod nmod){
        return core.getOptionsManager().isNModElementEnabled(nmod.getUUID());
    }

    public void setNModEnabled(NMod nmod, boolean enabled){
        core.getOptionsManager().setNModElementEnabled(nmod.getUUID(), enabled);
    }

    public ArrayList<NMod> getEnabledNMods(){
        ArrayList<NMod> result = new ArrayList<>();
        for(NMod nmod: allNMods){
            if(isNModEnabled(nmod))
                result.add(nmod);
        }
        return result;
    }

    public ArrayList<NMod> getAllNMods(){
        return allNMods;
    }
}
