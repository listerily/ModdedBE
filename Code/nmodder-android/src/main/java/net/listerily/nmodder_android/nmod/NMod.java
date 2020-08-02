package net.listerily.nmodder_android.nmod;

import java.io.File;
import java.util.ArrayList;

public class NMod
{
    private NModInfo mInfo;
    private ArrayList<NModWarning> mWarnings = new ArrayList<>();
    private String mPackageName;
    private File mInstallationPath;
    private I18nReader i18nReader;

    public static final String MANIFEST_NAME = "nmod_manifest.json";

    public NMod(File installationPath) throws NModException
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

    public final ArrayList<NModWarning> getWarnings()
    {
        return new ArrayList<>(mWarnings);
    }

    public String getChangeLog()
    {
        return mInfo.change_log;
    }
    
    private static final class TextOverrideBean
    {
        public String path = null;
        public String mode = MODE_REPLACE;

        public static final String MODE_REPLACE = "replace";
        public static final String MODE_APPEND = "append";
        public static final String MODE_PREPEND = "prepend";
    }

    private static final class JsonOverrideBean
    {
        public String path = null;
        public String mode = MODE_REPLACE;

        public static final String MODE_REPLACE = "replace";
        public static final String MODE_MERGE = "merge";
    }

    private static final class I18nBean
    {
        public String path = null;
        public String locale = null;
    }

    private static class NModInfo
    {
        public TextOverrideBean[] text_overrides = null;
        public JsonOverrideBean[] json_overrides = null;
        public String[] file_overrides = null;
        public String[] native_libs = null;
        public int version_code = -1;
        public String version_name = null;
        public String[] target_game_versions = null;
        public String name = null;
        public String package_name = null;
        public String description = null;
        public String author = null;
        public String author_email = null;
        public String change_log = null;
        public I18nBean[] i18n = null;

    }
}
