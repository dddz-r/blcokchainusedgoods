package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainGridAdapter extends BaseAdapter {

    ArrayList<MainGridItem> GridItem;//= new ArrayList<>();

    /*private  int icons[];
    private  String name[];
    private Context context;
    private LayoutInflater inflater;*/

    public MainGridAdapter(ArrayList<MainGridItem> GridItem){
        this.GridItem = GridItem;
    }

    @Override
    public int getCount() {
        return GridItem.size();
    }

    @Override
    public Object getItem(int position) {
        return GridItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context c = parent.getContext();

        if(convertView == null){
            LayoutInflater li = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.main_grid_item,parent,false);
        }

        TextView  grid_item_name = (TextView) convertView.findViewById(R.id. grid_item_name);
        TextView grid_item_price = (TextView) convertView.findViewById(R.id.grid_item_price);
        //ImageView grid_item_img = (ImageView) convertView.findViewById(R.id.grid_item_img);

        MainGridItem g = GridItem.get(position);

        grid_item_name.setText(g.getGrid_item_name());
        grid_item_price.setText(g.getGrid_item_price());
        //grid_item_img.setImageDrawable(g.getGrid_item_img());

        return convertView;
    }

    //순서 : 그림 이름 가격
    public void addGridItem(String register_number,String grid_item_name, String grid_item_price){
        MainGridItem g = new MainGridItem(register_number,grid_item_name,grid_item_price);

        g.setRegister_number(register_number);
        //g.setGrid_item_img(grid_item_img);
        g.setGrid_item_name(grid_item_name);
        g.setGrid_item_price(grid_item_price);
        //g.setGrid_item_category(grid_item_category);

        GridItem.add(g);
    }
}

