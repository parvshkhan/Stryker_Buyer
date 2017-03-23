package com.app.stryker;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.adapters.CityListAdapter;
import com.app.jsoncall.JsonCall;
import com.app.model.City;
import com.app.model.ServiceResponse;
import com.app.utill.AppUtil;
import com.app.utill.SessionUtils;

import java.util.ArrayList;

public class SelectCity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ListView listView;
    ProgressDialog dialog;
    private ArrayList<String> cityList = new ArrayList<String>();
    CityListAdapter cityAdapter;
    EditText selectCity;
    SwipeRefreshLayout swipeRefreshLayout;

    private ImageView imageView;
    boolean check = true;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.black));
        }

        setContentView(R.layout.activity_select_city);
        listView = (ListView) findViewById(R.id.listCity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        imageView = (ImageView) findViewById(R.id.back);
        swipeRefreshLayout.setOnRefreshListener(this);
        listView.setDivider(null);
        setData();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView textView = (TextView) view.findViewById(R.id.ctyname);
                Intent intent = new Intent(SelectCity.this, Generalstore_list_new.class);
                intent.putExtra("city", textView.getText().toString());
                startActivity(intent);
                finish();


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.selectcity_menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search1).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                int textlength = newText.length();
                ArrayList<String> tempArrayList = new ArrayList<String>();
                for (String c : cityList) {
                    if (textlength <= c.length()) {
                        if (c.toLowerCase().contains(newText.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }

                cityAdapter = new CityListAdapter(getApplicationContext(), R.layout.city_row, tempArrayList);

                listView.setAdapter(cityAdapter);


                return true;
            }
        });

        return true;
    }

    private void setData() {
        ServiceResponse cityServiceResponse = AppUtil.getGsonInstance().fromJson(SessionUtils.getCitiesForSearch(SelectCity.this, SessionUtils.SESSION_CITY), ServiceResponse.class);

        if (cityServiceResponse != null && cityServiceResponse.commandResult != null) {
            cityList.clear();
            if (cityServiceResponse.commandResult.success == 1) {
                for (City city : cityServiceResponse.commandResult.data.cities) {
                    cityList.add(city.Name);
                }
                cityAdapter = new CityListAdapter(getApplicationContext(), R.layout.city_row, cityList);
                cityAdapter.setDropDownViewResource(R.layout.row_city);
                listView.setAdapter(cityAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        } else {
            GetCityTask getCityTask = new GetCityTask();
            getCityTask.execute(new String[]{});
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        refresh();
    }

    private void refresh() {

        check = false;
        GetCityTask getCityTask = new GetCityTask();
        getCityTask.execute(new String[]{});

    }


    /**
     * add store asyntask
     */
    private class GetCityTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(SelectCity.this);
            dialog.setMessage("loading");
            if (check)
                dialog.show();
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.getMasterData(getApplicationContext()
                        .getResources().getString(R.string.master_city_url));
                Log.e("City Listing", "" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            try {
                if (result != null) {
                    ServiceResponse serviceResponse = AppUtil.getGsonInstance().fromJson(result, ServiceResponse.class);
                    SessionUtils.setCitiesForSearch(getApplicationContext()
                            , result, SessionUtils.SESSION_CITY);
                    cityList.clear();

                    if (serviceResponse != null && serviceResponse.commandResult != null) {
                        if (serviceResponse.commandResult.success == 1) {

                            for (City city : serviceResponse.commandResult.data.cities) {
                                cityList.add(city.Name);
                            }


                            cityAdapter = new CityListAdapter(getApplicationContext(), R.layout.city_row, cityList);
                            cityAdapter.setDropDownViewResource(R.layout.row_city);
                            listView.setAdapter(cityAdapter);
                            swipeRefreshLayout.setRefreshing(false);
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

        }
    }


    public void back(View view) {
        finish();

    }


}
