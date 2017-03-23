package com.app.stryker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.app.listeners.ICountCallBack;
import com.app.model.ContactModelNew;

import java.util.List;

public class MyArrayAdapterContact extends ArrayAdapter {
    Context context;
    int res;
    List<ContactModelNew> contactModelsList;
    LayoutInflater layoutInflater;
    public static int count = 0;
    private TextView textViewCount;
    ICountCallBack icallBack;


    public MyArrayAdapterContact(Context context, int res, List<ContactModelNew> contactModelsList) {
        super(context, res, contactModelsList);

        this.context = context;
        this.res = res;
        this.contactModelsList = contactModelsList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {


        convertView = layoutInflater.inflate(res, null);


        textViewCount = (TextView) parent.findViewById(R.id.count);


        TextView textView = (TextView) convertView.findViewById(R.id.tvname);
        TextView txtInitial = (TextView) convertView.findViewById(R.id.txt_initial);

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.chkboccontact);

        textView.setText(contactModelsList.get(position).getName());

        String value = null;

        try {
            value = getInitial(contactModelsList.get(position).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try
        {
            txtInitial.setText(value.charAt(0)+"");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        if(contactModelsList.get(position).getPhonNum().length()>=10)
        {
            checkBox.setVisibility(View.INVISIBLE);
        }


        if (contactModelsList.get(position).isExist == 0) {
            checkBox.setVisibility(View.VISIBLE);

            ((TextView) convertView.findViewById(R.id.mrktapp)).setVisibility(View.INVISIBLE);
        } else if (contactModelsList.get(position).isExist == 2) {
            checkBox.setVisibility(View.INVISIBLE);
            ((TextView) convertView.findViewById(R.id.mrktapp)).setVisibility(View.INVISIBLE);
        } else {
            checkBox.setVisibility(View.INVISIBLE);

            ((TextView) convertView.findViewById(R.id.mrktapp)).setVisibility(View.VISIBLE);

        }


        checkBox.setTag(position);
        textView.setTag(position);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int id = Integer.parseInt(buttonView.getTag().toString());
                if (isChecked) {
                    count++;
                    contactModelsList.get(id).isChecked = true;
                    icallBack.onCount(count);
                    Log.i("name",contactModelsList.get(position).getName());
                } else {
                    count--;
                    contactModelsList.get(id).isChecked = false;
                    icallBack.onCount(count);
                }

            }
        });

        return convertView;
    }

    private String getInitial(String contactName) throws Exception {
        String value = "";
        String c[] = contactName.split(" ");
        if (c.length > 0)
            for (int i = 0; i < c.length; i++) {
                value = value + c[i].charAt(0) + "";
            }

        return value;
    }
}
