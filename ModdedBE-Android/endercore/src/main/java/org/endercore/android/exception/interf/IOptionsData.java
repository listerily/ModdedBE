package org.endercore.android.exception.interf;

import java.io.IOException;

public interface IOptionsData {
    String getJSONAsString(String val) throws IOException;

    void setJSONAsString(String val) throws IOException;
}
