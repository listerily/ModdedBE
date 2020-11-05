package net.listerily.endercore.android;

import net.listerily.endercore.android.operator.GamePackageManager;

public class EnderCoreOptions {
    private OptionsBean optionsData;

    public EnderCoreOptions()
    {
        optionsData = null;
    }

    public EnderCoreOptions(OptionsBean optionsData)
    {
        this.optionsData = optionsData;
    }

    public void setSafeMode(boolean safeMode) {
        optionsData.safe_mode = safeMode;
    }

    public void setPackageName(String packageName) {
        optionsData.game_package_name = packageName;
    }

    public OptionsBean getOptionsData() {
        return optionsData;
    }

    public void setOptionsData(OptionsBean optionsData) {
        this.optionsData = optionsData;
    }

    public String getPackageName() {
        if(optionsData.game_package_name == null)
            return optionsData.game_package_name = GamePackageManager.PACKAGE_NAME;
        return optionsData.game_package_name;
    }

    public boolean isSafeMode() {
        return optionsData.safe_mode;
    }

    public final static class OptionsBean
    {
        public boolean safe_mode = false;
        public String game_package_name = GamePackageManager.PACKAGE_NAME;
    }
}
