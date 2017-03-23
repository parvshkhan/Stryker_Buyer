package com.app.model;

public class ModelProdutsArray {

    public ModelProdutsArray() {
        super();
    }

    String prodId, prodName, proDesc, proImage, proPrice, proCategoryId,
            proCatName;
    int count = 0;
    String isHotProduct;

    public String isHotProduct() {
        return isHotProduct;
    }

    public void setHotProduct(String isHotProduct) {
        this.isHotProduct = isHotProduct;
    }

    int productCost;


    public float getProductCost() {

        try {
            return Float.parseFloat(this.proPrice) * this.count;
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    public ModelProdutsArray(String prodId, String prodName, String proDesc,
                             String proImage, String proPrice, String proCategoryId,
                             String proCatName, int count, String isHotProduct) {
        super();
        this.prodId = prodId;
        this.prodName = prodName;
        this.proDesc = proDesc;
        this.proImage = proImage;
        this.proPrice = proPrice;
        this.proCategoryId = proCategoryId;
        this.proCatName = proCatName;
        this.count = count;
        this.isHotProduct = isHotProduct;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProDesc() {
        return proDesc;
    }

    public void setProDesc(String proDesc) {
        this.proDesc = proDesc;
    }

    public String getProImage() {
        return proImage;
    }

    public void setProImage(String proImage) {
        this.proImage = proImage;
    }

    public String getProPrice() {
        return proPrice;
    }

    public void setProPrice(String proPrice) {
        this.proPrice = proPrice;
    }

    public String getProCategoryId() {
        return proCategoryId;
    }

    public void setProCategoryId(String proCategoryId) {
        this.proCategoryId = proCategoryId;
    }

    public String getProCatName() {
        return proCatName;
    }

    public void setProCatName(String proCatName) {
        this.proCatName = proCatName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public String toString() {
        // TODO Auto-generated method stub
        return this.getProdId();
    }
}
