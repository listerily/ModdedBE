package net.listerily.endercore.android;

import android.content.Context;

import net.listerily.endercore.android.operator.FileManager;
import net.listerily.endercore.android.operator.GamePackageManager;
import net.listerily.endercore.android.operator.Launcher;
import net.listerily.endercore.android.operator.NModManager;

import java.io.IOException;

public class EnderCore {

    private Launcher launcher;
    private NModManager nmodManager;
    private GamePackageManager gamePackageManager;
    private EnderCoreOptions enderCoreOptions;
    private boolean initialized;
    private boolean destroyed;

    public static final EnderCore instance = new EnderCore();
    public static final int SDK_VERSION = 1;

    private EnderCore() {
        launcher = null;
        nmodManager = null;
        gamePackageManager = null;
        enderCoreOptions = null;
        initialized = false;
        destroyed = false;
    }

    public void initialize(Context context) {
        if(initialized)
            throw new RuntimeException("Duplicate initialization.EnderCore has already been destroyed");
        try {
            initialized = true;
            destroyed = false;
            FileManager fileManager = new FileManager(context);
            enderCoreOptions = fileManager.loadOptionsFile();
            gamePackageManager = new GamePackageManager(context,this);
            launcher = new Launcher(this);
        } catch(IOException e) {
            throw new RuntimeException("Initialization failed.",e);
        }
    }

    public Launcher getLauncher() {
        checkIfValid();
        return launcher;
    }

    public EnderCoreOptions getEnderCoreOptions() {
        checkIfValid();
        return enderCoreOptions;
    }

    public GamePackageManager getGamePackageManager() {
        checkIfValid();
        return gamePackageManager;
    }

    private void checkIfValid() {
        if(!initialized)
            throw new RuntimeException("EnderCore is uninitialized.Please initialize EnderCore with Context.");
        if(destroyed)
            throw new RuntimeException("EnderCore has already been destroyed.Please reinitialize it.");
    }

    public NModManager getNModManager() {
        return nmodManager;
    }

    public void destroy()
    {
        destroyed = true;
        launcher = null;
        nmodManager = null;
        gamePackageManager = null;
        enderCoreOptions = null;
    }

    public static EnderCore getInstance() {
        return instance;
    }

    public static int getSDKVersion() {
        return SDK_VERSION;
    }
}
