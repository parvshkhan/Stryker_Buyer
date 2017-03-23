 package com.app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.utill.Blur;
import com.app.model.StoreListModel;
import com.app.stryker.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<StoreListModel> mDataset;
    private static MyClickListener myClickListener;
    private Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
           /* implements View
            .OnClickListener*/ {
        TextView label;
        ImageView imageView;
        View view;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.category_text);
            imageView = (ImageView) itemView.findViewById(R.id.category_image);
            view = (View) itemView.findViewById(R.id.topmargin);
            Log.i(LOG_TAG, "Adding Listener");
            // itemView.setOnClickListener(con);
        }

    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(Context context, ArrayList<StoreListModel> myDataset) {
        mDataset = myDataset;
        this.context = context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_row_grid_layout, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);


        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {

        if (position == 0) {
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            buttonLayoutParams.setMargins(0, 0, 0, 0);
            holder.label.setLayoutParams(buttonLayoutParams);
            holder.label.setText("");

            holder.view.setVisibility(View.VISIBLE);


        } else {
            holder.view.setVisibility(View.GONE);
            holder.label.setText(mDataset.get(position).getStoreName());

        }

        Transformation blurTransformation = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                Bitmap blurred = Blur.fastblur(context, source, 10);
                source.recycle();
                return blurred;
            }

            @Override
            public String key() {
                return "blur()";
            }
        };
        final int imageViewWidth=500;
        final  int imageViewHeight=500;
        Picasso.with(context)
                .load("http://marketapp.online/web/uploads/" + mDataset.get(position).getStoreImage()) // thumbnail url goes here
                .resize(imageViewWidth, imageViewHeight)
                .transform(blurTransformation)
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.with(context)
                                .load("http://marketapp.online/web/uploads/" + mDataset.get(position).getStoreImage()) // image url goes here
                                .resize(imageViewWidth, imageViewHeight)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .placeholder(holder.imageView.getDrawable())
                                .into(holder.imageView);
                    }

                    @Override
                    public void onError() {
                    }
                });

/*

        Picasso.with(context)
                .load("http://marketapp.online/web/uploads/" + mDataset.get(position).getStoreImage())
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(holder.imageView);
*/


        //holder.imageView.setText(mDataset.get(position).getmText2());

    }

    public void addItem(StoreListModel dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }


}