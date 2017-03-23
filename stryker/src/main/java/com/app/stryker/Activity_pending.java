package com.app.stryker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.adapters.Pending_Adapter;
import com.app.jsoncall.JsonCall;
import com.app.model.DbModel.User;
import com.app.model.Pending_model;
import com.app.utill.AppUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Inflac on 13-10-2015.
 */
public class Activity_pending extends Activity {

    Context context;
    ImageView img_add1;
    TextView txt_header_pending, txt_store_name, txt_price, txt_date1, txt_date2;
    ImageView imgCancelOrder;
    ListView pending_listview;
    ProgressDialog dialog;
    Typeface tfbold;
    Typeface tfregular;
    Typeface tfthin;
    User user;
    TextView txtTime;
    int totalCost = 0;
    String order_id = "", time = "";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        context = this;
        user = new Gson().fromJson(AppUtil.getULoggedInUser(context), User.class);
        init();
        setListener();

        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("data")) {
                try {
                    JSONArray jaData = new JSONArray(b.getString("data"));
                    setData(jaData);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (b.containsKey("order_date")) {
                txt_date2.setText(b.getString("order_date"));

                time = b.getString("order_date");
            }

            if (b.containsKey("store_name")) {
                txt_store_name.setText(b.getString("store_name"));

            }

            if (b.containsKey("order_type")) {
                txt_header_pending.setText("Order " + b.getString("order_type") + "..");
                if (b.getString("order_type").equalsIgnoreCase("pending")) {
                    imgCancelOrder.setVisibility(View.VISIBLE);
                } else {
                    imgCancelOrder.setVisibility(View.GONE);
                }
            }
        }
        if (b.containsKey("order_id")) {
            order_id = b.getString("order_id");
        }


    }

    public void setData(JSONArray data) {
        Pending_Adapter pending_adapter = new Pending_Adapter(context, R.layout.row_pending);

        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject jo = data.getJSONObject(i);
                pending_adapter.addSectionHeaderItem(jo.getString("name"));

                JSONArray jaItem = jo.getJSONArray("items");
                for (int j = 0; j < jaItem.length(); j++) {
                    JSONObject jo1 = jaItem.getJSONObject(j);

                    pending_adapter.add(new Pending_model(jo1.getString("ProName"), jo1.getString("ProDescription"), jo1.getString("Quantity"),
                            jo1.getString("TotalCost")));
                    try {
                        totalCost += Integer.parseInt(jo1.getString("TotalCost"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }

        txt_price.setText(totalCost + "");

        pending_listview.setAdapter(pending_adapter);
    }


    public void init() {

        tfbold = Typeface.createFromAsset(context.getAssets(), "Roboto-Bold.ttf");
        tfregular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
        tfthin = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");

        img_add1 = (ImageView) findViewById(R.id.img_add1);
        txt_header_pending = (TextView) findViewById(R.id.txt_header_pending);
        txt_header_pending.setTypeface(tfregular);
        txt_store_name = (TextView) findViewById(R.id.txt_store_name);
        txt_store_name.setTypeface(tfbold);

        pending_listview = (ListView) findViewById(R.id.lv_items);
        pending_listview.setVisibility(View.VISIBLE);

        View footerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_pending_list, null, false);

        txt_price = (TextView) footerView.findViewById(R.id.txt_price);
        txt_price.setTypeface(tfbold);

        txt_date1 = (TextView) footerView.findViewById(R.id.txt_date1);
        txt_date1.setTypeface(tfbold);
        txt_date2 = (TextView) footerView.findViewById(R.id.txt_date2);
        txt_date2.setTypeface(tfbold);

        txtTime = (TextView) findViewById(R.id.txt_time);
        txtTime.setText(time);
        imgCancelOrder = (ImageView) findViewById(R.id.img_cancel_order);

        pending_listview.addFooterView(footerView);


    }

    public void setListener() {
        imgCancelOrder.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                // status id = 3 for buyer and 4 for seller
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure ? want to cancel order.")
                        .setCancelable(false)
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog1, int id) {
                                /**call asynTask */
                                if (AppUtil.isNetworkAvailable(context)) {
                                    dialog = ProgressDialog.show(context, "", "please wait..");
                                    CancelOrderTask task = new CancelOrderTask();
                                    task.execute(new String[]{order_id, "3", AppUtil.getUserId(context), AppUtil.getDeviceId(context)});
                                } else {
                                    AppUtil.showCustomToast(context, "please check your internet");
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog1, int id) {
                                dialog1.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });
        img_add1.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    /**
     * cancel order asyntask
     */
    private class CancelOrderTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.cancelOrder(urls[0], urls[1], urls[2], urls[3],
                        context.getResources().getString(R.string.cancel_order_status));
                Log.e("cancel order RESPONSE", "" + response);
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
                        AppUtil.showCustomToast(context, message);
                        //JSONObject job1 = job.getJSONObject("data");

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
    /**remove store task*/
}
