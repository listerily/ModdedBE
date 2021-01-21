package org.endercore.android.exception.nmod.overrider;

import java.io.File;

public abstract class BaseOverrider {
    protected final File overridePath;
    public BaseOverrider(File overridePath){
        this.overridePath = overridePath;
    }

    public abstract void performOverride(File root,String name,int mode) throws Exception;
}
