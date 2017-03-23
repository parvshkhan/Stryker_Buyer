package com.app.stryker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.adapters.TabsPagerProductListAdapter;
import com.app.fragments.ChatListFragment;
import com.app.fragments.ProductListFragment;
import com.app.jsoncall.JsonCall;
import com.app.utill.AppUtil;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.sample.chat.core.Chat;
import com.quickblox.sample.chat.core.ChatService;
import com.quickblox.sample.chat.ui.activities.BaseActivity;
import com.quickblox.sample.chat.ui.activities.ChatActivity;
import com.quickblox.sample.chat.ui.adapters.ChatAdapter;
import com.quickblox.sample.chat.ui.adapters.DialogsAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends BaseActivity {

    Context context;
    private ViewPager viewPager;
    private TabsPagerProductListAdapter mAdapter;
    RelativeLayout tab1StoreFront, tab2ProductList, tab3ChatList;
    private Double mLat = 0.0, mLong = 0.0;
    TextView txtTabstore, txtTabProductList, txtChatList;
    ImageView imgSetting, imgSearch, imgAddStore;
    ProgressDialog dialog;
    android.support.v4.app.FragmentTransaction ft;
    String store_id = "", from = "", seller_id = "", sellerChatId = "";

    public static String ProductListData = "";

    boolean bDataDownload = false;

    private ChatAdapter adapter;
    private ListView messagesContainer;
    private Chat chat;
    Bundle bundle;
    private static final String TAG = ProductListActivity.class.getSimpleName();
    static QBDialog selectedDialog;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //ProductListFragment plf= new ProductListFragment();
        requestCode &= 0xffff;
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        context = this;

        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("store_id")) {
                store_id = b.getString("store_id");

            }
            if (b.containsKey("from")) {
                from = b.getString("from");
            }
            if (b.containsKey("name")) {
                ((TextView) findViewById(R.id.txt_back_storename)).setText(b.getString("name"));
            }
            if (b.containsKey("seller_id")) {
                seller_id = b.getString("seller_id");

            }
            if (b.containsKey("sellerChat_id")) {
                sellerChatId = b.getString("sellerChat_id");

            }
        }

        init();
        setListener();

    }


    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();


        if (from.equalsIgnoreCase("chat")) {
            viewPager.setCurrentItem(2);
            setTabSelection(2);
        } else if (from.equalsIgnoreCase("productlist")) {
            viewPager.setCurrentItem(1);
            setTabSelection(1);
        } else {
            viewPager.setCurrentItem(0);
            setTabSelection(0);
        }

        Log.e("sellerid------->", seller_id);
        //setSellerId(seller_id);
        if (!store_id.equalsIgnoreCase(AppUtil.getpreStoreId(context))) {
            ProductListFragment.arlistProduct.clear();
            ProductListFragment.arlistHotProducts.clear();
            ProductListFragment.arlistMyCart.clear();
            Log.e("DATA CLEARED", "****");

        }

        AppUtil.setpreStoreId(context, store_id);
    }


    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
