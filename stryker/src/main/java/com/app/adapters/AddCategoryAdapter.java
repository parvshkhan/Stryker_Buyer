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
 * Created by admin on 1/14/2017.
 */

public class AddCategoryAdapter extends ArrayAdapter {
    Context context;
    ArrayList<String> categoryList;
    int id;
    LayoutInflater layoutInflater;


    public AddCategoryAdapter(Context applicationContext, int category_row_search, ArrayList<String> categoryList) {
        super(applicationContext, category_row_search, categoryList);

        context = applicationContext;
        id = category_row_search;
        this.categoryList = categoryList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        convertView = layoutInflater.inflate(id, null);

        TextView textView = (TextView) convertView.findViewById(R.id.name);

        textView.setText(categoryList.get(position));

        return convertView;
    }


/*
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        categoryList.clear();

        if(charText.length()!=0)
        {

            if()

        }

        notifyDataSetChanged();
    }*/


}
