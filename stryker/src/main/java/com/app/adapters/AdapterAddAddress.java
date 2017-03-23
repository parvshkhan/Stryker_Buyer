package com.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.listeners.DeleteAddressListener;
import com.app.model.ModelAddress;
import com.app.stryker.R;

import java.util.List;

/**
 * Created by Inflac on 14-10-2015.
 */
public class AdapterAddAddress extends ArrayAdapter {
    DeleteAddressListener listener;
    int selectedimage = 0;

    public AdapterAddAddress(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.listener = (DeleteAddressListener) context;
    }

    class DataHandler {
        TextView address, count;
        ImageView selecter, imgDelete;
        int posi;
    }

    public int imageselector(int selectedimage) {
        this.selectedimage = selectedimage;
        return selectedimage;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        //final DataHandler handler = new DataHandler();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.row_address, parent, false);
            DataHandler handler = new DataHandler();
            handler.address = (TextView) row
                    .findViewById(R.id.txt_address_line);
            handler.count = (TextView) row
                    .findViewById(R.id.txt_address_number);
            handler.selecter = (ImageView) row
                    .findViewById(R.id.img_select_address);
            handler.imgDelete = (ImageView) row
                    .findViewById(R.id.img_delete_address);
            row.setTag(handler);

        }
        final DataHandler handler = (DataHandler) row.getTag();

        handler.posi = position;
        ModelAddress model_address;
        model_address = (ModelAddress) this.getItem(position);
        handler.address.setText(model_address.getAddress_line1() + "\n"
                + model_address.getAddress_line2() + "\n"
                + model_address.getCountry() + "\n" + model_address.getState()
                + "\n" + model_address.getCity());

        //handler.count.setText("" + position);


        if (position == imageselector(selectedimage)) {

            handler.selecter.setImageResource(R.drawable.on);
        } else
            handler.selecter.setImageResource(R.drawable.off);

        notifyDataSetChanged();

        handler.imgDelete.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                /** delete address task here */
                listener.deleteAddress(handler.posi);
            }
        });
        return row;
    }
}
