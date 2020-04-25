package com.example.myapplication;

import android.graphics.drawable.Drawable;

public class BuyListItem {
    private String bli_register_number;
    private String bli_device_name;
    private String bli_device_condition;
    //private String time;
    private String coast;
    //private Drawable bli_d; //private int bli_d;

    //순서 : 이름 가격 상태 그림
    public BuyListItem(String bli_register_number,String bli_device_name, String bli_device_condition, String coast){
        //this.bli_d=bli_d;
        this.bli_device_condition=bli_device_condition;
        this.bli_device_name=bli_device_name;
        this.bli_register_number=bli_register_number;
        //this.time = time;
        this.coast = coast;
    }


    //public void setD(Drawable d) { this.bli_d = d; }

    public void setDevice_condition(String device_condition) {
        this.bli_device_condition = device_condition;
    }

    public void setDevice_name(String device_name) {
        this.bli_device_name = device_name;
    }

    public void setRegister_number(String register_number) {
        this.bli_register_number = register_number;
    }

    /*public void setTime(String time){
        this.time = time;
    }*/

    public void setCoast(String coast){
        this.coast=coast;
    }

    //public Drawable getD() { return bli_d; }

    public String getDevice_condition() {
        return bli_device_condition;
    }

    public String getDevice_name() {
        return bli_device_name;
    }

    public String getRegister_number() {
        return bli_register_number;
    }

    //public String getTime(){ return time;}
    public String getCoast(){return coast;}
}
