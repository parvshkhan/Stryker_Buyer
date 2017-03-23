package com.app.stryker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.Database.SQLiteHelper;
import com.app.Dialog.UpdateDialog;
import com.app.SimpleDividerItemDecoration;
import com.app.adapters.ImageViewPagerAdapterGeneralStore;
import com.app.adapters.MyRecyclerViewAdapter;
import com.app.jsoncall.JsonCall;
import com.app.model.StoreListModel;
import com.app.utill.AppUtil;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Clear;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Generalstore_list_new extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, SwipeRefreshLayout.OnRefreshListener, ViewPager.OnPageChangeListener {


    private TextView tvSearch, lblmymarket;
    private Spinner spinnerCity;
    private ArrayList<String> cityList = new ArrayList<String>();
    private ArrayAdapter<String> cityAdapter;
    private ProgressDialog dialog;
    ArrayList<StoreListModel> categoryList;
    GridView gcategoryListing;
    RecyclerView recyclerView;
    ImageView imgViewCategory;


    String result = null;
    TextView txtOutputLat, txtOutputLon;
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    double lat, lon;
    private ProgressDialog progressDialog;
    Geocoder geocoder;

    MyRecyclerViewAdapter adapter = null;
    NavigationView navigationView;
    ImageView imageView;
    ImageView imageHamburger;
    DrawerLayout drawerLayout;
    FloatingActionButton btStore;
    FrameLayout frameLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    private boolean check = false;
    private LinearLayout pager_indicator;
    ViewPager viewPager;

    private int dotsCount;
    private ImageView[] dots;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generalstore_list_new);
        if(AppUtil.getFlagUpdate(Generalstore_list_new.this))
        {
            checkAppVersion();
        }


        //setUiPageViewController();


        frameLayout = (FrameLayout) findViewById(R.id.frm);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        btStore = (FloatingActionButton) findViewById(R.id.store);
        imageView = (ImageView) findViewById(R.id.arr_down);
        imageHamburger = (ImageView) findViewById(R.id.image_hamburger);
        lblmymarket = (TextView) findViewById(R.id.lblmymarket);
        lblmymarket.setText(Html.fromHtml("<center><strong>&nbsp;&nbsp;&nbsp;&nbsp;My <br/> Market</strong></center>"));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvSearch.performClick();
            }
        });
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvSearch.performClick();
            }
        });
        btStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Generalstore_list_new.this, HomeActivity.class));
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.nav);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setItemIconTintList(null);

        imageHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout != null && !drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.openDrawer(Gravity.LEFT);

                } else
                    drawerLayout.closeDrawer(Gravity.LEFT);


            }
        });

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);

                        //Closing drawer on item click
                        drawerLayout.closeDrawers();


                        switch (menuItem.getItemId()) {


                            //Replacing the main content with ContentFragment Which is our Inbox View;
                            case R.id.busiregistration:

                                Intent in = new Intent(getApplicationContext(), ActivityBusinessAccount.class);
                                startActivity(in);
                                return true;

                            case R.id.inviteandearn:
                                startActivity(new Intent(Generalstore_list_new.this, InviteandEarn.class));

                                return true;
                            case R.id.shareapp:

                                try {
                                    Intent i = new Intent(Intent.ACTION_SEND);
                                    i.setType("text/plain");
                                    i.putExtra(Intent.EXTRA_SUBJECT, "Market App");
                                    String sAux = "\nLet me recommend you this application\n\n";
                                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.app.stryker \n\n";
                                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                                    startActivity(Intent.createChooser(i, "choose one"));
                                } catch (Exception e) {
                                    //e.toString();
                                }
                                return true;

                            //Replacing the main content with ContentFragment Which is our Inbox View;
                            case R.id.rateus:

                                rateApp();
                                return true;

                            case R.id.feedback:
                                Intent Email = new Intent(Intent.ACTION_SEND);
                                Email.setType("text/email");
                                Email.putExtra(Intent.EXTRA_EMAIL, new String[]{"hello@marketapp.online"});
                                Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                                startActivity(Intent.createChooser(Email, "Send Feedback:"));
                                return true;
                            case R.id.location:
                                startActivity(new Intent(getApplicationContext(), SelectCity.class));


                                return true;

                            case R.id.signout:
                                Intent i = new Intent(getApplicationContext(),
                                        SettingActivity.class);
                                startActivity(i);
                                return true;
                        }

                        return true;
                    }
                });

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        tvSearch = (TextView) findViewById(R.id.selectcity);
        imgViewCategory = (ImageView) findViewById(R.id.selectCategory);
        //  spinnerCity = (Spinner)findViewById(R.id.spinner_city_new);
        recyclerView.setHasFixedSize(true);
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        buildGoogleApiClient();
        // adapter = new MyRecyclerViewAdapter(getApplicationContext(), categoryList);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.BLACK, Color.BLACK, Color.BLACK);

        imgViewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Generalstore_list_new.this, SearchActivityNew.class));

            }
        });


        //    final   RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);

        GridLayoutManager glm = new GridLayoutManager(getApplicationContext(), 3);

        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position == 0) ? 3 : 1;
            }
        });


        recyclerView.setLayoutManager(glm);

        SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(Generalstore_list_new.this);
        categoryList = sqLiteHelper.getCategoryListing();

        if (categoryList.isEmpty()) {
            check = true;
            GetCategoryTask getCategoryTask = new GetCategoryTask();
            getCategoryTask.execute(new String[]{});
        } else {
            adapter = new MyRecyclerViewAdapter(getApplicationContext(), categoryList);
            recyclerView.setAdapter(adapter);

        }

        //setData();
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Generalstore_list_new.this, SelectCity.class);
                startActivity(intent);
                finish();
            }
        });

    }
