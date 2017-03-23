package com.app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.stryker.R;

import java.util.ArrayList;

/**
 * Created by admin on 1/16/2017.
 */

public class CityListAdapter extends ArrayAdapter {

    Context context;
    int res;
    ArrayList<String> cityArrayList;
    LayoutInflater layoutInflater;

    public CityListAdapter(Context context, int resource, ArrayList<String> cityArrayList) {
        super(context, resource, cityArrayList);


        this.context = context;
        res = resource;
        this.cityArrayList = cityArrayList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(res, null);
        TextView textView = (TextView) convertView.findViewById(R.id.ctyname);

        textView.setText(cityArrayList.get(position).toUpperCase());


        return convertView;
    }
}
