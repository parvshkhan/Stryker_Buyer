package com.quickblox.sample.chat;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.quickblox.core.QBSettings;

//import com.quickblox.core.QBSettings;

// amr import vc908.stickerfactory.StickersManager;

public class ApplicationSingletonBuyer extends Application {
    private static final String TAG = ApplicationSingletonBuyer.class.getSimpleName();

    public static final String APP_ID = "29540";
    public static final String AUTH_KEY = "euCZqPezkQ2mcvY";
    public static final String AUTH_SECRET = "y2vr6gjvBzWESSa";
    public static final String STICKER_API_KEY = "72921666b5ff8651f374747bfefaf7b2";

    //public static final String USER_LOGIN = "vishal";
    //  public static final String USER_PASSWORD = "12345678";

    private static ApplicationSingletonBuyer instance;

    public static ApplicationSingletonBuyer getInstance() {
        return instance;
    }


    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate");

        instance = this;

        // Initialise QuickBlox SDK
        //
        QBSettings.getInstance().fastConfigInit(APP_ID, AUTH_KEY, AUTH_SECRET);
    }

    public int getAppVersion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
