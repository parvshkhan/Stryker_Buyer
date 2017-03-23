package com.app.model;

public class StoreListModel {
    String storeId, storeImage, storeName, storeCode, storeAddress, storeDescription, Industrytypeid, IndustryName;

    public StoreListModel() {
    }

    public StoreListModel(String storeId, String storeImage, String storeName,
                          String storeCode, String storeAddress, String storeDescription, String industrytypeid, String IndustryName) {
        super();
        this.storeId = storeId;
        this.storeImage = storeImage;
        this.storeName = storeName;
        this.storeCode = storeCode;
        this.storeAddress = storeAddress;
        this.storeDescription = storeDescription;
        this.Industrytypeid = industrytypeid;
        this.IndustryName = IndustryName;

    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreDescription() {
        return storeDescription;
    }

    public void setStoreDescription(String storeDescription) {
        this.storeDescription = storeDescription;
    }

    public String getStoreindustrytypeid() {
        return Industrytypeid;
    }

    public void setStoreindustrytypeid(String storeindustrytypeid) {
        this.Industrytypeid = storeindustrytypeid;
    }

    public String getStoreindustrytypeName() {
        return IndustryName;
    }

    public void setStoreindustrytypeName(String IndustryName) {
        this.IndustryName = IndustryName;
    }


}
