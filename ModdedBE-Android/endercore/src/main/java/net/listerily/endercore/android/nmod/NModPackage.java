package net.listerily.endercore.android.nmod;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

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
            } catch (IOException e) {
                throw new NModException("Zip read failed.Please ensure that the NMod package is zip-encoded.",e);
            }catch(JsonSyntaxException | JsonIOException e) {
                throw new NModException("Manifest: nmod_manifest.json read failed.Please check if there is any syntax error.",e);
            }

            if(manifest.uuid == null)
                throw new NModException("UUID isn't allocated.Please allocate an UUID for this NMod in manifest.");
            UUID uuid = UUID.fromString(manifest.uuid);
            if(uuid == null)
                throw new NModException("Invalid uuid " + manifest.uuid + ".");

            if(info.name == null)
                detectedWarnings.add(new NModWarning("NMod name not found.Please assign it a name."));
            if(info.game_supports == null)
                detectedWarnings.add(new NModWarning("This NMod patches any code."));
            if(info.version_name == null)
                detectedWarnings.add(new NModWarning("NMod version name not found.Please assign it a version name."));
            if(info.version_code == -1)
                detectedWarnings.add(new NModWarning("NMod version code unassigned.Please assign it a version code for package updates."));
            if(info.author == null)
                detectedWarnings.add(new NModWarning("NMod author not found in the manifest."));
            if(info.author_email == null)
                detectedWarnings.add(new NModWarning("NMod author not found in the manifest."));
        }
        catch(Throwable t)
        {
            throw new NModException("An unexpected error detected in reading the package.",t);
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