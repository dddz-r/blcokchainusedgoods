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

//톡방 안에 내용임!
public class TalkAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    ArrayList<TalkItem> talkItems = new ArrayList<>();
    private LayoutInflater inflater;


 /*   public ChatAdapter() {
        this.context = applicationContext;
        this.layout = talklist;
        this.chatData = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.id= id;
    }*/

    @Override
    public int getCount() {
        return talkItems.size();
    }

    @Override
    public Object getItem(int position) {
        return talkItems.get(position);
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
            convertView = li.inflate(R.layout.talk_contents_item,parent,false);
        }

        TextView tci_opposit_id = (TextView) convertView.findViewById(R.id.tci_opposit_id);
        TextView tci_contents = (TextView) convertView.findViewById(R.id.tci_contents);
        TextView tci_time = (TextView) convertView.findViewById(R.id.tci_time);

        TalkItem t = talkItems.get(position);

        tci_opposit_id.setText(t.getOpposit_id());
        tci_contents.setText(t.getContents());
        tci_time.setText(t.getTime());

        return convertView;


    }

    //순서 : 그림 이름 가격 상태
    public void addTalkItem(String owner_id, String opposit_id, String contents, String time){
        TalkItem t = new TalkItem(owner_id,opposit_id,contents,time);

        t.setOwner_id(owner_id);
        t.setOpposit_id(opposit_id);
        t.setContents(contents);
        t.setTime(time);

        talkItems.add(t);
    }
}
