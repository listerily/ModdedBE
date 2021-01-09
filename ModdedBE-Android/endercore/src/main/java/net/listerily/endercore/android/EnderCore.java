package net.listerily.endercore.android;

import android.content.Context;
import android.os.Build;

import net.listerily.endercore.android.operator.FileManager;
import net.listerily.endercore.android.operator.GamePackageManager;
import net.listerily.endercore.android.operator.Launcher;
import net.listerily.endercore.android.operator.NModManager;

import java.io.IOException;

public final class EnderCore {

    private Launcher launcher;
    private NModManager nmodManager;
    private GamePackageManager gamePackageManager;
    private EnderCoreOptions enderCoreOptions;
    private boolean initialized;
    private boolean destroyed;

    public static final EnderCore instance = new EnderCore();
    public static final int SDK_INT = 1;

    private EnderCore() {
        launcher = null;
        nmodManager = null;
        gamePackageManager = null;
        enderCoreOptions = null;
        initialized = false;
        destroyed = false;
    }

    public void initialize(Context context) throws IOException {
        if(destroyed)
            throw new RuntimeException("EnderCore has already been destroyed.");
        if(initialized)
            throw new RuntimeException("Duplicate initialization.");
        initialized = true;
        FileManager fileManager = new FileManager(context);
        enderCoreOptions = fileManager.loadEnderCoreOptionsFile();
        gamePackageManager = new GamePackageManager(context,this);
        launcher = new Launcher(this);
    }

    public Launcher getLauncher() {
        if(!initialized)
            throw new RuntimeException("EnderCore is uninitialized.Please initialize EnderCore with Context.");
        if(destroyed)
            throw new RuntimeException("EnderCore has already been destroyed.Please reinitialize it.");
        return launcher;
    }

    public EnderCoreOptions getEnderCoreOptions() {
        if(!initialized)
            throw new RuntimeException("EnderCore is uninitialized.Please initialize EnderCore with Context.");
        if(destroyed)
            throw new RuntimeException("EnderCore has already been destroyed.Please reinitialize it.");
        return enderCoreOptions;
    }

    public GamePackageManager getGamePackageManager() {
        if(!initialized)
            throw new RuntimeException("EnderCore is uninitialized.Please initialize EnderCore with Context.");
        if(destroyed)
            throw new RuntimeException("EnderCore has already been destroyed.Please reinitialize it.");
        return gamePackageManager;
    }

    public NModManager getNModManager() {
        if(!initialized)
            throw new RuntimeException("EnderCore is uninitialized.Please initialize EnderCore with Context.");
        if(destroyed)
            throw new RuntimeException("EnderCore has already been destroyed.Please reinitialize it.");
        return nmodManager;
    }

    public void destroy()
    {
        if(!initialized)
            throw new RuntimeException("EnderCore hasn't been initialized and there's no need for destruction.");
        if(destroyed)
            throw new RuntimeException("EnderCore has already been destroyed");
        destroyed = true;
        launcher = null;
        nmodManager = null;
        gamePackageManager = null;
        enderCoreOptions = null;
    }

    public static EnderCore getInstance() {
        return instance;
    }
}