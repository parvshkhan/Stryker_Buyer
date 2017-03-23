package com.app.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapters.GalleryAdapter;
import com.app.adapters.StoreHomeListAdapter;
import com.app.gpstracker.GPSTracker;
import com.app.jsoncall.JsonCall;
import com.app.stryker.ProductListActivity;
import com.app.stryker.R;
import com.app.utill.AppUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class StoreFrontFragment extends android.support.v4.app.Fragment {

    Context context;

    StoreHomeListAdapter adapter;
    private Double mLat = 0.0, mLong = 0.0;
    ProgressDialog dialog;

    ProductListActivity activity;
    ImageView imageStore, imgPrev, imgNext;
    ArrayList<String> argallery = new ArrayList<String>();
    int posiGallery = 0;
    RelativeLayout rl_broucher;
    String BroucherUrl = "";
    String data = "", storeId = "";
    ProductListActivity activity1;
    RelativeLayout rlPopupNumber, rlAddress;
    TextView txtPhoneNum1, txtPhoneNum2, txtstoreName, txtAddress;
    String minOrderValue = "";

    ProgressBar progressbar;

    ViewPager mViewPager;
    GalleryAdapter galleryAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootview = inflater.inflate(R.layout.fragment_store_front,
                container, false);

        return rootview;
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        activity1 = (ProductListActivity) context;
        GPSTracker mGPS = new GPSTracker(context);
        if (mGPS.canGetLocation) {
            mLat = mGPS.getLatitude();
            mLong = mGPS.getLongitude();
        }
        activity = (ProductListActivity) getActivity();
        storeId = activity.StoreId();
        init();


        galleryAdapter = new GalleryAdapter(context, argallery);
        mViewPager = (ViewPager) getView().findViewById(R.id.pager_gallery);
        mViewPager.setAdapter(galleryAdapter);

        setListener();
        //setdata(data);
        if (AppUtil.isNetworkAvailable(context)) {
            //dialog = ProgressDialog.show(context, "", "Please wait...");
            progressbar.setVisibility(View.VISIBLE);
            ProductListTask task = new ProductListTask();
            task.execute(new String[]{storeId});
        } else {
            AppUtil.showCustomToast(context,
                    "please check your internet connection");

        }

    }


    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        //setdata();

    }


    private void init() {
        //TextView txtAddress = (TextView) getView().findViewById(R.id.txt_address);
        imageStore = (ImageView) getView().findViewById(R.id.img_store);
        imgPrev = (ImageView) getView().findViewById(R.id.img_left_arr);
        imgNext = (ImageView) getView().findViewById(R.id.img_right_arr);
        rl_broucher = (RelativeLayout) getView().findViewById(R.id.rl_broucher);

        rlPopupNumber = (RelativeLayout) getView().findViewById(R.id.rl_popup_number);
        rlAddress = (RelativeLayout) getView().findViewById(R.id.rl_add);


        txtstoreName = (TextView) getView().findViewById(R.id.txt_p_name);
        txtAddress = (TextView) getView().findViewById(R.id.txt_p_address);
        txtPhoneNum1 = (TextView) getView().findViewById(R.id.txt_p_mobile_num1);
        txtPhoneNum2 = (TextView) getView().findViewById(R.id.txt_p_mobile_num2);

        progressbar = (ProgressBar) getView().findViewById(R.id.progressBar);
    }

    public void setListener() {
        rlPopupNumber.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                rlPopupNumber.setVisibility(View.GONE);
            }
        });
        rlAddress.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                rlPopupNumber.setVisibility(View.VISIBLE);
            }
        });
        txtPhoneNum1.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                String number = txtPhoneNum1.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });
        txtPhoneNum2.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                String number = txtPhoneNum1.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });
        imgNext.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (posiGallery < argallery.size() - 1) {
                    posiGallery++;
                    mViewPager.setCurrentItem(posiGallery);

                    if(mViewPager.getCurrentItem()==argallery.size() - 1)
                    {
                        imgNext.setVisibility(View.INVISIBLE);
                        imgPrev.setVisibility(View.VISIBLE);
                    }

                }



			/*	if(posiGallery < argallery.size()-1){
				try {
					posiGallery++;
					Picasso.with(context).load(argallery.get(posiGallery))
					.placeholder(R.drawable.oplaceholder)
					//.transform(new CircleTransform())
					.skipMemoryCache()
					.fit()
					.into(imageStore);

					if(posiGallery == argallery.size()-1){
						imgNext.setVisibility(View.INVISIBLE);
						imgPrev.setVisibility(View.VISIBLE);
					}else if(posiGallery > 0){
						imgPrev.setVisibility(View.VISIBLE);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}*/
            }
        });

        imgPrev.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (posiGallery > 0) {
                    posiGallery--;
                    mViewPager.setCurrentItem(posiGallery);


                    if(mViewPager.getCurrentItem()==0)
                    {
                        imgNext.setVisibility(View.VISIBLE);
                        imgPrev.setVisibility(View.INVISIBLE);
                    }
                }
				
		/*		if(posiGallery > 0 ){					
					try {
						posiGallery--;
						Picasso.with(context).load(argallery.get(posiGallery))
						.placeholder(R.drawable.oplaceholder)
						//.transform(new CircleTransform())
						.fit()
						.into(imageStore);
						
						
						if(posiGallery == 0 && argallery.size() == 1){
							imgNext.setVisibility(View.INVISIBLE);
							imgPrev.setVisibility(View.INVISIBLE);
						}else if(posiGallery == 0){
							imgNext.setVisibility(View.VISIBLE);
							imgPrev.setVisibility(View.INVISIBLE);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}*/
            }
        });

        rl_broucher.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.e("Pdf Clicked", "***");
                String[] split = BroucherUrl.split("/");
                int length = split.length;
                String filename = split[length - 1];

                File root = new File(Environment.getExternalStorageDirectory() + "/Market");
                if (root.exists() && root.isDirectory()) {
                    File file = new File(root.getAbsolutePath() + "/" + filename);
                    if (file.exists()) {
                        //open file from device storage
                        Log.e("Pdf Open", "***");
                        Intent target = new Intent(Intent.ACTION_VIEW);

                        if (filename.endsWith(".jpeg") || filename.endsWith(".png") || filename.endsWith(".jpg")) {
                            target.setDataAndType(Uri.fromFile(file), "image/*");
                        } else if (filename.endsWith(".pdf")) {
                            target.setDataAndType(Uri.fromFile(file), "application/pdf");
                        } else {
                            target.setDataAndType(Uri.fromFile(file), "application/*");
                        }
                        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        Intent intent = Intent.createChooser(target, "Open File");
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            // Instruct the user to install a PDF reader here, or something
                        }
                    } else {
                        if (AppUtil.isNetworkAvailable(context)) {
                            Log.e("Pdf start download", "***");
                            dialog = ProgressDialog.show(context, "", "downloading file ..");
                            DownloadTask task = new DownloadTask(getResources().getString(R.string.image_base_url) + BroucherUrl, filename);
                            task.execute(new String[]{});
                        } else {
                            Toast.makeText(context, "Please check your internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (AppUtil.isNetworkAvailable(context)) {
                        Log.e("Pdf start download", "***");
                        dialog = ProgressDialog.show(context, "", "downloading file ..");
                        DownloadTask task = new DownloadTask(getResources().getString(R.string.image_base_url), filename);
                        task.execute(new String[]{});
                    } else {
                        Toast.makeText(context, "Please check your internet", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

    }

    public void setdata(String data) {


        //String data = activity.productListData();
//		/String data = mCallback.onPostData(data);
        try {
            JSONObject jo = new JSONObject(data);
            JSONObject joStoreFront = jo.getJSONObject("store");
            //String storeId = joStoreFront.getString("Id");
            String storeName = joStoreFront.getString("Name");
            String busiNessName = joStoreFront.getString("BusinessName");
            String discription = joStoreFront.getString("Description");
            String StoreCode = joStoreFront.getString("StoreCode");
            String Address = joStoreFront.getString("Address");
            String Phone = joStoreFront.getString("Phone");
            String Mobile = joStoreFront.getString("Mobile");
            String Email = joStoreFront.getString("Email");
            BroucherUrl = joStoreFront.getString("BroucherUrl");
            String OpenningTime = joStoreFront.getString("OpenningTime");
            String ClosingTime = joStoreFront.getString("ClosingTime");
            int isMondayopen = joStoreFront.getInt("IsMonOpen");
            int isTuesdayOpen = joStoreFront.getInt("IsTueOpen");
            int isWednesdayopen = joStoreFront.getInt("IsWedOpen");
            int isThuOpen = joStoreFront.getInt("IsThuOpen");
            int isFriOpen = joStoreFront.getInt("IsFriOpen");
            int isSatOpen = joStoreFront.getInt("IsSatOpen");
            int isSunOpen = joStoreFront.getInt("IsSunOpen");

            int isDeleveryAvailable = joStoreFront.getInt("IsDeliveryAvailable");
            String statnerdDeleveryTime = joStoreFront.getString("StandardDeliveryTime");
            String minDistance = joStoreFront.getString("Distance");
            String about_us = joStoreFront.getString("AboutUs");

            if (joStoreFront.has("MinimumOrderValue")) {
                minOrderValue = joStoreFront.getString("MinimumOrderValue");
            }
            ((TextView) getView().findViewById(R.id.txt_address)).setText(Address);
            ((TextView) getView().findViewById(R.id.txt_mobile_num)).setText(Mobile + "  " + Phone);
            ((TextView) getView().findViewById(R.id.txt_busi)).setText(discription);
            ((TextView) getView().findViewById(R.id.txt_descr)).setText("Opening Time: " + OpenningTime +
                    "\n" + "Closing Time  : " + ClosingTime);

            ((TextView) getView().findViewById(R.id.txt_about_us)).setText(about_us);

            txtPhoneNum1.setText(Phone);
            txtPhoneNum2.setText(Mobile);
            txtstoreName.setText(storeName);
            txtAddress.setText(Address);

            if (isDeleveryAvailable == 1) {
                ((TextView) getView().findViewById(R.id.txt_order_detail))
                        .setText("Delivery Available : " + "Yes" + "\n" + "Delivery Time : " +
                                statnerdDeleveryTime + "\n" + "Distance : " + minDistance + "" + "\n" +
                                "Minimum Order Value : " + minOrderValue);
            } else {
                ((TextView) getView().findViewById(R.id.txt_order_detail))
                        .setText("Delivery Not Available");
            }

            JSONArray jaGallery = joStoreFront.getJSONArray("StoreGallery");

            for (int i = 0; i < jaGallery.length(); i++) {
                JSONObject jobGallery = jaGallery.getJSONObject(i);
                argallery.add(jobGallery.getString("ImageUrl"));
            }

            if(argallery.size()>1)
            {
               imgNext.setVisibility(View.VISIBLE);

            }



            galleryAdapter.notifyDataSetChanged();

            ((TextView) getView().findViewById(R.id.txt_lbl))
                    .setVisibility(View.VISIBLE);
			/*if(argallery.size() > posiGallery ){
				Picasso.with(context).load(getResources().getString(R.string.image_base_url)+argallery.get(posiGallery))
				.placeholder(R.drawable.oplaceholder)
				.fit()
				.into(imageStore);
			}
			
			if(argallery.size() <= 1){
				imgNext.setVisibility(View.INVISIBLE);
				imgPrev.setVisibility(View.INVISIBLE);
			}else{
				imgNext.setVisibility(View.VISIBLE);
				imgPrev.setVisibility(View.INVISIBLE);
			}*/

            if (isMondayopen == 1) {
                ((ImageView) getView().findViewById(R.id.img_d1))
                        .setImageResource(R.drawable.m_e);
            } else {
                ((ImageView) getView().findViewById(R.id.img_d1))
                        .setImageResource(R.drawable.m);
            }
            if (isTuesdayOpen == 1) {
                ((ImageView) getView().findViewById(R.id.img_d2))
                        .setImageResource(R.drawable.t_e);
            } else {
                ((ImageView) getView().findViewById(R.id.img_d2))
                        .setImageResource(R.drawable.t);
            }
            if (isWednesdayopen == 1) {
                ((ImageView) getView().findViewById(R.id.img_d3))
                        .setImageResource(R.drawable.w_e);
            } else {
                ((ImageView) getView().findViewById(R.id.img_d3))
                        .setImageResource(R.drawable.w);
            }
            if (isThuOpen == 1) {
                ((ImageView) getView().findViewById(R.id.img_d4))
                        .setImageResource(R.drawable.t_e);
            } else {
                ((ImageView) getView().findViewById(R.id.img_d4))
                        .setImageResource(R.drawable.t);
            }
            if (isFriOpen == 1) {
                ((ImageView) getView().findViewById(R.id.img_d5))
                        .setImageResource(R.drawable.f_e);
            } else {
                ((ImageView) getView().findViewById(R.id.img_d5))
                        .setImageResource(R.drawable.f);
            }
            if (isSatOpen == 1) {
                ((ImageView) getView().findViewById(R.id.img_d6))
                        .setImageResource(R.drawable.s_e);
            } else {
                ((ImageView) getView().findViewById(R.id.img_d6))
                        .setImageResource(R.drawable.s);
            }
            if (isSunOpen == 1) {
                ((ImageView) getView().findViewById(R.id.img_d7))
                        .setImageResource(R.drawable.s_e);
            } else {
                ((ImageView) getView().findViewById(R.id.img_d7))
                        .setImageResource(R.drawable.s);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        String downloadUrls = "", fileName = "";

        protected void onPreExecute() {
            super.onPreExecute();

        }

        public DownloadTask(String path, String filename) {
            downloadUrls = path;
            fileName = filename;
        }

        protected String doInBackground(String... urls) {

            try {
                Log.e("download start", "***");
                downloadFile(downloadUrls, fileName);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        protected void onPostExecute(String result) {
            JSONObject jObject;
            try {
                Log.e("download stop", "***");
                File root = new File(Environment.getExternalStorageDirectory() + "/Market");
                File file = new File(root.getAbsolutePath() + "/" + fileName);
                if (file.exists()) {
                    Intent target = new Intent(Intent.ACTION_VIEW);
                    if (fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
                        target.setDataAndType(Uri.fromFile(file), "image/*");
                    } else if (fileName.endsWith(".pdf")) {
                        target.setDataAndType(Uri.fromFile(file), "application/pdf");
                    } else {
                        target.setDataAndType(Uri.fromFile(file), "application/*");
                    }
                    target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    Intent intent = Intent.createChooser(target, "Open File");
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        // Instruct the user to install a PDF reader here, or something
                    }
                }
                if (dialog != null) {
                    dialog.dismiss();
                }

            } catch (Exception e) {
                e.printStackTrace();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        }
    }

    public void downloadFile(String fileURL, String fileName) {
        Log.e("Download STart", "*****");
        StatFs stat_fs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double avail_sd_space = (double) stat_fs.getAvailableBlocks() * (double) stat_fs.getBlockSize();
        //double GB_Available = (avail_sd_space / 1073741824);
        double MB_Available = (avail_sd_space / 10485783);
        //System.out.println("Available MB : " + MB_Available);
        Log.d("MB", "" + MB_Available);
        try {
            File root = new File(Environment.getExternalStorageDirectory() + "/Market");
            if (root.exists() && root.isDirectory()) {
				/*File file = new File(root.getAbsolutePath() + "/" + fileName);
				if(file.exists()){
					 file.delete();
			           Log.d("FILE-DELETE","YES");
			           }*/
            } else {
                root.mkdir();
            }
            Log.d("CURRENT PATH", root.getPath());
            URL u = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            //c.setDoOutput(true);
            c.connect();
            int fileSize = c.getContentLength() / 1048576;
            Log.d("FILESIZE", "" + fileSize);
            if (MB_Available <= fileSize) {
                Toast.makeText(context, "No memory available", Toast.LENGTH_SHORT).show();
                c.disconnect();
                return;
            }
            File myfile = new File(root.getPath(), fileName);

            FileOutputStream f = new FileOutputStream(myfile);

            InputStream in = c.getInputStream();


            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();
	      /* File file = new File(root.getAbsolutePath() + "/" + fileName);
	       if(file.exists()){
	           file.delete();
	           Log.d("FILE-DELETE","YES");
	       }else{
	           Log.d("FILE-DELETE","NO");
	       }
	       File from =new File(root.getAbsolutePath() + "/" + fileName);
	       File to = new File(root.getAbsolutePath() + "/" + "some.mp3");*/


        } catch (Exception e) {
            Log.d("Downloader", e.getMessage());

        }
    }


    /**
     * product list asyntask
     */
    private class ProductListTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.getProductList(urls[0], context
                        .getResources().getString(R.string.product_list_url));
                Log.e("product list data RRESPONSE", "" + response);
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

                        JSONObject jo = job.getJSONObject("data");

                        setdata(jo.toString());

                    } else {
                        AppUtil.showCustomToast(context, message);
                    }

                } else {
                    AppUtil.showCustomToast(context,
                            "please check your internet connection");
                }
                if (dialog != null)
                    dialog.cancel();
                progressbar.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
                if (dialog != null)
                    dialog.cancel();
                progressbar.setVisibility(View.GONE);

            }
        }
    }


}
