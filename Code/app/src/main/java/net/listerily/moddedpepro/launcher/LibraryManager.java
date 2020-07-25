package net.listerily.moddedpepro.launcher;

public class LibraryManager {
    private Launcher launcher;

    public String NATIVE_LIBRARY_MINECRAFTPE = "libminecraftpe.so";
    public String NATIVE_LIBRARY_SUBSTRATE = "libsubstrate.so";
    public String NATIVE_LIBRARY_CORE = "libcore.so";

    public LibraryManager(Launcher launcher) {
        this.launcher = launcher;
    }

    public void extractAllGameNativeLibraries()
    {

    }

}
