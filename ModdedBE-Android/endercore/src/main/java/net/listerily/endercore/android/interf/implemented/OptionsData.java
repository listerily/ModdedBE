package net.listerily.endercore.android.interf.implemented;

import net.listerily.endercore.android.interf.IFileEnvironment;
import net.listerily.endercore.android.interf.IOptionsData;
import net.listerily.endercore.android.utils.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class OptionsData implements IOptionsData {
    private final IFileEnvironment environment;

    public OptionsData(IFileEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public String getJSONAsString(String val) throws IOException {
        File jsonFile = new File(environment.getOptionsFilePath());
        if (jsonFile.exists()) {
            return FileUtils.readFileAsString(jsonFile);
        }
        return val;
    }

    @Override
    public void setJSONAsString(String val) throws IOException {
        File jsonFile = new File(environment.getOptionsFilePath());
        FileUtils.copy(new ByteArrayInputStream(val.getBytes()), jsonFile);
    }
}
