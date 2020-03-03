package com.example.myapplication;

import android.graphics.drawable.Drawable;

public class MainGridItem {

    private String grid_item_name;
    private String grid_item_price;
    private Drawable grid_item_img;
    //private String grid_item_category;

    public MainGridItem( Drawable grid_item_img,String grid_item_name, String grid_item_price){
        this.grid_item_img = grid_item_img;
        this.grid_item_name=grid_item_name;
        this.grid_item_price=grid_item_price;
        //this.grid_item_category=grid_item_category;

    }

    public void setGrid_item_name(String grid_item_name){
        this.grid_item_name=grid_item_name;
    }

    public void setGrid_item_price(String grid_item_price){
        this.grid_item_price=grid_item_price;
    }

    public void setGrid_item_img(Drawable grid_item_img){
        this.grid_item_img=grid_item_img;
    }

    //public void setGrid_item_category(String grid_item_category){this.grid_item_category=grid_item_category};

    public Drawable getGrid_item_img(){
        return grid_item_img;
    }

    public String getGrid_item_name(){
        return grid_item_name;
    }

    public String getGrid_item_price(){
        return grid_item_price;
    }

    //public String getGrid_item_category(){return grid_item_category};

}
