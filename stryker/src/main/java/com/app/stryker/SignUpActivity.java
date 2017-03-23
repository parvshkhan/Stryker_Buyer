package com.app.stryker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.gpstracker.GPSTracker;
import com.app.jsoncall.JsonCall;
import com.app.model.DbModel.User;
import com.app.utill.AppUtil;
import com.google.gson.Gson;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.request.ProgressHttpEntityWrapper;
import com.quickblox.sample.chat.core.ChatService;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.json.JSONObject;

import java.util.List;

public class SignUpActivity extends Activity {

    Context context;
    ProgressDialog dialog;
    EditText editEmail, editPassword, editCnfpassword, edtPhone, signup_full_name;

    ImageView img_chkbox, img_unchkbox;

    private Double mLat = 0.0, mLong = 0.0;
    String from = "";

    String email = "", userName = "";
    RelativeLayout rl_counter;
    TextView txt_counter;
    User loggedInUser;
    TextView txt_signin;

    int icounter = 10;
    boolean isRunning = true;
    ImageView imgBack;
    SharedPreferences sharedPreferences;
    String chatuserid;
    ProgressDialog progressDialog;


    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        context = this;
        loggedInUser = new Gson().fromJson(AppUtil.getULoggedInUser(context), User.class);
        init();
        setListener();


    }

    public void init() {

        try {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                if (b.containsKey("from")) {
                    from = b.getString("from");
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        GPSTracker mGPS = new GPSTracker(this);
        if (mGPS.canGetLocation) {
            mLat = mGPS.getLatitude();
            mLong = mGPS.getLongitude();
        }
        Typeface tfbold = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Bold.ttf");
        Typeface tfRegular = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Regular.ttf");
        Typeface tfThin = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Thin.ttf");
        signup_full_name = (EditText) findViewById(R.id.signup_full_name);
        editEmail = ((EditText) findViewById(R.id.signup_emailaddress_text));
        editPassword = ((EditText) findViewById(R.id.signup_password_text));
        editCnfpassword = ((EditText) findViewById(R.id.signup_confirmpassword_text));
        imgBack = (ImageView) findViewById(R.id.backimage_in_signup);

        //edtPhone = ((EditText) findViewById(R.id.edt_phone));
        //edtPhone.setText("9818944767");

        //	((TextView) findViewById(R.id.signup_skipfornow_text))
        //			.setTypeface(tfRegular);

        ((Button) findViewById(R.id.signup_signup_bton)).setTypeface(tfRegular);
        editEmail.setTypeface(tfRegular);
        editPassword.setTypeface(tfRegular);
        editCnfpassword.setTypeface(tfRegular);
//		edtPhone.setTypeface(tfRegular);
        rl_counter = (RelativeLayout) findViewById(R.id.rl_counter1);
        txt_counter = (TextView) findViewById(R.id.txt_counter1);

        txt_signin = (TextView) findViewById(R.id.txt_signin);


        img_chkbox = (ImageView) findViewById(R.id.img_chkbox);
        img_unchkbox = (ImageView) findViewById(R.id.img_unchkbox);


    }

    public void setListener() {

        imgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    /*	((TextView) findViewById(R.id.signup_skipfornow_text))
				.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(SignUpActivity.this,
								HomeActivity.class);
						startActivity(i);
						finish();
					}
				});*/

        txt_signin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(context, LoginActivity.class);
                startActivity(in);
            }
        });


        img_chkbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                img_unchkbox.setVisibility(View.VISIBLE);
                img_chkbox.setVisibility(View.GONE);
            }
        });

        img_unchkbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                img_chkbox.setVisibility(View.VISIBLE);
                img_unchkbox.setVisibility(View.GONE);
            }
        });


        ((ImageView) findViewById(R.id.backimage_in_signup))
                .setOnClickListener(new OnClickListener() {


                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                });
        ((Button) findViewById(R.id.signup_signup_bton))
                .setOnClickListener(new OnClickListener() {


                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        AppUtil.onKeyBoardDown(context);
                        if (editEmail.getText().toString().trim().equalsIgnoreCase("")) {
                            editEmail.setError("Cannot be blank");
                            return;
                        } else if (editPassword.getText().toString().trim().equalsIgnoreCase("")) {

                            editPassword.setError("Cannot be blank");
                            return;
                        } else {

                            String email_phone = editEmail.getText().toString().trim();

                            if (email_phone.contains("@")) {
                                if (AppUtil.isNetworkAvailable(context)) {
                                    dialog = ProgressDialog.show(context, "",
                                            "please wait..");
                                    RegisterTask task = new RegisterTask();
                                    task.execute(new String[]{editEmail.getText().toString(),
                                            editPassword.getText().toString(), signup_full_name.getText().toString(), mLat + "",
                                            mLong + "", AppUtil.getDeviceId(context), "android", "3",
                                    });
                                } else
                                    AppUtil.showCustomToast(context,
                                            "Please check your internet");
                            } else {
                                String email_mobile = editEmail.getText().toString().trim();
                                char check = email_mobile.charAt(0);

                                if (check == '0' || check == '1' || check == '2' || check == '3' || check == '4' || check == '5' || check == '6' || email_mobile.length() < 10) {

                                    editEmail.setError("Check Mobile Number");
                                    return;
                                } else {
                                    if (AppUtil.isNetworkAvailable(context)) {
                                        dialog = ProgressDialog.show(context, "",
                                                "please wait..");
                                        RegisterTask task = new RegisterTask();
                                        task.execute(new String[]{editEmail.getText().toString(),
                                                editPassword.getText().toString(), signup_full_name.getText().toString(), mLat + "",
                                                mLong + "", AppUtil.getDeviceId(context), "android", "3",
                                        });
                                    } else
                                        AppUtil.showCustomToast(context,
                                                "Please check your internet");

                                }


                            }


                        }

//kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk
					/*	if (!editEmail.getText().toString().trim().equalsIgnoreCase("")) {

							String email_mobile = editEmail.getText().toString().trim();
							char check =email_mobile.charAt(0);

							if(   check == '0' ||check == '1' ||check == '2' ||check == '3' ||check == '4' ||check == '5' ||check == '6' || email_mobile.length()<10)
							{

								Toast.makeText(context, "Invalid entry", Toast.LENGTH_SHORT).show();
								return;
							}
						} else if (editPassword.getText().toString().trim()
								.equalsIgnoreCase("")) {
							AppUtil.showCustomToast(context,
									"please enter your password");
						}

						else if (AppUtil.isNetworkAvailable(context)) {

							dialog = ProgressDialog.show(context, "",
									"please wait..");

							RegisterTask task = new RegisterTask();

							task.execute(new String[] { editEmail.getText().toString(),
									editPassword.getText().toString(), signup_full_name.getText().toString(),mLat+"",
									mLong + "",AppUtil.getDeviceId(context),"android",  "3",
							});
						} else {
							AppUtil.showCustomToast(context,
									"Please check your internet");
						}
*/

                    }
                });

    }

    /**
     * signup  asyntask
     */
    private class RegisterTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        // email, password, phone, login_type, socialId, latitude, longitude,
        // name, age, gender, location, deviceToken
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.getRegister(urls[0], urls[1], urls[2],
                        urls[3], urls[4], urls[5], urls[6], urls[7],
                        context.getResources().getString(R.string.RegisterUrl));
                Log.e("registration RRESPONSE", "" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            JSONObject jObject;
            try {
                if (result != null) {
                    final com.app.model.DbModel.ServiceResponse serviceResponse = new Gson().fromJson(result,
                            com.app.model.DbModel.ServiceResponse.class);
                    if (serviceResponse != null && serviceResponse.commandResult != null) {
                        if (serviceResponse.commandResult.success.equalsIgnoreCase("1")) {

                            AppUtil.setLoggedInUser(context, new Gson().toJson(serviceResponse.commandResult.data.user,
                                    com.app.model.DbModel.User.class));

                            AppUtil.setUserId(context, serviceResponse.commandResult.data.user.Id);
                            Log.i("user id", serviceResponse.commandResult.data.user.Id);

                            if (serviceResponse.commandResult.data.user.Email.length() > 0) {

                                AppUtil.setChatUserLoginId(context, serviceResponse.commandResult.data.user.Email);
                            } else {

                                AppUtil.setChatUserLoginId(context, serviceResponse.commandResult.data.user.Phone);
                            }


                            AppUtil.setUserName(context, serviceResponse.commandResult.data.user.FullName);
                            AppUtil.setUserPic(context, serviceResponse.commandResult.data.user.ProfileImage);
                            AppUtil.setLogin(context, false);

                            if (from.equalsIgnoreCase("checkout")) {
                                Intent i = new Intent(SignUpActivity.this,
                                        ActivityAddress.class);
                                startActivity(i);
                                finish();
                            } else {


                                if (serviceResponse.commandResult.data.user.Phone.length() > 0) {

                                    Intent in = new Intent(context, SellerVerificationActivityNew.class);
                                    in.putExtra("email_id", serviceResponse.commandResult.data.user.Phone);
                                    in.putExtra("user_name", serviceResponse.commandResult.data.user.FullName);

                                    startActivity(in);
                                } else {
                                    // CREATE CHAT USER
                                    ChatService.initIfNeed(context);
                                    progressDialog = new ProgressDialog(SignUpActivity.this);
                                    progressDialog.setMessage("Registring...");
                                    progressDialog.setCancelable(true);
                                    progressDialog.show();
                                    QBAuth.createSession(new QBEntityCallbackImpl<QBSession>() {

                                        public void onSuccess(QBSession session,
                                                              Bundle params) {
                                            // success
                                            chatuserid= serviceResponse.commandResult.data.user.Email;
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
                               /*     Intent i = new Intent(SignUpActivity.this,
                                            HomeActivity.class);
                                    i.putExtra("email_id", serviceResponse.commandResult.data.user.Email);
                                    i.putExtra("user_name", serviceResponse.commandResult.data.user.FullName);
                                    startActivity(i);
                                    finish();*/
                                }


                            }
                            rl_counter.setVisibility(View.GONE);
                        } else {
                            AppUtil.showCustomToast(context, serviceResponse.commandResult.message);
                        }
                    }


                } else {
                    AppUtil.showCustomToast(context, "error executing service");
                }


            } catch (Exception e) {
                e.printStackTrace();

                if (dialog != null)
                    dialog.cancel();
                rl_counter.setVisibility(View.GONE);

            } finally {
                dialog.dismiss();
            }
        }
    }


    // UPdate chatid task


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
                    progressDialog.dismiss();

                    jObject = new JSONObject(result);
                    JSONObject job = jObject.getJSONObject("commandResult");
                    int success = job.getInt("success");
                    String message = job.getString("message");
                    if (success == 1) {

                        if (from.equalsIgnoreCase("checkout")) {
                            Intent i = new Intent(SignUpActivity.this,
                                    ActivityAddress.class);
                            startActivity(i);
                            finish();
                        } else {
                            Intent i = new Intent(SignUpActivity.this,
                                    HomeActivity.class);
                            startActivity(i);
                            finish();
                           /* Intent i = new Intent(SignUpActivity.this,
                                    SellerVerificationActivityNew.class);
                            i.putExtra("email_id", email);
                            i.putExtra("user_name", userName);
                            startActivity(i);
                            finish();*/
                        }
                        rl_counter.setVisibility(View.GONE);
                    }


                }

                if (dialog != null)
                    dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                if (dialog != null)
                    dialog.dismiss();
            }
        }


    }


    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        isRunning = false;


    }

    public void startCounter() {

        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                txt_counter.setText(" " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                startCounter();
            }
        }.start();


    }




}
