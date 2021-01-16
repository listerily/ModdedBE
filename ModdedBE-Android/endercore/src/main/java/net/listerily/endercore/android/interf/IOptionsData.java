package net.listerily.endercore.android.interf;

import java.io.IOException;

public interface IOptionsData {
    String getJSONAsString(String val) throws IOException;
    void setJSONAsString(String val) throws IOException;
}
