package com.example.weiver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeSet;

//톡방 안에 내용임!
public class TalkAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    ArrayList<TalkItem> talkItems = new ArrayList<>();

    private  static  final  int TYPE_ITEM =  0 ;
    private  static  final  int TYPE_SEPARATOR =  1 ;
    private  static  final  int TYPE_MAX_COUNT = TYPE_SEPARATOR +  1 ;
    private TreeSet mSeparatorsSet = new TreeSet();

    /*public TalkAdapter(ArrayList<TalkItem> talkItmes){
        this.talkItems = talkItmes;
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
    public int getItemViewType(int position) {
        return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        Context c = parent.getContext();
        //PersonViewHolder viewHolder;

        //if(convertView == null){

            LayoutInflater li = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TalkItem t = talkItems.get(position);

            TextView opposit_id;
            TextView time;
            TextView contents;
            switch (type) {
                case TYPE_ITEM:
                    convertView = li.inflate(R.layout.sent_talk, parent, false);
                    contents = (TextView) convertView.findViewById(R.id.sent_talk_contents);
                    time = (TextView) convertView.findViewById(R.id.sent_talk_time);

                    contents.setText(t.getContents());
                    time.setText(t.getTime());
                    break;

                case TYPE_SEPARATOR:
                    convertView = li.inflate(R.layout.received_talk, parent, false);
                    opposit_id = (TextView) convertView.findViewById(R.id.received_talk_opposit);
                    contents = (TextView) convertView.findViewById(R.id.received_talk_contents);
                    time = (TextView) convertView.findViewById(R.id.received_talk_time);

                    opposit_id.setText(t.getOpposit_id());
                    contents.setText(t.getContents());
                    time.setText(t.getTime());
                    break;
            }


    //}else{
            //viewHolder = (PersonViewHolder) convertView.getTag();
        //}
        return convertView;


    }

/*    public class PersonViewHolder
    {
        public TextView contents;
        public TextView time;
        public TextView opposit_id;
    }
*/
    public void addMyTalkItem(String owner_id, String opposit_id, String contents, String time){

        TalkItem t = new TalkItem(owner_id,opposit_id,contents,time);
        t.setOwner_id(owner_id);
        t.setOpposit_id(opposit_id);
        t.setContents(contents);
        t.setTime(time);

        talkItems.add(t);
        notifyDataSetChanged();
    }

    public void addOppositTalkItem(String owner_id, String opposit_id, String contents, String time){

        TalkItem t = new TalkItem(owner_id,opposit_id,contents,time);
        t.setOwner_id(owner_id);
        t.setOpposit_id(opposit_id);
        t.setContents(contents);
        t.setTime(time);

        talkItems.add(t);
        mSeparatorsSet.add(talkItems.size() - 1);
    }
}
