package org.endercore.android.exception.utils;

import android.content.pm.PackageInfo;

public final class NModJsonBean {
    public static final class TextOverrideData {
        public String path = null;
        public String mode = MODE_REPLACE;

        public static final String MODE_REPLACE = "replace";
        public static final String MODE_APPEND = "append";
        public static final String MODE_PREPEND = "prepend";
    }

    public static final class FileOverrideData {
        public String path = null;
    }

    public static final class JsonOverrideData {
        public String path = null;
        public String mode = MODE_REPLACE;

        public static final String MODE_REPLACE = "replace";
        public static final String MODE_MERGE = "merge";
    }

    public static final class NativeLibData {
        public String name = null;
        public String main = null;
    }

    public static final class GameSupportData {
        public TextOverrideData[] text_overrides = null;
        public JsonOverrideData[] json_overrides = null;
        public FileOverrideData[] file_overrides = null;
        public NativeLibData[] native_libs = null;
        public String name = null;
        public String platform = null;
        public String[] target_game_versions = null;
    }

    public static final class PackageManifest {
        public PackageInfo android = null;
        public String uuid = null;
        public GameSupportData[] game_supports = null;
        public int target_sdk_version = -1;
        public int min_sdk_version = -1;
        public int manifest_version = -1;
        public int version_code = -1;
        public String version_name = null;
        public String name = null;
        public String description = null;
        public String author = null;
        public String author_email = null;
        public String change_log = null;
        public String icon = null;
        public String banner = null;
    }
}