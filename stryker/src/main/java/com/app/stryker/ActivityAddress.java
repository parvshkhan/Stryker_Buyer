package com.app.stryker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapters.AdapterAddAddress;
import com.app.fragments.ProductListFragment;
import com.app.jsoncall.JsonCall;
import com.app.listeners.DeleteAddressListener;
import com.app.model.DbModel.ServiceResponse;
import com.app.model.ModelAddress;
import com.app.utill.AppUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Inflac on 14-10-2015.
 */
public class ActivityAddress extends Activity implements DeleteAddressListener {

    ListView addresslistview;
    List addresslist = new ArrayList();
    Context context;
    TextView txt_new_address;
    AdapterAddAddress adapter_add_address;
    Button bton_place_your_order;
    ImageView img_add1;

    JSONObject orderJson, addressJson;
    JSONArray productJsonArray;
    ProgressDialog dialogLoader;

    String storeId = "";

    int posiselect = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        context = this;

        init();
        setListener();
        setAddressList();

        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("store_id")) {
                storeId = b.getString("store_id");
            }
        }

    }

    public void setAddressList() {
        addresslist.clear();
        try {
            JSONArray ja = new JSONArray(AppUtil.getUserAddress(context));
            for (int i = 0; i < ja.length(); i++) {
                JSONObject job = ja.getJSONObject(i);
                addresslist.add(new ModelAddress(job.getString("ID"), job.getString("Address1"), job.getString("Address2"), job.getString("Country"),
                        job.getString("State"), job.getString("City"), job.getString("ZipCode")));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        adapter_add_address = new AdapterAddAddress(context, R.layout.row_address, addresslist);
        addresslistview.setAdapter(adapter_add_address);

    }

    public void init() {
        addresslistview = (ListView) findViewById(R.id.lv_store_details);
        txt_new_address = (TextView) findViewById(R.id.txt_new_address);
        bton_place_your_order = (Button) findViewById(R.id.bton_place_your_order);
        img_add1 = (ImageView) findViewById(R.id.img_add1);
    }

    public boolean isPermissible() {
        boolean flag = true;
        float amount = 0.0f;
        float totalBill = ProductListFragment.calculateTotalBill();
        String value = AppUtil.getMinOrderValue(context).trim();
        if (!(value.equalsIgnoreCase("") || value.equals(null))) {
            try {
                amount = Float.parseFloat(value);
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (amount == 0.0) {
            flag = true;
        } else if (totalBill < amount) {
            flag = false;
        }

        return flag;
    }

    public void setListener() {
        img_add1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });

        bton_place_your_order.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                /** checkout service here*/

                if (AppUtil.getLogin(context)) {
                    if (isPermissible()) {
                        generatePlaceOrder();
                        if (!(productJsonArray.length() > 0)) {
                            Toast.makeText(
                                    context,
                                    "please add products"
                                            + " in your cart to place order.",
                                    2000).show();
                        } else if (!(addresslist.size() > 0)) {
                            Toast.makeText(
                                    context,
                                    "please add new address first before checkout.",
                                    2000).show();
                        } else if (AppUtil.isNetworkAvailable(context)) {
                            dialogLoader = ProgressDialog.show(context, "",
                                    "please wait..");
                            CheckOutTask task = new CheckOutTask();
                            task.execute(new String[]{orderJson.toString(),
                                    addressJson.toString(),
                                    productJsonArray.toString()});
                        } else {
                            AppUtil.showCustomToast(context,
                                    "please check your internet connection");
                        }
                    } else {
                        AppUtil.showCustomToast(context,
                                "Sorry, minimum order should be Rs. "
                                        + AppUtil.getMinOrderValue(context)
                                        .trim());
                    }
                } else {
                    AppUtil.showCustomToast(context, "Please Login/Register for checkout.");
                    Intent in = new Intent(ActivityAddress.this, LoginActivity.class);
                    in.putExtra("from", "checkout");
                    startActivity(in);
                }

            }
        });
        addresslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                posiselect = position;

                ModelAddress mod = (ModelAddress) addresslist.get(position);
                mod.getAddress_line1();
                adapter_add_address.imageselector(position);
                adapter_add_address.notifyDataSetChanged();
            }
        });

        txt_new_address.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (AppUtil.getLogin(context)) {
                    Intent in = new Intent(ActivityAddress.this, ActivityNewAddress.class);
                    startActivityForResult(in, 92);
                } else {
                    AppUtil.showCustomToast(context, "Please Login/Register to add new address.");
                    Intent in = new Intent(ActivityAddress.this, LoginActivity.class);
                    in.putExtra("from", "checkout");
                    startActivity(in);
                }

            }
        });
    }

    public void generatePlaceOrder() {
        //productJsonArray
        productJsonArray = new JSONArray();
        if (ProductListFragment.arlistProduct.size() > 0) {
            for (int i = 0; i < ProductListFragment.arlistProduct.size(); i++) {
                for (int j = 0; j < ProductListFragment.arlistProduct.get(i).getArrProductsByCategory().size(); j++) {
                    if (ProductListFragment.arlistProduct.get(i).getArrProductsByCategory().get(j).getCount() > 0) {
                        try {
                            JSONObject joProduct = new JSONObject();
                            joProduct.put("product_id", ProductListFragment.arlistProduct.get(i).getArrProductsByCategory().get(j).getProdId());
                            joProduct.put("quantity", ProductListFragment.arlistProduct.get(i).getArrProductsByCategory().get(j).getCount());
                            joProduct.put("total_price", ProductListFragment.arlistProduct.get(i).getArrProductsByCategory().get(j).getProductCost());
                            //ProductListFragment.arlistProduct.get(i).getArrProductsByCategory().get(j).getCount();
                            //ProductListFragment.arlistProduct.get(i).getArrProductsByCategory().get(j).getProdId();
                            //ProductListFragment.arlistProduct.get(i).getArrProductsByCategory().get(j).getProductCost();
                            //{"product_id":"1", "quantity":"2", "total_price":"40"}

                            productJsonArray.put(joProduct);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

            }
        }

        /**hot product*/
        if (ProductListFragment.arlistHotProducts.size() > 0) {
            for (int i = 0; i < ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory().size(); i++) {
                if (ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory().get(i).getCount() > 0) {
                    try {
                        JSONObject joProduct = new JSONObject();
                        joProduct.put("product_id", ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory().get(i).getProdId());
                        joProduct.put("quantity", ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory().get(i).getCount());
                        joProduct.put("total_price", ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory().get(i).getProductCost());
                        //ProductListFragment.arlistProduct.get(i).getArrProductsByCategory().get(j).getCount();
                        //ProductListFragment.arlistProduct.get(i).getArrProductsByCategory().get(j).getProdId();
                        //ProductListFragment.arlistProduct.get(i).getArrProductsByCategory().get(j).getProductCost();
                        //{"product_id":"1", "quantity":"2", "total_price":"40"}

                        productJsonArray.put(joProduct);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }


            }
        }

        /** order JSOn*/
        //{"user_id":"3", "store_id":"5", "total_amount":"200","order_datetime": "2015-10-12 04:11:22","user_address_id": "0"  }';
        orderJson = new JSONObject();
        try {
            if (addresslist.size() > 0) {
                ModelAddress mod = (ModelAddress) addresslist.get(posiselect);
                orderJson.put("user_id", AppUtil.getUserId(context));
                orderJson.put("store_id", storeId);
                orderJson.put("total_amount", ProductListFragment.calculateTotalBill());
                //orderJson.put("order_datetime", DateFormat.getDateTimeInstance().format(new Date()));
                orderJson.put("user_address_id", mod.getid());
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /**address json */
        //'{"address_1":"H23", "address_2":"Sector 63", "country": "1","state": "1","city":"1","zipCode": "201301" ,"phone": "9555436989"}';

        addressJson = new JSONObject();
        try {
            if (addresslist.size() > 0) {
                ModelAddress mod = (ModelAddress) addresslist.get(addresslist.size() - 1);
                addressJson.put("address_1", mod.getAddress_line1());
                addressJson.put("address_2", mod.getAddress_line2());
                addressJson.put("country", mod.getCountry());
                addressJson.put("state", mod.getState());
                addressJson.put("city", mod.getCity());
                addressJson.put("zipCode", mod.getPin());
                addressJson.put("phone", "");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 92 && resultCode == 110) {
            addresslist.clear();

            try {
                JSONArray ja = new JSONArray(data.getStringExtra("data"));
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject job = ja.getJSONObject(i);
                    addresslist.add(new ModelAddress(job.getString("ID"), job.getString("Address1"), job.getString("Address2"),
                            job.getString("Country"), job.getString("State"), job.getString("City"), job.getString("ZipCode")));

                    posiselect = addresslist.size() - 1;
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            adapter_add_address.imageselector(addresslist.size() - 1);
            adapter_add_address.notifyDataSetChanged();

        }
    }

    /**
     * checkout asyntask
     */
    private class CheckOutTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        // email, password, phone, login_type, socialId, latitude, longitude,
        // name, age, gender, location, deviceToken
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.checkOut(urls[0], urls[1], urls[2],
                        context.getResources().getString(R.string.checkout_url));
                Log.e("checkout RRESPONSE", "" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            JSONObject jObject;
            try {
                if (result != null) {
                    com.app.model.DbModel.ServiceResponse serviceResponse = new Gson().fromJson(result, ServiceResponse.class);
                    if (serviceResponse != null && serviceResponse.commandResult != null) {

                        if (serviceResponse.commandResult.success.equalsIgnoreCase("1")) {

                            AppUtil.showCustomToast(context, serviceResponse.commandResult.message);
                            clearCartData();

                        } else {
                            AppUtil.showCustomToast(context, serviceResponse.commandResult.message);
                        }

                    }


				/*	jObject = new JSONObject(result);
                    JSONObject job = jObject.getJSONObject("commandResult");
					int success = job.getInt("success");
					String message = job.getString("message");
					if (success == 1) {						
						AppUtil.showCustomToast(context, message);
						
						//finish();						
						clearCartData();
					} else {
						AppUtil.showCustomToast(context, message);
					}*/

                } else {
                    AppUtil.showCustomToast(context,
                            "please check your internet connection");
                }
                if (dialogLoader != null)
                    dialogLoader.cancel();
            } catch (Exception e) {
                e.printStackTrace();
                if (dialogLoader != null)
                    dialogLoader.cancel();

            }
        }
    }

    public void clearCartData() {
        for (int i = 0; i < ProductListFragment.arlistProduct.size(); i++) {
            for (int j = 0; j < ProductListFragment.arlistProduct.get(i).getArrProductsByCategory().size(); j++) {
                ProductListFragment.arlistProduct.get(i).getArrProductsByCategory().get(j).setCount(0);
            }
        }
        if (ProductListFragment.arlistHotProducts.size() > 0) {
            for (int i = 0; i < ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory().size(); i++) {
                ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory().get(i).setCount(0);
            }
        }

        Intent i = new Intent(ActivityAddress.this, HomeActivity.class);
        i.putExtra("from", "cart");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }


    public void deleteAddress(int position) {
        // TODO Auto-generated method stub
        /**call the delete task */
        final int posi = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure ? want to delete address.")
                .setCancelable(false)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (AppUtil.isNetworkAvailable(context)) {
                            dialogLoader = ProgressDialog.show(context, "", "please wait..");
                            ModelAddress mod = (ModelAddress) addresslist.get(posi);
                            DeleteAddressTask task = new DeleteAddressTask(posi);
                            task.execute(new String[]{AppUtil.getUserId(context), mod.getid()});
                        } else {
                            AppUtil.showCustomToast(context, "lLease check your internet");
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();


    }

    /**
     * delete address asyntask
     */
    private class DeleteAddressTask extends AsyncTask<String, Void, String> {
        int position;

        public DeleteAddressTask(int position) {
            this.position = position;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.deleteAddress(urls[0], urls[1],
                        context.getResources().getString(R.string.delete_address_url));
                Log.e("delete address RRESPONSE", "" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            JSONObject jObject;
            try {
                if (result != null) {
                    jObject = new JSONObject(result);
                    JSONObject job = jObject.getJSONObject("commandResult");
                    int success = job.getInt("success");
                    String message = job.getString("message");
                    if (success == 1) {
                        AppUtil.showCustomToast(context, message);
                        addresslist.remove(position);
                        adapter_add_address.notifyDataSetChanged();
                    } else {
                        AppUtil.showCustomToast(context, message);
                    }

                } else {
                    AppUtil.showCustomToast(context,
                            "please check your internet connection");
                }
                if (dialogLoader != null)
                    dialogLoader.cancel();
            } catch (Exception e) {
                e.printStackTrace();
                if (dialogLoader != null)
                    dialogLoader.cancel();

            }
        }
    }

}
