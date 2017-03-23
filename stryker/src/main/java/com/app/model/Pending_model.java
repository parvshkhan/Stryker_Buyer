package com.app.model;

/**
 * Created by Inflac on 13-10-2015.
 */
public class Pending_model {

    String txt_store_name, txt_desc;
    String txt_count;
    String txt_price;


    public Pending_model() {
    }

    public Pending_model(String txt_store_name, String txt_desc,
                         String txt_count, String txt_price) {
        super();
        this.txt_store_name = txt_store_name;
        this.txt_desc = txt_desc;
        this.txt_count = txt_count;
        this.txt_price = txt_price;
    }


    public String getTxt_store_name() {
        return txt_store_name;
    }

    public void setTxt_store_name(String txt_store_name) {
        this.txt_store_name = txt_store_name;
    }

    public String getTxt_desc() {
        return txt_desc;
    }

    public void setTxt_desc(String txt_desc) {
        this.txt_desc = txt_desc;
    }

    public String getTxt_count() {
        return txt_count;
    }

    public void setTxt_count(String txt_count) {
        this.txt_count = txt_count;
    }

    public String getTxt_price() {
        return txt_price;
    }

    public void setTxt_price(String txt_price) {
        this.txt_price = txt_price;
    }


}
