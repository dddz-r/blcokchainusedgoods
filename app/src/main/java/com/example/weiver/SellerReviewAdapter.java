package com.example.weiver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SellerReviewAdapter extends BaseAdapter {
    ArrayList<SellerReviewItem> reviewItem = new ArrayList<>();


   /*public SellerReviewAdapter(ArrayList<SellerReviewItem> reviewItem) {
        this.reviewItem = reviewItem;
    }*/

    @Override
    public int getCount() {
        return reviewItem.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewItem.get(position);
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
            convertView = li.inflate(R.layout.seller_review_item,parent,false);
        }

        TextView sri_commenter_id = (TextView) convertView.findViewById(R.id.sri_commenter_id);
        TextView sri_Score = (TextView) convertView.findViewById(R.id.sri_score);
        TextView sri_comment = (TextView) convertView.findViewById(R.id.sri_comment);

        SellerReviewItem r = reviewItem.get(position);

        sri_commenter_id.setText(r.getSri_commenter_id());
        sri_Score.setText(r.getSri_score());
        sri_comment.setText(r.getSri_comment());

        return convertView;
    }
    //순서 : 아이디 점수 후기
    public void addSellerReview(String sri_commenter_id, String sri_score, String sri_comment){
        SellerReviewItem r = new SellerReviewItem(sri_commenter_id, sri_score, sri_comment);

        r.setSri_commenter_id(sri_commenter_id);
        r.setSri_score(sri_score);
        r.setSri_comment(sri_comment);

        reviewItem.add(r);
    }
}
