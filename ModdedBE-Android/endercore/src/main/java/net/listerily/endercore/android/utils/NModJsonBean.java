package net.listerily.endercore.android.utils;

public class NModJsonBean {
    public static final class TextOverrideData
    {
        public String path = null;
        public String mode = MODE_REPLACE;

        public static final String MODE_REPLACE = "replace";
        public static final String MODE_APPEND = "append";
        public static final String MODE_PREPEND = "prepend";
    }

    public static final class FileOverrideData
    {
        public String path = null;
        public String mode = MODE_REPLACE;
        public String code = null;

        public static final String MODE_REPLACE = "replace";
        public static final String MODE_SCRIPT = "script";
        public static final String MODE_JAVA = "java";
        public static final String MODE_NATIVE = "native";
    }

    public static final class JsonOverrideData
    {
        public String path = null;
        public String mode = MODE_REPLACE;

        public static final String MODE_REPLACE = "replace";
        public static final String MODE_MERGE = "merge";
    }

    public static final class NativeLibData
    {
        public String name = null;
        public String main = null;
    }

    public static final class DependencyData
    {
        public String uuid = null;
        public String url = null;
        public String[] versions = null;
    }

    public static final class DexLibData
    {
        public String name = null;
        public String main = null;
    }

    public static final class I18nData
    {
        public String path = null;
        public String locale = null;
    }

    public static final class GameSupportData
    {
        public TextOverrideData[] text_overrides = null;
        public JsonOverrideData[] json_overrides = null;
        public FileOverrideData[] file_overrides = null;
        public NativeLibData[] native_libs = null;
        public DexLibData[] dex_libs = null;
        public DependencyData[] dependencies = null;
        public String name = null;
        public String[] target_game_versions = null;
    }

    public static final class NModInfoAndroid
    {
        public GameSupportData[] game_supports = null;
        public int version_code = -1;
        public String version_name = null;
        public String name = null;
        public String description = null;
        public String author = null;
        public String author_email = null;
        public String change_log = null;
        public String icon = null;
        public String banner = null;
        public I18nData[] i18n = null;
    }

    public static final  class NModManifest
    {
        public NModInfoAndroid android = null;
        public String uuid = null;
        public int target_sdk_version = -1;
        public int min_sdk_version = -1;
        public int manifest_version = 0;
    }
}