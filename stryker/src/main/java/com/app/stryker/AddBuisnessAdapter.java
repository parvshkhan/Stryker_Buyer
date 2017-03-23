package com.app.stryker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.model.StoreListModel;
import com.app.utill.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 1/14/2017.
 */
public class AddBuisnessAdapter extends ArrayAdapter {

    Context context;
    int res;
    ArrayList<StoreListModel> arSearchList;
    LayoutInflater layoutInflater;

    public AddBuisnessAdapter(Context applicationContext, int search_buisness, ArrayList<StoreListModel> arSearchList) {

        super(applicationContext, search_buisness, arSearchList);
        context = applicationContext;
        res = search_buisness;
        this.arSearchList = arSearchList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(res, null);
        TextView txtName = (TextView) convertView.findViewById(R.id.txt_name);
        TextView txtCode = (TextView) convertView.findViewById(R.id.txt_code);
        TextView txtAddress = (TextView) convertView.findViewById(R.id.txt_address);
        TextView txtDescription = (TextView) convertView.findViewById(R.id.txt_description);
        ImageView img_store = (ImageView) convertView.findViewById(R.id.img_store);

        txtName.setText(arSearchList.get(position).getStoreName());
        txtCode.setText(arSearchList.get(position).getStoreCode());
        txtAddress.setText(arSearchList.get(position).getStoreAddress());
        txtDescription.setText(arSearchList.get(position).getStoreDescription());


        Picasso.with(context).load(context.getString(R.string.image_base_url) + arSearchList.get(position).getStoreImage())
                .placeholder(R.drawable.new_placeholder)
                .resize(200, 200)
                .transform(new CircleTransform()).into(img_store);


        return convertView;
    }
}
