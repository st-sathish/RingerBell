package com.ringerbell.bootstrap;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.provider.Settings;

import com.ringerbell.db.DatabaseHelper;


public class BootstrapApplication extends Application {

    private static String mDeviceID;

    private DatabaseHelper helper = null;

    public BootstrapApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Initialize the database
        helper = DatabaseHelper.getHelper(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if(null != helper) {
            helper.close();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
