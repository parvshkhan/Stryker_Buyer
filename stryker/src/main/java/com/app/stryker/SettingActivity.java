package com.app.stryker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.utill.AppUtil;

public class SettingActivity extends Activity {
    Context context;


    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = this;

        init();
        setListener();

    }

    public void init() {
        if (AppUtil.getLogin(context)) {
            ((TextView) findViewById(R.id.txt_login)).setText("Logout");
        } else {
            ((TextView) findViewById(R.id.txt_login)).setText("Login");
        }
    }

    public void setListener() {

	/*	((TextView) findViewById(R.id.txt_back)).setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});*/

        ((RelativeLayout) findViewById(R.id.rl_login)).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (AppUtil.getLogin(context)) {
                    /**logout here*/
                    //	((TextView) findViewById(R.id.txt_back)).setText("Logout");
                    AppUtil.setLogin(context, false);
                    AppUtil.setUserId(context, "");
                    AppUtil.setUserAddress(context, "");
                    AppUtil.setChatUserId(context, "");
                    AppUtil.setChatUserLoginId(context, "");
                    AppUtil.setrefreshValue(context, "yes");

                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    // if you are checking for this in your other Activities
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    /**login here, redirect to login screen*/
                    Intent i = new Intent(SettingActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}
