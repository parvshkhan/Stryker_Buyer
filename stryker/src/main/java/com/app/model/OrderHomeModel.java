package com.app.model;

import org.json.JSONArray;

import java.util.ArrayList;

public class OrderHomeModel {
    String orderId, store_id, storeName, orderTime, orderDate, expectedDate,
            expectedTime, totalPrice, categoryName, imageUrl, orderDateTime, OrderType;
    ArrayList<Item> orderDetails;

    public String getOrderDateTime() {


        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {

        this.orderDateTime = orderDateTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    JSONArray categoryJosnArray;


    public OrderHomeModel(String storeName, String orderId, String orderTime, String categoryName, String orderDate) {

        super();
        this.storeName = storeName;
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.categoryName = categoryName;
        this.orderDate = orderDate;

    }

	/*public OrderHomeModel(String storeName, String desc, String, String price, String count){


	}
*/

    public OrderHomeModel(String orderId, String store_id, String storeName,
                          String orderTime, String orderDate, String expectedDate,
                          String expectedTime, String totalPrice, String categoryName,
                          JSONArray categoryJosnArray, String imageUrl, String orderDateTime, ArrayList<Item> orderDetails, String OrderType) {
        super();
        this.orderId = orderId;
        this.store_id = store_id;
        this.storeName = storeName;
        this.orderTime = orderTime;
        this.orderDate = orderDate;
        this.expectedDate = expectedDate;
        this.expectedTime = expectedTime;
        this.totalPrice = totalPrice;
        this.categoryName = categoryName;
        this.categoryJosnArray = categoryJosnArray;
        this.imageUrl = imageUrl;
        this.orderDateTime = orderDateTime;
        this.orderDetails = orderDetails;
        this.OrderType = OrderType;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public ArrayList<Item> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(ArrayList<Item> orderDetails) {
        this.orderDetails = orderDetails;
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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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

    public JSONArray getCategoryJosnArray() {
        return categoryJosnArray;
    }

    public void setCategoryJosnArray(JSONArray categoryJosnArray) {
        this.categoryJosnArray = categoryJosnArray;
    }

	/*public String ConverTimeDate(String dateTime){	
		Log.e("Datetime----------->",dateTime);
		
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		
		//DateFormat outputFormat = new SimpleDateFormat("MMM dd, yyy h:mm a");
		Date d = new Date();
		try {
			if(dateTime ==null|| dateTime=="" || dateTime.equals(null)){
				dateTime = d.toString();
				Log.e("Datetime- Null---------->",dateTime);
			}
			d = dateFormatGmt.parse(dateTime);
			d.setTime(d.getTime() + 60000*330);
			//Log.e("dsf", d.getDay()+"-"+d.getMonth() +"-"+ d.getYear());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		Date date = new Date();
		try {
			date = outputFormat.parse(d.toString());
			date.setTime(date.getTime() + 60000 * 330);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.e("Datetime1----------->",d.toString());

		return  d.toString().replace("GMT+05:30", "");
		
		//return dateTime;
	}*/

}
