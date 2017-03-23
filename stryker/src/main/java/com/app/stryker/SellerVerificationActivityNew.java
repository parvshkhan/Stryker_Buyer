package com.app.stryker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.gpstracker.GPSTracker;
import com.app.jsoncall.JsonCall;
import com.app.listeners.SmsReceivedListner;
import com.app.model.DbModel.User;
import com.app.receiver.IncomingSms;
import com.app.utill.AppUtil;
import com.google.gson.Gson;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.sample.chat.core.ChatService;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.json.JSONObject;

import java.util.List;

public class SellerVerificationActivityNew extends Activity implements SmsReceivedListner {

    Context context;
    User loggedInUser;
    static EditText edtNumber;
    Button btnSubmit;

    ProgressDialog dialog;
    RelativeLayout rl_resend;
    ImageView imgBack;
    private Double mLat = 0.0, mLong = 0.0;

    String email = "", userName = "";
    SharedPreferences sharedPreferences;
    CountDownTimer ctimer;
    IncomingSms receiveSms;

    String chatuserid = "";

    boolean isRegOnChat = false;

    String messageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_verification_new);
        context = this;
        loggedInUser = new Gson().fromJson(AppUtil.getULoggedInUser(context), User.class);
        init();
        setListener();
        startCounter();

    }

    public void init() {

        try {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                if (b.containsKey("email_id")) {
                    email = b.getString("email_id");
                }
                if (b.containsKey("user_name")) {
                    userName = b.getString("user_name");
                }
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        GPSTracker mGPS = new GPSTracker(this);
        if (mGPS.canGetLocation) {
            mLat = mGPS.getLatitude();
            mLong = mGPS.getLongitude();
        }

        edtNumber = (EditText) findViewById(R.id.edt_verify_code);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        //  rl_resend = (RelativeLayout)findViewById(R.id.rl_resend);
        imgBack = (ImageView) findViewById(R.id.img_back);

        receiveSms = new IncomingSms();
        receiveSms.setOnSmsReceivedListener(this);
        registerReceiver(receiveSms, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

        try {
            chatuserid = email;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // CREATE CHAT USER
        ChatService.initIfNeed(context);
        QBAuth.createSession(new QBEntityCallbackImpl<QBSession>() {

            public void onSuccess(QBSession session,
                                  Bundle params) {
                // success

                final QBUser user1 = new QBUser(chatuserid,
                        "12345678");
                // user1.setExternalId("453415");
                // user1.setFacebookId("100233453457767");
                // user1.setTwitterId("182334635457");
                //user1.setEmail(loggedInUser.Email);
                user1.setFullName(loggedInUser.FullName);
                // user1.setPhone("+189045678121");
                StringifyArrayList<String> tags = new StringifyArrayList<String>();

                // user1.setWebsite("www.mysite.com");

                QBUsers.signUp(user1,
                        new QBEntityCallbackImpl<QBUser>() {

                            public void onSuccess(QBUser user,
                                                  Bundle args) {
                                Log.e("response------------->",
                                        " user created");
                            
                                if(!user.getId().toString().isEmpty() && !chatuserid.isEmpty())
                                {
                                    AppUtil.setChatUserLoginId(context, chatuserid);
                                    AppUtil.setChatUserId(context, user.getId().toString());
                                    SavechatidTask task = new SavechatidTask();
                                    task.execute(new String[]{AppUtil.getUserId(context), user.getId().toString(), user.getLogin().toString()});
                                }
                                else
                                {
                                    
                                    finish();
                                    Toast.makeText(context, "Something Went Wrong ..Please Register Again ", Toast.LENGTH_SHORT).show();
                                }
                                

                            }


                            public void onError(
                                    List<String> errors) {
                                Log.e("response------------>",
                                        " user creation failed");

                                Toast.makeText(context, "Something Went Wrong ..Try again", Toast.LENGTH_SHORT).show();

                            }
                        });

            }


            public void onError(List<String> errors) {
                // errors
                Toast.makeText(context, "Something Went Wrong ..Try again", Toast.LENGTH_SHORT).show();


            }
        });

        // END CHAT USER


    }


    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        try {
            unregisterReceiver(receiveSms);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setListener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                AppUtil.onKeyBoardDown(context);
                if (AppUtil.isNetworkAvailable(context)) {
                    dialog = ProgressDialog.show(context, "",
                            "please wait..");
                    VerifyCodeTask task = new VerifyCodeTask();
                    Log.i("user id seller", AppUtil.getUserId(context));
                    task.execute(new String[]{AppUtil.getUserId(context), edtNumber.getText().toString()});
                } else {
                    AppUtil.showCustomToast(context,
                            "Please check your internet");
                }

            }
        });


        ((ImageView) findViewById(R.id.img_resend)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (AppUtil.isNetworkAvailable(context)) {
                    if (ctimer != null)
                        ctimer.cancel();
                    startCounter();
                    dialog = ProgressDialog.show(context, "",
                            "please wait..");
                    ResendCodeTask task = new ResendCodeTask();
                    task.execute(new String[]{AppUtil.getUserId(context)});
                } else {
                    AppUtil.showCustomToast(context,
                            "Please check your internet");
                }
            }
        });

      /*  imgBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });*/
    }


    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

    }

    private class VerifyCodeTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.verifyCode(urls[0], urls[1], context.getResources().getString(R.string.verify_mobile_urlstagging));
                Log.e("VerifyCode RRESPONSE", "" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            JSONObject jObject;
            try {
                if (result != null) {
                    jObject = new JSONObject(result);
                    JSONObject job = jObject.getJSONObject("commandResult");
                    int success = job.getInt("success");
                    String message = job.getString("message");
                    if (success == 1) {
                        //Boolean status = job.getBoolean("data");
                        AppUtil.setLogin(context, true);
                        sharedPreferences = context.getSharedPreferences("refer", Context.MODE_PRIVATE);
                        String refercode = sharedPreferences.getString("refercode", null);
                        if (refercode !=null) {
                            AddStoreTask task = new AddStoreTask();
                            task.execute(new String[]{AppUtil.getUserId(getApplicationContext()),
                                    refercode +"",
                                    AppUtil.getDeviceId(SellerVerificationActivityNew.this)});
                        }
                        else
                        {
                            Intent in = new Intent(SellerVerificationActivityNew.this, HomeActivity.class);
                            in.putExtra("email_id", email);
                            in.putExtra("user_name", userName);

                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(in);
                            finish();
                            AppUtil.showCustomToast(context, message);
                        }

                    } else {
                        AppUtil.showCustomToast(context, message);
                    }

                } else {
                    AppUtil.showCustomToast(context,
                            "please check your internet connection");
                }
                if (dialog != null)
                    dialog.cancel();
            } catch (Exception e) {
                e.printStackTrace();
                if (dialog != null)
                    dialog.cancel();

            }
        }
    }

    private class ResendCodeTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.resendCode(urls[0], context.getResources().getString(R.string.resend_code_url));
                Log.e("ResendCode RRESPONSE", "" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            JSONObject jObject;
            try {
                if (result != null) {
                    jObject = new JSONObject(result);
                    JSONObject job = jObject.getJSONObject("commandResult");
                    int success = job.getInt("success");
                    String message = job.getString("message");
                    if (success == 1) {
                        // Boolean status = job.getBoolean("data");
                        AppUtil.showCustomToast(context, message);

                    } else {
                        AppUtil.showCustomToast(context, message);
                    }

                } else {
                    AppUtil.showCustomToast(context,
                            "please check your internet connection");
                }
                if (dialog != null)
                    dialog.cancel();
            } catch (Exception e) {
                e.printStackTrace();
                if (dialog != null)
                    dialog.cancel();

            }
        }
    }

    private class SavechatidTask extends AsyncTask<String, Void, String> {


        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.updatechat(urls[0], urls[1], urls[2], context.getResources().getString(R.string.update_chat));
                Log.e("GetDialog", "" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            JSONObject jObject;
            try {
                if (result != null) {


                    jObject = new JSONObject(result);
                    JSONObject job = jObject.getJSONObject("commandResult");
                    int success = job.getInt("success");
                    String message = job.getString("message");
                    if (success == 1) {
                        isRegOnChat = true;

                        if (messageCode != null) {
                            edtNumber.setText(messageCode);
                            ctimer.cancel();
                            if (message != null) {
                                btnSubmit.performClick();
                            }
                        }

	                	/*   Intent i = new Intent(SignUpActivity.this,
                                    SellerVerificationActivity.class);
							startActivity(i);
							finish();*/

                    }


                }

                if (dialog != null)
                    dialog.cancel();
            } catch (Exception e) {
                e.printStackTrace();
                if (dialog != null)
                    dialog.cancel();
            }
        }


    }

    public void startCounter() {
		/*  ((ProgressBar) findViewById(R.id.pb_counter)).getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"),
	                 android.graphics.PorterDuff.Mode.MULTIPLY);*/
        ((ProgressBar) findViewById(R.id.pb_counter)).setVisibility(View.VISIBLE);
        ctimer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                ((TextView) findViewById(R.id.txt_counter))
                        .setText("" + millisUntilFinished / 1000);

            }

            public void onFinish() {
                btnSubmit.setEnabled(true);
                ((ProgressBar) findViewById(R.id.pb_counter)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.txt_counter)).
                        setText("We are unable to fetch code,please type below manually.");
            }
        }.start();


    }

    public void onSmsReceived(String message) {
        // TODO Auto-generated method stub
        Log.e("Message received", message + "***");
		/* edtNumber.setText(message);
		 ctimer.cancel();
		 if(message != null ){
		 btnSubmit.performClick();
		 }*/

        messageCode = message;
        if (isRegOnChat) {
            edtNumber.setText(messageCode);
            btnSubmit.setEnabled(true);

            ctimer.cancel();
            if (messageCode != null) {
                btnSubmit.performClick();
            }
        }
    }

    private class AddStoreTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SellerVerificationActivityNew.this);
            progressDialog.setMessage("Adding Your store");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.addStore(urls[0], urls[1], urls[2], getApplicationContext()
                        .getResources().getString(R.string.add_store_url));
                Log.e("add Store RRESPONSE", "" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            JSONObject jObject;
            progressDialog.dismiss();

            try {
                if (result != null) {
                    jObject = new JSONObject(result);
                    JSONObject job = jObject.getJSONObject("commandResult");
                    int success = job.getInt("success");
                    String message = job.getString("message");
                    if (success == 1) {
                        Intent in = new Intent(SellerVerificationActivityNew.this, HomeActivity.class);
                        in.putExtra("email_id", email);
                        in.putExtra("user_name", userName);

                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(in);
                        finish();
                        AppUtil.showCustomToast(context, message);
                      /*  JSONObject job1 = job.getJSONObject("data");
                        JSONArray ja = job1.getJSONArray("stores");
                        String imageUrl = "";

                        AppUtil.setrefreshValue(getApplicationContext(), "yes");

                        AppUtil.onKeyBoardDown(getApplicationContext());
                        Intent i = new Intent(LoginActivity.this,
                                HomeActivity.class);
                        startActivity(i);
                        finish();*/
                    } else {
                        AppUtil.showCustomToast(getApplicationContext(), message);
                    }

                } else {
                    AppUtil.showCustomToast(getApplicationContext(),
                            "please check your internet connection");
                }

            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }

}
