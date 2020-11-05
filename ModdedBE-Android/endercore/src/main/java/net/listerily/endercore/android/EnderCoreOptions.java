package net.listerily.endercore.android;

import com.google.gson.Gson;

import net.listerily.endercore.android.operator.GamePackageManager;

public class EnderCoreOptions {
    private OptionsData optionsData;

    public EnderCoreOptions()
    {
        optionsData = new OptionsData();
    }

    public EnderCoreOptions(String jsonContent) {
        this.optionsData = new Gson().fromJson(jsonContent, OptionsData.class);
    }

    public void setSafeMode(boolean safeMode) {
        optionsData.safe_mode = safeMode;
    }

    public void setPackageName(String packageName) {
        optionsData.game_package_name = packageName;
    }

    public String getPackageName() {
        if(optionsData.game_package_name == null)
            return optionsData.game_package_name = GamePackageManager.PACKAGE_NAME;
        return optionsData.game_package_name;
    }

    public boolean isSafeMode() {
        return optionsData.safe_mode;
    }

    public String toJsonContent(){
        return new Gson().toJson(optionsData);
    }

    public boolean fromJsonContent(String jsonContent)
    {
        this.optionsData = new Gson().fromJson(jsonContent, OptionsData.class);
        if(optionsData == null)
        {
            optionsData = new OptionsData();
            return false;
        }
        return true;
    }

    private final static class OptionsData
    {
        public boolean safe_mode = false;
        public String game_package_name = GamePackageManager.PACKAGE_NAME;
    }
}
