package com.app.stryker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.jsoncall.JsonCall;
import com.app.utill.AppUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ForgotPhoneActivity extends Activity {
    Context context;
    String MobileNo_email = "";
    EditText edt_password, edt_confirm_password;
    Button bton_submit_login;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_phone);
        context = this;
        if (getIntent().hasExtra("email_pn")) {
            MobileNo_email = getIntent().getStringExtra("email_pn");
        }
        init();
        setListener();
    }

    public void init() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        edt_password = (EditText) findViewById(R.id.signup_password_text);
        edt_confirm_password = (EditText) findViewById(R.id.signup_confirmpassword_text);
        bton_submit_login = (Button) findViewById(R.id.bton_submit_login);
    }

    public void setListener() {
        bton_submit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstpass = edt_password.getText().toString();
                String secondpass = edt_confirm_password.getText().toString();

                if (firstpass.isEmpty()) {
                    edt_password.setError("Cannot be blank");

                } else if (secondpass.isEmpty()) {
                    edt_confirm_password.setError("Cannot be blank");


                } else {
                    if (!firstpass.equalsIgnoreCase(secondpass)) {

                        Toast.makeText(context, "Password not match", Toast.LENGTH_SHORT).show();

                    } else {
                        if (AppUtil.isNetworkAvailable(context)) {
                            progressDialog.show();
                            Log.i("mobile", MobileNo_email);
                            resetPassword();

                            /*
                            ForgotMobilePasswordTask forgotMobilePasswordTask = new ForgotMobilePasswordTask();
                            forgotMobilePasswordTask.execute(new String[]{MobileNo_email, edt_password.getText().toString(), AppUtil.getDeviceId(context)});*/
                        } else {

                            AppUtil.showCustomToast(context, "Please check your internet");

                        }


                    }


                }

            }
        });
    }

    private class ForgotMobilePasswordTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "",
                    "please wait..");
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.ResetMobilePassword(urls[0], urls[1], urls[2],
                        context.getResources().getString(R.string.forgot_password_reset_mobile_new));
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
                                AppUtil.showCustomToast(context, message);
                                startActivity(new Intent(ForgotPhoneActivity.this, LoginActivity.class));
                                finish();
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

    private void resetPassword() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://marketapp.online/web/service/resetPassword",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i("reset password response", response);
                        //Toast.makeText(ForgotPhoneActivity.this,response,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("commandResult");
                            String success = jsonObject1.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                startActivity(new Intent(ForgotPhoneActivity.this, LoginActivity.class));
                                Toast.makeText(context, "Please Login to Continue", Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForgotPhoneActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.headers == null) {
                    // cant just set a new empty map because the member is final.
                    response = new NetworkResponse(
                            response.statusCode,
                            response.data,
                            Collections.<String, String>emptyMap(), // this is the important line, set an empty but non-null map.
                            response.notModified,
                            response.networkTimeMs);
                }

                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("loginId", MobileNo_email);
                params.put("password", edt_password.getText().toString());
                params.put("cpassword", edt_password.getText().toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
