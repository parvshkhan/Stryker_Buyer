package com.app.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.utill.Blur;
import com.app.fragments.StoresHomeFragment;
import com.app.listeners.DeleteStoreListener;
import com.app.model.StoreHomeListModel;
import com.app.stryker.ProductListActivity;
import com.app.stryker.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class StoreHomeListAdapter extends ArrayAdapter<StoreHomeListModel> {

    int layoutId;
    Context con;
    ProgressDialog dialog;
    String blank = "";
    List<StoreHomeListModel> listStore;
    DeleteStoreListener listener;
    StoresHomeFragment frag = new StoresHomeFragment();

    public StoreHomeListAdapter(Context context, int resource,
                                List<StoreHomeListModel> objects, StoresHomeFragment frag) {
        super(context, resource, objects);
        // TODO Auto-generated constructor stub
        con = context;
        layoutId = resource;
        listStore = objects;
        listener = (DeleteStoreListener) frag;
    }

    class ViewHolder {
        TextView txtStoreName, txt_productList, txt_Chat;
        ImageView imgStore, imgDelete;
        RelativeLayout rl_ProductList, rl_chat, rl_info;
        int posi;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) con
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View row = convertView;
        final StoreHomeListModel hgetter = getItem(position);
        if (row == null) {
            row = inflater.inflate(layoutId, null);
            ViewHolder holder = new ViewHolder();
            holder.txtStoreName = (TextView) row
                    .findViewById(R.id.txt_store_name);
            holder.txt_productList = (TextView) row
                    .findViewById(R.id.txt_product_list);
            holder.txt_Chat = (TextView) row.findViewById(R.id.txt_chat);
            holder.imgStore = (ImageView) row.findViewById(R.id.img_store);
            holder.imgDelete = (ImageView) row.findViewById(R.id.img_delete);
            holder.rl_ProductList = (RelativeLayout) row
                    .findViewById(R.id.rl_product_list);
            holder.rl_chat = (RelativeLayout) row.findViewById(R.id.rl_chat);
            holder.rl_info = (RelativeLayout) row.findViewById(R.id.rl_info);
            row.setTag(holder);

        }
        final ViewHolder holder = (ViewHolder) row.getTag();
        holder.posi = position;

        holder.txtStoreName.setText(hgetter.getStore_name());

        try {
            if (!hgetter.getStore_image().equalsIgnoreCase("")) {

                Transformation blurTransformation = new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap source) {
                        Bitmap blurred = Blur.fastblur(con, source, 10);
                        source.recycle();
                        return blurred;
                    }

                    @Override
                    public String key() {
                        return "blur()";
                    }
                };

                final int imageViewWidth=500;
                final    int imageViewHeight=500;
                Picasso.with(con)
                        .load(con.getResources().getString(R.string.image_base_url) + hgetter.getStore_image()) // thumbnail url goes here
                        .placeholder(R.drawable.placeholder)
                        .resize(imageViewWidth, imageViewHeight)
                        .transform(blurTransformation)
                        .into(holder.imgStore, new Callback() {
                            @Override
                            public void onSuccess() {
                                Picasso.with(con)
                                        .load(con.getResources().getString(R.string.image_base_url) + hgetter.getStore_image()) // image url goes here
                                        .networkPolicy(NetworkPolicy.NO_CACHE)
                                        .resize(imageViewWidth, imageViewHeight)
                                        .placeholder(holder.imgStore.getDrawable())
                                        .into(holder.imgStore);
                            }

                            @Override
                            public void onError() {
                            }
                        });


                //////////////////////////////////////////////////////////////////////////////////////////////////




              /*  Picasso.with(con).load(con.getResources().getString(R.string.image_base_url) + hgetter.getStore_image())
                        .placeholder(R.drawable.placeholder)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .error(R.drawable.placeholder)
                        //.resize(1050, 780)
                        //.centerCrop()
                        .into(holder.imgStore);*/




            }


            else {
                holder.imgStore.setImageResource(R.drawable.placeholder);
            }
        } catch (NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            holder.imgStore.setImageResource(R.drawable.placeholder);

        }

        holder.imgDelete.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                /** call delete store service */
                listener.onDeleteStoreClick(holder.posi, hgetter.getSrore_id());
                // listStore.remove(holder.posi);
                // notifyDataSetChanged();
            }
        });

        holder.rl_ProductList.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                /** switch to product list activity */
                Intent i = new Intent(con, ProductListActivity.class);
                i.putExtra("store_id", hgetter.getSrore_id());
                i.putExtra("name", hgetter.getStore_name());
                i.putExtra("from", "productlist");
                i.putExtra("seller_id", hgetter.getseller_id());
                i.putExtra("sellerChat_id", hgetter.getSellerChatId());
                con.startActivity(i);
            }
        });

        holder.imgStore.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                /** switch to product list activity */
                Intent i = new Intent(con, ProductListActivity.class);
                i.putExtra("store_id", hgetter.getSrore_id());
                i.putExtra("name", hgetter.getStore_name());
                i.putExtra("from", "productlist");
                i.putExtra("seller_id", hgetter.getseller_id());
                i.putExtra("sellerChat_id", hgetter.getSellerChatId());
                con.startActivity(i);
            }
        });

        holder.rl_chat.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                /** switch to chat list activity */
                Intent i = new Intent(con, ProductListActivity.class);
                i.putExtra("from", "chat");
                i.putExtra("name", hgetter.getStore_name());
                i.putExtra("store_id", hgetter.getSrore_id());
                i.putExtra("seller_id", hgetter.getseller_id());
                i.putExtra("sellerChat_id", hgetter.getSellerChatId());
                con.startActivity(i);
            }
        });
        holder.rl_info.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                /** switch to chat list activity */
                Intent i = new Intent(con, ProductListActivity.class);
                i.putExtra("from", "info");
                i.putExtra("name", hgetter.getStore_name());
                i.putExtra("store_id", hgetter.getSrore_id());
                i.putExtra("sellerChat_id", hgetter.getSellerChatId());
                con.startActivity(i);
            }
        });

        return row;
    }

}