/*
    private void setUiPageViewController() {

        dotsCount = imageViewPagerAdapterGeneralStore.getCount();
        dots = new ImageView[dotsCount];


        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.back));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.add1));

    }*/

    private void checkAppVersion() {

        final   int code = Integer.parseInt(AppUtil.getVersionCode(Generalstore_list_new.this));

        Firebase.setAndroidContext(this);
        Firebase ref = new Firebase("https://strkerbuyer.firebaseio.com/");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String liveCode = String.valueOf(dataSnapshot.child("version").getValue());
                int liveCodeInt= Integer.parseInt(liveCode);
                if(liveCodeInt>code)
                {
                    UpdateDialog updateDialog = new UpdateDialog(Generalstore_list_new.this);
                    updateDialog.show();

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(500000); // Update location every second

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lon = mLastLocation.getLongitude();

        }
        updateUI();
    }

    private void updateUI() {

        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addressList != null && addressList.size() > 0) {
            Address address = addressList.get(0);

            if (getIntent().hasExtra("city")) {
                tvSearch.setText(getIntent().getStringExtra("city"));
            } else {
                tvSearch.setText(address.getLocality());
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
        updateUI();
    }

    @Override
    public void onRefresh() {
        check = false;
        swipeRefreshLayout.setRefreshing(true);
        Clear.clearCache(Picasso.with(Generalstore_list_new.this));
        refreshList();
    }

    private void refreshList() {

        GetCategoryTask getCategoryTask = new GetCategoryTask();
        getCategoryTask.execute(new String[]{});
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Toast.makeText(this, "position"+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * add store asyntask
     */


    private class GetCategoryTask extends AsyncTask<String, Void, String> {


        protected void onPreExecute() {
            super.onPreExecute();
            SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(Generalstore_list_new.this);
            sqLiteHelper.deleteCategory();
            progressDialog = new ProgressDialog(Generalstore_list_new.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            if (check)
                progressDialog.show();


        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.getMasterData(getApplicationContext()
                        .getResources().getString(R.string.master_category_url));
                Log.i("add Store RRESPONSE", "" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            try {
                if (result != null) {
                    categoryList = new ArrayList<StoreListModel>();
                    SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(Generalstore_list_new.this);
                    Log.i("category", result);
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject rootobj = jsonObject.getJSONObject("commandResult");
                    JSONObject object = rootobj.getJSONObject("data");
                    JSONArray jsonArray = object.getJSONArray("categories");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        StoreListModel storeListModel = new StoreListModel();
                        storeListModel.setStoreName(jsonArray.getJSONObject(i).getString("Name"));
                        storeListModel.setStoreImage(jsonArray.getJSONObject(i).getString("ImageUrl"));
                        sqLiteHelper.insertCategory(storeListModel);
                        categoryList.add(storeListModel);
                    }



                    adapter = new MyRecyclerViewAdapter(getApplicationContext(), categoryList);

                    recyclerView.setAdapter(adapter);
                    progressDialog.dismiss();
                    swipeRefreshLayout.setRefreshing(false);


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

    private void initViews() {


    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click

                        if (position == 0)
                            return;

                        Intent intent = new Intent(Generalstore_list_new.this, SearchActivityNew.class);
                        intent.putExtra("data", categoryList.get(position).getStoreName());
                        intent.putExtra("pos", position);
                        startActivity(intent);


                        Log.i("item", position + "");
                    }
                })
        );

    }
}
