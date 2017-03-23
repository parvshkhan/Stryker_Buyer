package com.app.model;

public class HotProductListModel extends SProduct {

    String id, name, description, imageUrl;
    int price = 0, count = 0;

    int productCost;

    public int getProductCost() {

        try {
            return this.price * this.count;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public HotProductListModel() {
        // TODO Auto-generated constructor stub
    }

    public HotProductListModel(String id, String name, String description,
                               String imageUrl, int price, int count) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
