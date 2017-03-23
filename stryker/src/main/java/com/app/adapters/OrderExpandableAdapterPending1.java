package com.app.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.model.ModelProdutsArray;
import com.app.model.Order;
import com.app.stryker.R;

import java.util.ArrayList;

/**
 * Created by MIHIR on 1/12/2017.
 */

public class OrderExpandableAdapterPending1 extends BaseExpandableListAdapter {

    private Context context;
    //ArrayList<Pending_model> pending_modelsArrraylist;

    ArrayList<Order> arlistOrderPending;


    public OrderExpandableAdapterPending1(FragmentActivity activity, ArrayList<Order> arlistOrderPending) {

        context = activity;
        this.arlistOrderPending = arlistOrderPending;

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ModelProdutsArray> childdetaillist = (ArrayList<ModelProdutsArray>) arlistOrderPending.get(groupPosition).getOrderDetails();
        return childdetaillist.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        ModelProdutsArray modelProdutsArray = (ModelProdutsArray) getChild(groupPosition, childPosition);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_order_expandable, null);

            TextView txt_store_name = (TextView) convertView.findViewById(R.id.txt_store_name);
            TextView txt_count = (TextView) convertView.findViewById(R.id.txt_count);
            TextView txt_desc = (TextView) convertView.findViewById(R.id.txt_desc);
            TextView txt_price = (TextView) convertView.findViewById(R.id.txt_price);

/*
            txt_store_name.setText(.get(listPosition).getTxt_store_name());
            txt_count.setText(pending_modelsArrraylist.get(listPosition).getTxt_count());
            txt_desc.setText(pending_modelsArrraylist.get(listPosition).getTxt_desc());
            txt_price.setText(pending_modelsArrraylist.get(listPosition).getTxt_price());*/


        }

        try {

            if (convertView == null) {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_order_expandable, null);

            }

            TextView txt_store_name = (TextView) convertView.findViewById(R.id.txt_store_name);
            TextView txt_count = (TextView) convertView.findViewById(R.id.txt_count);
            TextView txt_desc = (TextView) convertView.findViewById(R.id.txt_desc);
            TextView txt_price = (TextView) convertView.findViewById(R.id.txt_price);

/*
          txt_store_name.setText(pending_modelsArrraylist.get(listPosition).getTxt_store_name());
          txt_count.setText(pending_modelsArrraylist.get(listPosition).getTxt_count());
          txt_desc.setText(pending_modelsArrraylist.get(listPosition).getTxt_desc());
          txt_price.setText(pending_modelsArrraylist.get(listPosition).getTxt_price());*/

        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return 3;
    }

    @Override
    public Object getGroup(int listPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return arlistOrderPending.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
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


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
