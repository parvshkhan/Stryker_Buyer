package com.app.stryker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.Database.SQLiteHelper;
import com.app.adapters.AddCategoryAdapter;
import com.app.adapters.AddStoreListAdapter;
import com.app.jsoncall.JsonCall;
import com.app.model.Category;
import com.app.model.ServiceResponse;
import com.app.model.StoreListModel;
import com.app.utill.AppUtil;
import com.app.utill.SessionUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivityNew extends Activity {

    EditText edSearchBusiness;
    AutoCompleteTextView edSearchCategories1;
    ListView listStore;

    ArrayList<String> cityList = new ArrayList<String>();
    ArrayList<String> categoryList = new ArrayList<String>();

    ArrayList<StoreListModel> arSearchList = new ArrayList<StoreListModel>();
    ArrayList<StoreListModel> arStoreList = new ArrayList<StoreListModel>();

    AddCategoryAdapter addCategoryAdapter;
    AddStoreListAdapter adapter;

    ProgressDialog dialog1;
    ProgressDialog dialog2 = null;
    int position;
    String searchString;
    Intent intent;
    String searchAutocomplete;
    ImageView imageViewback;
    FloatingActionButton floatingActionButtonStores;
    ArrayList<StoreListModel> tempArrayList = new ArrayList<>();
    ArrayList<StoreListModel> storeListModels;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("auto", searchAutocomplete);
        //  Log.i("onsaved",searchAutocomplete);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category_business);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (getIntent() != null)
            Log.i("positin", getIntent().getIntExtra("pos", 0) + "");


        dialog1 = new ProgressDialog(SearchActivityNew.this);
        imageViewback = (ImageView) findViewById(R.id.back);
        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

                startActivity(new Intent(SearchActivityNew.this, Generalstore_list_new.class));

            }
        });

        edSearchCategories1 = (AutoCompleteTextView) findViewById(R.id.category);
        edSearchCategories1.setThreshold(1);
        edSearchCategories1.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);
        edSearchBusiness = (EditText) findViewById(R.id.business);


        edSearchCategories1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                // String data  = getIntent().getStringExtra("data");
                String searchString = charSequence.toString();
                //searchString = arStoreList.get(position).getStoreindustrytypeName();

                Log.i("search", searchString);
                int textlength = searchString.length();
                tempArrayList.clear();
                tempArrayList = new ArrayList<StoreListModel>();
                for (StoreListModel c : arStoreList) {
                    if (textlength <= c.getStoreindustrytypeName().length()) {
                        if (c.getStoreindustrytypeName().toLowerCase().equalsIgnoreCase(searchString.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }

                AddBuisnessAdapter addBuisnessAdapter = new AddBuisnessAdapter(getApplicationContext(), R.layout.search_buisness, tempArrayList);

                listStore.setAdapter(addBuisnessAdapter);
                TextView textView = null;
               /* if(tempArrayList.size()<=0)
                {
                   textView = ((TextView)findViewById(R.id.txt_nodata));
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("No Record Found");
                    ((RelativeLayout)findViewById(R.id.rl_nodata)).setVisibility(View.VISIBLE);

                }

                else
                {
                    textView.setVisibility(View.GONE);
                    ((RelativeLayout)findViewById(R.id.rl_nodata)).setVisibility(View.GONE);

                }*/


            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        listStore = (ListView) findViewById(R.id.listStore);

        try {
            edSearchCategories1.setText(getIntent().getStringExtra("auto"));

        } catch (Exception e) {
        }


       /* GetCategoryTask getCategoryTask = new GetCategoryTask();
        getCategoryTask.execute(new String[]{});*/



        SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(SearchActivityNew.this);

        storeListModels = sqLiteHelper.getCategoryListing();
        for (int i = 0; i < storeListModels.size(); i++) {
            categoryList.add(storeListModels.get(i).getStoreName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchActivityNew.this, android.R.layout.simple_dropdown_item_1line, categoryList);
        edSearchCategories1.setAdapter(adapter);


        StoreListTask storeListTask = new StoreListTask();
        storeListTask.execute();

    /*    edSearchCategories1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;                                                             // editComment.getCompoundDrawables()[DRAWABLE_LEFT].getBounds‌​().width()))

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (edSearchCategories1.getRight() - edSearchCategories1.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        Intent intent = new Intent(SearchActivityNew.this,Generalstore_list_new.class);
                        finish();
                        startActivity(intent);

                        // your action here

                        return true;
                    }
                }


                return false;
            }
        });*/
        edSearchCategories1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                searchAutocomplete = edSearchCategories1.getText().toString();


                Log.i("serach value", searchAutocomplete);
                edSearchCategories1.setText(searchAutocomplete);

            }
        });


        edSearchBusiness.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                int textlength = charSequence.length();
                tempArrayList.clear();
                tempArrayList = new ArrayList<StoreListModel>();
                for (StoreListModel c : arStoreList) {
                    if (textlength <= c.getStoreName().length()) {
                        if (c.getStoreName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }

                AddBuisnessAdapter addBuisnessAdapter = new AddBuisnessAdapter(getApplicationContext(), R.layout.search_buisness, tempArrayList);

                listStore.setAdapter(addBuisnessAdapter);
                TextView textView = null;
                if (tempArrayList.size() <= 0) {
                    textView = ((TextView) findViewById(R.id.txt_nodata));
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("No Record Found");
                    ((RelativeLayout) findViewById(R.id.rl_nodata)).setVisibility(View.VISIBLE);

                } else {
                    textView = ((TextView) findViewById(R.id.txt_nodata));
                    textView.setVisibility(View.GONE);
                    ((RelativeLayout) findViewById(R.id.rl_nodata)).setVisibility(View.GONE);

                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        listStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // edSearchBusiness.setText(((EditText) view.findViewById(R.id.business)).getText().toString());

                AppUtil.onKeyBoardDown(getApplicationContext());
                if (AppUtil.isNetworkAvailable(SearchActivityNew.this)) {

                    dialog2 = ProgressDialog.show(SearchActivityNew.this, "", "please wait..");
                    AddStoreTask task = new AddStoreTask();
                    task.execute(new String[]{AppUtil.getUserId(getApplicationContext()),
                            tempArrayList.get(i).getStoreId(),
                            AppUtil.getDeviceId(SearchActivityNew.this)});

                } else {
                    AppUtil.showCustomToast(getApplicationContext(), "please check your internet");
                }

            }
        });

    }

    private class GetCategoryTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            dialog1.setCancelable(false);
            dialog1.setMessage("Please wait");
            dialog1.show();


        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.getMasterData(getApplicationContext()
                        .getResources().getString(R.string.master_category_url));
                Log.e("add Store RRESPONSE", "" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            try {
                if (result != null) {
                    ServiceResponse serviceResponse = AppUtil.getGsonInstance().fromJson(result, ServiceResponse.class);
                    SessionUtils.setCitiesForSearch(getApplicationContext(), result, SessionUtils.SESSION_CATEGORY);
                    categoryList.clear();
                    if (serviceResponse != null && serviceResponse.commandResult != null) {
                        if (serviceResponse.commandResult.success == 1) {
                            for (Category category : serviceResponse.commandResult.data.categories) {
                                categoryList.add(category.Name);
                            }

                            /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchActivityNew.this, android.R.layout.simple_dropdown_item_1line, categoryList);
                            edSearchCategories1.setAdapter(adapter);*/

                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (dialog1 != null)
                    dialog1.dismiss();
            }
        }
    }

    private class StoreListTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SearchActivityNew.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        // email, password, phone, login_type, socialId, latitude, longitude,
        // name, age, gender, location, deviceToken
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.storeList(getApplicationContext().getResources().getString(
                        R.string.store_listing_for_add_url));
                Log.e("store list RRESPONSE", "" + response);
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
                        arStoreList.clear();
                        JSONObject job1 = job.getJSONObject("data");
                        JSONArray ja = job1.getJSONArray("stores");
                        String imageUrl = "";
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            JSONArray jar = jo.getJSONArray("StoreGallery");
                            if (jar.length() > 0) {
                                JSONObject j = jar.getJSONObject(0);
                                imageUrl = j.getString("ImageUrl");
                            } else {
                                imageUrl = "";
                            }

                            arStoreList.add(new StoreListModel(jo
                                    .getString("StoreId"), imageUrl, jo
                                    .getString("Name"), jo
                                    .getString("StoreCode"), jo
                                    .getString("Address"), jo
                                    .getString("Description"), jo
                                    .getString("industrytypeID"), jo
                                    .getString("industrytype")));

                        }

                    } else {
                        AppUtil.showCustomToast(getApplicationContext(), message);
                        if (progressDialog != null)
                            progressDialog.cancel();
                    }

                } else {
                    AppUtil.showCustomToast(getApplicationContext(),
                            "please check your internet connection");
                    if (progressDialog != null)
                        progressDialog.cancel();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (progressDialog != null)
                    progressDialog.cancel();

            } finally {

                if (getIntent() != null) {

                    int pos = getIntent().getIntExtra("pos", 0);

                    String data = getIntent().getStringExtra("data");
                    Log.i("position", pos + "");
                    position = Integer.valueOf(pos);
                    searchString = data;
                    int textlength = 0;
                    //searchString = arStoreList.get(position).getStoreindustrytypeName();

//                    Log.i("search",searchString);
                    if (searchString != null) {
                        textlength = searchString.length();
                    }
                    ArrayList<StoreListModel> tempArrayList = new ArrayList<StoreListModel>();
                    for (StoreListModel c : arStoreList) {
                        if (textlength <= c.getStoreindustrytypeName().length()) {
                            if (searchString != null) {
                                if (c.getStoreindustrytypeName().toLowerCase().equalsIgnoreCase(searchString.toString().toLowerCase())) {
                                    tempArrayList.add(c);
                                }
                            }
                        }
                    }
                    edSearchCategories1.setText(data);
                    AddBuisnessAdapter addBuisnessAdapter = new AddBuisnessAdapter(getApplicationContext(), R.layout.search_buisness, tempArrayList);

                    listStore.setAdapter(addBuisnessAdapter);
/*
                    if(tempArrayList.size()<=0)
                    {
                        ((TextView)findViewById(R.id.txt_nodata)).setText("No Data Found");
                        ((RelativeLayout)findViewById(R.id.rl_nodata)).setVisibility(View.VISIBLE);

                    }

                    else
                    {
                        ((TextView)findViewById(R.id.txt_nodata)).setVisibility(View.GONE);
                        ((RelativeLayout)findViewById(R.id.rl_nodata)).setVisibility(View.GONE);

                    }

                    if(tempArrayList.size()<=0)
                    {
                        ((TextView)findViewById(R.id.txt_nodata)).setText("No Data Found");
                        ((RelativeLayout)findViewById(R.id.rl_nodata)).setVisibility(View.VISIBLE);

                    }

                    else
                    {
                        ((TextView)findViewById(R.id.txt_nodata)).setVisibility(View.GONE);
                        ((RelativeLayout)findViewById(R.id.rl_nodata)).setVisibility(View.GONE);

                    }
*/


                }

                if (progressDialog != null)
                    progressDialog.cancel();
            }
        }
    }


    private class AddStoreTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
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
            try {
                if (result != null) {
                    jObject = new JSONObject(result);
                    JSONObject job = jObject.getJSONObject("commandResult");
                    int success = job.getInt("success");
                    String message = job.getString("message");
                    if (success == 1) {
                        JSONObject job1 = job.getJSONObject("data");
                        JSONArray ja = job1.getJSONArray("stores");
                        String imageUrl = "";

                        AppUtil.setrefreshValue(getApplicationContext(), "yes");

                        AppUtil.onKeyBoardDown(getApplicationContext());
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        AppUtil.showCustomToast(getApplicationContext(), message);
                    }

                } else {
                    AppUtil.showCustomToast(getApplicationContext(),
                            "please check your internet connection");
                }
                if (dialog2 != null)
                    dialog2.cancel();
            } catch (Exception e) {
                e.printStackTrace();
                if (dialog2 != null)
                    dialog2.cancel();

            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, Generalstore_list_new.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Exit me", true);
        startActivity(intent);


    }
}
