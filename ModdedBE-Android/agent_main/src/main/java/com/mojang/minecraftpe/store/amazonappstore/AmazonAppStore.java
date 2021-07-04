package com.mojang.minecraftpe.store.amazonappstore;

import android.content.Context;

import com.mojang.minecraftpe.store.ExtraLicenseResponseData;
import com.mojang.minecraftpe.store.Store;
import com.mojang.minecraftpe.store.StoreListener;

public class AmazonAppStore implements Store {
    StoreListener mListener;
    private boolean mForFireTV;

    public AmazonAppStore(Context context, StoreListener listener) {
        mListener = listener;
        // TODO: THIS FUNCTION IS MODIFIED
    }

    public AmazonAppStore(Context context, StoreListener listener, boolean forFireTV) {
        mListener = listener;
        mForFireTV = forFireTV;
        // TODO: THIS FUNCTION IS MODIFIED
    }

    public String getStoreId() {
        return "android.amazonappstore";
        // TODO: THIS FUNCTION IS MODIFIED
    }

    public boolean hasVerifiedLicense() {
        return true;
        // TODO: THIS FUNCTION IS MODIFIED
    }

    public boolean receivedLicenseResponse() {
        return true;
        // TODO: THIS FUNCTION IS MODIFIED
    }

    public void queryProducts(String[] productIds) {
        // TODO: THIS FUNCTION IS MODIFIED
    }

    public void acknowledgePurchase(String receipt, String productType) {
        // TODO: THIS FUNCTION IS MODIFIED
    }

    public void queryPurchases() {
        // TODO: THIS FUNCTION IS MODIFIED
    }

    public String getProductSkuPrefix() {
        return mForFireTV ? "firetv." : "";
        // TODO: THIS FUNCTION IS MODIFIED
    }

    public String getRealmsSkuPrefix() {
        return mForFireTV ? "firetv." : "";
        // TODO: THIS FUNCTION IS MODIFIED
    }

    public void destructor() {
        // TODO: THIS FUNCTION IS MODIFIED
    }

    @Override
    public ExtraLicenseResponseData getExtraLicenseData() {
        long[] data = new long[]{60000, 0, 0};
        return new ExtraLicenseResponseData(data[0], data[1], data[2]);
        // TODO: THIS FUNCTION IS MODIFIED
    }

    public void purchase(String productId, boolean isSubscription, String payload) {
        // TODO: THIS FUNCTION IS MODIFIED
    }

    public void purchaseGame() {
        // TODO: THIS FUNCTION IS MODIFIED
    }
}