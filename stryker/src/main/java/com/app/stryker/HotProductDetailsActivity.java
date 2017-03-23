package com.app.stryker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.fragments.ProductListFragment;
import com.app.model.ModelProdutsArray;
import com.app.utill.CircleTransform;
import com.squareup.picasso.Picasso;

//import com.google.android.gms.internal.fn;

/**
 * Created by Inflac on 14-10-2015.
 */
public class HotProductDetailsActivity extends Activity {

    Context context;
    Typeface tfbold;
    Typeface tfregular;
    Typeface tfthin;

    ImageView img_back_icon_product, img_setting_product, img_productimage,
            img_add6;
    TextView txt_header_product, txt_productname, txt_producttype, txt_price,
            txt_product_detail, txt_cart_notification,
            productdetails_totalprice, productdetais_checkout_text;
    ImageView btnAdd;
    ModelProdutsArray selectedData;

    TextView txtInit;

    String storeID = "";

    RelativeLayout rl_withoutDesc;
    TextView txtBigPrice, txtBigName, txtBigCate;
    ImageView imgBigAdd, imgBigChat;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        context = this;
        tfbold = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Bold.ttf");
        tfregular = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Regular.ttf");
        tfthin = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Thin.ttf");

        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("position")) {
                int po = b.getInt("position");
                Log.e("Posi**", b.getInt("position") + "");
                selectedData = new ModelProdutsArray();
                for (int i = 0; i < ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory().size(); i++) {
                    if (i == b.getInt("position")) {
                        selectedData = ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory().get(i);
                        continue;
                    }

                }

            }

            if (b.containsKey("store_id")) {
                storeID = b.getString("store_id");
            }


        }

        init();
        img_back_icon_product.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });

        img_setting_product.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(HotProductDetailsActivity.this,
                        SettingActivity.class);
                startActivity(i);
            }
        });

        txt_cart_notification.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

            }
        });

        productdetais_checkout_text
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        Intent i = new Intent(context, CartActivity.class);
                        i.putExtra("store_id", storeID);
                        startActivity(i);

                    }
                });
        btnAdd.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                /** update data in cart */
                addTocart();
            }
        });
        imgBigAdd.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                /** update data in cart */
                addTocart();
            }
        });

    }

    public void addTocart() {
        for (int i = 0; i < ProductListFragment.arlistProduct.size(); i++) {

            for (int j = 0; j < ProductListFragment.arlistProduct.get(i)
                    .getArrProductsByCategory().size(); j++) {

                if (selectedData.getProdId() == ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory()
                        .get(i).getProdId()) {

                    ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory()
                            .get(i)
                            .setCount(
                                    ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory()
                                            .get(i).getCount() + 1);
                    continue;
                }

            }

        }

        ((TextView) findViewById(R.id.productdetails_totalprice))
                .setText(ProductListFragment.calculateTotalBill() + "");
        // ((TextView)
        // findViewById(R.id.productdetails_totalprice)).setText(ProductListFragment.+"");
    }

    public void init() {

        btnAdd = (ImageView) findViewById(R.id.img_add);
        img_back_icon_product = (ImageView) findViewById(R.id.img_back_icon_product);

        img_setting_product = (ImageView) findViewById(R.id.img_setting_product);

        img_productimage = (ImageView) findViewById(R.id.img_productimage);

        // img_add6 = (ImageView)findViewById(R.id.img_add6);

        txt_header_product = (TextView) findViewById(R.id.txt_header_product);
        txt_header_product.setTypeface(tfregular);

        txt_productname = (TextView) findViewById(R.id.txt_productname);
        txt_productname.setTypeface(tfbold);

        txt_producttype = (TextView) findViewById(R.id.txt_producttype);
        txt_producttype.setTypeface(tfthin);

        txt_price = (TextView) findViewById(R.id.text_price);
        txt_price.setTypeface(tfbold);

        txt_product_detail = (TextView) findViewById(R.id.text_product_detail);
        txt_product_detail.setTypeface(tfthin);

        txt_cart_notification = (TextView) findViewById(R.id.txt_cart_notification);
        txt_cart_notification.setTypeface(tfbold);

        productdetails_totalprice = (TextView) findViewById(R.id.productdetails_totalprice);
        productdetails_totalprice.setTypeface(tfbold);

        productdetais_checkout_text = (TextView) findViewById(R.id.productdetais_checkout_text);
        productdetais_checkout_text.setTypeface(tfregular);

        txtInit = (TextView) findViewById(R.id.txt_init);
        txtInit.setTypeface(tfbold);


        rl_withoutDesc = (RelativeLayout) findViewById(R.id.rl_withoutdesc);
        txtBigPrice = (TextView) findViewById(R.id.txt_big_price);
        txtBigName = (TextView) findViewById(R.id.txt_big_name);
        txtBigCate = (TextView) findViewById(R.id.txt_big_cate);
        imgBigAdd = (ImageView) findViewById(R.id.img_add_big);
        imgBigChat = (ImageView) findViewById(R.id.img_chat_big);
        txtBigPrice.setTypeface(tfregular);
        txtBigName.setTypeface(tfregular);
        txtBigCate.setTypeface(tfregular);


        try {
            if (selectedData.getProdId().length() > 0) {
                txt_productname.setText(selectedData.getProdName());
                txtBigName.setText(selectedData.getProdName());
                txt_price.setText(selectedData.getProPrice());
                txtBigPrice.setText(selectedData.getProPrice());
                txtBigCate.setText("Hot Product");
                txt_product_detail.setText(selectedData.getProDesc());

            }
            if (selectedData.getProdId().length() > 0 && selectedData.getProDesc().length() > 0) {
                rl_withoutDesc.setVisibility(View.GONE);
            } else {
                rl_withoutDesc.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
        }

        ((TextView) findViewById(R.id.productdetails_totalprice))
                .setText(ProductListFragment.calculateTotalBill() + "");

        if (selectedData.getProImage().length() > 0) {
            ((RelativeLayout) findViewById(R.id.rl_initials))
                    .setVisibility(View.GONE);
            try {
                Picasso.with(context)
                        .load(context.getString(R.string.image_base_url)
                                + selectedData.getProImage())
                        .placeholder(R.drawable.circle_1)
                        .transform(new CircleTransform())
                        .transform(new CircleTransform())
                        .into(img_productimage);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            img_productimage.setVisibility(View.GONE);
            ((RelativeLayout) findViewById(R.id.rl_initials))
                    .setVisibility(View.VISIBLE);
            setInitLetter();
        }
    }

    public void setInitLetter() {
        if (selectedData.getProdName().toUpperCase().startsWith("A")
                || selectedData.getProdName().toUpperCase().startsWith("B")
                || selectedData.getProdName().toUpperCase().startsWith("C")
                || selectedData.getProdName().toUpperCase().startsWith("D")
                || selectedData.getProdName().toUpperCase().startsWith("E")) {

            ((ImageView) findViewById(R.id.img_init_bg)).setBackgroundResource(R.drawable.circle_1);
        } else if (selectedData.getProdName().toUpperCase().startsWith("F")
                || selectedData.getProdName().toUpperCase().startsWith("G")
                || selectedData.getProdName().toUpperCase().startsWith("H")
                || selectedData.getProdName().toUpperCase().startsWith("I")
                || selectedData.getProdName().toUpperCase().startsWith("J")) {

            ((ImageView) findViewById(R.id.img_init_bg)).setBackgroundResource(R.drawable.circle_2);
        } else if (selectedData.getProdName().toUpperCase().startsWith("K")
                || selectedData.getProdName().toUpperCase().startsWith("L")
                || selectedData.getProdName().toUpperCase().startsWith("M")
                || selectedData.getProdName().toUpperCase().startsWith("N")
                || selectedData.getProdName().toUpperCase().startsWith("O")) {

            ((ImageView) findViewById(R.id.img_init_bg)).setBackgroundResource(R.drawable.circle_3);
        } else if (selectedData.getProdName().toUpperCase().startsWith("P")
                || selectedData.getProdName().toUpperCase().startsWith("Q")
                || selectedData.getProdName().toUpperCase().startsWith("R")
                || selectedData.getProdName().toUpperCase().startsWith("S")
                || selectedData.getProdName().toUpperCase().startsWith("T")) {

            ((ImageView) findViewById(R.id.img_init_bg)).setBackgroundResource(R.drawable.circle_4);
        } else if (selectedData.getProdName().toUpperCase().startsWith("U")
                || selectedData.getProdName().toUpperCase().startsWith("V")
                || selectedData.getProdName().toUpperCase().startsWith("W")
                || selectedData.getProdName().toUpperCase().startsWith("X")
                || selectedData.getProdName().toUpperCase().startsWith("Y")
                || selectedData.getProdName().toUpperCase().startsWith("Z")) {

            ((ImageView) findViewById(R.id.img_init_bg)).setBackgroundResource(R.drawable.circle_5);

            try {
                txtInit.setText(selectedData.getProdName().charAt(0));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
