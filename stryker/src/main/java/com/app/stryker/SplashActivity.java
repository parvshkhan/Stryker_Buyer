package com.app.stryker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.app.service.OnClearFromRecentService;
import com.app.utill.AppUtil;
import com.app.utill.MyExceptionHandler;

import java.util.List;
// amr import vc908.stickerfactory.StickersManager;


public class SplashActivity extends Activity {

    Context context;
    private static final int Read_Phone_State_flag = 121;
    public static final String STICKER_API_KEY = "72921666b5ff8651f374747bfefaf7b2";

    double mLat, mLong;
    static String city = null;

    static List<Address> addresses;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        context = this;

        AppUtil.setVersionCode("1",context);
        String exception = getIntent().getStringExtra("uncaughtException");
    //   if (exception != null)
      //      Toast.makeText(this, "Try again after some time.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {


            public void run() {
                // TODO Auto-generated method stub
                if (AppUtil.getLogin(context)) {
                    AppUtil.setFirstLogin(context, false);
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 3000);

        startService(new Intent(getBaseContext(), OnClearFromRecentService.class));

       // Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, SplashActivity.class));
    }

}



