package com.example.myapplication;

import android.graphics.drawable.Drawable;

public class BuyListItem {
    private String bli_device_name;
    private String bli_device_price;
    private String bli_device_condition;
    private Drawable bli_d;


    public void setD(Drawable d) {
        this.bli_d = d;
    }

    public void setDevice_condition(String device_condition) {
        this.bli_device_condition = device_condition;
    }

    public void setDevice_name(String device_name) {
        this.bli_device_name = device_name;
    }

    public void setDevice_price(String device_price) {
        this.bli_device_price = device_price;
    }

    public Drawable getD() {
        return bli_d;
    }

    public String getDevice_condition() {
        return bli_device_condition;
    }

    public String getDevice_name() {
        return bli_device_name;
    }

    public String getDevice_price() {
        return bli_device_price;
    }
}
