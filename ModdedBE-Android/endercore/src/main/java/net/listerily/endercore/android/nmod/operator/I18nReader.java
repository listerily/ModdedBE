package net.listerily.endercore.android.nmod.operator;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class I18nReader
{
    private File file;
    private String locale;
    private HashMap<String,String> map;
    public I18nReader(File file,String locale) throws IOException
    {
        this.file = file;
        this.locale = locale;
        this.map = new HashMap<>();

        Gson gson = new Gson();
        //TODO read data
    }

    public String getLocale() {
        return locale;
    }

    public String get(String key,String v)
    {
        String result = map.get(key);
        if(result == null)
            return v;
        return result;
    }

    private static final class KeyBean
    {
        String key = null;
        String value = null;
    }

    private static final class FileBean{
        KeyBean[] translations = null;
    }

}
