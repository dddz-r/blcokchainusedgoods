package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//톡방 안에 내용임!
public class TalkAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    ArrayList<TalkItem> talkItems = new ArrayList<>();
    private int id;

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
            TalkItem t = talkItems.get(position);

            //message.getSender().getUserId().equals(SendBird.getCurrentUser().getUserId())
            //if(t.getOwner_id().equals(id)){
                if(id==0){
                convertView = li.inflate(R.layout.sent_talk,parent,false);
                TextView contents = (TextView) convertView.findViewById(R.id.sent_talk_contents);
                TextView time = (TextView) convertView.findViewById(R.id.sent_talk_time);

                contents.setText(t.getContents());
                time.setText(t.getTime());


            }else {
                convertView = li.inflate(R.layout.received_talk,parent,false);
                TextView opposit_id = (TextView) convertView.findViewById(R.id.received_talk_opposit);
                TextView contents = (TextView) convertView.findViewById(R.id.received_talk_contents);
                TextView time = (TextView) convertView.findViewById(R.id.received_talk_time);

                opposit_id.setText(t.getOpposit_id());
                contents.setText(t.getContents());
                time.setText(t.getTime());
            }


    }
        return convertView;


    }


    public void addMyTalkItem(String owner_id, String opposit_id, String contents, String time){
        TalkItem t = new TalkItem(owner_id,opposit_id,contents,time);

        id=0;
        t.setOwner_id(owner_id);
        t.setOpposit_id(opposit_id);
        t.setContents(contents);
        t.setTime(time);


        talkItems.add(t);
    }

    public void addOppositTalkItem(String owner_id, String opposit_id, String contents, String time){
        TalkItem t = new TalkItem(owner_id,opposit_id,contents,time);

        t.setOwner_id(owner_id);
        t.setOpposit_id(opposit_id);
        t.setContents(contents);
        t.setTime(time);

        talkItems.add(t);
    }
}