/*
        init();
		setListener();
		Log.e("sellerid------->",seller_id);
		//setSellerId(seller_id);
		if (!store_id.equalsIgnoreCase(AppUtil.getpreStoreId(context))) {
			ProductListFragment.arlistProduct.clear();
			ProductListFragment.arlistHotProducts.clear();
			ProductListFragment.arlistMyCart.clear();
			Log.e("DATA CLEARED", "****");
			
		}

		AppUtil.setpreStoreId(context, store_id);*/
    }


    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        try {
            AppUtil.onKeyBoardDown(context);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public String StoreId() {
        return store_id;
    }

    public void init() {
        tab1StoreFront = (RelativeLayout) findViewById(R.id.rl_storefront);
        tab2ProductList = (RelativeLayout) findViewById(R.id.rl_productlist);
        tab3ChatList = (RelativeLayout) findViewById(R.id.rl_chat);

        viewPager = (ViewPager) findViewById(R.id.pager_home_item);
        imgSetting = (ImageView) findViewById(R.id.img_settings);
        imgSearch = (ImageView) findViewById(R.id.img_search);
        imgAddStore = (ImageView) findViewById(R.id.img_add1);

        mAdapter = new TabsPagerProductListAdapter(
                this.getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                setTabSelection(position);
            }


            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }


            public void onPageScrollStateChanged(int arg0) {
            }
        });
		/*if (from.equalsIgnoreCase("chat")) {
			viewPager.setCurrentItem(2);
			setTabSelection(2);
		} else if (from.equalsIgnoreCase("productlist")) {
			viewPager.setCurrentItem(1);
			setTabSelection(1);
		} else {
			viewPager.setCurrentItem(0);
			setTabSelection(0);
		}*/
    }

    private void setTabSelection(int posi) {
        switch (posi) {
            case 0:
                tab1StoreFront.setBackgroundResource(R.drawable.select_03);
                tab2ProductList.setBackgroundResource(R.drawable.deselect);
                tab3ChatList.setBackgroundResource(R.drawable.deselect);
                break;
            case 1:
                tab1StoreFront.setBackgroundResource(R.drawable.deselect);
                tab2ProductList.setBackgroundResource(R.drawable.select_03);
                tab3ChatList.setBackgroundResource(R.drawable.deselect);
                break;

            case 2:

                tab1StoreFront.setBackgroundResource(R.drawable.deselect);
                tab2ProductList.setBackgroundResource(R.drawable.deselect);
                tab3ChatList.setBackgroundResource(R.drawable.select_03);
                break;

        }
    }

    public void setListener() {
        tab1StoreFront.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                viewPager.setCurrentItem(0);
            }
        });
        tab2ProductList.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                viewPager.setCurrentItem(1);
            }
        });
        tab3ChatList.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                viewPager.setCurrentItem(2);
            }
        });

        imgSetting.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(ProductListActivity.this,
                        SettingActivity.class);
                startActivity(i);
            }
        });

        imgSearch.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(ProductListActivity.this,
                        SearchStoreToAddActivity.class);
                startActivity(i);
            }
        });
        ((ImageView) findViewById(R.id.img_add1))
                .setOnClickListener(new OnClickListener() {


                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                });
        ((TextView) findViewById(R.id.txt_back_storename))
                .setOnClickListener(new OnClickListener() {


                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        AppUtil.onKeyBoardDown(context);
                        finish();
                    }
                });
    }

    /**
     * product list asyntask
     */
    private class ProductListTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String response = "";
            try {
                JsonCall obj = new JsonCall();
                response = obj.getProductList(urls[0], context.getResources()
                        .getString(R.string.product_list_url));
                Log.e("product list data RRESPONSE", "" + response);
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
                        JSONObject jo = job.getJSONObject("data");
                        ProductListData = jo.toString();
                        bDataDownload = true;

                        // Bundle b = new Bundle();
                        // b.putString("data", jo.toString());
                        // StoresHomeFragment Frag = new StoresHomeFragment();
                        // Frag.setArguments(b);

                    } else {
                        AppUtil.showCustomToast(context, message);
                    }

                } else {
                    AppUtil.showCustomToast(context,
                            "please check your internet connection");
                }
                if (dialog != null)
                    dialog.cancel();
            } catch (Exception e) {
                e.printStackTrace();
                if (dialog != null)
                    dialog.cancel();

            }
        }
    }

    /**
     * chat
     */
    private void getDialogs() {
        //   progressBar.setVisibility(View.VISIBLE);

        // Get dialogs
        //
        ChatService.getInstance().getDialogs(new QBEntityCallbackImpl() {

            public void onSuccess(Object object, Bundle bundle) {
                //  progressBar.setVisibility(View.GONE);

                final ArrayList<QBDialog> dialogs = (ArrayList<QBDialog>) object;

                // build list view
                //
                buildListView(dialogs);
            }


            public void onError(List errors) {
                // progressBar.setVisibility(View.GONE);

                AlertDialog.Builder dialog = new AlertDialog.Builder(ProductListActivity.this);
                dialog.setMessage("get dialogs errors: " + errors).create().show();
            }
        });
    }

    public void buildListView(List<QBDialog> dialogs) {
        final DialogsAdapter adapter = new DialogsAdapter(dialogs, ProductListActivity.this);


        selectedDialog = (QBDialog) adapter.getItem(0);
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable(ChatActivity.EXTRA_DIALOG, selectedDialog);
        bundle = bundle1;

    }

    public QBDialog getSelectedDialog() {

        return selectedDialog;
    }

    public Bundle getBundleDialog() {
        return bundle;
    }


    public void showMessage(QBChatMessage message) {

        Log.e("", "MESSAGE_____________>" + message.getBody());
        //	FragmentManager manager = getSupportFragmentManager();
        //	Fragment fragment = manager.findFragmentById(R.id.rl_chat);
        //((ChatListFragment) fragment).showMessage(message);

        //ChatListFragment frg = (ChatListFragment) getSupportFragmentManager().findFragmentById(R.id.rl_chat);
        //frg.showMessage1(message);

        Fragment CurrFrag = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager_home_item +
                ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 2 && CurrFrag != null) {
            ((ChatListFragment) CurrFrag).showMessage1(message);
        }


        //ChatListFragment frag = (ChatListFragment) getFragmentManager().findFragmentById(R.id.rl_chat);

        //ChatListFragment fragment = (ChatListFragment) getFragmentManager().findFragmentById(R.id.rl_chat);
        //	fragment.<specific_function_name>();
 /*	adapter.add(message);

     runOnUiThread(new Runnable() {
         
         public void run() {
             adapter.notifyDataSetChanged();
             scrollDown();
         }
     });*/
    }


    public String getSellerChatId() {

        return sellerChatId;
    }

    public String getSellerId() {


        return seller_id;


        //ChatListFragment frag = (ChatListFragment) getFragmentManager().findFragmentById(R.id.rl_chat);

        //ChatListFragment fragment = (ChatListFragment) getFragmentManager().findFragmentById(R.id.rl_chat);
        //	fragment.<specific_function_name>();
	 /*	adapter.add(message);

	     runOnUiThread(new Runnable() {
	         
	         public void run() {
	             adapter.notifyDataSetChanged();
	             scrollDown();
	         }
	     });*/
    }


    private void scrollDown() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }
}
