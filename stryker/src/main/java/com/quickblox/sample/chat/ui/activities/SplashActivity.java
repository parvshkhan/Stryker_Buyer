package com.quickblox.sample.chat.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.app.stryker.R;

public class SplashActivity extends Activity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent intent = new Intent(SplashActivity.this, DialogsActivity.class);
        startActivity(intent);
        finish();
        // Login to REST API
        //
    /*    final QBUser user = new QBUser();
        user.setLogin(ApplicationSingletonBuyer.USER_LOGIN);
        user.setPassword(ApplicationSingletonBuyer.USER_PASSWORD);

        ChatService.initIfNeed(this);

        ChatService.getInstance().login(user, new QBEntityCallbackImpl() {

            
            public void onSuccess() {
                // Go to Dialogs screen
                //
                Intent intent = new Intent(SplashActivity.this, DialogsActivity.class);
                startActivity(intent);
                finish();
            }

            
            public void onError(List errors) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SplashActivity.this);
                dialog.setMessage("chat login errors: " + errors).create().show();
            }
        });*/
    }
}