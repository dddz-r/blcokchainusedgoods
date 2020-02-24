package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SellerInform extends AppCompatActivity {

    TextView si_seller_name;
    Button si_buy_list;
    Button si_sell_list;
    Button si_review;
    Button si_lets_talk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_inform);

        si_seller_name = findViewById(R.id.si_seller_name);
        si_buy_list = findViewById(R.id.si_buy_list);
        si_sell_list = findViewById(R.id.si_sell_list);
        si_review = findViewById(R.id.si_review);
        si_lets_talk = findViewById(R.id.si_lets_talk);

        si_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellerInform.this, SellerReview.class));
            }
        });

        si_lets_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellerInform.this, TalkRoom.class));
            }
        });


    }
}
