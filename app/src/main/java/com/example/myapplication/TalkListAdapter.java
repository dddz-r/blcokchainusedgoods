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

public class TalkListAdapter extends BaseAdapter {

    ArrayList<TalkListItem> TalkListItem;//= new ArrayList<>();

    public TalkListAdapter(ArrayList<TalkListItem> TalkListItem){
        this.TalkListItem = TalkListItem;
    }
    @Override
    public int getCount() {
        return TalkListItem.size();
    }

    @Override
    public Object getItem(int position) {
        return TalkListItem.get(position);
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
            convertView = li.inflate(R.layout.talk_list_item,parent,false);
        }

        TextView tli_opposit_id = (TextView) convertView.findViewById(R.id.tli_opposit_id);
        TextView tli_date = (TextView) convertView.findViewById(R.id.tli_date);

        TalkListItem t = TalkListItem.get(position);

        tli_opposit_id.setText(t.getOpposit_id());
        tli_date.setText(t.getTime());

        return convertView;
    }
    //순서 : 그림 이름 가격 상태
    public void addTalkListItem(String opposit_id, String time){
        TalkListItem t = new TalkListItem(opposit_id, time);

        t.setOpposit_id(opposit_id);
        t.setTime(time);

        TalkListItem.add(t);
    }
}
