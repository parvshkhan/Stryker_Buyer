package com.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.model.StoreListModel;
import com.app.stryker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CustomGridCategory extends BaseAdapter {


    Context mContext;
    ArrayList<StoreListModel> storeListModels;

    public CustomGridCategory(Context context, ArrayList<StoreListModel> storeListModels) {
        mContext = context;
        this.storeListModels = storeListModels;
    }

    @Override
    public int getCount() {
        return storeListModels.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.category_row_grid_layout, null);
            TextView textView = (TextView) grid.findViewById(R.id.category_text);
            ImageView imageView = (ImageView) grid.findViewById(R.id.category_image);
            textView.setText(storeListModels.get(i).getStoreName());


            Picasso.with(mContext)
                    .load("http://marketapp.online/web/uploads/" + storeListModels.get(i).getStoreImage())
                    .into(imageView);

        } else {
            grid = (View) view;
        }

        return grid;

    }
}
