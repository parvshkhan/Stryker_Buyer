package com.app.stryker;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v4.app.ActivityCompat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.gpstracker.GPSTracker;
import com.app.jsoncall.JsonCall;
import com.app.runtimePermission.RuntimePermissionsActivity;
import com.app.utill.AppUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.inflectica.facebook.AsyncFacebookRunner;
import com.inflectica.facebook.AsyncFacebookRunner.RequestListener;
import com.inflectica.facebook.DialogError;
import com.inflectica.facebook.Facebook;
import com.inflectica.facebook.Facebook.DialogListener;
import com.inflectica.facebook.FacebookError;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.sample.chat.core.ChatService;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

//import com.example.testchat.MainActivity;

public class LoginActivity extends RuntimePermissionsActivity implements ConnectionCallbacks,
        OnConnectionFailedListener {

    // ===========fro facebook==========================
    private static String APP_ID = "555248704625984";

    // Instance of Facebook Class
    private Facebook facebook = new Facebook(APP_ID);
    private AsyncFacebookRunner mAsyncRunner;
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
    private String TAG = getClass().getSimpleName(), fb_Fname = "",
            fb_Lname = "", fb_email = "", fb_id = "";
    int flag = 0;
    String rData = "******";
    // ============================================
    // google plus
    private static final int RC_SIGN_IN = 0;
    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    // Used to store the PendingIntent most recently returned by Google Play
    // services until the user clicks 'sign in'.
    private PendingIntent mSignInIntent;

    String android_id = "", from = "";
    Context context;
    ProgressDialog dialog;

    private Double mLat = 0.0, mLong = 0.0;

    EditText editEmail, editPassword;
    TextView txtSkip, txtForgotPassword, txtSignUp, txt_business_registration;
    Button btnSubmit, btnFb, btnGplus;
    ImageView img_show_password;

    RelativeLayout rl_counter;
    TextView txt_counter;

    int icounter = 10;
    boolean isRunning = true;

    int requestcode = 1234;
    SharedPreferences sharedPreferences;


    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.setStatusBarColor(Color.TRANSPARENT);
        }


        context = this;

        // for facebook
        // ========================================================
        mAsyncRunner = new AsyncFacebookRunner(facebook);
        // ===========================================================
        LoginActivity.super.requestAppPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION}, R.string.Runtimepermissointext, 20);

        init();
        setListener();
        initializeCredentials();

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            displayPromptForEnablingGPS(LoginActivity.this);
            ;
        }


    }

    @Override
    public void onPermissionsGranted(int requestCode) {

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
        Typeface tfbold = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Bold.ttf");
        Typeface tfRegular = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Regular.ttf");
        Typeface tfThin = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Thin.ttf");
        editEmail = (EditText) findViewById(R.id.edt_email);
        editPassword = (EditText) findViewById(R.id.edt_pass);
        txtSkip = (TextView) findViewById(R.id.txt_skip);

        txt_business_registration = (TextView) findViewById(R.id.txt_business_registration);

        txtForgotPassword = (TextView) findViewById(R.id.txt_forgot);
        txtSignUp = (TextView) findViewById(R.id.txt_sign_up);
        btnSubmit = (Button) (findViewById(R.id.btn_signIn));
        btnFb = (Button) (findViewById(R.id.btn_fb_login));
        btnGplus = (Button) (findViewById(R.id.btn_G_login));
        editEmail.setTypeface(tfRegular);
        editPassword.setTypeface(tfRegular);
        txtSkip.setTypeface(tfRegular);
        txtForgotPassword.setTypeface(tfRegular);
        txtSignUp.setTypeface(tfRegular);
        btnSubmit.setTypeface(tfRegular);
        img_show_password = (ImageView) findViewById(R.id.img_show_password);

        // rl_counter = (RelativeLayout) findViewById(R.id.rl_counter);
        // txt_counter = (TextView) findViewById(R.id.txt_counter);

		/*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB){
			   dialog = new ProgressDialog(new ContextThemeWrapper(context, ProgressDialog.THEME_HOLO_LIGHT));
			}else{
				dialog = new ProgressDialog(context);
			}
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("Please wait");*/
    }

    public void setListener() {
        txtSkip.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        txtForgotPassword.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });
        txtSignUp.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                i.putExtra("from", from);
                startActivity(i);
            }
        });

        txt_business_registration.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(context, ActivityBusinessAccount.class);
                startActivity(in);

            }
        });


        img_show_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Pressed show password
                    //Toast.makeText(context, "pressed", Toast.LENGTH_SHORT).show();
                    editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Released hide password
                    //editPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //Toast.makeText(context, "released ", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        btnSubmit.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                AppUtil.onKeyBoardDown(context);
                if (editEmail.getText().toString().trim().equalsIgnoreCase("")) {
                    editEmail.setError("Cannot be blank");
                    return;
                } else if (editPassword.getText().toString().trim()
                        .equalsIgnoreCase("")) {
                    editPassword.setError("Enter Password");

                    return;
                } else if (AppUtil.isNetworkAvailable(context)) {

                    dialog = ProgressDialog.show(context, "",
                            "please wait..");
                    LoginTask task = new LoginTask();
                    task.execute(new String[]{editEmail.getText().toString(),
                            editPassword.getText().toString(), mLat + "", mLong + "",
                            AppUtil.getDeviceId(context), "android"});

                } else {
                    AppUtil.showCustomToast(context,"Please check your internet");
                    return;
                }
            }
        });

        btnFb.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (AppUtil.isNetworkAvailable(context)) {
                    FbClick();
                } else {
                    AppUtil.showCustomToast(context, "please check your internet");
                }

            }
        });

        btnGplus.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (AppUtil.isNetworkAvailable(context)) {
                    onGplusClick();
                } else {
                    AppUtil.showCustomToast(context, "please check your internet");
                }
            }
        });
    }

    public void initializeCredentials() {
        GPSTracker mGPS = new GPSTracker(this);
        if (mGPS.canGetLocation) {
            mLat = mGPS.getLatitude();
            mLong = mGPS.getLongitude();
        }

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        android_id = Secure.getString(getBaseContext().getContentResolver(),
                Secure.ANDROID_ID);
        AppUtil.setDeviceId(context, android_id);
        // Initializing google plus api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

    }

    // g+


    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            mConnectionResult = result;
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }


    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        // facebook.authorizeCallback(requestCode, responseCode, intent);

        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }

    }


    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        // Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

        // Get user's information
        getGplusProfileInformation();

        // Update the UI after signin
        // updateUI(true);

    }


    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        // updateUI(false);
    }

    /**
     * Sign-in into google
     */
    private void signInWithGplus() {

        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    /**
     * Method to resolve any signin errors
     */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (Exception e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    /**
     * Fetching user's information name, email, profile pic
     */
    private void getGplusProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                // String place = currentPerson.getPlacesLived().toString();

                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String personGID = currentPerson.getId();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e("**", "Name: " + personName + "ID: ," + personGID
                        + ", plusProfile: " + personGooglePlusProfile
                        + ", email: " + email + ", Image: " + personPhotoUrl);

                // txtName.setText(personName);
                // txtEmail.setText(email);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                // new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

                callSocialLoginTask("3", email, personGID, personName, "", "",
                        "", personGID);

                if (AppUtil.isNetworkAvailable(context)) {
					/*
					 * DownloadImageTask task = new
					 * DownloadImageTask(personName, personGID, email,
					 * personPhotoUrl); task.execute(personPhotoUrl,
					 * "profilepic");
					 */
                } else {
                    AppUtil.showCustomToast(context,
                            "please check your internet connection");

                }
                signOutFromGplus();

				/*
				 * Intent iGp = new
				 * Intent(LoginActivity.this,PassengerRegisterActivity.class);
				 * iGp.putExtra("name", personName); iGp.putExtra("email",
				 * email); startActivity(iGp);
				 */

            } else {
                AppUtil.showCustomToast(context, "Person information is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sign-out from google
     */
    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            // updateUI(false);
        }
    }


    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        isRunning = false;
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        try {
            System.gc();
            signOutFromGplus();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void onGplusClick() {
        signInWithGplus();
    }

    public void FbClick() {
        /** call facebook login sdk */
        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);

        if (access_token != null) {
            // saveUserName(getFBLoginId());
            // setSignOutData(1);
			/*
			 * MyUtil.setUserId(context, MyUtil.getFbUserId(context));
			 * MyUtil.setAcType(context, "4"); Intent i = new
			 * Intent(getBaseContext(), HomePrivate.class); startActivity(i);
			 */
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString("access_token", null);
            editor.putLong("access_expires", 0);
            editor.commit();
            flag = 1;

            loginToFacebook();

        } else {
            loginToFacebook();
        }
    }

    // for facebook============================================

    /**
     * Function to login into facebook
     */
    public void loginToFacebook() {

        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);

        if (access_token != null) {
            facebook.setAccessToken(access_token);

            getProfileInformation();

			/*
			 * btnFbLogin.setVisibility(View.INVISIBLE);
			 * 
			 * // Making get profile button visible
			 * btnFbGetProfile.setVisibility(View.VISIBLE);
			 * 
			 * // Making post to wall visible
			 * btnPostToWall.setVisibility(View.VISIBLE);
			 * 
			 * // Making show access tokens button visible
			 * btnShowAccessTokens.setVisibility(View.VISIBLE);
			 * btn_logout.setVisibility(View.VISIBLE);
			 */
            Log.d("FB Sessions", "" + facebook.isSessionValid());
        }

        if (expires != 0) {
            facebook.setAccessExpires(expires);
        }

        if (flag == 1) {
            // old public_stream//public_actions

            facebook.authorize(this,
                    new String[]{"public_profile, email, user_birthday, user_friends"},
                    Facebook.FORCE_DIALOG_AUTH, new DialogListener() {

                        public void onCancel() {
                            // Function to handle cancel event
                        }

                        public void onComplete(Bundle values) {
                            // Function to handle complete event
                            // Edit Preferences and update facebook acess_token
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString("access_token",
                                    facebook.getAccessToken());
                            editor.putLong("access_expires",
                                    facebook.getAccessExpires());
                            editor.commit();
                            rData = rData + "facebook.authorize      ";
                            getProfileInformation();

                            // Toast.makeText(getBaseContext(),
                            // "authorize onComplete",
                            // Toast.LENGTH_SHORT).show();

							/*
							 * // Making Login button invisible
							 * btnFbLogin.setVisibility(View.INVISIBLE);
							 * 
							 * // Making logout Button visible
							 * btnFbGetProfile.setVisibility(View.VISIBLE);
							 * 
							 * // Making post to wall visible
							 * btnPostToWall.setVisibility(View.VISIBLE);
							 * btn_logout.setVisibility(View.VISIBLE);
							 * 
							 * // Making show access tokens button visible
							 * btnShowAccessTokens.setVisibility(View.VISIBLE);
							 */
                        }

                        public void onError(DialogError error) {
                            // Function to handle error

                            // Toast.makeText(getBaseContext(),
                            // "authorize onError "+error.toString(),
                            // Toast.LENGTH_SHORT).show();

                        }

                        public void onFacebookError(FacebookError fberror) {
                            // Function to handle Facebook errors
                            // Toast.makeText(getBaseContext(),
                            // "authorize onFacebookError "+fberror.toString(),
                            // Toast.LENGTH_SHORT).show();
                        }

                    });

        }


        if (!facebook.isSessionValid()) {
            facebook.authorize(this,
                    new String[]{"public_profile, email, user_birthday, user_friends"},
                    Facebook.FORCE_DIALOG_AUTH, new DialogListener() {

                        public void onCancel() {

                        }

                        public void onComplete(Bundle values) {

                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString("access_token",
                                    facebook.getAccessToken());
                            editor.putLong("access_expires",
                                    facebook.getAccessExpires());
                            editor.commit();
                            rData = rData + "facebook.authorize      ";
                            getProfileInformation();

                            // Toast.makeText(getBaseContext(),
                            // "authorize onComplete",
                            // Toast.LENGTH_SHORT).show();

							/*
							 * // Making Login button invisible
							 * btnFbLogin.setVisibility(View.INVISIBLE);
							 * 
							 * // Making logout Button visible
							 * btnFbGetProfile.setVisibility(View.VISIBLE);
							 * 
							 * // Making post to wall visible
							 * btnPostToWall.setVisibility(View.VISIBLE);
							 * btn_logout.setVisibility(View.VISIBLE);
							 * 
							 * // Making show access tokens button visible
							 * btnShowAccessTokens.setVisibility(View.VISIBLE);
							 */

                        }

                        public void onError(DialogError error) {

                            // Function to handle error

                            // Toast.makeText(getBaseContext(),
                            // "authorize onError "+error.toString(),
                            // Toast.LENGTH_SHORT).show();

                        }

                        public void onFacebookError(FacebookError fberror) {
                            // Function to handle Facebook errors
                            // Toast.makeText(getBaseContext(),
                            // "authorize onFacebookError "+fberror.toString(),
                            // Toast.LENGTH_SHORT).show();

                        }

                    });
        }
    }

    public void getProfileInformation() {

        rData = rData + "getProfileInformation   ";

        mAsyncRunner.request("me", new RequestListener() {
            public void onComplete(String response, Object state) {
                Log.e("Profile json", response);
                try {
                    rData = rData + "mAsyncRunner.request     response"
                            + response;
					/*
					 * {"id":"100001840327352","name":"Nitay Nit","first_name":
					 * "Nitay","last_name":"Nit", "link":
					 * "https:\/\/www.facebook.com\/nitay.nit","username":"nitay.nit",
					 * "favorite_teams"
					 * :[{"id":"335742425454","name":"v"}],"gender"
					 * :"male","email":"b"
					 * "eauty4more\u0040gmail.com","timezone"
					 * :5.5,"locale":"en_US","verified":true,
					 * "updated_time":"2011-06-11T15:13:40+0000"}
					 */

                    String json = response;

                    // Facebook Profile JSON data
                    JSONObject profile = new JSONObject(json);

                    // getting name of the user

                    // Toast.makeText(getBaseContext(),
                    // profile.getString("birthday")+profile.getString("location"),
                    // Toast.LENGTH_SHORT).show();

                    if (profile.has("first_name")) {
                        final String name = profile.getString("first_name");
                        fb_Fname = name;
                    }
                    if (profile.has("last_name")) {
                        fb_Lname = profile.getString("last_name");
                    }
                    // getting email of the user

                    if (profile.has("email")) {

                        final String email = profile.getString("email");
                        fb_email = email;
                    }
                    // getting email of the user
                    if (profile.has("id")) {

                        final String id = profile.getString("id");
                        fb_id = id;
                    }
                    runOnUiThread(new Runnable() {

                        public void run() {
							/*
							 * Toast.makeText( getApplicationContext(), "Name: "
							 * + name + "\nEmail: " + email + "\nId: " + id,
							 * Toast.LENGTH_LONG) .show();
							 * 
							 * Log.e("result", "Name: " + name + "\nEmail: " +
							 * email + "\nId: " + id);
							 */

                            registerFBUser();

							/*
							 * Intent i=new Intent(getApplicationContext(),
							 * SearchScreen.class); startActivity(i);
							 */
                        }

                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("access_token", null);
                    editor.commit();
                    if (dialog != null)
                        dialog.cancel();
                    registerFBUser();
                }
            }

            public void onIOException(IOException e, Object state) {

            }

            public void onFileNotFoundException(FileNotFoundException e,
                                                Object state) {
            }

            public void onMalformedURLException(MalformedURLException e,
                                                Object state) {

            }

            public void onFacebookError(FacebookError e, Object state) {
            }
        });
    }

    private void registerFBUser() {
		/*
		 * GPSTracker gpsTraker=new GPSTracker(context); String url =
		 * getString(R.string.url_base) + getString(R.string.url_login); new
		 * AsyncPostDataResponse(context, 1, new
		 * RequestData().loginRequestData(fb_email, "", "2",
		 * gpsTraker.getLatitude()+"", gpsTraker.getLongitude()+"",
		 * AppUtils.getDeviceUdId(context), fb_Fname, fb_id), url);
		 */
        callSocialLoginTask("2", fb_email, fb_id, fb_Fname + fb_Lname, "", "",
                "", fb_id);

    }

    public void callSocialLoginTask(String logintype, String email,
                                    String password, String fullname, String age, String gender,
                                    String phone, String socialid) {
        if (AppUtil.isNetworkAvailable(context)) {
            dialog = ProgressDialog.show(context, "Processing", "please wait...");
            //dialog.show();
            LoginTask task = new LoginTask();
            task.execute(new String[]{logintype, email, password, fullname,
                    age, gender, phone, socialid, mLat + "", mLong + "",
                    AppUtil.getDeviceId(context)});

        } else {
            Toast.makeText(context, "Please check your internet connection",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // =============================================================================

    /**
     * login asyntask
     */
    private class LoginTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        // email, password, phone, login_type, socialId, latitude, longitude,
        // name, age, gender, location, deviceToken
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.getLogin(urls[0], urls[1], urls[2], urls[3],
                        urls[4], urls[5],
                        context.getResources().getString(R.string.Login_urlStagging));
                Log.e("Login RRESPONSE", "" + response);
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
                            AppUtil.setLoggedInUser(context, new Gson().toJson(serviceResponse.commandResult.data.user,
                                    com.app.model.DbModel.User.class));
                        }
                    }
                    jObject = new JSONObject(result);
                    JSONObject job = jObject.getJSONObject("commandResult");
                    int success = job.getInt("success");
                    String message = job.getString("message");
                    if (success == 1) {
                        JSONObject job1 = job.getJSONObject("data");
                        JSONObject data = job1.getJSONObject("user");
                        final String userId = data.getString("Id");
                        final String userName = data.getString("FullName");
                        String userProfilePic = data.getString("ProfileImage");
                        final String email = data.getString("Email");
                        String phoneNo = data.getString("Phone");
                        String chatid = data.getString("ChatID");
                        String chatLoginid = data.getString("ChatLoginID");
                        int isPhoneVerified = data.getInt("IsPhoneVerified");
                        String addressArray = data.getJSONArray("useraddresses").toString();
                        AppUtil.setUserAddress(context, addressArray);
                        // String userLocation = data.getString("Location");
                        // JSONObject joSetting = data.getJSONObject("setting");
                        // String gpsEnable = joSetting.getString("IsGpsOn");
                        // String IsChatLogOn =
                        // joSetting.getString("IsChatLogOn");
                        AppUtil.setUserEmail(context, email);
                        AppUtil.setChatUserId(context, chatid);
                        AppUtil.setChatUserLoginId(context, chatLoginid);
                        AppUtil.setUserId(context, userId);
                        AppUtil.setUserName(context, userName);
                        AppUtil.setUserPic(context, userProfilePic);
                        AppUtil.setLogin(context, true);


                        // rl_counter.setVisibility(View.VISIBLE);


                        //startCounter();

                        if (chatid.isEmpty()) {
                            QBAuth.createSession(new QBEntityCallbackImpl<QBSession>() {

                                public void onSuccess(QBSession session, Bundle params) {
                                    // success

                                    final QBUser user1 = new QBUser(userId, "12345678");
                                    user1.setEmail(email);
                                    user1.setFullName(userName);
                                    //user1.setPhone("+1890425678121");
                                    StringifyArrayList<String> tags = new StringifyArrayList<String>();

                                    //user1.setWebsite("www.mysite.com");

                                    QBUsers.signUp(user1, new QBEntityCallbackImpl<QBUser>() {

                                        public void onSuccess(QBUser user, Bundle args) {
                                            Log.e("response------------->", " user created(login)");


                                            AppUtil.setChatUserLoginId(context, user.getLogin().toString());
                                            AppUtil.setUserChatID(context, user.getId().toString());
                                            initChat(userId);

                                        }


                                        public void onError(List<String> errors) {
                                            Log.e("response------------>", " user creation failed(login)");
                                            Intent i = new Intent(LoginActivity.this,
                                                    HomeActivity.class);
                                            startActivity(i);
                                            finish();

                                        }
                                    });

                                }


                                public void onError(List<String> errors) {
                                    // errors
                                }
                            });


                        } else {
                            //	 initChat(userId);

                            if (from.equalsIgnoreCase("checkout")) {
                                Intent i = new Intent(LoginActivity.this,
                                        ActivityAddress.class);
                                startActivity(i);
                                finish();
                            } else {
								/*Intent i = new Intent(LoginActivity.this,
		    							HomeActivity.class);
		    					startActivity(i);
		    					finish();*/
                                if (isPhoneVerified == 1) {
                                    if (serviceResponse.commandResult.data.user.storeCount > 0) {
                                        AppUtil.setLogin(context, true);
                                        sharedPreferences = context.getSharedPreferences("refer", Context.MODE_PRIVATE);
                                        String refercode = sharedPreferences.getString("refercode", null);

                                       // String strRef = "701";
                                        if (refercode !=null) {
                                            AddStoreTask task = new AddStoreTask();
                                            task.execute(new String[]{AppUtil.getUserId(getApplicationContext()),
                                                    refercode +"",
                                                    AppUtil.getDeviceId(LoginActivity.this)});
                                        }
                                        else
                                        {
                                            Intent i = new Intent(LoginActivity.this,
                                                    HomeActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    } else {
                                        AppUtil.setLogin(context, true);
                                        sharedPreferences = context.getSharedPreferences("refer", Context.MODE_PRIVATE);
                                        String refercode = sharedPreferences.getString("refercode", null);

                                        if (refercode != null) {
                                            AddStoreTask task = new AddStoreTask();
                                            task.execute(new String[]{AppUtil.getUserId(getApplicationContext()),
                                                    refercode +"",
                                                    AppUtil.getDeviceId(LoginActivity.this)});
                                        }
                                        else
                                        {
                                            Intent i = new Intent(LoginActivity.this,
                                                    Generalstore_list_new.class);
                                            startActivity(i);
                                            finish();
                                        }

                                    }
                                } else {
                                    AppUtil.setLogin(context, false);
                                    AppUtil.showCustomToast(context, "Please verify your phone number first");
                                    Intent i = new Intent(LoginActivity.this,
                                            SellerVerificationActivityNew.class);
                                    startActivity(i);
                                    finish();
                                }
                            }

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
                //rl_counter.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void displayPromptForEnablingGPS(final Activity activity) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Please turn on the GPS?";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();
    }


    public void initChat(String userid) {
        ChatService.initIfNeed(LoginActivity.this);

        final QBUser user = new QBUser(userid, "12345678");
        QBAuth.createSession(user, new QBEntityCallbackImpl<QBSession>() {

            public void onSuccess(QBSession session, Bundle params) {
                // success, login to chat
                user.setId(session.getUserId());
                ChatService.getInstance().login(user, new QBEntityCallbackImpl() {

                    public void onSuccess() {
                        // success
                        Log.e("Login", " in side success--->");
                        sharedPreferences = context.getSharedPreferences("refer", Context.MODE_PRIVATE);
                        String refercode = sharedPreferences.getString("refercode", null);
                        if (refercode !=null) {
                            AddStoreTask task = new AddStoreTask();
                            task.execute(new String[]{AppUtil.getUserId(getApplicationContext()),
                                    refercode +"",
                                    AppUtil.getDeviceId(LoginActivity.this)});
                        }


                    }


                    public void onError(List errors) {
                        // errror
                        Log.e("Login", " in side failure--->" + errors.get(0).toString());

                        if (errors.get(0).toString() == "You have already logged in chat") {
                            Log.e("sdfdfgdfgdf", "logindfgdfgdfgdfgdgf");
                            Intent i = new Intent(LoginActivity.this,
                                    HomeActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                });
            }

            public void onError(List<String> errors) {
                // errors
            }
        });
    }
	/*public void startCounter(){
		new CountDownTimer(10000, 1000) {
			public void onTick(long millisUntilFinished) {
				txt_counter.setText(" " + millisUntilFinished / 1000);
			}

			public void onFinish() {
				startCounter();
			}
		}.start();

	}*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }


    private class AddStoreTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
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
                        Intent i = new Intent(LoginActivity.this,
                                HomeActivity.class);
                        startActivity(i);
                        finish();
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
                Intent i = new Intent(LoginActivity.this,
                        HomeActivity.class);
                startActivity(i);
                finish();


            }

        }
    }
}
