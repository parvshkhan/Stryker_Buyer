package com.app.stryker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.app.adapters.NotificationsAdapter;
import com.app.jsoncall.JsonCall;
import com.app.model.NotificationModel;
import com.app.utill.AppUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Inflectica on 10/13/2015.
 */
public class NotificationActivity extends Activity {

    Context context;
    ProgressDialog dialog;
    private Double mLat = 0.0, mLong = 0.0;
    // ArrayList<NotificationModel> arlistNotifications = new ArrayList<>();
    ListView listNotification;
    // LocalNotificationsAdapter adapter;
    NotificationsAdapter adapter;
    ArrayList<NotificationModel> arlistNotifications = new ArrayList<NotificationModel>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        context = this;
        init();

        ((ImageView) findViewById(R.id.img_back)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                finish();
            }
        });
        if (AppUtil.isNetworkAvailable(context)) {
            dialog = ProgressDialog.show(context, "", "Please wait...");
            NotificationTask task = new NotificationTask();
            task.execute(new String[]{AppUtil.getUserId(context), "88"});

        } else {
            AppUtil.showCustomToast(context,
                    "please check your internet connection");
        }

      /*  if (AppUtil.isNetworkAvailable(context)) {
            dialog = ProgressDialog.show(context, "", "please wait...");
            LocalNotifications task = new LocalNotifications();
            task.execute(new String[]{AppUtil.getUserId(context)});

        } else {
            AppUtil.showCustomToast(context, "Please check your in internet connection");

        }*/
    }

    public void init() {

        listNotification = (ListView) findViewById(R.id.lv_notifications);
    }


    private class NotificationTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.showNotifications(urls[0], urls[1],
                        context.getResources().getString(R.string.notification_list_url));
                Log.e("Notification RRESPONSE", "" + response);
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

                        arlistNotifications.clear();

                        JSONObject job1 = job.getJSONObject("data");
                        JSONArray ja_notification = job1.getJSONArray("notification");

                        for (int i = 0; i < ja_notification.length(); i++) {

                            JSONObject jobj = ja_notification.getJSONObject(i);

                            String notDesc = jobj.getString("NotificationMessage");
                            String notId = jobj.getString("ID");
                            String notDate = jobj.getString("DateCreated");

                            arlistNotifications.add(new NotificationModel
                                    (notId, notDate, notDesc));
                        }

                        adapter = new NotificationsAdapter(context,
                                R.layout.seller_row_push_notification, arlistNotifications);
                        listNotification.setAdapter(adapter);

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
