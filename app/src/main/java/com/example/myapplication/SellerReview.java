package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SellerReview extends AppCompatActivity {

    ListView sr_listview;
    Button sr_write_review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_review);

        sr_listview = (ListView)findViewById(R.id.sr_listview);
        sr_write_review = (Button)findViewById(R.id.sr_write_review);

        SellerReviewAdapter sellerReviewAdapter = new SellerReviewAdapter();
        sellerReviewAdapter.addSellerReview("권순영","★☆☆☆☆","별이하나fkㅡㅡ");
        sellerReviewAdapter.addSellerReview("부승관","★★☆☆☆","별이다섯개별이다섯개스크로로로로롤별개별이다섯개별이다섯개별이다섯개별이다섯개별이다섯개별이다붖더해얗asdfqerzxcv섯개별이다섯개");
        sr_listview.setAdapter(sellerReviewAdapter);

        sr_write_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellerReview.this, WriteReview.class));
            }
        });
    }
}
