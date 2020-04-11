package com.example.stayathome;

import android.app.Application;
import android.content.Context;

import com.msg91.sendotpandroid.library.internal.SendOTP;
public class BaseApplication extends Application {


    private static BaseApplication sInstance = null;
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        SendOTP.initializeApp(this);
    }

    public static synchronized BaseApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}
