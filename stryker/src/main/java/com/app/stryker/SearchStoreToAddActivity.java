package com.app.stryker;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.adapters.AddStoreListAdapter;
import com.app.gpstracker.GPSTracker;
import com.app.jsoncall.JsonCall;
import com.app.model.Category;
import com.app.model.City;
import com.app.model.ServiceResponse;
import com.app.model.StoreListModel;
import com.app.utill.AppUtil;
import com.app.utill.SessionUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
//import com.google.gson.JsonObject;

public class SearchStoreToAddActivity extends Activity {

    Context context;
    ProgressDialog dialog;
    ListView listStore;
    EditText editSearch;
    TextView btn_invite;
    ArrayList<StoreListModel> arStoreList = new ArrayList<StoreListModel>();
    ArrayList<StoreListModel> arSearchList = new ArrayList<StoreListModel>();
    ArrayList<String> cityList = new ArrayList<String>();
    ArrayList<String> categoryList = new ArrayList<String>();
    AddStoreListAdapter adapter;
    TextView txt_city, txt_category, txt_my_market;
    Spinner spinner_city, spinner_category;
    ImageView imgSetting, img_notif;
    LinearLayout ll_setting;
    TextView txt_Settings;
    ArrayAdapter<String> categoryAdapter;
    ArrayAdapter<String> cityAdapter;

