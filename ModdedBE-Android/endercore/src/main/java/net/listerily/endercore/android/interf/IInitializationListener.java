package net.listerily.endercore.android.interf;

import net.listerily.endercore.android.nmod.NMod;

public interface IInitializationListener
{
    void onStart();
    void onLoadGameFilesStart();
    void onCopyGameFile(String name);
    void onLoadNativeLibrariesStart();
    void onLoadNativeLibrary(String name);
    void onLoadNativeLibrariesFinish();
    void onLoadJavaLibrariesStart();
    void onLoadJavaLibrary(String name);
    void onLoadJavaLibrariesFinish();
    void onLoadResourcesStart();
    void onLoadResourcesFinish();
    void onLoadGameFilesFinish();
    void onLoadNModsStart();
    void onLoadNMod(NMod nmod);
    void onLoadNModNativeLibrary(NMod nmod,String name);
    void onLoadNModAsset(String name);
    void onLoadNModsFinish();
    void onArrange();
    void onFinish();
    void onSuspend();
}