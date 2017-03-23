package com.app.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.stryker.R;

import java.util.ArrayList;

public class CityCategoryAdapter extends ArrayAdapter<String> {

    int layoutId;
    Context con;
    ProgressDialog dialog;
    String blank = "";
    ArrayList<String> masterlist;
    //DeleteAddressListener listener;

    public CityCategoryAdapter(Context context, int resource,
                               ArrayList<String> objects) {
        super(context, resource, objects);
        // TODO Auto-generated constructor stub
        con = context;
        layoutId = resource;
        masterlist = objects;


    }

    class ViewHolder {
        TextView txtcity;

    }


    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO Auto-generated method stub

        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) con
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.row_city, parent, false);
            holder = new ViewHolder();

            holder.txtcity = (TextView) row.findViewById(R.id.txtcity);

            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.txtcity.setText(masterlist.get(position));

        return row;
    }

}
