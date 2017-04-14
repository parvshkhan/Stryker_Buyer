package com.app.stryker;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.Database.SQLiteHelper;
import com.app.model.AssociateModel;
import com.app.utill.AppUtil;

import org.jivesoftware.smackx.muc.packet.MUCUser;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InviteandEarn extends Activity implements View.OnClickListener {


    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final String ASSOCIATE_USER_URL = "http://marketapp.online/web/salesline/api/inviteusers";
    private static final String NEW_USER_URL = "http://marketapp.online/web/salesline/api/inviteusersbynew";
    TextView shareContacts;
    ImageView backArrow;
    EditText edAssociate, edPassword;
    EditText edName, edPhone;
    Button btSubmitAssociate, btNewUser;
    int confirmNext = -1;
    private String sponserID;
    private String userName,Pass;
    int limit;
    private static final int MY_SOCKET_TIMEOUT_MS = 50000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            //Toast.makeText(this, "Large screen", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_inviteand_earnlarge);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            //Toast.makeText(this, "Normal sized screen", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_inviteand_earn);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            //Toast.makeText(this, "Small sized screen", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_inviteand_earn);
        }
        else {
            setContentView(R.layout.activity_inviteand_earn);
            //Toast.makeText(this, "Screen size is neither large, normal or small", Toast.LENGTH_LONG).show();
        }




        shareContacts = (TextView) findViewById(R.id.shareContacts);
        backArrow = (ImageView) findViewById(R.id.back);

        edAssociate = (EditText) findViewById(R.id.assctUser);
        edPassword = (EditText) findViewById(R.id.pass1);


        edName = (EditText) findViewById(R.id.name);
        edPhone = (EditText) findViewById(R.id.edmobile);



        btSubmitAssociate = (Button) findViewById(R.id.btSubmitAssciate);
        btNewUser = (Button) findViewById(R.id.btSubmitNewUser);

        btSubmitAssociate.setOnClickListener(this);
        btNewUser.setOnClickListener(this);

        shareContacts.setOnClickListener(this);
        backArrow.setOnClickListener(this);

        String associateName = AppUtil.getAssociateName(InviteandEarn.this);
        String pass= AppUtil.getAssociatepass(InviteandEarn.this);
        if(associateName!=null && pass!=null)
        {

            edAssociate.setText(associateName);
            edPassword.setText(pass);
        }



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.shareContacts:

                showContacts();

                break;
            case R.id.back:
                finish();
                break;

            case R.id.btSubmitAssciate:
                submitAssociate();
                break;

            case R.id.btSubmitNewUser:
                submitNewUser();
                break;


        }

    }

    private void submitNewUser() {

        String name = edName.getText().toString();
        String phone = edPhone.getText().toString();
        char check='1';
        if(!phone.equalsIgnoreCase(""))
            check = phone.charAt(0);
        if (name.isEmpty()) {
            edName.setError("cannot be null");
            edName.requestFocus();
        } else if (phone.isEmpty()) {

            edPhone.setError("Cannot be null");
            edPhone.requestFocus();
        } else if (check == '0' || check == '1' || check == '2' || check == '3' || check == '4' || check == '5' || check == '6' || phone.length() < 10) {

            edPhone.setError("Check Mobile Number");
            edPhone.requestFocus();
            return;
        }else
            ServerRequestNewUser(name, phone);


    }

    private void ServerRequestNewUser(final String name, final String phone) {


        final ProgressDialog progressDialog = new ProgressDialog(InviteandEarn.this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(true);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NEW_USER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            progressDialog.dismiss();
                            shoJsonNewUser(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InviteandEarn.this, "Please try again", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("loginuser", AppUtil.getChatUserId(InviteandEarn.this));
                params.put("name", name);
                params.put("mobile", phone);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void shoJsonNewUser(String response) throws Exception {

        if (response != null) {
            AppUtil.onKeyBoardDown(InviteandEarn.this);
            JSONObject jsonObject = new JSONObject(response);
            String success = jsonObject.getString("success");
            if (success.equalsIgnoreCase("1")) {
                confirmNext = 0;
                Toast.makeText(this, "Now You can share Contacts", Toast.LENGTH_SHORT).show();
               /* edName.setText("");
                edPhone.setText("");*/

            /*    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(InviteandEarn.this,Contact.class);
                startActivity(intent);*/

            } else if (success.equalsIgnoreCase("0")) {
                Toast.makeText(this, "UnAuthenticate User", Toast.LENGTH_SHORT).show();
            } else if (success.equalsIgnoreCase("2")) {
                Toast.makeText(this, "You have already sent Invitations", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Something Went wrong ..Try again", Toast.LENGTH_SHORT).show();


        }

    }


    private void submitAssociate() {

        String associate = edAssociate.getText().toString();
        String pass = edPassword.getText().toString();


        if (associate.isEmpty()) {
            edAssociate.setError("cannot be null");
            edAssociate.requestFocus();
        } else if (pass.isEmpty()) {
            edPassword.setError("Cannot be null");
            edPassword.requestFocus();
        } else
            ServerRequestAssociate(associate, pass);


    }

    private void ServerRequestAssociate(final String associate, final String pass) {

        final ProgressDialog progressDialog = new ProgressDialog(InviteandEarn.this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(true);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ASSOCIATE_USER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            progressDialog.dismiss();

                            shoJsonAssociate(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InviteandEarn.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", associate);
                params.put("password", pass);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void shoJsonAssociate(String response) throws Exception {
        if (response != null) {
            AppUtil.onKeyBoardDown(InviteandEarn.this);
            Log.i("Associate", response);

            JSONObject jsonObject = new JSONObject(response);
            String success = jsonObject.getString("success");
            if (success.equalsIgnoreCase("1")) {
                JSONObject jsonObject1  = jsonObject.getJSONObject("data");
                limit = jsonObject1.getInt("current_limit");

                AppUtil.setAssociateName(edAssociate.getText().toString(),InviteandEarn.this);
                AppUtil.setAssociatePass(edPassword.getText().toString(),InviteandEarn.this);

                confirmNext = 1;

                JSONObject jsonObj = jsonObject.getJSONObject("data");
                sponserID = jsonObj.getString("sponserID");

                Intent intent = new Intent(InviteandEarn.this,Contact.class);
                intent.putExtra("sponserId",sponserID);
                intent.putExtra("limit",limit);
                startActivity(intent);
            } else if (success.equalsIgnoreCase("0")) {
                Toast.makeText(this, "UnAuthenticate User", Toast.LENGTH_SHORT).show();
            } else if (success.equalsIgnoreCase("2")) {
                Toast.makeText(this, "You have already sent Invitations", Toast.LENGTH_SHORT).show();
            } else

                Toast.makeText(this, "Not matched Try again", Toast.LENGTH_SHORT).show();


        }
    }

    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);

            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
           if (confirmNext != -1) {
                Intent intent = new Intent(InviteandEarn.this, Contact.class);
                intent.putExtra("sponserId", sponserID);
                intent.putExtra("limit",5000);
                startActivity(intent);
            } else
                Toast.makeText(this, "Please Login Above", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                startActivity(new Intent(getApplicationContext(), Contact.class));
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
