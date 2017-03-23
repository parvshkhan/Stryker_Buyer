package com.app.utill;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by admin on 12/10/2016.
 */

public class SessionUtils {
    public static final String SESSION_CITY = "Citylist";
    public static final String SESSION_CATEGORY = "Categorylist";
    public static final String SESSION_HOMEDATA = "HomeData";

    public static void setCitiesForSearch(Context context, String JsonResponse, String Qualifier) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor sEditor = sharedPreferences.edit();
        sEditor.putString(Qualifier, JsonResponse);
        sEditor.commit();
    }

    public static String getCitiesForSearch(Context context, String Qualifier) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(Qualifier, "");
    }
}
