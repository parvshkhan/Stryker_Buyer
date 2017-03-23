package com.app.model;

import java.util.List;

/**
 * Created by Inflac on 10-12-2016.
 */
public class Store {

    public int Id;
    public String Name;
    public String BusinessName;
    public String Description;
    public String StoreCode;
    public String Address;
    public String Phone;
    public String Mobile;
    public String Email;
    public String BroucherUrl;
    public String OpenningTime;
    public String ClosingTime;
    public String Latitude;
    public String Longitude;
    public Object CraetedBy;
    public String DateCreated;
    public int IsActive;
    public int IsDeleted;
    public int IsMonOpen;
    public int IsTueOpen;
    public int IsWedOpen;
    public int IsThuOpen;
    public int IsFriOpen;
    public int IsSatOpen;
    public int IsSunOpen;
    public int IsDeliveryAvailable;
    public String StandardDeliveryTime;
    public String Distance;
    public User Seller;
    public List<StoreGallery> StoreGallery;
    public String MinimumOrderValue;
    public String logo;
    public Object IndustryType;
    public int IsHotProductsPublished;
    public int UserCount;
    public String franchiseCode;
    public String AboutUs;
    //new StoreHomeListModel(storeId, imageUrl, storeName,seller_id,sellerChat_id)


}
