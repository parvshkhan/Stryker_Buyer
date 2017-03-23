package com.app.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.fragments.OrderHomeFragment;
import com.app.listeners.CancelOrderListener;
import com.app.model.OrderHomeModel;
import com.app.stryker.R;
import com.app.utill.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class OrderHomeListAdapter extends ArrayAdapter<OrderHomeModel> {

    int layoutId;
    Context con;
    ProgressDialog dialog;
    String blank = "", from = "";
    List<OrderHomeModel> listStore;
    CancelOrderListener listener;

    LayoutInflater inflater;


    public OrderHomeListAdapter(Context context, int resource,
                                List<OrderHomeModel> objects, OrderHomeFragment frag, String from) {
        super(context, resource, objects);
        // TODO Auto-generated constructor stub
        con = context;
        layoutId = resource;
        listStore = objects;
        this.from = from;
        this.listener = (CancelOrderListener) frag;

        inflater = (LayoutInflater) context
                .getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    class ViewHolder {
        TextView txtStoreName, txtOrderNumber, txtTime, txtInitialName;
        ImageView imgCancel, imgPic;
        int posi;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View row = convertView;
        final OrderHomeModel hgetter = getItem(position);
        if (row == null) {
            row = inflater.inflate(layoutId, null);
            ViewHolder holder = new ViewHolder();

            holder.txtStoreName = (TextView) row.findViewById(R.id.txt_name);
            holder.txtOrderNumber = (TextView) row.findViewById(R.id.txt_order_id);
            holder.txtTime = (TextView) row.findViewById(R.id.txt_time);
            holder.txtInitialName = (TextView) row.findViewById(R.id.txt_initname);
            holder.imgCancel = (ImageView) row.findViewById(R.id.img_cancel);
            holder.imgPic = (ImageView) row.findViewById(R.id.img_order);
            row.setTag(holder);

        }
        final ViewHolder holder = (ViewHolder) row.getTag();
        holder.posi = position;
        holder.txtStoreName.setText(hgetter.getStoreName());
        holder.txtTime.setText("Order placed at " + hgetter.getOrderDateTime());
        holder.txtOrderNumber.setText("Order Number " + hgetter.getOrderId());

        if (hgetter.getImageUrl().length() > 0) {
            Picasso.with(con).load(con.getResources().getString(R.string.image_base_url) + hgetter.getImageUrl())
                    .placeholder(R.drawable.new_placeholder)
                    .transform(new CircleTransform())
                    .transform(new CircleTransform()).into(holder.imgPic);
            holder.txtInitialName.setVisibility(View.GONE);
            holder.imgPic.setVisibility(View.VISIBLE);
        } else {
            holder.txtInitialName.setVisibility(View.VISIBLE);
            holder.imgPic.setVisibility(View.GONE);
            holder.txtInitialName.setText(hgetter.getStoreName().substring(0, 1));
        }

        if (from.equalsIgnoreCase("pending")) {
            holder.imgCancel.setVisibility(View.VISIBLE);
        } else {
            holder.imgCancel.setVisibility(View.GONE);
        }

        holder.imgCancel.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                listener.cancelOrderListPosition(holder.posi);
            }
        });

        return row;
    }

}
