package com.app.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.app.adapters.SeparatedListAdapter;
import com.app.jsoncall.JsonCall;
import com.app.model.ModelProdutList;
import com.app.model.ModelProdutsArray;
import com.app.stryker.ActivityProductDetails;
import com.app.stryker.CartActivity;
import com.app.stryker.ProductListActivity;
import com.app.stryker.R;
import com.app.utill.AppUtil;
import com.app.utill.CircleTransform;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductListFragment extends android.support.v4.app.Fragment {

    Context context;

    GestureDetector mGestureDetector;
    ListView listStore;
    public static ArrayList<ModelProdutList> arlistHotProducts = new ArrayList<ModelProdutList>();
    public static ArrayList<ModelProdutList> arlistProduct = new ArrayList<ModelProdutList>();
    public static ArrayList<ModelProdutList> arlistMyCart = new ArrayList<ModelProdutList>();


    ProgressDialog dialog;
    //SwipeRefreshLayout swipeLayout;
    String storeId = "";
    SeparatedListAdapter adapterSep;

    TextView txtCheckOut, txtCartCount, txtTotalAmount;
    RelativeLayout rlCart;

    ProductListActivity activity;
    /**
     * hot product flipper
     */
    ViewFlipper flipHotProduct;
    /**
     * flipper next / previous
     */
    ImageView imgNext, imgPrev;
    /**
     * flipper position
     */
    int flipperPosi = 0;
    RelativeLayout rlhotLayout;
    ImageView imgAddHotPro, imgPlusHot, imgMinusHot;
    TextView txtHotPrice, txtHotName, txtHotCount;
    LinearLayout llHotPlus_Minus;

    ImageView imageFlip;

    ViewGroup header;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootview = inflater.inflate(R.layout.fragment_product_list,
                container, false);
        activity = (ProductListActivity) getActivity();
        storeId = activity.StoreId();

        return rootview;
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();


        init(1);

        // setData();
        init_next();


    }

    public void addListHeader() {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        header = (ViewGroup) inflater.inflate(R.layout.hot_product_flipper_layout, listStore, false);


        listStore.addHeaderView(header, null, false);
        rlhotLayout = (RelativeLayout) header.findViewById(R.id.rl_store_pics);
        flipHotProduct = (ViewFlipper) header.findViewById(R.id.product_view_flipper);
        imgNext = (ImageView) header.findViewById(R.id.img_next);
        imgPrev = (ImageView) header.findViewById(R.id.img_prev);
        imgAddHotPro = (ImageView) header.findViewById(R.id.img_add_hot);
        txtHotName = (TextView) header.findViewById(R.id.txt_hot_p_name);
        txtHotPrice = (TextView) header.findViewById(R.id.txt_hot_price1);
        imgPlusHot = (ImageView) header.findViewById(R.id.img_h_add1);
        imgMinusHot = (ImageView) header.findViewById(R.id.img_h_minus1);
        txtHotCount = (TextView) header.findViewById(R.id.txt_h_nmbr);
        llHotPlus_Minus = (LinearLayout) header.findViewById(R.id.ll_add_h_remove);


    }


    public void setData() {

    }

    public void setHotProducts() {

        flipHotProduct.removeAllViews();
        flipperPosi = 0;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        if (arlistHotProducts.get(0).getArrProductsByCategory().size() <= 0) {
            //listStore.removeHeaderView((ViewGroup) inflater.inflate(R.layout.hot_product_flipper_layout, listStore,false));
            header.setVisibility(View.GONE);
            rlhotLayout.setVisibility(View.GONE);
            flipHotProduct.setVisibility(View.GONE);

        } else {
            header.setVisibility(View.VISIBLE);
            rlhotLayout.setVisibility(View.VISIBLE);
            flipHotProduct.setVisibility(View.VISIBLE);
        }


        for (int i = 0; i < arlistHotProducts.get(0).getArrProductsByCategory().size(); i++) {
            //  This will create dynamic image view and add them to ViewFlipper
            setFlipperImage(i, arlistHotProducts.get(0).getArrProductsByCategory().get(i).getProImage());
            Log.e("added flipper", "**flipper added*");
        }

        if (arlistHotProducts.get(0).getArrProductsByCategory().size() > 0) {
            //((RelativeLayout) getView().findViewById(R.id.rl_hot)).setVisibility(View.VISIBLE);
            // rlhotLayout.setVisibility(View.VISIBLE);
            flipHotProduct.setVisibility(View.VISIBLE);
            setHotProductData(0);
        } else {
            //((RelativeLayout) getView().findViewById(R.id.rl_hot)).setVisibility(View.GONE);
            rlhotLayout.setVisibility(View.GONE);
            flipHotProduct.setVisibility(View.GONE);
        }

        if (arlistHotProducts.get(0).getArrProductsByCategory().size() <= 1) {
            imgNext.setVisibility(View.INVISIBLE);
            imgPrev.setVisibility(View.INVISIBLE);
        } else {
            imgNext.setVisibility(View.VISIBLE);
            imgPrev.setVisibility(View.VISIBLE);
        }
    }

    private void setFlipperImage(int posi, String url) {
        Log.i("Set Filpper Called", "");
        ImageView imageFlip = new ImageView(context);
        // imageFlip.setScaleType(ImageView.ScaleType.FIT_XY);
        try {
            if (url.length() > 0) {
                Picasso.with(context).load(context.getString(R.string.image_base_url) + url)
                        .placeholder(R.drawable.oplaceholder)
                        //.transform(new CircleTransform())
                        .resize(1200, 700)
                        //.centerCrop()
                        .into(imageFlip);
            } else {
                imageFlip.setBackgroundResource(R.drawable.oplaceholder);
                /**replace with initial name*/
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        imageFlip.setTag(posi);
        flipHotProduct.addView(imageFlip);

	   /* imageFlip.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
			int posi = (int) v.getTag();
			Intent i = new Intent(getActivity(),ActivityProductDetails.class);				
			i.putExtra("position", posi);
			i.putExtra("store_id", storeId);
			i.putExtra("type", "hot");
			startActivity(i);
			}
		});*/

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        txtTotalAmount.setText(calculateTotalBill() + "");
        //setCartCount();
        txtCartCount.setText(calculateCartCount() + "");
        init(2);
        init_next();

    }


    private void init_next() {

        if (!(arlistProduct.size() > 0)) {
            if (AppUtil.isNetworkAvailable(context)) {
                dialog = ProgressDialog.show(context, "", "Please wait...");
                ProductListTask task = new ProductListTask();
                task.execute(new String[]{storeId});
                Log.e("datahit", "^^^^^^^^^^^^");
            } else {
                AppUtil.showCustomToast(context,
                        "please check your internet connection");
            }
        } else {
            adapterSep = new SeparatedListAdapter(context);
            if (arlistProduct.size() > 0) {
                for (int i = 0; i < arlistProduct.size(); i++) {
                    adapterSep.addSection(arlistProduct.get(i).getCategoryName(), new MyAdapter(context, R.layout.row_product_list, arlistProduct.get(i).getArrProductsByCategory()));
                }

            }

            Log.e("List frag called againg without new load", "&&&&&&&&&&&&&&&&&&");
			/*adapter = new MyAdapter(StationHistoryActivity.this,
					R.layout.history_row, al);*/
            listStore.setAdapter(adapterSep);
            if (arlistHotProducts.size() > 0) {
                setHotProducts();
                header.setVisibility(View.VISIBLE);
                rlhotLayout.setVisibility(View.VISIBLE);
                flipHotProduct.setVisibility(View.VISIBLE);
            } else {
                header.setVisibility(View.GONE);
                rlhotLayout.setVisibility(View.GONE);
                flipHotProduct.setVisibility(View.GONE);
            }

            //setHotProductData(0);
        }

    }

    private void init(int flag) {

        listStore = (ListView) getView().findViewById(R.id.lv_store_details);
        txtCheckOut = (TextView) getView().findViewById(R.id.txt_checkout);
        txtCartCount = (TextView) getView().findViewById(R.id.txt_cart_count);
        txtTotalAmount = (TextView) getView().findViewById(R.id.txt_price_count);
        rlCart = (RelativeLayout) getView().findViewById(R.id.rl_cart);
        if (flag == 1)
            addListHeader();
        txtTotalAmount.setText(calculateTotalBill() + "");
        //setCartCount();
        txtCartCount.setText(calculateCartCount() + "");

        flipHotProduct.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                int posi = flipperPosi;
                //AppUtil.showCustomToast(context, posi+"");
                Log.e("hotproductSize**", arlistHotProducts.get(0).getArrProductsByCategory().size() + "");
                Intent i = new Intent(getActivity(), ActivityProductDetails.class);
                i.putExtra("position", posi);
                Log.e("posi", "**" + posi);
                i.putExtra("store_id", storeId);
                i.putExtra("type", "hot");
                startActivity(i);
            }
        });

        listStore.setOnItemClickListener(new OnItemClickListener() {


            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                /** perform the action on click of store list */
                //Intent i = new Intent(context,ActivityProductDetails.class);
                //i.putExtra("product_detail", arlistProduct.get(position).getArrProductsByCategory().get);
				/* int iCounter = 0;
				 ModelProdutsArray selectedData = new ModelProdutsArray();
				 for (int i = 0; i < arlistProduct.size(); i++) {
					 iCounter++;
					 for (int j = 0; j < arlistProduct.get(i).getArrProductsByCategory().size(); j++) {
						 iCounter++;
						if(iCounter == position){
							selectedData = arlistProduct.get(i).getArrProductsByCategory().get(j);
							continue;
						}
					}
					
				}*/
                //AppUtil.showCustomToast(context, position+"");
                //position -= listStore.getHeaderViewsCount();

                Intent i = new Intent(context, ActivityProductDetails.class);
                i.putExtra("position", position);
                i.putExtra("store_id", storeId);
                i.putExtra("type", "product");
                //startActivity(i);
                startActivityForResult(i, 10001);

                //AppUtil.showCustomToast(context, position+"");
            }
        });
        rlCart.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                /** move via intent on my cart screen with cart data*/
            }
        });
        txtCheckOut.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                /** move via intent on my cart screen with cart data*/
                Intent i = new Intent(context, CartActivity.class);
                i.putExtra("store_id", storeId);
                //startActivity(i);

                //Intent intent = new Intent(getActivity(), register .class);
                startActivityForResult(i, 10001);


            }
        });
        imgNext.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.e("flipper position next", "" + flipperPosi);

                if (arlistHotProducts.get(0).getArrProductsByCategory().size() > flipperPosi + 1) {
                    // Next screen comes in from right.
                    flipHotProduct.setInAnimation(context, R.anim.slide_in_from_right);
                    // Current screen goes out from left.
                    flipHotProduct.setOutAnimation(context, R.anim.slide_out_to_left);
                    // Display previous screen.
                    flipHotProduct.showNext();
                    flipperPosi++;
                    Log.e("flipper position next", "" + flipperPosi);
                    setHotProductData(flipperPosi);
                }
            }
        });
        imgPrev.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (flipperPosi == 0) {
                } else {
                    // Next screen comes in from left.
                    flipHotProduct.setInAnimation(context, R.anim.slide_in_from_left);
                    // Current screen goes out from right.
                    flipHotProduct.setOutAnimation(context, R.anim.slide_out_to_right);
                    // Display next screen.
                    flipHotProduct.showPrevious();
                    flipperPosi--;
                    Log.e("flipper position prev", "" + flipperPosi);
                    setHotProductData(flipperPosi);
                }
            }
        });

        imgAddHotPro.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                //AppUtil.showCustomToast(context, "Added");
                if (arlistHotProducts.get(0).getArrProductsByCategory().size() > flipperPosi) {
                    int newCount = arlistHotProducts.get(0).getArrProductsByCategory().get(flipperPosi).getCount() + 1;
                    //HotProductListModel mod = arlistHotProducts.get(flipperPosi);
                    arlistHotProducts.get(0).getArrProductsByCategory().get(flipperPosi).setCount(newCount);
                    updateQuantity(arlistHotProducts.get(0).getArrProductsByCategory().get(flipperPosi).getProdId(), newCount);
                    //mod.setCount(newCount);
                    //arlistHotProducts.set(flipperPosi, mod);
                    txtCartCount.
                            setText(calculateCartCount() + "");
                    txtHotCount.setText(newCount + "");
                    txtTotalAmount.setText(ProductListFragment.calculateTotalBill() + "");
                    imgAddHotPro.setVisibility(View.GONE);
                    llHotPlus_Minus.setVisibility(View.VISIBLE);
                }
            }
        });

        imgPlusHot.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (arlistHotProducts.get(0).getArrProductsByCategory().size() > flipperPosi) {
                    int newCount = arlistHotProducts.get(0).getArrProductsByCategory().get(flipperPosi).getCount() + 1;
                    //HotProductListModel mod = arlistHotProducts.get(flipperPosi);
                    arlistHotProducts.get(0).getArrProductsByCategory().get(flipperPosi).setCount(newCount);
                    updateQuantity(arlistHotProducts.get(0).getArrProductsByCategory().get(flipperPosi).getProdId(), newCount);
                    //mod.setCount(newCount);
                    //arlistHotProducts.set(flipperPosi, mod);
                    txtHotCount.setText(newCount + "");
                    txtCartCount.
                            setText(calculateCartCount() + "");
                    txtTotalAmount.setText(ProductListFragment.calculateTotalBill() + "");

                }
            }
        });

        imgMinusHot.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (arlistHotProducts.get(0).getArrProductsByCategory().size() > flipperPosi) {
                    if (arlistHotProducts.get(0).getArrProductsByCategory().get(flipperPosi).getCount() > 0) {
                        int newCount = arlistHotProducts.get(0).getArrProductsByCategory().get(flipperPosi).getCount() - 1;
                        //HotProductListModel mod = arlistHotProducts.get(flipperPosi);
                        arlistHotProducts.get(0).getArrProductsByCategory().get(flipperPosi).setCount(newCount);
                        updateQuantity(arlistHotProducts.get(0).getArrProductsByCategory().get(flipperPosi).getProdId(), newCount);
                        //mod.setCount(newCount);
                        //arlistHotProducts.set(flipperPosi, mod);
                        txtHotCount.setText(newCount + "");
                        txtCartCount.
                                setText(calculateCartCount() + "");
                        txtTotalAmount.setText(ProductListFragment.calculateTotalBill() + "");
                        if (newCount == 0) {
                            imgAddHotPro.setVisibility(View.VISIBLE);
                            llHotPlus_Minus.setVisibility(View.GONE);
                        }
                    } else {
                        //int newCount = 0;
                        //arlistHotProducts.get(0).getArrProductsByCategory().get(flipperPosi).setCount(newCount);
                        //txtHotCount.setText(newCount+"");
                        //setCartCount();
                        //txtTotalAmount.setText(ProductListFragment.calculateTotalBill()+"");
                        imgAddHotPro.setVisibility(View.VISIBLE);
                        llHotPlus_Minus.setVisibility(View.GONE);
                    }

                }
            }
        });

    }

    public void setHotProductData(int Position) {
        if (arlistHotProducts.get(0).getArrProductsByCategory().size() > Position) {
            Log.e("hot product position", Position + "***");
            Log.e("hot product size", arlistHotProducts.get(0).getArrProductsByCategory().size() + "***");
            //Log.e("name position", arlistHotProducts.get(Position).getName() +"***");
            //Log.e("price position", arlistHotProducts.get(Position).getPrice() +"***");

            txtHotName.setText(arlistHotProducts.get(0).getArrProductsByCategory().get(Position).getProdName() + "");
            txtHotPrice.setText(arlistHotProducts.get(0).getArrProductsByCategory().get(Position).getProPrice() + "");

            txtHotCount.setText(arlistHotProducts.get(0).getArrProductsByCategory().get(Position).getCount() + "");
            if (arlistHotProducts.get(0).getArrProductsByCategory().get(Position).getCount() > 0) {
                llHotPlus_Minus.setVisibility(View.VISIBLE);
                imgAddHotPro.setVisibility(View.GONE);
            } else {
                imgAddHotPro.setVisibility(View.VISIBLE);
                llHotPlus_Minus.setVisibility(View.GONE);
            }

        }

    }
	
	/*public void setCartCount(){
		int count = 0 ;
		//int totalBill ;
		for (int i = 0; i < arlistProduct.size(); i++) {			
			for(int j = 0;j < arlistProduct.get(i).getArrProductsByCategory().size(); j++){
				if(arlistProduct.get(i).getArrProductsByCategory().get(j).getCount() > 0){
					count++;
					break;
				}
			}
			

			if (arlistHotProducts.size() > 0) {
				for (int j = 0; j < arlistHotProducts.get(0)
						.getArrProductsByCategory().size(); j++) {
					if (arlistHotProducts.get(0).getArrProductsByCategory().get(j)
							.getCount() > 0) {
						count++;
					}
				}
			}
		}
		txtCartCount.setText(count+"");
	}*/

    public static int calculateCartCount() {
        int count = 0;
        //int totalBill ;
        for (int i = 0; i < arlistProduct.size(); i++) {
            for (int j = 0; j < arlistProduct.get(i).getArrProductsByCategory().size(); j++) {
                if (arlistProduct.get(i).getArrProductsByCategory().get(j).getCount() > 0) {
                    count++;
                }
            }
        }


        if (arlistHotProducts.size() > 0) {
            for (int j = 0; j < arlistHotProducts.get(0)
                    .getArrProductsByCategory().size(); j++) {
                if (arlistHotProducts.get(0).getArrProductsByCategory().get(j)
                        .getCount() > 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public static float calculateTotalBill() {
        float totalBill = 0.0f;
        for (int i = 0; i < arlistProduct.size(); i++) {
            for (int j = 0; j < arlistProduct.get(i).getArrProductsByCategory().size(); j++) {
                if (arlistProduct.get(i).getArrProductsByCategory().get(j).getCount() > 0) {
                    try {
                        totalBill += arlistProduct.get(i).getArrProductsByCategory().get(j).getProductCost();
                    } catch (NumberFormatException e) {
                        // TODO Auto-generated catch block
                        totalBill = 0;
                    }

                }
            }
        }
        if (arlistHotProducts.size() > 0) {
            for (int j = 0; j < arlistHotProducts.get(0).getArrProductsByCategory().size(); j++) {
                try {
                    if (arlistHotProducts.get(0).getArrProductsByCategory().get(j).getCount() > 0) {
                        totalBill += arlistHotProducts.get(0).getArrProductsByCategory().get(j).getProductCost();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return totalBill;
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
                response = obj.getProductList(urls[0], context
                        .getResources().getString(R.string.product_list_url));
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


                        JSONObject joStoreFront = jo.getJSONObject("store");
                        String minOrderValue = joStoreFront.getString("MinimumOrderValue");
                        AppUtil.setMinOrderValue(context, minOrderValue);

                        JSONArray jaHotProducts = jo
                                .getJSONArray("hotproducts");
						
					/*	ArrayList<ModelProdutsArray> arr2 = new ArrayList<ModelProdutsArray>();
						for (int i = 0; i < jaHotProducts.length(); i++) {							
							JSONObject joHot = jaHotProducts.getJSONObject(i);
							String id = joHot.getString("Id");
							String name = joHot.getString("Name");
							String description = joHot.getString("Description");
							String imageurl = joHot.getString("ImageUrl");
							String price = joHot.getString("Price");
							arlistHotProducts.add(new ModelProdutList(id,
									name, description, imageurl, price,"0","Hot Product",0));
							
							arr2.add(new ModelProdutsArray(id, name, description,
									imageurl, price, "0", "Hot Product", 0));
							
							//prodId, prodName, proDesc, proImage, proPrice, proCategoryId,
							//proCatName;
							//int	count = 0
						}*/
                        //arlistHotProducts.add(new ModelProdutList("Hot Product",arr2));

                        JSONObject joProduct = jo.getJSONObject("products");
                        JSONArray jaProducts = joProduct.getJSONArray("category");

                        ArrayList<ModelProdutsArray> arrHot = new ArrayList<ModelProdutsArray>();
                        for (int i = 0; i < jaProducts.length(); i++) {
                            JSONObject joProducts = jaProducts.getJSONObject(i);
                            String categoryName = joProducts.getString("name");

                            JSONArray jaItem = joProducts.getJSONArray("items");

                            ArrayList<ModelProdutsArray> arr = new ArrayList<ModelProdutsArray>();
                            ArrayList<ModelProdutsArray> arrtotal = new ArrayList<ModelProdutsArray>();
                            for (int j = 0; j < jaItem.length(); j++) {
                                JSONObject jo1 = jaItem.getJSONObject(j);
                                String prodId = jo1.getString("ProID");
                                String prodName = jo1.getString("ProName");
                                String proDesc = jo1
                                        .getString("ProDescription");
                                String proImage = jo1.getString("ProImageUrl");
                                String proPrice = jo1.getString("ProPrice");
                                String proCategoryId = jo1
                                        .getString("ProCategoryID");
                                String proCatName = jo1
                                        .getString("ProCategoryName");
                                String isHotPro = jo1.getString("ProHot");

                                arrtotal.add(new ModelProdutsArray(prodId, prodName, proDesc, proImage, proPrice,
                                        proCategoryId, proCatName, 0, isHotPro));

                                if (isHotPro.equalsIgnoreCase("0")) {
                                    arr.add(new ModelProdutsArray(prodId, prodName, proDesc,
                                            proImage, proPrice, proCategoryId, proCatName, 0, isHotPro));
                                } else {
                                    Log.e("***", "hot pro********");
                                    arrHot.add(new ModelProdutsArray(prodId, prodName, proDesc, proImage,
                                            proPrice, proCategoryId, proCatName, 0, isHotPro));
                                }

                            }
                            if (arr.size() > 0) {
                                arlistProduct.add(new ModelProdutList(categoryName, arr));
                            }
                            arlistMyCart.add(new ModelProdutList(categoryName, arrtotal));

                        }
                        if (arrHot.size() > 0) {
                            Log.e("hot array size", "***" + arrHot.size());
                            arlistHotProducts.add(new ModelProdutList("Hot Product", arrHot));
                        }

                        adapterSep = new SeparatedListAdapter(context);
                        if (arlistProduct.size() > 0) {
                            for (int i = 0; i < arlistProduct.size(); i++) {
                                adapterSep.addSection(arlistProduct.get(i).getCategoryName(), new MyAdapter(context, R.layout.row_product_list, arlistProduct.get(i).getArrProductsByCategory()));
                            }

                        }
							

						/*adapter = new MyAdapter(StationHistoryActivity.this,
								R.layout.history_row, al);*/
                        listStore.setAdapter(adapterSep);

                        if (arlistHotProducts.size() > 0) {
                            setHotProducts();
                            header.setVisibility(View.VISIBLE);
                            rlhotLayout.setVisibility(View.VISIBLE);
                            flipHotProduct.setVisibility(View.VISIBLE);
                        } else {
                            header.setVisibility(View.GONE);
                            rlhotLayout.setVisibility(View.GONE);
                            flipHotProduct.setVisibility(View.GONE);
                        }
						
						
						/* adapter = new StoreHomeListAdapter(context,
						 R.layout.row_product_list, arlistProduct);
						 listStore.setAdapter(adapter);*/

                    } else {
                        AppUtil.showCustomToast(context, message);
                    }

                    if (arlistMyCart.size() <= 0) {
                        ((RelativeLayout) getView().findViewById(R.id.rl_nodata)).setVisibility(View.VISIBLE);
                    } else {
                        ((RelativeLayout) getView().findViewById(R.id.rl_nodata)).setVisibility(View.GONE);
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


    ///
    public class MyAdapter extends ArrayAdapter<ModelProdutsArray> {

        ArrayList<ModelProdutsArray> alist;
        int layoutId;
        private Context mContext;
        LayoutInflater mInflater;
        //OnNoteChange listener;
        boolean marker = true;

        // int posi;

        public MyAdapter(Context context, int resource,
                         ArrayList<ModelProdutsArray> objects) {
            super(context, resource, objects);
            // TODO Auto-generated constructor stub
            mContext = context;
            // this.listener=(OnNoteChange)context;
            mInflater = LayoutInflater.from(mContext);
            layoutId = resource;
            alist = objects;
        }

        class ViewHolder {

            public TextView txtInital, txtProductName, txtCount, txtPrice, txtDesc;
            ImageView imgAdd, imgAdd1, imgMinus1, imgProduct;
            LinearLayout ll_plus_minus;

            int posi;
        }

        public int getCount() {
            return alist.size();
        }

        public long getItemId(int position) {
            return 0;
        }


        public View getView(int position, View convertView, ViewGroup parent) {

            // GetterSetterHistory hgetter = getItem(position);
            ModelProdutsArray hgetter = alist.get(position);
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.row_product_list, null);
                ViewHolder holder = new ViewHolder();

                holder.txtInital = (TextView) convertView.findViewById(R.id.txt_initial);
                holder.txtProductName = (TextView) convertView.findViewById(R.id.txt_store_name);
                holder.txtCount = (TextView) convertView.findViewById(R.id.txt_nmbr);
                holder.txtPrice = (TextView) convertView.findViewById(R.id.txt_price);
                holder.txtDesc = (TextView) convertView.findViewById(R.id.txt_desc);
                holder.imgAdd = (ImageView) convertView.findViewById(R.id.img_add);
                holder.imgAdd1 = (ImageView) convertView.findViewById(R.id.img_add1);
                holder.imgMinus1 = (ImageView) convertView.findViewById(R.id.img_minus1);
                holder.ll_plus_minus = (LinearLayout) convertView.findViewById(R.id.ll_add_remove);
                holder.imgProduct = (ImageView) convertView.findViewById(R.id.img_prod_img);
                convertView.setTag(holder);
            }

            final ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.posi = position;
            if (hgetter.getProdName().length() > 1)
                holder.txtInital.setText(hgetter.getProdName().substring(0, 1));
            holder.txtProductName.setText(hgetter.getProdName());
            holder.txtPrice.setText(hgetter.getProPrice());
            holder.txtDesc.setText(hgetter.getProDesc());
            holder.txtCount.setText(hgetter.getCount() + "");

            if (alist.get(position).getCount() == 0) {
                holder.ll_plus_minus.setVisibility(View.GONE);
                holder.imgAdd.setVisibility(View.VISIBLE);
            } else {
                holder.ll_plus_minus.setVisibility(View.VISIBLE);
                holder.imgAdd.setVisibility(View.GONE);
            }

            /**add image path validation here*/

            if (!hgetter.getProImage().equalsIgnoreCase("")) {
                try {
                    holder.txtInital.setVisibility(View.GONE);
                    holder.imgProduct.setVisibility(View.VISIBLE);

                    Picasso.with(context).load(context.getString(R.string.image_base_url) + hgetter.getProImage())
                            .placeholder(R.drawable.new_placeholder)
                            .transform(new CircleTransform())
                            .resize(300, 300)
                            .into(holder.imgProduct);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                holder.txtInital.setText("" + (hgetter.getProdName()).toUpperCase().charAt(0) + "");

                holder.txtInital.setVisibility(View.VISIBLE);
                holder.imgProduct.setVisibility(View.GONE);
            }


            holder.imgAdd.setOnClickListener(new OnClickListener() {


                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    holder.ll_plus_minus.setVisibility(View.VISIBLE);
                    holder.imgAdd.setVisibility(View.GONE);

                    int newCount = alist.get(holder.posi).getCount() + 1;

                    ModelProdutsArray mod = alist.get(holder.posi);
                    mod.setCount(newCount);
                    alist.set(holder.posi, mod);
                    updateQuantity(mod.getProdId(), newCount);

                    adapterSep.notifyDataSetChanged();
                    txtTotalAmount.setText(ProductListFragment.calculateTotalBill() + "");
                    //setCartCount();
                    txtCartCount.setText(calculateCartCount() + "");

                }
            });

            holder.imgAdd1.setOnClickListener(new OnClickListener() {


                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int newCount = alist.get(holder.posi).getCount() + 1;
                    ModelProdutsArray mod = alist.get(holder.posi);
                    mod.setCount(newCount);
                    alist.set(holder.posi, mod);
                    updateQuantity(mod.getProdId(), newCount);
                    adapterSep.notifyDataSetChanged();
                    txtTotalAmount.setText(ProductListFragment.calculateTotalBill() + "");
                    //setCartCount();
                    txtCartCount.setText(calculateCartCount() + "");
                }
            });

            holder.imgMinus1.setOnClickListener(new OnClickListener() {


                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (alist.get(holder.posi).getCount() > 0) {
                        int newCount = alist.get(holder.posi).getCount() - 1;
                        ModelProdutsArray mod = alist.get(holder.posi);
                        mod.setCount(newCount);
                        alist.set(holder.posi, mod);
                        updateQuantity(mod.getProdId(), newCount);
                    } else {
                        holder.ll_plus_minus.setVisibility(View.GONE);
                        holder.imgAdd.setVisibility(View.VISIBLE);
                    }
                    adapterSep.notifyDataSetChanged();
                    txtTotalAmount.setText(ProductListFragment.calculateTotalBill() + "");
                    //setCartCount();
                    txtCartCount.setText(calculateCartCount() + "");
                }
            });


            return convertView;

        }
    }

    public static void updateQuantity(String productId, int quantity) {
        for (int i = 0; i < arlistMyCart.size(); i++) {
            for (int j = 0; j < arlistMyCart.get(i).getArrProductsByCategory().size(); j++) {
                if (arlistMyCart.get(i).getArrProductsByCategory().get(j).getProdId().equals(productId)) {
                    arlistMyCart.get(i).getArrProductsByCategory().get(j).setCount(quantity);
                }
            }
        }
    }

	/* class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

			// Swipe left (next)
			if (e1.getX() > e2.getX()) {
				flipHotProduct.showNext();
			}

			// Swipe right (previous)
			if (e1.getX() < e2.getX()) {
				flipHotProduct.showPrevious();
			}

			return super.onFling(e1, e2, velocityX, velocityY);
		}





	}*/


}
