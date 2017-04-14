package com.app.stryker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.app.listeners.ICountCallBack;
import com.app.model.ContactModelNew;

import java.util.List;

public class MyArrayAdapterContact extends BaseAdapter {
    Context context;
    int res;
    List<ContactModelNew> contactModelsList;
    LayoutInflater layoutInflater;
    public static int count = 0;
    private TextView textViewCount;
    ICountCallBack icallBack;
    TextView tvContact;


    public MyArrayAdapterContact(Context context, int res, List<ContactModelNew> contactModelsList ,TextView tvContact) {
        this.context = context;
        this.res = res;
        this.contactModelsList = contactModelsList;

        this.tvContact = tvContact;

    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        TextView tvname=null;
        TextView txtInitial=null;
        CheckBox checkBox=null;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(res, null);
            textViewCount = (TextView) parent.findViewById(R.id.count);
            tvname = (TextView) convertView.findViewById(R.id.tvname);
            txtInitial = (TextView) convertView.findViewById(R.id.txt_initial);
            checkBox = (CheckBox) convertView.findViewById(R.id.chkboccontact);






        tvname.setText(contactModelsList.get(position).getName());
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
        tvname.setTag(position);
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


  /*  static class Holder{

        TextView tvname;
        TextView txtInitial;
        CheckBox chkAll;


    }*/

    @Override
    public Object getItem(int position) {
        return contactModelsList.get(position);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return contactModelsList.size();
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return contactModelsList.indexOf(getItem(arg0));
    }
}
