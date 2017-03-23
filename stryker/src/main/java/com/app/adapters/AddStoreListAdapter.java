package com.app.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.model.StoreListModel;
import com.app.stryker.R;
import com.app.utill.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AddStoreListAdapter extends ArrayAdapter<StoreListModel> {

    int layoutId;
    Context con;
    ProgressDialog dialog;
    String blank = "";
    List<StoreListModel> listStore;
    //DeleteAddressListener listener;

    public AddStoreListAdapter(Context context, int resource,
                               List<StoreListModel> objects) {
        super(context, resource, objects);
        // TODO Auto-generated constructor stub
        con = context;
        layoutId = resource;
        listStore = objects;


    }

    class ViewHolder {
        TextView txtName, txtCode, txtAddress, txtDescription, txtInit;
        ImageView imgStore, img_add_store;
        int posi;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) con
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View row = convertView;
        final StoreListModel hgetter = getItem(position);
        if (row == null) {
            row = inflater.inflate(layoutId, null);
            ViewHolder holder = new ViewHolder();
            holder.txtName = (TextView) row.findViewById(R.id.txt_name);
            holder.txtCode = (TextView) row.findViewById(R.id.txt_code);
            holder.txtAddress = (TextView) row.findViewById(R.id.txt_address);
            holder.txtDescription = (TextView) row
                    .findViewById(R.id.txt_description);
            holder.imgStore = (ImageView) row.findViewById(R.id.img_store);
            holder.img_add_store = (ImageView) row.findViewById(R.id.img_add_store);
            holder.txtInit = (TextView) row.findViewById(R.id.txt_init);
            row.setTag(holder);

        }
        final ViewHolder holder = (ViewHolder) row.getTag();
        holder.posi = position;

        holder.txtName.setText(hgetter.getStoreName());
        holder.txtCode.setText(hgetter.getStoreCode());
        holder.txtAddress.setText(hgetter.getStoreAddress());
//		holder.txtDescription.setText(hgetter.getStoreDescription());


        if (!hgetter.getStoreImage().equalsIgnoreCase("")) {
            try {

                holder.txtInit.setVisibility(View.GONE);
                holder.imgStore.setVisibility(View.VISIBLE);

                Picasso.with(con).load(con.getString(R.string.image_base_url) + hgetter.getStoreImage())
                        .placeholder(R.drawable.new_placeholder)
                        .resize(200, 200)
                        .transform(new CircleTransform()).into(holder.imgStore);


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {

            holder.txtInit.setText(hgetter.getStoreName().charAt(0) + "");
            holder.txtInit.setVisibility(View.VISIBLE);
            holder.imgStore.setVisibility(View.GONE);
        }


        return row;
    }

}
