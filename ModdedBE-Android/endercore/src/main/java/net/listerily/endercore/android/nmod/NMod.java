package net.listerily.endercore.android.nmod;

import net.listerily.endercore.android.exception.NModException;
import net.listerily.endercore.android.exception.NModWarning;
import net.listerily.endercore.android.utils.NModData;

import java.io.File;
import java.util.ArrayList;

public class NMod
{
    private NModData.NModInfoAndroid mInfo;
    private ArrayList<NModWarning> detectedWarnings = new ArrayList<>();

    public static final String MANIFEST_NAME = "nmod_manifest.json";
    public static final String NMOD_PLATFORM = "android";

    public NMod(File packagePath) throws NModException
    {

    }

    public final String getName()
    {
        return mInfo.name;
    }

    public final String getDescription()
    {
        return mInfo.description;
    }

    public final String getAuthor()
    {
        return mInfo.author;
    }

    public final String getVersionName()
    {
        return mInfo.version_name;
    }

    public final ArrayList<NModWarning> getDetectedWarnings()
    {
        return new ArrayList<>(detectedWarnings);
    }

    public String getChangeLog()
    {
        return mInfo.change_log;
    }
}
