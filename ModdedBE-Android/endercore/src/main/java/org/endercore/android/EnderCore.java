package org.endercore.android;

import android.content.Context;

import org.endercore.android.interf.IFileEnvironment;
import org.endercore.android.interf.IOptionsData;
import org.endercore.android.interf.implemented.FileEnvironment;
import org.endercore.android.interf.implemented.OptionsData;
import org.endercore.android.operator.GamePackageManager;
import org.endercore.android.operator.Launcher;
import org.endercore.android.operator.NModManager;
import org.endercore.android.operator.OptionsManager;

import java.io.IOException;

public final class EnderCore {

    private Launcher launcher;
    private NModManager nmodManager;
    private GamePackageManager gamePackageManager;
    private OptionsManager optionsManager;
    private IFileEnvironment environment;
    private IOptionsData optionsData;
    private boolean initialized;
    private boolean destroyed;
    private int mode;

    public static final EnderCore instance = new EnderCore();
    public static final int SDK_INT = 1;
    public static final int MODE_PUBLIC = 0;
    public static final int MODE_PRIVATE = 1;

    private EnderCore() {
        launcher = null;
        nmodManager = null;
        gamePackageManager = null;
        optionsManager = null;
        initialized = false;
        destroyed = false;
    }

    public void initialize(Context context, int mode) {
        if (destroyed)
            throw new RuntimeException("EnderCore has already been destroyed.");
        if (initialized)
            throw new RuntimeException("Duplicate initialization.");
        if (mode != MODE_PUBLIC && mode != MODE_PRIVATE)
            throw new RuntimeException("Unsupported initialization mode value: " + mode + ".");

        initialized = true;
        this.mode = mode;

        try {
            if (environment == null)
                environment = new FileEnvironment(context);
            if (optionsData == null)
                optionsData = new OptionsData(environment);
            optionsManager = new OptionsManager(this);
            gamePackageManager = new GamePackageManager(context);
            nmodManager = new NModManager(this);
            launcher = new Launcher(this);
        } catch (IOException ioException) {
            initialized = false;
            throw new RuntimeException("Initialize failed.",ioException);
        }
    }

    public void destroy() {
        if (!initialized)
            throw new RuntimeException("EnderCore hasn't been initialized and there's no need for destruction.");
        if (destroyed)
            throw new RuntimeException("EnderCore has already been destroyed");
        destroyed = true;
        launcher = null;
        nmodManager = null;
        gamePackageManager = null;
        optionsManager = null;
    }

    public void setFileEnvironment(IFileEnvironment environment) {
        if (initialized)
            throw new RuntimeException("Please assign IEnvironment before EnderCore initialization.");
        this.environment = environment;
    }

    public void setOptionsData(IOptionsData optionsData) {
        if (initialized)
            throw new RuntimeException("Please assign IOptionsData before EnderCore initialization.");
        this.optionsData = optionsData;
    }

    public Launcher getLauncher() {
        if (!initialized)
            throw new RuntimeException("EnderCore is uninitialized.Please initialize EnderCore with Context.");
        if (destroyed)
            throw new RuntimeException("EnderCore has already been destroyed.Please reinitialize it.");
        return launcher;
    }

    public OptionsManager getOptionsManager() {
        if (!initialized)
            throw new RuntimeException("EnderCore is uninitialized.Please initialize EnderCore with Context.");
        if (destroyed)
            throw new RuntimeException("EnderCore has already been destroyed.Please reinitialize it.");
        return optionsManager;
    }

    public GamePackageManager getGamePackageManager() {
        if (!initialized)
            throw new RuntimeException("EnderCore is uninitialized.Please initialize EnderCore with Context.");
        if (destroyed)
            throw new RuntimeException("EnderCore has already been destroyed.Please reinitialize it.");
        return gamePackageManager;
    }

    public NModManager getNModManager() {
        if (!initialized)
            throw new RuntimeException("EnderCore is uninitialized.Please initialize EnderCore with Context.");
        if (destroyed)
            throw new RuntimeException("EnderCore has already been destroyed.Please reinitialize it.");
        return nmodManager;
    }

    public IFileEnvironment getFileEnvironment() {
        if (!initialized)
            throw new RuntimeException("EnderCore is uninitialized.Please initialize EnderCore with Context.");
        if (destroyed)
            throw new RuntimeException("EnderCore has already been destroyed.Please reinitialize it.");
        return environment;
    }

    public IOptionsData getOptionsData() {
        if (!initialized)
            throw new RuntimeException("EnderCore is uninitialized.Please initialize EnderCore with Context.");
        if (destroyed)
            throw new RuntimeException("EnderCore has already been destroyed.Please reinitialize it.");
        return optionsData;
    }

    public int getEnderCoreMode() {
        return mode;
    }

    public static EnderCore getInstance() {
        return instance;
    }
}