package com.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.model.NotificationModel;
import com.app.stryker.R;

import java.util.List;


public class NotificationsAdapter extends ArrayAdapter<NotificationModel> {

    int layoutId;
    Context con;
    List<NotificationModel> listNotification;
    int tab;

    public NotificationsAdapter(Context context, int resource,
                                List<NotificationModel> objects) {
        super(context, resource, objects);
        // TODO Auto-generated constructor stub
        con = context;
        layoutId = resource;
        listNotification = objects;
        Log.e("ListHistory", "" + listNotification);
    }

    class ViewHolder {
        TextView txtDesc, txtDate;
        int posi;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) con.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View row = convertView;
        final NotificationModel hgetter = getItem(position);

        if (row == null) {
            row = inflater.inflate(layoutId, null);
            ViewHolder holder = new ViewHolder();

            holder.txtDesc = (TextView) row.findViewById(R.id.txt_desc);
            holder.txtDate = (TextView) row.findViewById(R.id.txt_date);

            row.setTag(holder);

        }
        final ViewHolder holder = (ViewHolder) row.getTag();

        holder.posi = position;

        holder.txtDesc.setText(hgetter.getNotficiationDesc());
        holder.txtDate.setText(hgetter.getNotificationDate());


        return row;
    }


}
