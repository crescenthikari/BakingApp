package net.crescenthikari.bakingapp;

import com.facebook.stetho.Stetho;

public class DebugApp extends BakingApp {
    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}
