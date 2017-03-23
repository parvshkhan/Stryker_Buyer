package com.app.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.jsoncall.JsonCall;
import com.app.model.OrderHomeModel;
import com.app.model.Pending_model;
import com.app.stryker.HomeActivity;
import com.app.stryker.R;
import com.app.utill.AppUtil;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by MIHIR on 1/12/2017.
 */

public class OrderExpandableAdapterPending extends BaseExpandableListAdapter {

    private Context context;
    ArrayList<OrderHomeModel> arlistOrderPending;
    // HashMap<ArrayList<OrderHomeModel>, ArrayList<Pending_model>> pending_modelArraylist;

    public OrderExpandableAdapterPending(FragmentActivity activity, ArrayList<OrderHomeModel> arlistOrderPending, ArrayList<Pending_model> pending_modelsArrraylist) {

        context = activity;
        this.arlistOrderPending = arlistOrderPending;

    }

    @Override
    public Object getChild(int listPosition, int expandableListPosition) {
        return arlistOrderPending.get(listPosition).getOrderDetails().get(expandableListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandableListPosition) {
        return Integer.parseInt(arlistOrderPending.get(listPosition).getOrderDetails().get(expandableListPosition).ProductID);
    }

    @Override
    public View getChildView(int listPosition, int expandableListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        try {

            if (convertView == null) {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_order_expandable, null);

            }

            TextView txt_store_name = (TextView) convertView.findViewById(R.id.txt_store_name);
            TextView txt_count = (TextView) convertView.findViewById(R.id.txt_count);
            TextView txt_desc = (TextView) convertView.findViewById(R.id.txt_desc);
            TextView txt_price = (TextView) convertView.findViewById(R.id.txt_price);


            txt_store_name.setText(arlistOrderPending.get(listPosition).getOrderDetails().get(expandableListPosition).ProName);
            txt_count.setText(arlistOrderPending.get(listPosition).getOrderDetails().get(expandableListPosition).Quantity);
            txt_desc.setText(arlistOrderPending.get(listPosition).getOrderDetails().get(expandableListPosition).ProDescription);
            txt_price.setText(arlistOrderPending.get(listPosition).getOrderDetails().get(expandableListPosition).TotalCost);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return arlistOrderPending.get(groupPosition).getOrderDetails().size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return arlistOrderPending.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return arlistOrderPending.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return Integer.parseInt(arlistOrderPending.get(listPosition).getOrderId());
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.list_group_expandiblelistview, null);

        }

        TextView txt_store_name = (TextView) convertView.findViewById(R.id.txt_store_name);
        TextView txt_order_id = (TextView) convertView.findViewById(R.id.txt_order_id);
        TextView txt_orderdatetime = (TextView) convertView.findViewById(R.id.txt_orderdatetime);
        RelativeLayout rl_right = (RelativeLayout) convertView.findViewById(R.id.rl_right);


        txt_store_name.setText(arlistOrderPending.get(listPosition).getStoreName());
        txt_order_id.setText(arlistOrderPending.get(listPosition).getOrderId());
        txt_orderdatetime.setText(arlistOrderPending.get(listPosition).getOrderDateTime());

        if (arlistOrderPending.get(listPosition).getOrderType().equalsIgnoreCase("Pending")) {
            rl_right.setVisibility(View.VISIBLE);
            rl_right.setTag(arlistOrderPending.get(listPosition));
        } else {
            rl_right.setVisibility(View.GONE);
        }

        rl_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderHomeModel order = (OrderHomeModel) v.getTag();
                //AppUtil.showCustomToast(context, "Order Clicked "+order.getOrderId());
                CancelOrderTask cancelOrderTask = new CancelOrderTask();
                cancelOrderTask.execute(new String[]{order.getOrderId(), "3", AppUtil.getUserId(context), AppUtil.getDeviceId(context)});
            }
        });
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * cancel order asyntask
     */
    private class CancelOrderTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "", "Please wait....");
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
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.putExtra("loadOrder", "loadOrder");
                        context.startActivity(intent);
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
