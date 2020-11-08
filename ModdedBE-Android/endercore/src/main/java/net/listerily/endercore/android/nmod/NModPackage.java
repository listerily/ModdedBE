package net.listerily.endercore.android.nmod;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.listerily.endercore.android.EnderCore;
import net.listerily.endercore.android.exception.NModException;
import net.listerily.endercore.android.exception.NModWarning;
import net.listerily.endercore.android.utils.NModData;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class NModPackage {

    private File packagePath;
    private ArrayList<NModWarning> detectedWarnings;
    private NModData.NModInfoAndroid info;
    private NModData.NModManifest manifest;

    public NModPackage(File path) throws NModException
    {
        try
        {
            packagePath = path;
            detectedWarnings = new ArrayList<>();
            info = null;

            if(!path.exists())
                throw new NModException("This package file does not exist.");
            if(!path.isFile())
                throw new NModException("This path is not a file.");
            try {
                ZipFile zipFile = new ZipFile(path);
                ZipEntry entry = zipFile.getEntry(NMod.MANIFEST_NAME);
                if(entry == null)
                    throw new NModException("nmod_manifest.json opening failed.Please add a manifest file for this package");
                manifest = new Gson().fromJson(new InputStreamReader(zipFile.getInputStream(entry)), NModData.NModManifest.class);
                if(manifest == null)
                    throw new NModException("Manifest: nmod_manifest.json read failed.Please check if there is any syntax error.");
                info = manifest.android;
                if(info == null)
                    throw new NModException("Manifest: nmod_manifest.json read failed or tag " + NMod.NMOD_PLATFORM + " does not exists.Please check if there is any syntax error.");

                if(manifest.uuid == null)
                    throw new NModException("UUID isn't allocated.Please allocate an UUID for this NMod in manifest.");
                UUID uuid = UUID.fromString(manifest.uuid);
                if(uuid == null)
                    throw new NModException("Invalid uuid " + manifest.uuid + ".");
                if(manifest.min_sdk_version == -1)
                    throw new NModException("Min sdk version [tag=min_sdk_version] isn't assigned in manifest.");
                if(manifest.min_sdk_version > EnderCore.SDK_VERSION)
                    throw new NModException("Package min sdk version is higher than EnderCore sdk version.Please update EnderCore or minimize min sdk version in manifest.");
                if(manifest.target_sdk_version == -1)
                    throw new NModException("Target sdk version [tag=target_sdk_version] isn't assigned in manifest.");
                if(manifest.manifest_version == -1)
                    throw new NModException("Manifest version isn't assigned in manifest.");
                if(info.name == null)
                    throw new NModException("NMod name is not found.Please assign it a name.");
                if(info.game_supports == null)
                    throw new NModException("This NMod doesn't supports any game version.The tag game_supports is empty.");
                if(info.version_name == null)
                    throw new NModException("NMod version name is not found.Please assign it a version name.");
                if(info.version_code == -1)
                    throw new NModException("NMod version code isn't assigned.Please assign it a version code for package updates.");
                if(info.author == null)
                    detectedWarnings.add(new NModWarning("NMod author is not found in the manifest."));
                if(info.author_email == null)
                    detectedWarnings.add(new NModWarning("NMod author is not found in the manifest."));
                if(info.icon == null)
                    detectedWarnings.add(new NModWarning("This nmod doesn't have any icon."));
                if(info.banner == null)
                    detectedWarnings.add(new NModWarning("This nmod doesn't have any banner"));
                if(info.i18n == null)
                    detectedWarnings.add(new NModWarning("This nmod doesn't support language internationalization."));
                for(int i = 0;i < info.game_supports.length;++i)
                {
                    NModData.GameSupportData gameSupportData = info.game_supports[i];
                    String ordinal = i % 10 == 1 ? "st" : (i % 10 == 2 ? "nd" : (i % 10 == 3 ? "rd" : "th"));
                    if(gameSupportData.name == null)
                        throw new NModException("The " + i + ordinal + " game support has no valid name");
                    if(gameSupportData.target_game_versions == null)
                        throw new NModException("The " + i + ordinal + " game support has no valid supported game version names.");
                    for(String versionName : gameSupportData.target_game_versions)
                    {
                        if(versionName == null || versionName.isEmpty())
                            throw new NModException("The " + i + ordinal + " game support has a invalid supported game version name in manifest.");
                    }

                    if(gameSupportData.dependencies != null)
                        detectedWarnings.add(new NModWarning("Dependencies isn't supported in this EnderCore SDK"));

                    if(gameSupportData.native_libs != null)
                    {
                        for(NModData.NativeLibData nativeLibData : gameSupportData.native_libs)
                        {
                            if(nativeLibData.name == null)
                                throw new NModException("The " + i + ordinal + " game support has a invalid native library name.");
                        }
                    }

                    if(gameSupportData.dex_libs != null)
                    {
                        for(NModData.DexLibData dexLibData : gameSupportData.dex_libs)
                        {
                            if(dexLibData.name == null)
                                throw new NModException("The " + i + ordinal + " game support has a invalid dex library name.");
                        }
                    }

                    if(gameSupportData.text_overrides != null)
                    {
                        for(NModData.TextOverrideData textOverrideData : gameSupportData.text_overrides)
                        {
                            if(textOverrideData.path == null)
                                throw new NModException("The " + i + ordinal + " game support has a invalid text override path.");
                            if(zipFile.getEntry(NMod.NMOD_PLATFORM + "/" + gameSupportData.name + "/assets/" + textOverrideData.path) == null)
                                throw new NModException("The " + i + ordinal + " game support has a invalid text override path.The file to the path [=" + textOverrideData.path + "] isn't found.");
                            if(textOverrideData.mode == null || (!textOverrideData.mode.equals(NModData.TextOverrideData.MODE_APPEND) && !textOverrideData.mode.equals(NModData.TextOverrideData.MODE_PREPEND) && !textOverrideData.mode.equals(NModData.TextOverrideData.MODE_REPLACE)))
                                throw new NModException("The " + i + ordinal + " game support has a invalid text override mode.");
                        }
                    }

                    if(gameSupportData.json_overrides != null)
                    {
                        for(NModData.JsonOverrideData jsonOverrideData : gameSupportData.json_overrides)
                        {
                            if(jsonOverrideData.path == null)
                                throw new NModException("The " + i + ordinal + " game support has a invalid json override path.");
                            if(zipFile.getEntry(NMod.NMOD_PLATFORM + "/" + gameSupportData.name + "/assets/" + jsonOverrideData.path) == null)
                                throw new NModException("The " + i + ordinal + " game support has a invalid json override path.The file to the path [=" + jsonOverrideData.path + "] isn't found.");
                            if(jsonOverrideData.mode == null || (!jsonOverrideData.mode.equals(NModData.JsonOverrideData.MODE_MERGE) && !jsonOverrideData.mode.equals(NModData.JsonOverrideData.MODE_REPLACE)))
                                throw new NModException("The " + i + ordinal + " game support has a invalid json override mode.");
                        }
                    }

                    //TODO file overrides;icons;banners;cpu arches
                }
            } catch (IOException e) {
                throw new NModException("Zip read failed.Please ensure that the NMod package is zip-encoded.",e);
            }catch(JsonSyntaxException | JsonIOException e) {
                throw new NModException("Manifest: nmod_manifest.json read failed.Please check if there is any syntax error.",e);
            }
        }
        catch(Throwable t)
        {
            throw new NModException("An unexpected error is detected in reading the package.",t);
        }
    }

    public ArrayList<NModWarning> getDetectedWarnings() {
        return detectedWarnings;
    }

    public String getUUID()
    {
        return manifest.uuid;
    }

    public String getName()
    {
        return info.name;
    }

    public int getVersionCode()
    {
        return info.version_code;
    }

    public String getVersionName()
    {
        return info.version_name;
    }

    public String getAuthor()
    {
        return info.author;
    }

    public String getAuthorEmail()
    {
        return info.author_email;
    }

    public NModData.NModInfoAndroid getNModInfo()
    {
        return info;
    }

    public NModData.NModManifest getManifest() {
        return manifest;
    }

    public File getPackagePath()
    {
        return packagePath;
    }
}