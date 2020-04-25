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
import java.util.List;

public class BuyListAdapter extends BaseAdapter {

    ArrayList<BuyListItem> BuyItem;// = new ArrayList<>();


   public BuyListAdapter(ArrayList<BuyListItem> BuyItem) {
        this.BuyItem = BuyItem;
    }

    @Override
    public int getCount() {
        return BuyItem.size();
    }

    @Override
    public Object getItem(int position) {
        return BuyItem.get(position);
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
            convertView = li.inflate(R.layout.buy_list_item,parent,false);
        }

        TextView bli_device_name = (TextView) convertView.findViewById(R.id.bli_device_name);
        TextView bli_device_price = (TextView) convertView.findViewById(R.id.bli_device_price);
        TextView bli_device_condition = (TextView) convertView.findViewById(R.id.bli_device_condition);
        //TextView bli_time = (TextView) convertView.findViewById(R.id.bli_time);
        TextView bli_coast = (TextView) convertView.findViewById(R.id.bli_coast);
        //ImageView bli_device_image = (ImageView) convertView.findViewById(R.id.bli_device_image);

        BuyListItem b = BuyItem.get(position);

        bli_device_name.setText(b.getDevice_name());
        bli_device_price.setText(b.getRegister_number());
        bli_device_condition.setText(b.getDevice_condition());
        //bli_time.setText(b.getTime());
        bli_coast.setText(b.getCoast());
        //bli_device_image.setImageDrawable(b.getD());

        return convertView;
    }
    //순서 : 이름 가격 상태 그림
    /*public void addBuyItem(Drawable d,String name, String price, String condition){
        BuyListItem b = new BuyListItem(d,name,price,condition);

        b.setD(d);
        b.setDevice_condition(condition);
        b.setDevice_name(name);
        b.setDevice_price(price);

        BuyItem.add(b);
    }*/
}