    Double mLat, mLong;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generalstore_list);
        context = this;

        init();
        setListener();
        setData();
    }

    public void init() {
        Typeface tfbold = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Bold.ttf");
        Typeface tfRegular = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Regular.ttf");
        Typeface tfThin = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Thin.ttf");

        editSearch = (EditText) findViewById(R.id.generalstore_searchoption_text);
        listStore = (ListView) findViewById(R.id.generalstore_listview);

        btn_invite = (TextView) findViewById(R.id.btn_invite);

        txt_city = (TextView) findViewById(R.id.txt_city);
        spinner_city = (Spinner) findViewById(R.id.spinner_city);
        txt_category = (TextView) findViewById(R.id.txt_category);
        spinner_category = (Spinner) findViewById(R.id.spinner_category);
        txt_my_market = (TextView) findViewById(R.id.txt_my_market);


        imgSetting = (ImageView) findViewById(R.id.img_settings);
        ll_setting = (LinearLayout) findViewById(R.id.ll_setting);
        txt_Settings = (TextView) findViewById(R.id.txt_setting);
        img_notif = (ImageView) findViewById(R.id.img_notif);

        editSearch.setTypeface(tfRegular);
        listStore.setVisibility(View.VISIBLE);
        adapter = new AddStoreListAdapter(context,
                R.layout.row_add_store_list, arSearchList);
        listStore.setAdapter(adapter);

        cityList = new ArrayList<>();
        categoryList = new ArrayList<>();


        GPSTracker tracker = new GPSTracker(context);

        mLat = tracker.getLatitude();
        mLong = tracker.getLongitude();



	/*	categoryList = new ArrayList<>();
        categoryAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line,categoryList);
		categoryAdapter.setDropDownViewResource(R.layout.row_city);
		spinner_category.setAdapter(categoryAdapter);
		cityList = new ArrayList<>();
		cityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,cityList);
		cityAdapter.setDropDownViewResource(R.layout.row_city);
		spinner_city.setAdapter(cityAdapter);*/
    }

    public void setData() {
        ServiceResponse categoryServiceResponse = AppUtil.getGsonInstance().fromJson(SessionUtils.getCitiesForSearch(context,
                SessionUtils.SESSION_CATEGORY), ServiceResponse.class);
        if (categoryServiceResponse != null && categoryServiceResponse.commandResult != null) {
            categoryList.clear();
            if (categoryServiceResponse.commandResult.success == 1) {
                categoryList.add("Select Category");
                for (Category category : categoryServiceResponse.commandResult.data.categories) {
                    categoryList.add(category.Name);
                }
                categoryAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, categoryList);
                categoryAdapter.setDropDownViewResource(R.layout.row_city);
                spinner_category.setAdapter(categoryAdapter);

                //categoryAdapter.notifyDataSetChanged();
            }
        } else {
            GetCategoryTask getCategoryTask = new GetCategoryTask();
            getCategoryTask.execute(new String[]{});
        }


        ServiceResponse cityServiceResponse = AppUtil.getGsonInstance().fromJson(SessionUtils.getCitiesForSearch(context, SessionUtils.SESSION_CITY), ServiceResponse.class);
        if (cityServiceResponse != null && cityServiceResponse.commandResult != null) {
            cityList.clear();
            if (cityServiceResponse.commandResult.success == 1) {
			/*	if(SplashActivity.city!=null)
				cityList.add(SplashActivity.city);

				else
				cityList.add("select city");

*/
                cityList.add("Selet City");
                for (City city : cityServiceResponse.commandResult.data.cities) {
                    cityList.add(city.Name);
                }
//				cityAdapter.notifyDataSetChanged();
                cityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, cityList);
                cityAdapter.setDropDownViewResource(R.layout.row_city);
                spinner_city.setAdapter(cityAdapter);
            }
        } else {
            GetCityTask getCityTask = new GetCityTask();
            getCityTask.execute(new String[]{});
        }

        if (AppUtil.isNetworkAvailable(context)) {
            dialog = ProgressDialog.show(context, "", "please wait..");
            StoreListTask task = new StoreListTask();
            task.execute(new String[]{});

        } else {
            AppUtil.showCustomToast(context,
                    "Please check your internet connection");
        }
    }

    public void setListener() {


        img_notif.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent in = new Intent(SearchStoreToAddActivity.this, NotificationActivity.class);
                startActivity(in);

            }
        });


        imgSetting.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                ll_setting.setVisibility(View.VISIBLE);
            }
        });


        txt_Settings.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(SearchStoreToAddActivity.this, SettingActivity.class);
                ll_setting.setVisibility(View.GONE);
                startActivity(i);
            }
        });


        ll_setting.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                ll_setting.setVisibility(View.GONE);
            }
        });


        ((TextView) findViewById(R.id.txt_share)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                String appUrl = "https://play.google.com/store/apps/details?id=com.app.stryker&hl=en";
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Get the Market App on Google play store\n" + appUrl);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });

        ((ImageView) findViewById(R.id.img_search)).setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                AppUtil.onKeyBoardDown(context);
                String searchString = editSearch.getText().toString();
                int textLength = searchString.length();
                if (textLength < 4) {
                    listStore.setVisibility(View.INVISIBLE);
                    AppUtil.showCustomToast3(context, "No result found");
                } else if (arSearchList.size() == 0) {
                    listStore.setVisibility(View.INVISIBLE);
                    AppUtil.showCustomToast3(context, "No result found");
                } else {
                    listStore.setVisibility(View.VISIBLE);
                }
            }
        });

        ((ImageView) findViewById(R.id.generalstore_backimage)).setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                AppUtil.onKeyBoardDown(context);
                finish();
            }
        });

        listStore.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                AppUtil.onKeyBoardDown(context);
                if (AppUtil.isNetworkAvailable(context)) {
                    dialog = ProgressDialog.show(context, "", "please wait..");
                    AddStoreTask task = new AddStoreTask();
                    task.execute(new String[]{AppUtil.getUserId(context),
                            arSearchList.get(position).getStoreId(),
                            AppUtil.getDeviceId(context)});

                } else {
                    AppUtil.showCustomToast(context, "please check your internet");
                }
            }
        });

        /**
         * Enabling Search Filter
         * */
        editSearch.addTextChangedListener(new TextWatcher() {


            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                // When user changed the Text
                // RegionListHomeActivity.this.adapter.getFilter().filter(cs);
                String searchString = editSearch.getText().toString();
                int textLength = searchString.length();
                Log.e("1------------------>", txt_city.getText().toString());
                Log.e("2------------------>", txt_category.getText().toString());

                if (textLength > 3) {
                    // clear the initial data set
                    arSearchList.clear();
                    listStore.setVisibility(View.VISIBLE);
                    for (int i = 0; i < arStoreList.size(); i++) {
                        String storeName = arStoreList.get(i).getStoreName()
                                .toString();
                        String storeCode = arStoreList.get(i).getStoreCode()
                                .toString();

                        String storeindustryname = arStoreList.get(i).getStoreindustrytypeName()
                                .toString();


                        if (textLength <= storeName.length()
                                || textLength <= storeCode.length()) {
                            // compare the String in EditText with Names in the
                            // ArrayList
                            try {

                                if (txt_category.getText().toString() != "Select Category") {
                                    if ((searchString.equalsIgnoreCase(storeName
                                            .substring(0, textLength))
                                            || searchString
                                            .equalsIgnoreCase(storeCode
                                                    .substring(0,
                                                            textLength))) && txt_category.getText().toString().equalsIgnoreCase(storeindustryname))

                                        arSearchList.add(arStoreList.get(i));

                                } else {

                                    if (searchString.equalsIgnoreCase(storeName
                                            .substring(0, textLength))
                                            || searchString
                                            .equalsIgnoreCase(storeCode
                                                    .substring(0,
                                                            textLength)))

                                        arSearchList.add(arStoreList.get(i));

                                }


                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                    }
                    if (textLength == 0) {
                        arSearchList.clear();
                    }
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();

                    }
                }

            }


            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }


            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        btn_invite.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ShowInviteDialog();

            }
        });


        txt_city.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                spinner_city.performClick();
            }
        });

        txt_category.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                spinner_category.performClick();
            }
        });

        spinner_city.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                txt_city.setText(cityList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_category.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                txt_category.setText(categoryList.get(position));
                // asdasd
                arSearchList.clear();
                for (int i = 0; i < arStoreList.size(); i++) {
                    String industryname = arStoreList.get(i).getStoreindustrytypeName()
                            .toString();

                    if (industryname.equalsIgnoreCase(categoryList.get(position)))

                        arSearchList.add(arStoreList.get(i));


                }

                if (adapter != null) {
                    adapter.notifyDataSetChanged();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txt_my_market.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();

            }
        });

    }


    public void ShowInviteDialog() {

        final Dialog invitedialog = new Dialog(context);

        invitedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        invitedialog.setContentView(R.layout.invite_seller_layout);

        final EditText edt_name = (EditText) invitedialog.findViewById(R.id.edt_name);
        final EditText edt_phone_number = (EditText) invitedialog.findViewById(R.id.edt_phone_number);
        final EditText edt_city = (EditText) invitedialog.findViewById(R.id.edt_city);
        TextView txt_submit = (TextView) invitedialog.findViewById(R.id.txt_submit);
        ImageView img_delete = (ImageView) invitedialog.findViewById(R.id.img_delete);

        txt_submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (edt_name.getText().toString().equalsIgnoreCase("")) {
                    AppUtil.showCustomToast(context, "Please enter business name");

                } else if (edt_phone_number.getText().toString().equalsIgnoreCase("")) {
                    AppUtil.showCustomToast(context, "Please enter phone number");
                } else if (edt_city.getText().toString().equalsIgnoreCase("")) {
                    AppUtil.showCustomToast(context, "Please enter city name");
                } else {

                    if (AppUtil.isNetworkAvailable(context)) {
                        dialog = ProgressDialog.show(context, "", "Please wait...");
                        InviteStoreTask task = new InviteStoreTask();
                        AppUtil.onKeyBoardDown(context);
                        task.execute(new String[]{AppUtil.getUserId(context), edt_name.getText().toString(), edt_phone_number.getText().toString(), edt_city.getText().toString()});
                        invitedialog.dismiss();
                    } else {
                        AppUtil.showCustomToast(context, "Please check your internet connection");
                    }

                }

            }
        });

        img_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                invitedialog.dismiss();
            }
        });


        invitedialog.show();

    }

    /**
     * store list asyntask
     */
    private class StoreListTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        // email, password, phone, login_type, socialId, latitude, longitude,
        // name, age, gender, location, deviceToken
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.storeList(context.getResources().getString(
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
							/*arSearchList.add(new StoreListModel(jo
									.getString("StoreId"), imageUrl, jo
									.getString("Name"), jo
									.getString("StoreCode"), jo
									.getString("Address"), jo
									.getString("Description")));*/

                        }

						/*adapter = new AddStoreListAdapter(context,
								R.layout.row_add_store_list, arSearchList);
						listStore.setAdapter(adapter);*/

//						new GetCategoryTask().execute();
                    } else {
                        AppUtil.showCustomToast(context, message);
                        if (dialog != null)
                            dialog.cancel();
                    }

                } else {
                    AppUtil.showCustomToast(context,
                            "please check your internet connection");
                    if (dialog != null)
                        dialog.cancel();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (dialog != null)
                    dialog.cancel();

            } finally {
                if (dialog != null)
                    dialog.cancel();
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
                response = obj.addStore(urls[0], urls[1], urls[2], context
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

                        AppUtil.setrefreshValue(context, "yes");

						/*for (int i = 0; i < ja.length(); i++) {
							JSONObject jo = ja.getJSONObject(i);
							JSONObject jo1 = jo.getJSONObject("Store");
							String storeId = jo1.getString("Id");
							String storeName = jo1.getString("Name");
							String storeDescription = jo1.getString("Description");
							String storeCode = jo1.getString("StoreCode");
							String sddress = jo1.getString("Address");
							JSONArray jar = jo.getJSONArray("StoreGallery");
							if (jar.length() > 0) {
								JSONObject j = jar.getJSONObject(0);
								imageUrl = j.getString("ImageUrl");
							}

						}
*/						/*Intent i = new Intent();
						i.putExtra("data", ja.toString());
						setResult(512, i);*/
                        AppUtil.onKeyBoardDown(context);
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


    /**
     * invite store asyntask
     */
    private class InviteStoreTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.inviteStore(urls[0], urls[1], urls[2], urls[3], getString(R.string.sendInvite_url));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            JSONObject jObject;

            Log.e("Response>>>>>>>", result);
            try {
                if (result != null) {

                    JSONObject jsonObject = new JSONObject(result);

                    JSONObject commandResult = jsonObject.getJSONObject("commandResult");
                    if (commandResult.getString("success").equalsIgnoreCase("1")) {
                        AppUtil.showCustomToast(context, "Invitation successfully done");
                    } else {
                        AppUtil.showCustomToast(context, "Please try later");
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
     * add store asyntask
     */
    private class GetCategoryTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.getMasterData(context
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
                    SessionUtils.setCitiesForSearch(context, result, SessionUtils.SESSION_CATEGORY);
                    categoryList.clear();
                    if (serviceResponse != null && serviceResponse.commandResult != null) {
                        if (serviceResponse.commandResult.success == 1) {
                            categoryList.add("Select Category");
                            for (Category category : serviceResponse.commandResult.data.categories) {
                                categoryList.add(category.Name);
                            }
                            categoryAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, categoryList);
                            categoryAdapter.setDropDownViewResource(R.layout.row_city);
                            spinner_category.setAdapter(categoryAdapter);
                            //categoryAdapter.notifyDataSetChanged();
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (dialog != null)
                    dialog.dismiss();
            }

//			JSONObject jObject;
//			try {
//				if (result != null) {
//					jObject = new JSONObject(result);
//					JSONObject job = jObject.getJSONObject("commandResult");
//					int success = job.getInt("success");
//					String message = job.getString("message");
//					if (success == 1) {
//						JSONObject job1 = job.getJSONObject("data");
//						JSONArray ja = job1.getJSONArray("categories");
//
//						if (ja.length()>0) {
//
//							categoryList.add("Select Category");
//
//							for (int i = 0; i < ja.length(); i++) {
//
//								JSONObject jo = ja.getJSONObject(i);
//
//								categoryList.add(jo.getString("Name"));
//
//
//							}
//
//							ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line,categoryList);
//							categoryAdapter.setDropDownViewResource(R.layout.row_city);
//							spinner_category.setAdapter(categoryAdapter);
//							ServiceResponse serviceResponse = AppUtil.getGsonInstance().fromJson(SessionUtils.getCitiesForSearch(context, SessionUtils.SESSION_CITY), ServiceResponse.class);
//							if(serviceResponse != null && serviceResponse.commandResult != null && serviceResponse.commandResult.data.cities.size() > 0){
//						// Arraylist clear
//								// ArrayList set
//								// Notify DataSet change
//
//		                           cityList.clear();
//								cityList.add("Select City");
//
//								for (City city :serviceResponse.commandResult.data.cities) {
//									cityList.add(city.Name);
//								}
//
//								ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,cityList);
//								cityAdapter.setDropDownViewResource(R.layout.row_city);
//								spinner_city.setAdapter(cityAdapter);
//
//
//								if (dialog != null)
//									dialog.cancel();
//							}
//							else {
//								new GetCityTask().execute();
//
//							}
//
//
//						}
//					} else {
//						AppUtil.showCustomToast(context, message);
//						if (dialog != null)
//							dialog.cancel();
//					}
//
//				} else {
//					AppUtil.showCustomToast(context,
//							"please check your internet connection");
//					if (dialog != null)
//						dialog.cancel();
//				}
//
//				} catch (Exception e) {
//				e.printStackTrace();
//				if (dialog != null)
//					dialog.cancel();
//
//			}
        }
    }


    /**
     * add store asyntask
     */
    private class GetCityTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.getMasterData(context
                        .getResources().getString(R.string.master_city_url));
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
                    SessionUtils.setCitiesForSearch(context, result, SessionUtils.SESSION_CITY);
                    cityList.clear();

                    if (serviceResponse != null && serviceResponse.commandResult != null) {
                        if (serviceResponse.commandResult.success == 1) {

                            cityList.add("Select City");
                            for (City city : serviceResponse.commandResult.data.cities) {
                                cityList.add(city.Name);
                            }
                            cityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, cityList);
                            cityAdapter.setDropDownViewResource(R.layout.row_city);
                            spinner_city.setAdapter(cityAdapter);
                            //cityAdapter.notifyDataSetChanged();
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (dialog != null)
                    dialog.dismiss();
            }
//			JSONObject jObject;
//			try {
//				if (result != null) {
//					// Convert to Service Response for further Parsing
//					ServiceResponse serviceResponse = AppUtil.getGsonInstance().fromJson(result, ServiceResponse.class);
//					// Save to Session
//					SessionUtils.setCitiesForSearch(context, result, SessionUtils.SESSION_CITY);
//					jObject = new JSONObject(result);
//					JSONObject job = jObject.getJSONObject("commandResult");
//					int success = job.getInt("success");
//					String message = job.getString("message");
//					if (success == 1) {
//						JSONObject job1 = job.getJSONObject("data");
//						JSONArray ja = job1.getJSONArray("cities");
//
//						if (ja.length()>0) {
//
//							cityList.add("Select City");
//							for (int i = 0; i < ja.length(); i++) {
//
//								JSONObject jo = ja.getJSONObject(i);
//
//								cityList.add(jo.getString("Name"));
//
//
//							}
//
//							ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,cityList);
//							cityAdapter.setDropDownViewResource(R.layout.row_city);
//							spinner_city.setAdapter(cityAdapter);
//						}
//					} else {
//						AppUtil.showCustomToast(context, message);
//					}
//
//				} else {
//					AppUtil.showCustomToast(context,
//							"please check your internet connection");
//				}
//				if (dialog != null)
//					dialog.cancel();
//			} catch (Exception e) {
//				e.printStackTrace();
//				if (dialog != null)
//					dialog.cancel();
//
//			}
        }
    }


}
