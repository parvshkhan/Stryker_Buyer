package com.app.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.model.OrderHomeModel;
import com.app.model.Pending_model;
import com.app.stryker.R;

import java.util.ArrayList;

/**
 * Created by MIHIR on 1/12/2017.
 */

public class OrderExpandableAdapterCancelledMe extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<OrderHomeModel> arlistOrderCancelByMe;
    private ArrayList<Pending_model> cancellbyme_modellist;


    public OrderExpandableAdapterCancelledMe(FragmentActivity activity, ArrayList<OrderHomeModel> arlistOrderCancelByMe,
                                             ArrayList<Pending_model> cancellbyme_modellist) {

        this.context = activity;
        this.arlistOrderCancelByMe = arlistOrderCancelByMe;
        this.cancellbyme_modellist = cancellbyme_modellist;

    }

    @Override
    public Object getChild(int listPosition, int expandableListPosition) {
        return cancellbyme_modellist.get(listPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandableListPosition) {
        return expandableListPosition;
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


            txt_store_name.setText(cancellbyme_modellist.get(listPosition).getTxt_store_name());
            txt_count.setText(cancellbyme_modellist.get(listPosition).getTxt_count());
            txt_desc.setText(cancellbyme_modellist.get(listPosition).getTxt_desc());
            txt_price.setText(cancellbyme_modellist.get(listPosition).getTxt_price());


        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int listPosition) {
        return 0;
    }

    @Override
    public int getGroupCount() {
        return arlistOrderCancelByMe.size();
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


        txt_store_name.setText(arlistOrderCancelByMe.get(listPosition).getStoreName());
        txt_order_id.setText(arlistOrderCancelByMe.get(listPosition).getOrderId());
        txt_orderdatetime.setText(arlistOrderCancelByMe.get(listPosition).getOrderDateTime());


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
