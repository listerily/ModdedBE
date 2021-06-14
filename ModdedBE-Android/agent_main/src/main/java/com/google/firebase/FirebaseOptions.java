package com.google.firebase;

import android.content.Context;

public final class FirebaseOptions {
    FirebaseOptions(String str, String str2, String str3, String str4, String str5, String str6, String str7, byte b) {
        this(str, str2, str3, str4, str5, str6, str7);
    }

    private FirebaseOptions(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
    }

    public static FirebaseOptions fromResource(Context context) {
        return null;
    }

    public final String getApiKey() {
        return null;
    }

    public final String getApplicationId() {
        return null;
    }

    public final String getDatabaseUrl() {
        return null;
    }

    public final String getGaTrackingId() {
        return null;
    }

    public final String getGcmSenderId() {
        return null;
    }

    public final String getStorageBucket() {
        return null;
    }

    public final String getProjectId() {
        return null;
    }

    public final boolean equals(Object obj) {
        return true;
    }

    public final int hashCode() {
        return 0;
    }

    public final String toString() {
        return null;
    }

    public static final class Builder {

        public Builder() {
        }

        public Builder(FirebaseOptions firebaseOptions) {
        }

        public final Builder setApiKey(String str) {
            return this;
        }

        public final Builder setApplicationId(String str) {
            return this;
        }

        public final Builder setDatabaseUrl(String str) {
            return this;
        }

        public final Builder setGaTrackingId(String str) {
            return this;
        }

        public final Builder setGcmSenderId(String str) {
            return this;
        }

        public final Builder setStorageBucket(String str) {
            return this;
        }

        public final Builder setProjectId(String str) {
            return this;
        }

        public final FirebaseOptions build() {
            return null;
        }
    }
}