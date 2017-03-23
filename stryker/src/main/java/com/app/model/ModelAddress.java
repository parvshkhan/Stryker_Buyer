package com.app.model;

/**
 * Created by Inflac on 14-10-2015.
 */
public class ModelAddress {

    String id, address_line1, address_line2, country, state, city, pin;

    public ModelAddress(String id, String address_line1, String address_line2,
                        String country, String state, String city, String pin) {
        super();
        this.id = id;
        this.address_line1 = address_line1;
        this.address_line2 = address_line2;
        this.country = country;
        this.state = state;
        this.city = city;
        this.pin = pin;
    }

    public ModelAddress(String address_line1, String address_line2,
                        String country, String state, String city, String pin) {
        super();
        this.address_line1 = address_line1;
        this.address_line2 = address_line2;
        this.country = country;
        this.state = state;
        this.city = city;
        this.pin = pin;
    }

    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress_line1() {
        return address_line1;
    }

    public void setAddress_line1(String address_line1) {
        this.address_line1 = address_line1;
    }

    public String getAddress_line2() {
        return address_line2;
    }

    public void setAddress_line2(String address_line2) {
        this.address_line2 = address_line2;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }


}
