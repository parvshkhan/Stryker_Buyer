package com.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;


public class ReferrerCatcher extends BroadcastReceiver {

    private static String referrer = "";
    SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        referrer = "";
        sharedPreferences = context.getSharedPreferences("refer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Bundle extras = intent.getExtras();
        if (extras != null) {
            referrer = extras.getString("referrer");
            editor.putString("refercode", referrer);
            editor.commit();
        }

        Log.i("REFERRER", "Referer is: " + referrer);
    }
}