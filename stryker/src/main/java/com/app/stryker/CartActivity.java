package com.app.stryker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.adapters.SeparatedListAdapterCart;
import com.app.fragments.ProductListFragment;
import com.app.model.ModelProdutsArray;
import com.app.utill.AppUtil;
import com.app.utill.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartActivity extends Activity {

    //public static ArrayList<ModelProdutList> arlistMyCart = new ArrayList<ModelProdutList>();
    Context context;
    SeparatedListAdapterCart adapterSep;
    ListView listStore;
    TextView txtCheckOut;
    ProgressDialog dialogLoader;
    String storeID = "";


    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        context = this;

        listStore = (ListView) findViewById(R.id.lv_cart_details);
        init();
        setData();

        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("store_id")) {
                storeID = b.getString("store_id");
            }
        }

    }

    public void init() {
        txtCheckOut = (TextView) findViewById(R.id.txt_checkout);
        txtCheckOut.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (listStore.getAdapter().getCount() > 0) {
                    Intent i = new Intent(CartActivity.this, ActivityAddress.class);
                    i.putExtra("store_id", storeID);
                    startActivity(i);
                } else {
                    AppUtil.showCustomToast(context, "Please Add Products To Your Cart");
                }
                //finish();

            }
        });
        ((ImageView) findViewById(R.id.img_back)).setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                //amr
                setResult(Activity.RESULT_OK);

                finish();


            }
        });
    }

    public void setData() {
        /*ArrayList<ModelProdutList> arlistP = new ArrayList<ModelProdutList>();
		arlistP = ProductListFragment.arlistProduct;		 
		 for(int i = 0; i < arlistP.size(); i++) {
			 for(int j = 0;j < arlistP.get(i).getArrProductsByCategory().size(); j++){
				if(arlistP.get(i).getArrProductsByCategory().get(j).getCount() > 0){
					//ArrayList<ModelProdutsArray> arr = new ArrayList<ModelProdutsArray>();
					//arlistP.add(new ModelProdutList((arlistP.get(i).getCategoryName(), arrProductsByCategory))
				}else{
					arlistP.get(i).getArrProductsByCategory().remove(j);
				}
			 }
		}		 
		 arlistMyCart =  arlistP;*/

        ((TextView) findViewById(R.id.txt_price_count))
                .setText(ProductListFragment.calculateTotalBill() + "");
        ((TextView) findViewById(R.id.txt_cart_count)).
                setText(ProductListFragment.calculateCartCount() + "");

        adapterSep = new SeparatedListAdapterCart(context);
		
	/*	if(ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory().size() > 0){
			for (int i = 0; i < ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory().size(); i++) {
				if(ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory().get(i).getCount() > 0){
					adapterSep.addSection(ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory().get(i).getProCatName()+"*",
							new MyAdapter2(context,R.layout.row_product_list,ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory()));
				}
			}
				
		}*/

        if (ProductListFragment.arlistMyCart.size() > 0) {
            for (int i = 0; i < ProductListFragment.arlistMyCart.size(); i++) {
                ArrayList<ModelProdutsArray> arlistTemp = new ArrayList<ModelProdutsArray>();
                for (int j = 0; j < ProductListFragment.arlistMyCart.get(i).getArrProductsByCategory().size(); j++) {
                    if (ProductListFragment.arlistMyCart.get(i).getArrProductsByCategory().get(j).getCount() > 0) {
                        arlistTemp.add(ProductListFragment.arlistMyCart.get(i).getArrProductsByCategory().get(j));
                    }
                }
                if (arlistTemp.size() > 0) {
                    adapterSep.addSection(ProductListFragment.arlistMyCart.get(i).getCategoryName(),
                            new MyAdapter2(context, R.layout.row_product_list, arlistTemp));
                    //arlistTemp.clear();
                }

            }
        }
			
		
			

		/*adapter = new MyAdapter(StationHistoryActivity.this,
				R.layout.history_row, al);*/
        listStore.setAdapter(adapterSep);
        if (listStore.getAdapter().getCount() <= 0) {
            ((RelativeLayout) findViewById(R.id.rl_nodata)).setVisibility(View.VISIBLE);
        } else {
            ((RelativeLayout) findViewById(R.id.rl_nodata)).setVisibility(View.GONE);
        }
    }


    public class MyAdapter2 extends ArrayAdapter<ModelProdutsArray> {

        ArrayList<ModelProdutsArray> alist;
        int layoutId;
        private Context mContext;
        LayoutInflater mInflater;
        //OnNoteChange listener;
        boolean marker = true;

        // int posi;

        public MyAdapter2(Context context, int resource,
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
            ImageView imgAdd, imgAdd1, imgMinus1, imgDeleteProd, ImgProduct;
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
                holder.imgDeleteProd = (ImageView) convertView.findViewById(R.id.img_delete);
                holder.ll_plus_minus = (LinearLayout) convertView.findViewById(R.id.ll_add_remove);
                holder.ImgProduct = (ImageView) convertView.findViewById(R.id.img_prod_img);
                convertView.setTag(holder);
            }

            final ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.posi = position;
            if (hgetter.getProdName().length() > 0)
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
            holder.imgDeleteProd.setVisibility(View.VISIBLE);


            /**add image path validation here*/

            if (!hgetter.getProImage().equalsIgnoreCase("")) {
                try {
                    holder.txtInital.setVisibility(View.GONE);
                    holder.ImgProduct.setVisibility(View.VISIBLE);

                    Picasso.with(context).load(context.getString(R.string.image_base_url) + hgetter.getProImage())
                            .placeholder(R.drawable.new_placeholder)
                            .transform(new CircleTransform())
                            .resize(300, 300)
                            .into(holder.ImgProduct);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                holder.txtInital.setText("" + (hgetter.getProdName()).toUpperCase().charAt(0) + "");

                holder.txtInital.setVisibility(View.VISIBLE);
                holder.ImgProduct.setVisibility(View.GONE);
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
                    updateProductList(mod, newCount);
				
					
				/*for(int i= 0; i < arlistMyCart.size(); i ++){
						
					if(mod.getProCatName().equalsIgnoreCase(arlistMyCart.get(i).getCategoryName())){
						arlistMyCart.get(i).getArrProductsByCategory();
						ArrayList<ModelProdutsArray> arr = new ArrayList<ModelProdutsArray>();
						arr.add(new ModelProdutsArray(mod.getProdId(), mod.getProCatName(), mod.getProDesc(), mod.getProImage()
								, mod.getProPrice(), mod.getProCategoryId(), mod.getProCatName(), mod.getCount()));	
						ModelP}else{
						//alist.remove(holder.posi);
						//holder.ll_plus_minus.setVisibility(View.GONE);
						//holder.imgAdd.setVisibility(View.VISIBLE);
					}rodutList modu = new ModelProdutList(mod.getProCatName(),arr);						
						arlistMyCart.set(i, modu);
						
					}
					}
					ArrayList<ModelProdutsArray> arr = new ArrayList<ModelProdutsArray>();
					arr.add(new ModelProdutsArray(mod.getProdId(), mod.getProCatName(), mod.getProDesc(), mod.getProImage()
							, mod.getProPrice(), mod.getProCategoryId(), mod.getProCatName(), mod.getCount()));				
					arlistMyCart.add(new ModelProdutList(mod.getProCatName(), arr));
					
					*/
                    adapterSep.notifyDataSetChanged();
                    //calculateTotalBill();
                    //setCartCount();

                    //arlistMyCart.add(new ModelProdutList(alist.get(holder.posi).getProCatName(), arrProductsByCategory));

                }
            });

            holder.imgAdd1.setOnClickListener(new OnClickListener() {


                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int newCount = alist.get(holder.posi).getCount() + 1;
                    ModelProdutsArray mod = alist.get(holder.posi);
                    mod.setCount(newCount);
                    alist.set(holder.posi, mod);
                    updateProductList(mod, newCount);
                    adapterSep.notifyDataSetChanged();

                    ((TextView) findViewById(R.id.txt_price_count))
                            .setText(ProductListFragment.calculateTotalBill() + "");
                    //calculateTotalBill();
                    //setCartCount();
                    ((TextView) findViewById(R.id.txt_cart_count)).setText(ProductListFragment.calculateCartCount() + "");
                }
            });

            holder.imgMinus1.setOnClickListener(new OnClickListener() {


                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (alist.get(holder.posi).getCount() >= 1) {
                        int newCount = alist.get(holder.posi).getCount() - 1;
                        ModelProdutsArray mod = alist.get(holder.posi);
                        mod.setCount(newCount);
                        alist.set(holder.posi, mod);
                        updateProductList(mod, newCount);
                        if (newCount == 0) {
                            setData();
                        } else {
                            adapterSep.notifyDataSetChanged();
                        }
                        ((TextView) findViewById(R.id.txt_price_count))
                                .setText(ProductListFragment.calculateTotalBill() + "");
                        ((TextView) findViewById(R.id.txt_cart_count)).setText(ProductListFragment.calculateCartCount() + "");
                    }
                    //calculateTotalBill();
                    //setCartCount();
                }
            });

            holder.imgDeleteProd.setOnClickListener(new OnClickListener() {


                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //Toast.makeText(context, holder.posi+"**", 2000).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure ? want to remove product.")
                            .setCancelable(false)
                            .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int newCount = 0;
                                    ModelProdutsArray mod = alist.get(holder.posi);
                                    mod.setCount(newCount);
                                    alist.set(holder.posi, mod);
                                    updateProductList(mod, newCount);
                                    setData();

                                    ((TextView) findViewById(R.id.txt_price_count))
                                            .setText(ProductListFragment.calculateTotalBill() + "");
                                    ((TextView) findViewById(R.id.txt_cart_count)).setText(ProductListFragment.calculateCartCount() + "");
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
            });


            return convertView;

        }
    }

    public static void updateProductList(ModelProdutsArray mod, int Quantity) {
        Log.e("isHot pro mod", mod.isHotProduct() + "");
        if (mod.isHotProduct().equalsIgnoreCase("1")) {
            for (int i = 0; i < ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory().size(); i++) {
                for (int j = 0; j < ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory().size(); j++) {
                    if (ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory().get(j).getProdId().equals(mod.getProdId())) {
                        ProductListFragment.arlistHotProducts.get(0).getArrProductsByCategory().get(j).setCount(Quantity);
                        Log.e("pro hot update", "***");
                    }
                }
            }
        } else {
            for (int i = 0; i < ProductListFragment.arlistProduct.size(); i++) {
                for (int j = 0; j < ProductListFragment.arlistProduct.get(i).getArrProductsByCategory().size(); j++) {
                    if (ProductListFragment.arlistProduct.get(i).getArrProductsByCategory().get(j).getProdId().equals(mod.getProdId())) {
                        ProductListFragment.arlistProduct.get(i).getArrProductsByCategory().get(j).setCount(Quantity);
                        Log.e("pro update", "***");
                    }
                }
            }
        }
        for (int i = 0; i < ProductListFragment.arlistMyCart.size(); i++) {
            for (int j = 0; j < ProductListFragment.arlistMyCart.get(i).getArrProductsByCategory().size(); j++) {
                if (ProductListFragment.arlistMyCart.get(i).getArrProductsByCategory().get(j).getProdId().equals(mod.getProdId())) {
                    ProductListFragment.arlistMyCart.get(i).getArrProductsByCategory().get(j).setCount(Quantity);
                    Log.e("pro complete", "***");
                }
            }

        }
    }
}
