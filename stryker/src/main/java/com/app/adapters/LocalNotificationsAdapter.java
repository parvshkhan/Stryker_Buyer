package com.app.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.model.NotificationModel;
import com.app.stryker.R;

import java.util.ArrayList;


/**
 * Created by Inflac on 16-10-2015.
 */
public class LocalNotificationsAdapter extends ArrayAdapter {

    ArrayList<NotificationModel> list = new ArrayList<NotificationModel>();

    Context context;
    ProgressDialog dialog;

    public LocalNotificationsAdapter(Context context, int resource, ArrayList<NotificationModel> list) {
        super(context, resource, list);

        this.context = context;
        this.list = list;

    }

    class Dataholder {

        ImageView imgUser;
        TextView txtUserName, txtMessage, txtPrice, txtUnits, txtInitial, txt_header_add;
        RelativeLayout rl_price;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Dataholder holder = new Dataholder();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.seller_row_notifications, parent, false);

            holder.txt_header_add = (TextView) row.findViewById(R.id.txt_header_add);
            holder.imgUser = (ImageView) row.findViewById(R.id.img_user);
            holder.txtUserName = (TextView) row.findViewById(R.id.txt_name);
            holder.txtMessage = (TextView) row.findViewById(R.id.txt_message);
            holder.txtPrice = (TextView) row.findViewById(R.id.txt_price);
            holder.txtUnits = (TextView) row.findViewById(R.id.txt_units);
            holder.txtInitial = (TextView) row.findViewById(R.id.txt_initname);

            holder.rl_price = (RelativeLayout) row.findViewById(R.id.rl_price);

            row.setTag(holder);

        } else {

            holder = (Dataholder) row.getTag();
        }

        final NotificationModel model = (NotificationModel) this.getItem(position);

        if (model.getIsShowHeader()) {
            holder.txt_header_add.setVisibility(View.VISIBLE);
            holder.txt_header_add.setText(model.getNotificationDate());

        } else {
            holder.txt_header_add.setVisibility(View.GONE);
        }

        if (model.getNotTypeId().equalsIgnoreCase("1") || model.getNotTypeId().equalsIgnoreCase("2")
                || model.getNotTypeId().equalsIgnoreCase("3") || model.getNotTypeId().equalsIgnoreCase("1")) {

            holder.txtUserName.setText(model.getUserName());
            holder.txtMessage.setText(model.getNotficiationDesc());
            holder.txtPrice.setText("" + model.getTotalPrice());
            holder.txtUnits.setText("" + model.getQuantity() + " " + "Units");
            holder.txtInitial.setText("" + (model.getUserName()).toUpperCase().charAt(0) + "");
        } else {
            holder.rl_price.setVisibility(View.GONE);

            holder.txtUserName.setText(model.getUserName());
            holder.txtMessage.setText(model.getNotficiationDesc());
            holder.txtInitial.setText("" + (model.getUserName()).toUpperCase().charAt(0) + "");
        }
        return row;

    }

}
