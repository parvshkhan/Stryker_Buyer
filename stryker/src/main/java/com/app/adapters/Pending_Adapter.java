package com.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.model.Pending_model;
import com.app.stryker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Inflac on 13-10-2015.
 */
public class Pending_Adapter extends ArrayAdapter {

    List header = new ArrayList();
    List store_list = new ArrayList();
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    public Pending_Adapter(Context context, int resource) {
        super(context, resource);

    }


    public void add(Object object) {
        super.add(object);
        store_list.add(object);
    }

    public int getItemViewType(int position) {
        return header.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    public int getViewTypeCount() {
        return 2;
    }

    public long getItemId(int position) {
        return position;
    }

    public int getCount() {
        return store_list.size();
    }

    public Object getItem(int position) {

        return this.store_list.get(position);
    }

    class Datahandler {

        TextView txt_store_name, txt_desc, txt_price, txt_count, txt_initial;
        TextView header;

    }

    public void addSectionHeaderItem(String item) {

        store_list.add(item);
        header.add(store_list.size() - 1);
        notifyDataSetChanged();
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        Datahandler handler = new Datahandler();
        int rowType = getItemViewType(position);

        if (convertView == null) {

            switch (rowType) {


                case TYPE_ITEM:

                    row = inflater.inflate(R.layout.row_pending, parent, false);
                    handler.txt_store_name = (TextView) row.findViewById(R.id.txt_store_name);
                    handler.txt_desc = (TextView) row.findViewById(R.id.txt_desc);
                    handler.txt_price = (TextView) row.findViewById(R.id.txt_price);
                    handler.txt_count = (TextView) row.findViewById(R.id.txt_count);
                    handler.txt_initial = (TextView) row.findViewById(R.id.txt_initial);

                    break;

                case TYPE_SEPARATOR:

                    row = inflater.inflate(R.layout.custom_pending_header, parent, false);
                    handler.header = (TextView) row.findViewById(R.id.txt_header_list);
                    break;

            }

            row.setTag(handler);
        } else {

            handler = (Datahandler) row.getTag();
        }

        switch (rowType) {

            case TYPE_ITEM:

                Pending_model pending_model;
                pending_model = (Pending_model) this.getItem(position);
                handler.txt_store_name.setText(pending_model.getTxt_store_name());
                handler.txt_desc.setText(pending_model.getTxt_desc());
                handler.txt_count.setText("" + pending_model.getTxt_count());
                handler.txt_price.setText("" + pending_model.getTxt_price());
                handler.txt_initial.setText("" + (pending_model.getTxt_store_name()).toUpperCase().charAt(0) + "");

                break;

            case TYPE_SEPARATOR:

                handler.header.setText((String) store_list.get(position));

                break;

        }

        return row;
    }
}
