package com.app.model;

/**
 * Created by Inflectica on 10/29/2015.
 */
public class NotificationModel {

    String notificationId, notificationDate, notficiationDesc;
    String userName, userProfilePic, userOrderId, totalPrice, Quantity, notTypeId;
    Boolean isShowHeader;

    public NotificationModel(String notificationDate, String notificationId, String notficiationDesc, String notTypeId, String userName,
                             String userProfilePic, String totalPrice, String quantity, Boolean isShowHeader) {

        this.notificationDate = notificationDate;
        this.notificationId = notificationId;
        this.notficiationDesc = notficiationDesc;
        this.userName = userName;
        this.userProfilePic = userProfilePic;
        this.totalPrice = totalPrice;
        Quantity = quantity;
        this.notTypeId = notTypeId;
        this.isShowHeader = isShowHeader;
    }

    public NotificationModel(String notificationId, String notificationDate, String notficiationDesc) {
        this.notificationId = notificationId;
        this.notificationDate = notificationDate;
        this.notficiationDesc = notficiationDesc;
    }

    public Boolean getIsShowHeader() {
        return isShowHeader;
    }

    public void setIsShowHeader(Boolean isShowHeader) {
        this.isShowHeader = isShowHeader;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public String getUserOrderId() {
        return userOrderId;
    }

    public void setUserOrderId(String userOrderId) {
        this.userOrderId = userOrderId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getNotTypeId() {
        return notTypeId;
    }

    public void setNotTypeId(String notTypeId) {
        this.notTypeId = notTypeId;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getNotficiationDesc() {
        return notficiationDesc;
    }

    public void setNotficiationDesc(String notficiationDesc) {
        this.notficiationDesc = notficiationDesc;
    }
}
