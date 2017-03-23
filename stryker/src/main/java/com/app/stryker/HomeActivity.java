package com.app.stryker;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.adapters.TabsPagerAdapter;
import com.app.fragments.StoresHomeFragment.onSomeEventListener;
import com.app.utill.AppUtil;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.sample.chat.core.ChatService;
import com.quickblox.sample.chat.ui.activities.BaseActivity;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements onSomeEventListener {
    Context context;
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    RelativeLayout tab1Order, tab2Store, tab3Chat;
    private Double mLat = 0.0, mLong = 0.0;
    TextView txtTaborder, txtTabStore, txtTabChat;
    ImageView imgSetting, imgSearch, imgAddStore, imgNotify;
    ProgressDialog dialog;
    RelativeLayout rl_instruction;
    LinearLayout ll_setting;
    TextView txt_CloseInst, txt_Settings, txt_NotifCount;
    ;
    ListView leftDrawer;
    ArrayList<String> leftdrawerArrayList = new ArrayList<>();
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ImageView imageHamburger;

    String from = "";

    TextView textMarketText;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_stores);
        context = this;
        initChat1();


        imageHamburger = (ImageView) findViewById(R.id.image_hamburger);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setItemIconTintList(null);
        textMarketText = (TextView) navigationView.findViewById(R.id.markettext);
        /*Typeface custom_font = Typeface.createFromAsset(getAssets(),  "Roboto-Bold.ttf");
		textMarketText.setTypeface(custom_font);*/

        imageHamburger.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout != null && !drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.openDrawer(Gravity.LEFT);

                } else
                    drawerLayout.closeDrawer(Gravity.LEFT);


            }
        });


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);

                        //Closing drawer on item click
                        drawerLayout.closeDrawers();


                        switch (menuItem.getItemId()) {


                            //Replacing the main content with ContentFragment Which is our Inbox View;
                            case R.id.busiregistration:
                                Intent in = new Intent(getApplicationContext(), ActivityBusinessAccount.class);
                                startActivity(in);


                                return true;

                            case R.id.inviteandearn:
                                startActivity(new Intent(HomeActivity.this, InviteandEarn.class));
                                return true;
                            case R.id.shareapp:
                                try {
                                    Intent i = new Intent(Intent.ACTION_SEND);
                                    i.setType("text/plain");
                                    i.putExtra(Intent.EXTRA_SUBJECT, "Market App");
                                    String sAux = "\nLet me recommend you this application\n\n";
                                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.app.stryker \n\n";
                                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                                    startActivity(Intent.createChooser(i, "choose one"));
                                } catch (Exception e) {
                                    //e.toString();
                                }
                                return true;


                            //Replacing the main content with ContentFragment Which is our Inbox View;
                            case R.id.rateus:
                                rateApp();

                                return true;

                            case R.id.feedback:
                                Intent Email = new Intent(Intent.ACTION_SEND);
                                Email.setType("text/email");
                                Email.putExtra(Intent.EXTRA_EMAIL, new String[]{"hello@marketapp.online"});
                                Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                                Email.putExtra(Intent.EXTRA_TEXT, "Dear ...," + "");
                                startActivity(Intent.createChooser(Email, "Send Feedback:"));
                                return true;
                            case R.id.location:
                                startActivity(new Intent(getApplicationContext(), SelectCity.class));
                                return true;

                            case R.id.signout:
                                Intent i = new Intent(getApplicationContext(),
                                        SettingActivity.class);
                                startActivity(i);
                                return true;
                        }

                        return true;
                    }
                });
        /*try {

            if (b.containsKey("from")) {
                from = b.getString("from");
            } else {
                from = "";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            from = "";
        }*/


        try{

            Bundle b = getIntent().getExtras();
            if(b!=null)
            {
                from = b.getString("from");
            }
            else
                from = "";

        }

        catch (Exception e)
        {e.printStackTrace();}

        finally {
            from = "";
        }

    }

    private void rateApp() {

        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String s) {


        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", s, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;

    }


    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }


    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        init();
        setListener();
    }


    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        ll_setting.setVisibility(View.GONE);
    }


    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        AppUtil.onKeyBoardDown(context);
    }

    private void init() {

        viewPager = (ViewPager) findViewById(R.id.pager_home_item);
        tab1Order = (RelativeLayout) findViewById(R.id.rl_orders);
        tab2Store = (RelativeLayout) findViewById(R.id.rl_stores);
        tab3Chat = (RelativeLayout) findViewById(R.id.rl_chat);
        txtTaborder = (TextView) findViewById(R.id.txt_order);
        txtTabStore = (TextView) findViewById(R.id.txt_store);
        txtTabChat = (TextView) findViewById(R.id.txt_chat);

        imgSetting = (ImageView) findViewById(R.id.img_settings);// log out functionality currently invisible
        imgSearch = (ImageView) findViewById(R.id.img_search);
        imgAddStore = (ImageView) findViewById(R.id.img_add1);
        imgNotify = (ImageView) findViewById(R.id.img_notif);

        rl_instruction = (RelativeLayout) findViewById(R.id.rl_instruction);
        ll_setting = (LinearLayout) findViewById(R.id.ll_setting);
        txt_CloseInst = (TextView) findViewById(R.id.txt_close_instruction);
        txt_Settings = (TextView) findViewById(R.id.txt_setting);

        txt_NotifCount = (TextView) findViewById(R.id.txt_notif);

        if (AppUtil.getFirstLogin(context)) {
            rl_instruction.setVisibility(View.GONE);
        } else {
            rl_instruction.setVisibility(View.GONE);
        }
        mAdapter = new TabsPagerAdapter(this.getSupportFragmentManager());
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


        if (from.equalsIgnoreCase("cart")) {
            viewPager.setCurrentItem(0);
            setTabSelection(0);
            from = "";
        } else {
            if (getIntent().hasExtra("loadOrder")) {
                viewPager.setCurrentItem(0);
                setTabSelection(0);
            } else {
                viewPager.setCurrentItem(1);
                setTabSelection(1);
            }
        }
    }


	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i("=99", "BACK BUTTON");

	*//*		viewPager.setCurrentItem(1);
			setTabSelection(1);
				*//*


		}
		return true;
	}*/


    private void setTabSelection(int posi) {
        switch (posi) {
            case 0:
                tab1Order.setBackgroundResource(R.drawable.select2);
                tab2Store.setBackgroundResource(R.drawable.select1);
                tab3Chat.setBackgroundResource(R.drawable.select1);
                break;
            case 1:
                tab1Order.setBackgroundResource(R.drawable.select1);
                tab3Chat.setBackgroundResource(R.drawable.select1);
                tab2Store.setBackgroundResource(R.drawable.select2);
                break;

            case 2:
                tab1Order.setBackgroundResource(R.drawable.select1);
                tab2Store.setBackgroundResource(R.drawable.select1);
                tab3Chat.setBackgroundResource(R.drawable.select2);
                break;

        }
    }

    private void setListener() {
        tab1Order.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                viewPager.setCurrentItem(0);
            }
        });
        tab2Store.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                viewPager.setCurrentItem(1);
            }
        });
        tab3Chat.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                viewPager.setCurrentItem(2);
            }
        });

        imgAddStore.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(HomeActivity.this, Generalstore_list_new.class);
                startActivity(i);
            }
        });

        txt_Settings.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(i);
            }
        });

        imgSearch.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                //	Intent i = new Intent(HomeActivity.this,SearchStoreToAddActivity.class);
                Intent i = new Intent(HomeActivity.this, Generalstore_list_new.class);
                startActivity(i);
            }
        });

		/*imgSetting.setOnClickListener(new OnClickListener() {


			public void onClick(View v) {
				// TODO Auto-generated method stub
				ll_setting.setVisibility(View.VISIBLE);
			}
		});
*/
        ll_setting.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                ll_setting.setVisibility(View.GONE);
            }
        });

        txt_CloseInst.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                rl_instruction.setVisibility(View.GONE);
                AppUtil.setFirstLogin(context, false);
            }
        });

        imgNotify.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent in = new Intent(HomeActivity.this, NotificationActivity.class);
                startActivity(in);

            }
        });


    }

    public void initChat1() {

        ChatService.initIfNeed(context);
        final QBUser user = new QBUser();

        user.setLogin(AppUtil.getChatUserLoginId(context));
        user.setPassword("12345678");


        ChatService.getInstance().login(user, new QBEntityCallbackImpl() {


            public void onSuccess() {

                Log.e("response", " in side success--->");
              /*  if(((BaseActivity) context).isSessionActive()){
                    getDialogs();
              	 }*/

            }


            public void onError(List errors) {
                Log.e("response1", " in side failure--->" + errors.get(0).toString());

                if (errors.get(0).toString() == "You have already logged in chat") {
                    Log.e("sdfdfgdfgdf", "dfgdfgdfgdfgdgf");
                    /*if(((BaseActivity) context).isSessionActive()){
	                    getDialogs();
	              	 }*/

                }


            }
        });

    }


    public void someEvent(String s) {
        // TODO Auto-generated method stub
        txt_NotifCount.setText(s);
        if (s.equalsIgnoreCase("") || s.equalsIgnoreCase("0")) {
            txt_NotifCount.setVisibility(View.INVISIBLE);
        } else {
            txt_NotifCount.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}
