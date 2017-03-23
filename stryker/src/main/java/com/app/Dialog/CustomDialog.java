package com.app.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.app.fragments.OrderHomeFragment;
import com.app.listeners.IcallBack;
import com.app.stryker.R;

import java.util.ArrayList;

/**
 * Created by MIHIR on 1/11/2017.
 */

public class CustomDialog extends Dialog {

    Context context;
    ListView lview_dialog;

    ArrayList<String> orderlist = new ArrayList<>();

    ArrayAdapter adapter;

    OrderHomeFragment orderHomeFragment;


    public IcallBack icallBack;


    public CustomDialog(Context context) {
        super(context);
        this.context = context;


        orderHomeFragment = OrderHomeFragment.getInstance();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_order);

        orderlist.add("Pending");
        orderlist.add("Dispatched");
        orderlist.add("Cancelled by me");
        orderlist.add("Cancelled by Seller");

        init();
        setListener();


    }

    public void init() {

        lview_dialog = (ListView) findViewById(R.id.lview_dialog);

        ArrayAdapter adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, orderlist);


        lview_dialog.setAdapter(adapter);
    }

    public void setListener() {


        lview_dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                switch (position) {
                    case 0:

                        icallBack.result("pending");
                        //orderHomeFragment.setData(0);
                        dismiss();
                        break;
                    case 1:
                        icallBack.result("Dispatched");
                        // orderHomeFragment.setData(1);
                        dismiss();
                        break;
                    case 2:
                        icallBack.result("Cancelled by me");
                        //  orderHomeFragment.setData(2);
                        dismiss();
                        break;
                    case 3:

                        icallBack.result("Cancelled by User");
                        //  orderHomeFragment.setData(3);
                        dismiss();
                        break;
                }


            }
        });
    }


}