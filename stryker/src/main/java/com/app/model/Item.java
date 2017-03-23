package com.app.model;

/**
 * Created by Inflac on 10-12-2016.
 */
public class Item {

    public String OrderID;
    public String ProductID;
    public String Quantity;
    public String TotalCost;
    public String ProID;
    public String ProHot;
    public String ProName;
    public String ProDescription;
    public String ProCategoryID;
    public String ProStoreID;
    public String ProImageUrl;
    public String ProCategoryName;

    public Item(String productID, String quantity, String totalCost, String proName, String proDescription, String proCategoryName) {
        ProductID = productID;
        Quantity = quantity;
        TotalCost = totalCost;
        ProName = proName;
        ProDescription = proDescription;
        ProCategoryName = proCategoryName;
    }
}
