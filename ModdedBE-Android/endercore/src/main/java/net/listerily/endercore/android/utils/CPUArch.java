package net.listerily.endercore.android.utils;

import android.os.Build;

public class CPUArch {
    public static String[] getSupportedAbis() {
        return Build.SUPPORTED_ABIS;
    }

    public static boolean isSupportedAbi(String name) {
        String[] supported = getSupportedAbis();
        for (String s : supported)
            if (s.equals(name))
                return true;
        return false;
    }
}
