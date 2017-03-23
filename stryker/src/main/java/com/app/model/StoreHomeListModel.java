package com.app.model;

public class StoreHomeListModel {

    String srore_id, store_image, store_name, seller_id, sellerChatId;

    public String getSellerChatId() {
        return sellerChatId;
    }

    public void setSellerChatId(String sellerChatId) {
        this.sellerChatId = sellerChatId;
    }

    public StoreHomeListModel(String srore_id, String store_image,
                              String store_name, String seller_id, String sellerChatId) {
        super();
        this.srore_id = srore_id;
        this.store_image = store_image;
        this.store_name = store_name;
        this.seller_id = seller_id;
        this.sellerChatId = sellerChatId;
    }

    public String getSrore_id() {
        return srore_id;
    }

    public String getseller_id() {
        return seller_id;
    }

    public void setSrore_id(String srore_id) {
        this.srore_id = srore_id;
    }

    public String getStore_image() {
        return store_image;
    }

    public void setStore_image(String store_image) {
        this.store_image = store_image;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }
}
