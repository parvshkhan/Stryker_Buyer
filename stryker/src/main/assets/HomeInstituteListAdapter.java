package com.app.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.model.HomeInstitutesModel;
import com.app.myinfo.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class HomeInstituteListAdapter extends ArrayAdapter<HomeInstitutesModel> {

    int layoutId;
    Context con;
    ProgressDialog dialog;
    String blank = "";
    List<HomeInstitutesModel> listHomeInstitutes;


    public HomeInstituteListAdapter(Context context, int resource,
                                    List<HomeInstitutesModel> objects) {
        super(context, resource, objects);
        // TODO Auto-generated constructor stub
        con = context;
        layoutId = resource;
        listHomeInstitutes = objects;

    }

    class ViewHolder {
        TextView txtName, txtDistance, txtLocation;
        ImageView imgInstitute;
        int posi;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) con
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View row = convertView;
        final HomeInstitutesModel hgetter = getItem(position);
        if (row == null) {
            row = inflater.inflate(layoutId, null);
            ViewHolder holder = new ViewHolder();
            holder.txtName = (TextView) row.findViewById(R.id.txt_institute_name);
            holder.txtDistance = (TextView) row.findViewById(R.id.txt_distance);
            holder.txtLocation = (TextView) row.findViewById(R.id.txt_location);
            holder.imgInstitute = (ImageView) row.findViewById(R.id.img_institute);
            row.setTag(holder);

        }
        final ViewHolder holder = (ViewHolder) row.getTag();
        holder.posi = position;
        holder.txtName.setText(hgetter.getInstituteName());
        holder.txtDistance.setText(hgetter.getDistance() + " Km");
        holder.txtLocation.setText(hgetter.getRegionName());


        if (!hgetter.getImageUrl().equalsIgnoreCase("")) {
            if (hgetter.getImageUrl().startsWith("http")) {
                Picasso.with(con).load(hgetter.getImageUrl())
                        .placeholder(R.drawable.photo_placeholder)
                        // .transform(new CircleTransform())
                        .into(holder.imgInstitute);
            } else {
                Picasso.with(con)
                        .load(con.getResources().getString(R.string.base_url)
                                + hgetter.getImageUrl())
                        .placeholder(R.drawable.photo_placeholder)
                        // .transform(new CircleTransform())
                        .into(holder.imgInstitute);
            }

        }


        return row;
    }

}
