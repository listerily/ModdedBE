package net.listerily.moddedpepro.launcher;

public class LauncherOptions {
    private Launcher launcher;
    private boolean safeMode = false;
    private String packageName = GameManager.PACKAGE_NAME;
    public LauncherOptions(Launcher launcher)
    {
        this.launcher = launcher;
    }

    public void setSafeMode(boolean safeMode) {
        this.safeMode = safeMode;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean isSafeMode() {
        return safeMode;
    }
}
