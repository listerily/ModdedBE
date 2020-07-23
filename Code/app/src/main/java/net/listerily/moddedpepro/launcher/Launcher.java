package net.listerily.moddedpepro.launcher;

import android.app.Application;

public class Launcher {
    public static Launcher mInstance;

    public NModManager modManager;
    public ResourceManager resManager;

    private Application contect;

    public void init(Application context)
    {
        this.contect = context;
    }


}
