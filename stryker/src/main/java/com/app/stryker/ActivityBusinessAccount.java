package com.app.stryker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ActivityBusinessAccount extends Activity {

    private static final String REGISTER_URL = "http://marketapp.online/web/service/businessregistration";
    Context context;

    ImageView img_back;

    TextView txt_submit;

    EditText edt_full_name, edt_phone, edt_email, edt_business_type,
            edt_city, edt_reseller_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_account);
        context = this;

        init();
        setListener();
    }

    public void init() {

        img_back = (ImageView) findViewById(R.id.img_back);

        txt_submit = (TextView) findViewById(R.id.txt_submit);

        edt_full_name = (EditText) findViewById(R.id.edt_full_name);
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_business_type = (EditText) findViewById(R.id.edt_business_type);
        edt_city = (EditText) findViewById(R.id.edt_city);
        edt_reseller_code = (EditText) findViewById(R.id.edt_reseller_code);

    }

    public void setListener() {

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = edt_full_name.getText().toString();
                String phone = edt_phone.getText().toString();
                if (name.isEmpty()) {
                    edt_full_name.setError("Must be filled");
                } else if (phone.isEmpty()) {
                    edt_phone.setError("Must be Filled");
                } else {

                    registerUser(name, phone);

                }

/*
                Intent in = new Intent(context, Generalstore_list_new.class);
                startActivity(in);*/

            }
        });
    }

    private void registerUser(final String name, final String phone) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            showJson(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(ActivityBusinessAccount.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityBusinessAccount.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", edt_email.getText().toString());
                params.put("mobileno", phone);
                params.put("businesstype", edt_business_type.getText().toString());
                params.put("city", edt_city.getText().toString());
                params.put("resellercode", edt_reseller_code.getText().toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJson(String response) throws Exception {

        if (response != null)
            Log.i("response", response);

        JSONObject jsonObject = new JSONObject(response);
        JSONObject object = jsonObject.getJSONObject("commandResult");
        String succes = object.getString("success");

        if (succes.equalsIgnoreCase("1")) {
            startActivity(new Intent(ActivityBusinessAccount.this, Generalstore_list_new.class));
            finish();
        }

    }
}
