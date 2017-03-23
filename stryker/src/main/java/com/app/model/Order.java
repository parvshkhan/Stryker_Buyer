package com.app.model;

import java.util.List;

/**
 * Created by MIHIR on 1/13/2017.
 */

public class Order {
    public String StoreName;
    public String orderId, store_id, storeName, orderTime, orderDate, expectedDate,
            expectedTime, totalPrice, categoryName, imageUrl, orderDateTime;

    public List<ModelProdutsArray> OrderDetails;

    public Order(String storeName, String orderId, String store_id, String storeName1, String orderTime, String orderDate,
                 String expectedDate, String expectedTime, String totalPrice, String categoryName, String imageUrl,
                 String orderDateTime, List<ModelProdutsArray> orderDetails) {
        StoreName = storeName;
        this.orderId = orderId;
        this.store_id = store_id;
        this.storeName = storeName1;
        this.orderTime = orderTime;
        this.orderDate = orderDate;
        this.expectedDate = expectedDate;
        this.expectedTime = expectedTime;
        this.totalPrice = totalPrice;
        this.categoryName = categoryName;
        this.imageUrl = imageUrl;
        this.orderDateTime = orderDateTime;
        OrderDetails = orderDetails;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate;
    }

    public String getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(String expectedTime) {
        this.expectedTime = expectedTime;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public List<ModelProdutsArray> getOrderDetails() {
        return OrderDetails;
    }

    public void setOrderDetails(List<ModelProdutsArray> orderDetails) {
        OrderDetails = orderDetails;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }
}
