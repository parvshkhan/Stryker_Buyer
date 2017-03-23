package com.app.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.adapters.OrderExpandableAdapterCancelledMe;
import com.app.adapters.OrderExpandableAdapterCancelledSeller;
import com.app.adapters.OrderExpandableAdapterDispatched;
import com.app.adapters.OrderExpandableAdapterPending;
import com.app.adapters.OrderHomeListAdapter;
import com.app.Dialog.CustomDialog;
import com.app.gpstracker.GPSTracker;
import com.app.jsoncall.JsonCall;
import com.app.listeners.CancelOrderListener;
import com.app.listeners.IcallBack;
import com.app.model.Category;
import com.app.model.Item;
import com.app.model.ModelProdutsArray;
import com.app.model.OrderHomeModel;
import com.app.model.Pending;
import com.app.model.Pending_model;
import com.app.model.ServiceResponse;
import com.app.stryker.Activity_pending;
import com.app.stryker.R;
import com.app.utill.AppUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderHomeFragment extends android.support.v4.app.Fragment implements OnRefreshListener, CancelOrderListener {

    Context context;

    OrderHomeListAdapter adapter;
    ListView listOrder;

    ArrayList<OrderHomeModel> arlistOrderPending = new ArrayList<OrderHomeModel>();

    ArrayList<OrderHomeModel> arlistOrderDispatched = new ArrayList<OrderHomeModel>();
    ArrayList<OrderHomeModel> arlistOrderCancelBySeller = new ArrayList<OrderHomeModel>();
    ArrayList<OrderHomeModel> arlistOrderCancelByMe = new ArrayList<OrderHomeModel>();

    private int lastExpandedPosition = -1;

    ArrayList<ModelProdutsArray> arrlistchild = new ArrayList<>();
    OrderExpandableAdapterPending orderExpandableAdapter;
    ExpandableListView expandableListView;


    //ArrayList<ModelListHomeItem> listfollowing = new ArrayList<ModelListHomeItem>();
    private Double mLat = 0.0, mLong = 0.0;
    ProgressDialog dialogLoader;
    //SwipeRefreshLayout swipeLayout;
    int flag = 0;
    RelativeLayout rl_pending, rl_dispatched, rl_cancelByMe, rl_cancelBySeller, rl_orderlist;
    TextView txtPending, txtDispatched, txtCancelByMe, txtCancelBySeller, txt_pending;
    boolean isActiveHandler = false;

    ProgressBar progressbar;


    Spinner spinner_order;

    ArrayList<String> orderlist = new ArrayList<>();


    public OrderHomeFragment() {

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootview = inflater.inflate(R.layout.fragment_home_orders,
                container, false);


        return rootview;
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();

        GPSTracker mGPS = new GPSTracker(context);
        if (mGPS.canGetLocation) {
            mLat = mGPS.getLatitude();
            mLong = mGPS.getLongitude();
        }
        isActiveHandler = true;
        init();

        //setData();


        if (AppUtil.isNetworkAvailable(context)) {
            //dialogLoader = ProgressDialog.show(context, "", "Please wait...");
            progressbar.setVisibility(View.VISIBLE);
            HomeDataTask task = new HomeDataTask();
            task.execute(new String[]{AppUtil.getUserId(context), AppUtil.getDeviceId(context)});
        } else {
            AppUtil.showCustomToast(context,
                    "please check your internet connection");

        }

        orderlist.add("Pending");
        orderlist.add("Dispatched");
        orderlist.add("Cancelled by me");
        orderlist.add("Cancelled by Seller");

        orderExpandableAdapter = new OrderExpandableAdapterPending(getActivity(),
                arlistOrderPending, pending_modelsArrraylist);

        expandableListView.setAdapter(orderExpandableAdapter);

/*

		orderExpandableAdapter = new OrderExpandableAdapterPending(context, arlistOrderPending,  );
		expandableListView.setAdapter(orderExpandableAdapter);

*/

    }


    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        isActiveHandler = false;
    }


    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        isActiveHandler = false;
    }

    public void RefreshOrder() {
        if (isActiveHandler) {
            new Handler().postDelayed(new Runnable() {


                public void run() {
                    // TODO Auto-generated method stub
                    if (AppUtil.isNetworkAvailable(context)) {
                        //dialogLoader = ProgressDialog.show(context, "", "Please wait...");

                        HomeDataTask task = new HomeDataTask();
                        task.execute(new String[]{AppUtil.getUserId(context), AppUtil.getDeviceId(context)});
                    }
                }
            }, 3 * 60 * 1000);
        }
    }

    private void init() {
        //pb=(com.github.rahatarmanahmed.cpv.CircularProgressView) getView().findViewById(R.id.progressBar1);
        listOrder = (ListView) getView().findViewById(R.id.lv_order_details);


        //	txt_spinner_order = (TextView)getView().findViewById(R.id.txt_spinner_order);


        spinner_order = (Spinner) getView().findViewById(R.id.spinner_order);

        txt_pending = (TextView) getView().findViewById(R.id.txt_pending);

        //txt_pending.setText();

        rl_orderlist = (RelativeLayout) getView().findViewById(R.id.rl_orderlist);

        expandableListView = (ExpandableListView) getView().findViewById(R.id.expandableListView);

		
	/*	txtPending = (TextView) getView().findViewById(R.id.txt_pending);
        txtDispatched = (TextView) getView().findViewById(R.id.txt_dispatch);
		txtCancelByMe = (TextView) getView().findViewById(R.id.txt_cancel);
		txtCancelBySeller = (TextView) getView().findViewById(R.id.txt_cancel_2);*/
        progressbar = (ProgressBar) getView().findViewById(R.id.progressBar);
        listOrder.setOnItemClickListener(new OnItemClickListener() {


            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                /**perform the action on click of order list*/
                Intent i = new Intent(getActivity(), Activity_pending.class);
                if (flag == 0) {
                    i.putExtra("data", arlistOrderPending.get(position).getCategoryJosnArray().toString());
                    i.putExtra("order_id", arlistOrderPending.get(position).getOrderId());
                    i.putExtra("order_date", arlistOrderPending.get(position).getOrderDate());
                    i.putExtra("store_name", arlistOrderPending.get(position).getStoreName());
                    i.putExtra("order_type", "pending");
                } else if (flag == 1) {
                    i.putExtra("data", arlistOrderDispatched.get(position).getCategoryJosnArray().toString());
                    i.putExtra("order_id", arlistOrderDispatched.get(position).getOrderId());
                    i.putExtra("store_name", arlistOrderDispatched.get(position).getStoreName());
                    i.putExtra("order_date", arlistOrderDispatched.get(position).getOrderDate());
                    i.putExtra("order_type", "dispatched");
                } else if (flag == 2) {
                    i.putExtra("data", arlistOrderCancelByMe.get(position).getCategoryJosnArray().toString());
                    i.putExtra("order_id", arlistOrderCancelByMe.get(position).getOrderId());
                    i.putExtra("store_name", arlistOrderCancelByMe.get(position).getStoreName());
                    i.putExtra("order_date", arlistOrderCancelByMe.get(position).getOrderDate());
                    i.putExtra("order_type", "cancelbyme");
                } else if (flag == 3) {
                    i.putExtra("data", arlistOrderCancelBySeller.get(position).getCategoryJosnArray().toString());
                    i.putExtra("order_id", arlistOrderCancelBySeller.get(position).getOrderId());
                    i.putExtra("store_name", arlistOrderCancelBySeller.get(position).getStoreName());
                    i.putExtra("order_date", arlistOrderCancelBySeller.get(position).getOrderDate());
                    i.putExtra("order_type", "cencelbyseller");
                }
                startActivity(i);
            }
        });


        rl_orderlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomDialog dialog1 = new CustomDialog(context);

                //dialog1.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
                dialog1.setContentView(R.layout.dialog_submit);
                //	dialog1.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);


                dialog1.icallBack = new IcallBack() {
                    @Override
                    public void result(String s) {

                        switch (s) {

                            case "pending":
                                try {
                                    ServiceResponse serviceResponse = AppUtil.getGsonInstance().fromJson(AppUtil.getOrderServiceData(getActivity()), ServiceResponse.class);
                                    if (serviceResponse.commandResult.data.userorders.pending.size() > 0) {
                                        arlistOrderPending.clear();
                                        for (Pending pendingOrder : serviceResponse.commandResult.data.userorders.pending) {
                                            ArrayList<Item> orderItems = new ArrayList<>();
                                            for (Category category : pendingOrder.products.category) {
                                                for (Item item : category.items) {
                                                    orderItems.add(new Item(item.ProductID, item.Quantity, item.TotalCost, item.ProName, item.ProDescription, item.ProCategoryName));
                                                }
                                            }
                                            arlistOrderPending.add(new OrderHomeModel(pendingOrder.OrderID, pendingOrder.StoreID, pendingOrder.StoreName, pendingOrder.OrderTime,
                                                    pendingOrder.OrderDate, "", "", pendingOrder.TotalAmount, "", new JSONArray(), "", pendingOrder.OrderDateTime, orderItems, "Pending"));
                                        }

                                    }
//									getData(0);
//									setData(0);

                                    txt_pending.setText("Pending");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;

                            case "Dispatched":

                                try {
                                    ServiceResponse serviceResponse = AppUtil.getGsonInstance().fromJson(AppUtil.getOrderServiceData(getActivity()), ServiceResponse.class);
                                    if (serviceResponse.commandResult.data.userorders.dipatched.size() > 0) {
                                        arlistOrderPending.clear();
                                        for (Pending pendingOrder : serviceResponse.commandResult.data.userorders.dipatched) {
                                            ArrayList<Item> orderItems = new ArrayList<>();
                                            for (Category category : pendingOrder.products.category) {
                                                for (Item item : category.items) {
                                                    orderItems.add(new Item(item.ProductID, item.Quantity, item.TotalCost, item.ProName, item.ProDescription, item.ProCategoryName));
                                                }
                                            }
                                            arlistOrderPending.add(new OrderHomeModel(pendingOrder.OrderID, pendingOrder.StoreID, pendingOrder.StoreName, pendingOrder.OrderTime,
                                                    pendingOrder.OrderDate, "", "", pendingOrder.TotalAmount, "", new JSONArray(), "", pendingOrder.OrderDateTime, orderItems, "Dispatched"));
                                        }

                                    }
                                    txt_pending.setText("Dispatched");
//									getData(1);
//									setData(1);
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                                break;

                            case "Cancelled by me":
                                try {
                                    ServiceResponse serviceResponse = AppUtil.getGsonInstance().fromJson(AppUtil.getOrderServiceData(getActivity()), ServiceResponse.class);
                                    if (serviceResponse.commandResult.data.userorders.cancelledbyme.size() > 0) {
                                        arlistOrderPending.clear();
                                        for (Pending pendingOrder : serviceResponse.commandResult.data.userorders.cancelledbyme) {
                                            ArrayList<Item> orderItems = new ArrayList<>();
                                            for (Category category : pendingOrder.products.category) {
                                                for (Item item : category.items) {
                                                    orderItems.add(new Item(item.ProductID, item.Quantity, item.TotalCost, item.ProName, item.ProDescription, item.ProCategoryName));
                                                }
                                            }
                                            arlistOrderPending.add(new OrderHomeModel(pendingOrder.OrderID, pendingOrder.StoreID, pendingOrder.StoreName, pendingOrder.OrderTime,
                                                    pendingOrder.OrderDate, "", "", pendingOrder.TotalAmount, "", new JSONArray(), "", pendingOrder.OrderDateTime, orderItems, "CancelledByMe"));
                                        }

                                    }
                                    txt_pending.setText("Cancelled by Me");
//									getData(2);
//									setData(2);
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                                break;

                            case "Cancelled by User":
                                try {
                                    ServiceResponse serviceResponse = AppUtil.getGsonInstance().fromJson(AppUtil.getOrderServiceData(getActivity()), ServiceResponse.class);
                                    if (serviceResponse.commandResult.data.userorders.cancelledbyseller.size() > 0) {
                                        arlistOrderPending.clear();
                                        for (Pending pendingOrder : serviceResponse.commandResult.data.userorders.cancelledbyseller) {
                                            ArrayList<Item> orderItems = new ArrayList<>();
                                            for (Category category : pendingOrder.products.category) {
                                                for (Item item : category.items) {
                                                    orderItems.add(new Item(item.ProductID, item.Quantity, item.TotalCost, item.ProName, item.ProDescription, item.ProCategoryName));
                                                }
                                            }
                                            arlistOrderPending.add(new OrderHomeModel(pendingOrder.OrderID, pendingOrder.StoreID, pendingOrder.StoreName, pendingOrder.OrderTime,
                                                    pendingOrder.OrderDate, "", "", pendingOrder.TotalAmount, "", new JSONArray(), "", pendingOrder.OrderDateTime, orderItems, "CancelledBySeller"));
                                        }

                                    }
                                    txt_pending.setText("Cancelled by Seller");
                                    //getData(3);
//									setData(3);
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                                break;
                        }
                        orderExpandableAdapter.notifyDataSetChanged();
                    }
                };

                dialog1.show();

			/*	dialog1.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
				dialog1.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
				dialog1.show();*/

		/*		dialog1 = new Dialog(context);
				Window window = dialog.getWindow();
				window.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
				dialog.setContentView(R.layout.my_dialog_layout);
				window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.my_custom_header);
*/


            }
        });
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
    }

    ArrayList<Pending_model> pending_modelsArrraylist = new ArrayList<>();
    ArrayList<Pending_model> dispatch_modelsArrraylist = new ArrayList<>();
    ArrayList<Pending_model> cancellbyme_modellist = new ArrayList<>();
    ArrayList<Pending_model> cancelbyselle_modellist = new ArrayList<>();

    private void getData(int pos) {

        JSONArray jsonArray = arlistOrderPending.get(pos).getCategoryJosnArray();


        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jo = jsonArray.getJSONObject(i);
                //pending_adapter.addSectionHeaderItem(jo.getString("name"));

                JSONArray jaItem = jo.getJSONArray("items");
                for (int j = 0; j < jaItem.length(); j++) {
                    JSONObject jo1 = jaItem.getJSONObject(j);
                    Pending_model pendingModel = new Pending_model();

                    pendingModel.setTxt_store_name(jo1.getString("ProName"));
                    pendingModel.setTxt_desc(jo1.getString("ProDescription"));
                    pendingModel.setTxt_count(jo1.getString("Quantity"));
                    pendingModel.setTxt_price(jo1.getString("TotalCost"));
                    try {
                        //totalCost += Integer.parseInt(jo1.getString("TotalCost"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    pending_modelsArrraylist.add(pendingModel);
                }

                Log.i("size", pending_modelsArrraylist.size() + "");

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            JSONArray jsonArraydis = arlistOrderDispatched.get(pos).getCategoryJosnArray();


            for (int j = 0; j < jsonArray.length(); j++) {
                try {
                    JSONObject jo = jsonArraydis.getJSONObject(j);
                    //pending_adapter.addSectionHeaderItem(jo.getString("name"));

                    JSONArray jaItem = jo.getJSONArray("items");
                    for (int k = 0; k < jaItem.length(); k++) {
                        JSONObject jo1 = jaItem.getJSONObject(k);
                        Pending_model pendingModel = new Pending_model();

                        pendingModel.setTxt_store_name(jo1.getString("ProName"));
                        pendingModel.setTxt_desc(jo1.getString("ProDescription"));
                        pendingModel.setTxt_count(jo1.getString("Quantity"));
                        pendingModel.setTxt_price(jo1.getString("TotalCost"));
                        try {
                            //totalCost += Integer.parseInt(jo1.getString("TotalCost"));
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                        dispatch_modelsArrraylist.add(pendingModel);
                    }

                    Log.i("size", dispatch_modelsArrraylist.size() + "");

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }

        JSONArray jsonArraycnme = arlistOrderDispatched.get(pos).getCategoryJosnArray();


        for (int j = 0; j < jsonArray.length(); j++) {
            try {
                JSONObject jo = jsonArraycnme.getJSONObject(j);
                //pending_adapter.addSectionHeaderItem(jo.getString("name"));

                JSONArray jaItem = jo.getJSONArray("items");
                for (int k = 0; k < jaItem.length(); k++) {
                    JSONObject jo1 = jaItem.getJSONObject(k);
                    Pending_model pendingModel = new Pending_model();

                    pendingModel.setTxt_store_name(jo1.getString("ProName"));
                    pendingModel.setTxt_desc(jo1.getString("ProDescription"));
                    pendingModel.setTxt_count(jo1.getString("Quantity"));
                    pendingModel.setTxt_price(jo1.getString("TotalCost"));
                    try {
                        //totalCost += Integer.parseInt(jo1.getString("TotalCost"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    dispatch_modelsArrraylist.add(pendingModel);
                }

                Log.i("size", dispatch_modelsArrraylist.size() + "");

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }


    }


    public void setData(int tabPosition) throws JSONException {

        if (tabPosition == 0) {

			/*adapter = new OrderHomeListAdapter(context,
					R.layout.row_order_list, arlistOrderPending, this, "pending");
*/

            OrderExpandableAdapterPending orderExpandableAdapterPending = new OrderExpandableAdapterPending(getActivity(),
                    arlistOrderPending, pending_modelsArrraylist);

            expandableListView.setAdapter(orderExpandableAdapterPending);


        } else if (tabPosition == 1) {
            //txtDispatched.setTextColor(getResources().getColor(R.color.selected_tab_red_color));


            OrderExpandableAdapterDispatched orderExpandableAdapterDispatched = new OrderExpandableAdapterDispatched(getActivity(), arlistOrderDispatched,
                    dispatch_modelsArrraylist);

            expandableListView.setAdapter(orderExpandableAdapterDispatched);


        } else if (tabPosition == 2) {
            //txtCancelByMe.setTextColor(getResources().getColor(R.color.selected_tab_red_color));
			/*adapter = new OrderHomeListAdapter(context,
					R.layout.row_order_list, arlistOrderCancelByMe, this,"cancelbyme");*/

            OrderExpandableAdapterCancelledMe orderExpandableAdapterCancelledMe = new OrderExpandableAdapterCancelledMe(getActivity(), arlistOrderCancelByMe,
                    cancellbyme_modellist);
            expandableListView.setAdapter(orderExpandableAdapterCancelledMe);

        } else if (tabPosition == 3) {
            // txtCancelBySeller.setTextColor(getResources().getColor(R.color.selected_tab_red_color));
			/*adapter = new OrderHomeListAdapter(context,
					R.layout.row_order_list, arlistOrderCancelBySeller,this,"cancelbyseller");*/

            OrderExpandableAdapterCancelledSeller orderExpandableAdapterCancelledSeller = new OrderExpandableAdapterCancelledSeller(getActivity(), arlistOrderCancelBySeller,
                    cancelbyselle_modellist);

            expandableListView.setAdapter(orderExpandableAdapterCancelledSeller);


        }
        listOrder.setAdapter(adapter);

    }


    public void onRefresh() {
        // TODO Auto-generated method stub

    }

    /**
     * home data asyntask
     */
    private class HomeDataTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.homeData(urls[0], urls[1],
                        context.getResources().getString(R.string.home_data_url));
                //Log.e("Home Data RRESPONSE", "" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            JSONObject jObject;
//			Log.i("HometaskResponse", result);
            try {
                if (result != null) {
                    ServiceResponse serviceResponse = AppUtil.getGsonInstance().fromJson(result, ServiceResponse.class);
                    if (serviceResponse.commandResult.success == 1) {
                        AppUtil.setOrderServiceData(context, result);
                        if (serviceResponse.commandResult.data.userorders.pending.size() > 0) {
                            arlistOrderPending.clear();
                            for (Pending pendingOrder : serviceResponse.commandResult.data.userorders.pending) {
                                ArrayList<Item> orderItems = new ArrayList<>();
                                for (Category category : pendingOrder.products.category) {
                                    for (Item item : category.items) {
                                        orderItems.add(new Item(item.ProductID, item.Quantity, item.TotalCost, item.ProName, item.ProDescription, item.ProCategoryName));
                                    }
                                }
                                arlistOrderPending.add(new OrderHomeModel(pendingOrder.OrderID, pendingOrder.StoreID, pendingOrder.StoreName, pendingOrder.OrderTime,
                                        pendingOrder.OrderDate, "", "", pendingOrder.TotalAmount, "", new JSONArray(), "", pendingOrder.OrderDateTime, orderItems, "Pending"));
                            }

                        }
                        orderExpandableAdapter.notifyDataSetChanged();
                        txt_pending.setText("Pending");
                    }


                } else {
                    //AppUtil.showCustomToast(context,"please check your internet connection");
                }

                RefreshOrder();
                progressbar.setVisibility(View.GONE);
                if (dialogLoader != null)
                    dialogLoader.cancel();
            } catch (Exception e) {
                e.printStackTrace();
                progressbar.setVisibility(View.GONE);
                if (dialogLoader != null)
                    dialogLoader.cancel();
                RefreshOrder();

            }
        }
    }


    public void cancelOrderListPosition(int posi) {
        // TODO Auto-generated method stub
        /**cancel oder task*/
        final String orderId = arlistOrderPending.get(posi).getOrderId();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure want to cancel the order?")
                .setCancelable(false)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /**call asynTask */
                        if (AppUtil.isNetworkAvailable(context)) {
                            dialogLoader = ProgressDialog.show(context, "", "please wait..");
                            CancelOrderTask task = new CancelOrderTask();
                            task.execute(new String[]{orderId, "3", AppUtil.getUserId(context), AppUtil.getDeviceId(context)});
                        } else {
                            AppUtil.showCustomToast(context, "please check your internet");
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
     * cancel order asyntask
     */
    private class CancelOrderTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.cancelOrder(urls[0], urls[1], urls[2], urls[3],
                        context.getResources().getString(R.string.cancel_order_status));
                Log.e("cancel order RRESPONSE", "" + response);
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
                        //JSONObject job1 = job.getJSONObject("data");
                        if (AppUtil.isNetworkAvailable(context)) {
                            //dialog = ProgressDialog.show(context, "", "Please wait...");
                            HomeDataTask task = new HomeDataTask();
                            task.execute(new String[]{AppUtil.getUserId(context), AppUtil.getDeviceId(context)});
                        } else {
                            AppUtil.showCustomToast(context,
                                    "please check your internet connection");

                        }

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

    private static OrderHomeFragment orderHomeFragment = null;

    public static OrderHomeFragment getInstance() {

        if (orderHomeFragment == null)

            return new OrderHomeFragment();


        else

            return orderHomeFragment;

    }


	/*private void registerUser(){

		StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.home_data_url),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();
						Log.i("result", response);
						showJson(response);
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
					}
				}){
			@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();

				params.put("device_id", AppUtil.getUserId(context) );
				params.put("user_id", AppUtil.getDeviceId(context));

				return params;
			}

		};

		RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
		requestQueue.add(stringRequest);
	}

	private void showJson(String response) {

	}*/

}
