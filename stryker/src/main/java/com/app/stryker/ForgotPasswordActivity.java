package com.app.stryker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.jsoncall.JsonCall;
import com.app.utill.AppUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

public class ForgotPasswordActivity extends Activity {

    Context context;
    ProgressDialog dialog;
    EditText edForgetPassword;
    ImageView img_back;


    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_layout);
        context = this;

        init();
        setListener();
    }

    public void init() {
        Typeface tfbold = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Bold.ttf");
        Typeface tfRegular = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Regular.ttf");
        Typeface tfThin = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Thin.ttf");
        ((TextView) findViewById(R.id.forgotpwd_skipfornow_text)).setTypeface(tfRegular);
        ((TextView) findViewById(R.id.forgotpwd_forgotpassword_text)).setTypeface(tfRegular);
        ((TextView) findViewById(R.id.forgotpwd_hint_text)).setTypeface(tfRegular);
        ((Button) findViewById(R.id.forgotpwd_submit_bton)).setTypeface(tfRegular);
        ((EditText) findViewById(R.id.edt_email_forgot)).setTypeface(tfRegular);
        edForgetPassword = (EditText) findViewById(R.id.edt_email_forgot);

        img_back = (ImageView) findViewById(R.id.img_back);


    }

    public void setListener() {
        ((TextView) findViewById(R.id.forgotpwd_skipfornow_text))
                .setOnClickListener(new OnClickListener() {


                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent i = new Intent(ForgotPasswordActivity.this,
                                HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                });


        ((ImageView) findViewById(R.id.backimage_in_forgotpwd))
                .setOnClickListener(new OnClickListener() {


                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                });
        ((Button) findViewById(R.id.forgotpwd_submit_bton))
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        String email_phn = edForgetPassword.getText().toString();
                        if (email_phn.isEmpty()) {
                            edForgetPassword.setError("invalid entry");
                        } else {
                            if (AppUtil.isNetworkAvailable(context)) {
                                dialog = ProgressDialog.show(context, "",
                                        "please wait..");
                                ForgotPasswordTask task = new ForgotPasswordTask();
                                task.execute(email_phn);
                            } else
                                AppUtil.showCustomToast(context,
                                        "Please check your internet");
                        }

						/*if (((EditText) findViewById(R.id.edt_email_forgot))
                                .getText().toString().trim()
								.equalsIgnoreCase("")) {
							AppUtil.showCustomToast(context,
									"please enter your email/Phone");
						}else if (AppUtil.isNetworkAvailable(context)) {
							if (!AppUtil.isEmailValid(((EditText) findViewById(R.id.edt_email_forgot))
									.getText().toString().trim())){
								Intent intent = new Intent(context, ForgotPhoneActivity.class);
								intent.putExtra("mobile",((EditText) findViewById(R.id.edt_email_forgot)).getText().toString());
								startActivity(intent);
							} else
							{
								dialog = ProgressDialog.show(context, "",
										"please wait..");
								ForgotPasswordTask task = new ForgotPasswordTask();
								task.execute(new String[]{((EditText) findViewById(R.id.edt_email_forgot))
										.getText().toString()});
							}
						}

						else {
							AppUtil.showCustomToast(context,
									"Please check your internet");
						}


*/


                    }
                });

        img_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    /**
     * ForgotPassword asyntask
     */
    private class ForgotPasswordTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.forgotPassword(urls[0],
                        context.getResources().getString(R.string.forgot_password_new));
                Log.e("Forgot Password RRESPONSE", "" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            JSONObject jObject;
            try {
                if (result != null) {
                    com.app.model.DbModel.ServiceResponse serviceResponse = new Gson().fromJson(result, com.app.model.DbModel.ServiceResponse.class);
                    if (serviceResponse != null && serviceResponse.commandResult != null) {

                        if (serviceResponse.commandResult.success.equalsIgnoreCase("1")) {
                            jObject = new JSONObject(result);
                            JSONObject job = jObject.getJSONObject("commandResult");
                            int success = job.getInt("success");
                            String message = job.getString("message");
                            if (success == 1) {
                                Intent intent = new Intent(ForgotPasswordActivity.this, ForgotPhoneActivity.class);
                                intent.putExtra("email_pn", edForgetPassword.getText().toString());
                                startActivity(intent);
                                AppUtil.showCustomToast(context, message);

                            } else {
                                AppUtil.showCustomToast(context, message);
                            }


                        }
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

}
