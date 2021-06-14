package com.mojang.minecraftpe.store;

import com.mojang.minecraftpe.store.amazonappstore.AmazonAppStore;
import com.mojang.minecraftpe.store.googleplay.GooglePlayStore;

public class StoreFactory {
    static Store createGooglePlayStore(String googlePlayLicenseKey, StoreListener storeListener) {
        return new GooglePlayStore(null, googlePlayLicenseKey, storeListener);
    }

    static Store createAmazonAppStore(StoreListener storeListener, boolean forFireTV) {
        return new AmazonAppStore(null, storeListener, forFireTV);
    }
}