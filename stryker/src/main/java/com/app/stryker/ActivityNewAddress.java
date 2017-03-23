package com.app.stryker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.jsoncall.JsonCall;
import com.app.model.UserProfile;
import com.app.utill.AppUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Inflac on 14-10-2015.
 */
public class ActivityNewAddress extends Activity {
    Context context;
    EditText edt_house_num, edt_street, edt_pin, edtName, edtMobile;
    Button bton_submit_address;
    String house_num = "", street = "", city = "", country = "", pin, name = "", mobile = "";
    ImageView img_add1;
    TextView edt_country, edt_city, edt_state;
    ProgressDialog dialog;
    Spinner spinState, spinCountry, spinCity;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        context = this;
        init();

        img_add1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                AppUtil.onKeyBoardDown(context);
                finish();
            }
        });
        bton_submit_address.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                AppUtil.onKeyBoardDown(context);
                name = edtName.getText().toString().trim();
                mobile = edtMobile.getText().toString().trim();
                house_num = edt_house_num.getText().toString().trim();
                street = edt_street.getText().toString().trim();
                city = edt_city.getText().toString().trim();
                country = edt_country.getText().toString().trim();
                pin = edt_pin.getText().toString().trim();
                String landMark = ((EditText) findViewById(R.id.edt_landmark)).getText().toString();
                String state = edt_state.getText().toString().trim();
                JSONObject addressJson = new JSONObject();
                try {
                    addressJson.put("fullname", name);
                    addressJson.put("mobile", mobile);
                    addressJson.put("address_1", house_num);
                    addressJson.put("address_2", street);
                    addressJson.put("country", country);
                    addressJson.put("state", state);
                    addressJson.put("city", city);
                    addressJson.put("zipCode", pin);
                    addressJson.put("phone", "");
                    addressJson.put("landmark", landMark);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (name.equalsIgnoreCase("")) {
                    AppUtil.showCustomToast(context, "please enter your full name");
                } else if (mobile.equalsIgnoreCase("")) {
                    AppUtil.showCustomToast(context, "please enter mobile number");
                } else if (house_num.equalsIgnoreCase("")) {
                    AppUtil.showCustomToast(context, "please enter house number");
                } else if (street.equalsIgnoreCase("")) {
                    AppUtil.showCustomToast(context, "please enter street");
                } else if (country.equalsIgnoreCase("")) {
                    AppUtil.showCustomToast(context, "please select your country");
                } else if (state.equalsIgnoreCase("")) {
                    AppUtil.showCustomToast(context, "please select your state");
                } else if (city.equalsIgnoreCase("")) {
                    AppUtil.showCustomToast(context, "please select your city");
                } else if (pin.equalsIgnoreCase("")) {
                    AppUtil.showCustomToast(context, "please select pincode");
                } else if (AppUtil.isNetworkAvailable(context)) {
                    dialog = ProgressDialog.show(context, "", "please wait..");
                    AddAddressTask task = new AddAddressTask();
                    task.execute(new String[]{AppUtil.getUserId(context), addressJson.toString()});
                } else {
                    AppUtil.showCustomToast(context, "please check your internet");
                }
                
               /* Intent i = new Intent(ActivityNewAddress.this, ActivityAddress.class);
                i.putExtra("house_num",house_num);
                i.putExtra("street", street);
                i.putExtra("city", city);
                i.putExtra("country", country);
                i.putExtra("state", edt_state.getText().toString());
                i.putExtra("pin", pin);
                setResult(110, i);
                finish();*/


            }
        });


    }

    public void init() {
        spinCountry = (Spinner) findViewById(R.id.spin_country);
        spinState = (Spinner) findViewById(R.id.spin_state);
        spinCity = (Spinner) findViewById(R.id.spin_city);
        edt_house_num = (EditText) findViewById(R.id.edt_house_num);
        edt_street = (EditText) findViewById(R.id.edt_street);
        edt_city = (TextView) findViewById(R.id.edt_city);
        edt_country = (TextView) findViewById(R.id.edt_country);
        edt_state = (TextView) findViewById(R.id.edt_state);
        edt_pin = (EditText) findViewById(R.id.edt_pin);

        edtName = (EditText) findViewById(R.id.edt_name);
        edtMobile = (EditText) findViewById(R.id.edt_mobile);
        bton_submit_address = (Button) findViewById(R.id.bton_submit_address);

        img_add1 = (ImageView) findViewById(R.id.img_add1);

        if (AppUtil.isNetworkAvailable(context)) {
            dialog = ProgressDialog.show(context, "", "please wait..");
            AddressTask task = new AddressTask();
            task.execute();
        } else {
            AppUtil.showCustomToast(context, "please check you internet");
        }

        spinCountry.setOnItemSelectedListener(new OnItemSelectedListener() {


            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                // TODO Auto-generated method stub
                // Create the ArrayAdapter
                spinCountry.getSelectedItem().toString();
                edt_country.setText(spinCountry.getSelectedItem().toString());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ActivityNewAddress.this
                        , android.R.layout.simple_spinner_item, UserProfile.States(spinCountry.getSelectedItem().toString()));
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Set the Adapter
                spinState.setAdapter(arrayAdapter);


            }


            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        spinState.setOnItemSelectedListener(new OnItemSelectedListener() {


            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                spinState.getSelectedItem().toString();
                edt_state.setText(spinState.getSelectedItem().toString());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ActivityNewAddress.this
                        , android.R.layout.simple_spinner_item, UserProfile.Cities(spinCountry.getSelectedItem().toString(), spinState.getSelectedItem().toString()));
                // Set the Adapter
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinCity.setAdapter(arrayAdapter);
            }


            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        spinCity.setOnItemSelectedListener(new OnItemSelectedListener() {


            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                edt_city.setText(spinCity.getSelectedItem().toString());
            }


            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        edt_country.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                spinCountry.performClick();
            }
        });
        edt_city.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (edt_country.getText().toString().length() > 0 && edt_state.getText().toString().length() > 0) {
                    spinCity.performClick();
                } else {
                    AppUtil.showCustomToast(context, "Please select both country and state first.");
                }

            }
        });
        edt_state.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (edt_country.getText().toString().length() > 0) {
                    spinState.performClick();
                } else {
                    AppUtil.showCustomToast(context, "Please select country first.");
                }

            }
        });

    }

    /**
     * country asyntask
     */
    private class AddressTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.countryList(context.getResources().getString(R.string.country_list_url));
                Log.e("country list RRESPONSE", "" + response);
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
                        JSONObject job1 = job.getJSONObject("data");
                        UserProfile.PopulateCountryStateCity(job1.toString());

                        // Create the ArrayAdapter
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ActivityNewAddress.this
                                , android.R.layout.simple_spinner_item, UserProfile.Countries());
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Set the Adapter
                        spinCountry.setAdapter(arrayAdapter);

                        for (int i = 0; i < UserProfile.Countries().length; i++) {
                            if (UserProfile.Countries()[i].equalsIgnoreCase("india")) {
                                spinCountry.setSelection(i);
                                break;
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

            }
        }
    }

    /**
     * add address asyntask
     */
    private class AddAddressTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.addAddress(urls[0], urls[1], context.getResources().getString(R.string.add_address_url));
                Log.e("add address RRESPONSE", "" + response);
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
                        JSONArray job1 = job.getJSONArray("data");
                        Intent i = new Intent();
                        i.putExtra("data", job1.toString());
                        setResult(110, i);
                        finish();
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

}
