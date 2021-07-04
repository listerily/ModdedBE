package com.mojang.minecraftpe.store.googleplay;

import com.mojang.minecraftpe.MainActivity;
import com.mojang.minecraftpe.store.ExtraLicenseResponseData;
import com.mojang.minecraftpe.store.Store;
import com.mojang.minecraftpe.store.StoreListener;

public class GooglePlayStore implements Store {
    MainActivity mActivity;
    StoreListener mListener;

    public GooglePlayStore(MainActivity activity, String licenseKey, StoreListener listener) {
        mActivity = activity;
        mListener = listener;
        mListener.onStoreInitialized(true);
        // TODO: THIS FUNCTION IS MODIFIED
    }

    public String getStoreId() {
        return "android.googleplay";
        // TODO: THIS FUNCTION IS MODIFIED
    }

    public boolean hasVerifiedLicense() {
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
        return "";
        // TODO: THIS FUNCTION IS MODIFIED
    }

    public String getRealmsSkuPrefix() {
        return "";
        // TODO: THIS FUNCTION IS MODIFIED
    }

    public boolean receivedLicenseResponse() {
        return true;
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