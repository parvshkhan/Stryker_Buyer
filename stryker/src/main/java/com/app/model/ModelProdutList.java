package com.app.model;

import java.util.ArrayList;

public class ModelProdutList extends SProduct {

    String categoryName;
    ArrayList<ModelProdutsArray> arrProductsByCategory;

    public ModelProdutList() {

    }

    public ModelProdutList(String categoryName,
                           ArrayList<ModelProdutsArray> arrProductsByCategory) {
        super();
        this.categoryName = categoryName;
        this.arrProductsByCategory = arrProductsByCategory;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<ModelProdutsArray> getArrProductsByCategory() {
        return arrProductsByCategory;
    }

    public void setArrProductsByCategory(
            ArrayList<ModelProdutsArray> arrProductsByCategory) {
        this.arrProductsByCategory = arrProductsByCategory;
    }

}
