package com.app.stryker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.fragments.ProductListFragment;
import com.app.model.ModelProdutsArray;
import com.squareup.picasso.Picasso;

//import com.bumptech.glide.load.resource.bitmap.CenterCrop;

/**
 * Created by Inflac on 14-10-2015.
 */
public class ActivityProductDetails extends Activity implements OnTouchListener {

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

    TextView txtInit1;

    String storeID = "";

    RelativeLayout rl_withoutDesc;
    TextView txtBigPrice, txtBigName, txtBigCate;
    ImageView imgBigAdd, imgBigChat;

    LinearLayout llAdd_remove;
    ImageView imgPlus, imgMinus;
    TextView txtProductCount;
    Button btnDone;

    String initial = "";


    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    String savedItemClicked;


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
                if (b.containsKey("type")) {
                    int iCounter = 1;
                    int CounterHot = 0;
                    selectedData = new ModelProdutsArray();
                    if (b.getString("type").equalsIgnoreCase("hot")) {
                        for (int i = 0; i < ProductListFragment.arlistHotProducts.size(); i++) {

                            for (int j = 0; j < ProductListFragment.arlistHotProducts
                                    .get(i).getArrProductsByCategory().size(); j++) {

                                if (po == CounterHot) {
                                    selectedData = ProductListFragment.arlistHotProducts
                                            .get(0).getArrProductsByCategory().get(j);
                                    break;
                                }
                                CounterHot++;
                            }

                        }
                    } else {
                        for (int i = 0; i < ProductListFragment.arlistProduct.size(); i++) {
                            iCounter++;
                            //Log.e("counter**", iCounter+"**initial*");
                            for (int j = 0; j < ProductListFragment.arlistProduct
                                    .get(i).getArrProductsByCategory().size(); j++) {
                                //Log.e("counter**", iCounter+"***");

                                if (iCounter == b.getInt("position")) {
                                    selectedData = ProductListFragment.arlistProduct
                                            .get(i).getArrProductsByCategory().get(j);
                                    break;
                                }
                                iCounter++;

                            }

                        }
                    }
                }
            }


            if (b.containsKey("store_id")) {
                storeID = b.getString("store_id");
            }


        }

        init();
        setInitialCount();
        img_back_icon_product.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(Activity.RESULT_OK);

                finish();
            }
        });

        img_setting_product.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(ActivityProductDetails.this,
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
                llAdd_remove.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.GONE);
                addTocart();
                ProductListFragment.updateQuantity(selectedData.getProdId(), selectedData.getCount());
            }
        });

        imgPlus.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                /** update data in cart */
                addTocart();
                ProductListFragment.updateQuantity(selectedData.getProdId(), selectedData.getCount());
            }
        });
        imgMinus.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                /** update data in cart */
                removeTocart();
                ProductListFragment.updateQuantity(selectedData.getProdId(), selectedData.getCount());
            }
        });
        imgBigAdd.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                /** update data in cart */
                addTocart();
                ProductListFragment.updateQuantity(selectedData.getProdId(), selectedData.getCount());
            }
        });
        btnDone.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

    }

    public void setInitialCount() {
        int count = 0;
        for (int i = 0; i < ProductListFragment.arlistProduct.size(); i++) {

            for (int j = 0; j < ProductListFragment.arlistProduct.get(i)
                    .getArrProductsByCategory().size(); j++) {

                if (selectedData.getProdId() == ProductListFragment.arlistProduct
                        .get(i).getArrProductsByCategory().get(j).getProdId()) {

                    count = ProductListFragment.arlistProduct.get(i)
                            .getArrProductsByCategory().get(j)
                            .getCount();
                    continue;
                }

            }

        }
        txtProductCount.setText(count + "");
        if (count > 0) {
            llAdd_remove.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.GONE);
        } else {
            llAdd_remove.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
        }
    }

    public void addTocart() {
        int count = 0;
        Bundle b = getIntent().getExtras();
        if (b.getString("type").equalsIgnoreCase("hot")) {
            for (int i = 0; i < ProductListFragment.arlistHotProducts.size(); i++) {

                for (int j = 0; j < ProductListFragment.arlistHotProducts.get(i)
                        .getArrProductsByCategory().size(); j++) {

                    if (selectedData.getProdId() == ProductListFragment.arlistHotProducts
                            .get(i).getArrProductsByCategory().get(j).getProdId()) {

                        ProductListFragment.arlistHotProducts
                                .get(i)
                                .getArrProductsByCategory()
                                .get(j)
                                .setCount(
                                        ProductListFragment.arlistHotProducts.get(i)
                                                .getArrProductsByCategory().get(j)
                                                .getCount() + 1);
                        count = ProductListFragment.arlistHotProducts.get(i)
                                .getArrProductsByCategory().get(j)
                                .getCount();
                        continue;
                    }

                }

            }
        } else {
            for (int i = 0; i < ProductListFragment.arlistProduct.size(); i++) {

                for (int j = 0; j < ProductListFragment.arlistProduct.get(i)
                        .getArrProductsByCategory().size(); j++) {

                    if (selectedData.getProdId() == ProductListFragment.arlistProduct
                            .get(i).getArrProductsByCategory().get(j).getProdId()) {

                        ProductListFragment.arlistProduct
                                .get(i)
                                .getArrProductsByCategory()
                                .get(j)
                                .setCount(
                                        ProductListFragment.arlistProduct.get(i)
                                                .getArrProductsByCategory().get(j)
                                                .getCount() + 1);
                        count = ProductListFragment.arlistProduct.get(i)
                                .getArrProductsByCategory().get(j)
                                .getCount();
                        continue;
                    }

                }

            }
        }


        txtProductCount.setText(count + "");
        if (count > 0) {
            llAdd_remove.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.GONE);
        } else {
            llAdd_remove.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
        }

        ((TextView) findViewById(R.id.productdetails_totalprice))
                .setText(ProductListFragment.calculateTotalBill() + "");
        // ((TextView)
        // findViewById(R.id.productdetails_totalprice)).setText(ProductListFragment.+"");
    }

    public void removeTocart() {
        int count = 0;
        Bundle b = getIntent().getExtras();
        if (b.getString("type").equalsIgnoreCase("hot")) {
            for (int i = 0; i < ProductListFragment.arlistHotProducts.size(); i++) {

                for (int j = 0; j < ProductListFragment.arlistHotProducts.get(i)
                        .getArrProductsByCategory().size(); j++) {

                    if (selectedData.getProdId() == ProductListFragment.arlistHotProducts
                            .get(i).getArrProductsByCategory().get(j).getProdId()) {

                        ProductListFragment.arlistHotProducts
                                .get(i)
                                .getArrProductsByCategory()
                                .get(j)
                                .setCount(
                                        ProductListFragment.arlistHotProducts.get(i)
                                                .getArrProductsByCategory().get(j)
                                                .getCount() - 1);
                        count = ProductListFragment.arlistHotProducts.get(i)
                                .getArrProductsByCategory().get(j)
                                .getCount();
                        continue;
                    }

                }

            }
        } else {
            for (int i = 0; i < ProductListFragment.arlistProduct.size(); i++) {

                for (int j = 0; j < ProductListFragment.arlistProduct.get(i)
                        .getArrProductsByCategory().size(); j++) {

                    if (selectedData.getProdId() == ProductListFragment.arlistProduct
                            .get(i).getArrProductsByCategory().get(j).getProdId()) {

                        ProductListFragment.arlistProduct
                                .get(i)
                                .getArrProductsByCategory()
                                .get(j)
                                .setCount(
                                        ProductListFragment.arlistProduct.get(i)
                                                .getArrProductsByCategory().get(j)
                                                .getCount() - 1);
                        count = ProductListFragment.arlistProduct.get(i)
                                .getArrProductsByCategory().get(j)
                                .getCount();
                        continue;
                    }

                }

            }
        }


        txtProductCount.setText(count + "");
        if (count > 0) {
            llAdd_remove.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.GONE);
        } else {
            llAdd_remove.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
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
        // amr img_productimage.setOnTouchListener(this);

        img_productimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                System.out.println("Adding youtube thumbnail");

                Intent i = new Intent(context, ImageviewonfullscreenActivity.class);
                i.putExtra("imgurl", selectedData.getProImage());
                startActivity(i);

            }
        });


        // img_add6 = (ImageView)findViewById(R.id.img_add6);

        txt_header_product = (TextView) findViewById(R.id.txt_header_product);
        txt_header_product.setTypeface(tfregular);

        txt_productname = (TextView) findViewById(R.id.txt_productname);
        txt_productname.setTypeface(tfbold);

        txt_producttype = (TextView) findViewById(R.id.txt_producttype);
        txt_producttype.setTypeface(tfregular);

        txt_price = (TextView) findViewById(R.id.text_price);
        txt_price.setTypeface(tfregular);

        txt_product_detail = (TextView) findViewById(R.id.text_product_detail);
        txt_product_detail.setTypeface(tfregular);

        txt_cart_notification = (TextView) findViewById(R.id.txt_cart_notification);
        txt_cart_notification.setTypeface(tfbold);

        productdetails_totalprice = (TextView) findViewById(R.id.productdetails_totalprice);
        productdetails_totalprice.setTypeface(tfbold);

        productdetais_checkout_text = (TextView) findViewById(R.id.productdetais_checkout_text);
        productdetais_checkout_text.setTypeface(tfregular);

        txtInit1 = (TextView) findViewById(R.id.txt_init_letter);
        //txtInit1.setTypeface(tfbold);


        llAdd_remove = (LinearLayout) findViewById(R.id.ll_add_remove);
        imgPlus = (ImageView) findViewById(R.id.img_add1);
        imgMinus = (ImageView) findViewById(R.id.img_minus1);
        txtProductCount = (TextView) findViewById(R.id.txt_nmbr);

        //rl_withoutDesc = (RelativeLayout) findViewById(R.id.rl_withoutdesc);
        txtBigPrice = (TextView) findViewById(R.id.txt_big_price);
        txtBigName = (TextView) findViewById(R.id.txt_big_name);
        txtBigCate = (TextView) findViewById(R.id.txt_big_cate);
        imgBigAdd = (ImageView) findViewById(R.id.img_add_big);
        imgBigChat = (ImageView) findViewById(R.id.img_chat_big);
        txtBigPrice.setTypeface(tfregular);
        txtBigName.setTypeface(tfregular);
        txtBigCate.setTypeface(tfregular);

        btnDone = (Button) findViewById(R.id.btn_done);

        try {
            if (selectedData.getProdId().length() > 0) {
                txt_productname.setText(selectedData.getProdName());
                txt_producttype.setText(selectedData.getProCatName());
                txtBigName.setText(selectedData.getProdName());
                txt_price.setText(selectedData.getProPrice());
                txtBigPrice.setText(selectedData.getProPrice());
                txtBigCate.setText(selectedData.getProCatName());
                initial = selectedData.getProdName().substring(0, 1).toUpperCase();

                String des[] = selectedData.getProDesc().split("#");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < des.length; i++) {
                    if (i == 0) {
                        sb.append(des[i]);
                    } else {
                        sb.append("\n" + "\t" + "� " + des[i]);
                    }

                }
                txt_product_detail.setText(sb.toString());

            }
            /*if(selectedData.getProdId().length() > 0 && selectedData.getProDesc().length() > 0 ){
				rl_withoutDesc.setVisibility(View.GONE);
			}else{
				rl_withoutDesc.setVisibility(View.VISIBLE);
			}*/


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
                        //.transform(new CircleTransform())
                        .resize(1200, 800)
                        //.centerCrop()
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
            //((TextView) findViewById(R.id.txt_init_letter)).setText(selectedData.getProdName().charAt(0));
            ((ImageView) findViewById(R.id.img_init_bg)).setBackgroundResource(R.drawable.circle_1);
        } else if (selectedData.getProdName().toUpperCase().startsWith("F")
                || selectedData.getProdName().toUpperCase().startsWith("G")
                || selectedData.getProdName().toUpperCase().startsWith("H")
                || selectedData.getProdName().toUpperCase().startsWith("I")
                || selectedData.getProdName().toUpperCase().startsWith("J")) {
            //((TextView) findViewById(R.id.txt_init_letter)).setText(selectedData.getProdName().charAt(0));
            ((ImageView) findViewById(R.id.img_init_bg)).setBackgroundResource(R.drawable.circle_2);
        } else if (selectedData.getProdName().toUpperCase().startsWith("K")
                || selectedData.getProdName().toUpperCase().startsWith("L")
                || selectedData.getProdName().toUpperCase().startsWith("M")
                || selectedData.getProdName().toUpperCase().startsWith("N")
                || selectedData.getProdName().toUpperCase().startsWith("O")) {
            //((TextView) findViewById(R.id.txt_init_letter)).setText(selectedData.getProdName().charAt(0));
            ((ImageView) findViewById(R.id.img_init_bg)).setBackgroundResource(R.drawable.circle_3);
        } else if (selectedData.getProdName().toUpperCase().startsWith("P")
                || selectedData.getProdName().toUpperCase().startsWith("Q")
                || selectedData.getProdName().toUpperCase().startsWith("R")
                || selectedData.getProdName().toUpperCase().startsWith("S")
                || selectedData.getProdName().toUpperCase().startsWith("T")) {
            //((TextView) findViewById(R.id.txt_init_letter)).setText(selectedData.getProdName().charAt(0));
            ((ImageView) findViewById(R.id.img_init_bg)).setBackgroundResource(R.drawable.circle_4);
        } else if (selectedData.getProdName().toUpperCase().startsWith("U")
                || selectedData.getProdName().toUpperCase().startsWith("V")
                || selectedData.getProdName().toUpperCase().startsWith("W")
                || selectedData.getProdName().toUpperCase().startsWith("X")
                || selectedData.getProdName().toUpperCase().startsWith("Y")
                || selectedData.getProdName().toUpperCase().startsWith("Z")) {
            //((TextView) findViewById(R.id.txt_init_letter)).setText(selectedData.getProdName().charAt(0));
            ((ImageView) findViewById(R.id.img_init_bg)).setBackgroundResource(R.drawable.circle_5);


        }

        try {
            ((TextView) findViewById(R.id.txt_init_letter))
                    .setText(initial + "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    //@Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub

        ImageView view = (ImageView) v;
        dumpEvent(event);
        Log.e("dsfsfs", "sefsdfdsfsadfdsfsdf");
        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                //   Log.d(TAG, "mode=DRAG");
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                // Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    //     Log.d(TAG, "mode=ZOOM");
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                //   Log.d(TAG, "mode=NONE");
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    // ...
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY()
                            - start.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    //   Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix);
        return true;
    }

    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
        //    Log.d(TAG, sb.toString());
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }


}
