package net.listerily.endercore.android.operator;

import android.content.Context;

import com.google.gson.Gson;

import net.listerily.endercore.android.EnderCore;
import net.listerily.endercore.android.nmod.NMod;
import net.listerily.endercore.android.exception.NModException;
import net.listerily.endercore.android.nmod.NModOptions;
import net.listerily.endercore.android.nmod.NModPackage;
import net.listerily.endercore.android.utils.FileUtils;
import net.listerily.endercore.android.utils.NModData;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class NModManager {
    private final NModOptions nmodOptions;
    public NModManager(Context context) throws IOException {
        nmodOptions = new FileManager(context).loadNModOptionsFile();
    }

    public NMod installNMod(Context context,NModPackage nmodPackage) throws NModException
    {
        try
        {
            //Check installation availability
            if(!isSDKSupported(nmodPackage))
                throw new NModException("NMod package sdk version is higher than EnderCore sdk version.Please update EnderCore.");
            if(hasNewerVersionInstalled(context,nmodPackage))
                throw new NModException("A newer version has already been installed.");

            FileManager fileManager = new FileManager(context);
            File installationDir = fileManager.getNModDirFor(nmodPackage.getUUID());
            //Remove older files
            FileUtils.removeFiles(installationDir);
            installationDir.mkdirs();

            //Copy package to internal dir
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

            // Generate certificate
            String sha = FileUtils.getDigestSHA(fileManager.getNModDirFor(nmodPackage.getUUID()));
            String md5 = FileUtils.getDigestMD5(fileManager.getNModDirFor(nmodPackage.getUUID()));
            NModData.NModInstallationCertificate certificate = new NModData.NModInstallationCertificate();
            certificate.sha = sha;
            certificate.md5 = md5;
            FileUtils.copyFile(new ByteArrayInputStream(new Gson().toJson(certificate).getBytes()),fileManager.getNModCertificateFile(nmodPackage.getUUID()));

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
        try
        {
            FileManager fileManager = new FileManager(context);
            if(!fileManager.getNModCertificateFile(uuid).exists())
                throw new NModException("Cannot find nmod certificate file.");
            NModData.NModInstallationCertificate certificateContent = new Gson().fromJson(new FileReader(fileManager.getNModCertificateFile(uuid)), NModData.NModInstallationCertificate.class);
            if(certificateContent == null)
                throw new NModException("Failed to read installation certificate.Is there any json syntax errors?");
            String sha = FileUtils.getDigestSHA(fileManager.getNModDirFor(uuid));
            String md5 = FileUtils.getDigestMD5(fileManager.getNModDirFor(uuid));
            if(certificateContent.md5 == null || certificateContent.sha == null)
                throw new NModException("No md5 or sha found in certificate.");
            if(!sha.equals(certificateContent.sha) || !md5.equals(certificateContent.md5))
                throw new NModException("Invalid certificate.SHA=" + sha + ",MD5=" + md5 + ",certificate=[SHA=" + certificateContent.sha + ",MD5=" + md5 + "].");
        }
        catch(IOException e)
        {
            throw new NModException("Failed to read installation certificate.",e);
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new NModException("Failed to calculate sha and md5.",e);
        }
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
            FileUtils.removeFiles(fileManager.getNModCertificateFile(uuid));
        } catch (IOException e) {
            throw new NModException("Failed to remove installed files.",e);
        }
        nmodOptions.removeInstalledNModElement(uuid);
    }

    public boolean hasNewerVersionInstalled(Context context,NModPackage newPackage) throws NModException
    {
        NMod nmod = loadNModFromInstalled(context,newPackage.getUUID());
        return nmod.getVersionCode() > newPackage.getVersionCode();
    }

    public boolean isSDKSupported(NModPackage nmodPackage)
    {
        return EnderCore.SDK_INT >= nmodPackage.getManifest().min_sdk_version;
    }

    public boolean isValidPackage(Context context,NModPackage nmodPackage)
    {
        try {
            return isSDKSupported(nmodPackage) && !hasNewerVersionInstalled(context,nmodPackage);
        } catch (NModException e) {
            e.printStackTrace();
            return false;
        }
    }
}
