package com.app.stryker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by admin on 1/18/2017.
 */
public class CustomLeftDrawer<T> extends ArrayAdapter {


    Context context;
    ArrayList<String> leftdrawerArrayList;
    int id;
    LayoutInflater layoutInflater;


    public CustomLeftDrawer(Context applicationContext, int left_drawer_row, ArrayList<String> leftdrawerArrayList) {
        super(applicationContext, left_drawer_row, leftdrawerArrayList);

        context = applicationContext;
        id = left_drawer_row;
        this.leftdrawerArrayList = leftdrawerArrayList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        convertView = layoutInflater.inflate(id, null);

        TextView textView = (TextView) convertView.findViewById(R.id.draweritems);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView2);

        textView.setText(leftdrawerArrayList.get(position));

        return convertView;
    }
}
