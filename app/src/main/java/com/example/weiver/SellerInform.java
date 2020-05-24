package com.example.weiver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SellerInform extends AppCompatActivity {

    TextView si_seller_id;
    //TextView si_seller_id;
    Button si_buy_list;
    Button si_sell_list;
    Button si_review;
    Button si_lets_talk;

    User user;

    String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_inform);

        String seller_id = getIntent().getStringExtra("seller_id");
        si_seller_id = findViewById(R.id.si_seller_name);
        si_seller_id.setText(seller_id);


        si_buy_list = findViewById(R.id.si_buy_list);
        si_sell_list = findViewById(R.id.si_sell_list);
        si_review = findViewById(R.id.si_review);
        si_lets_talk = findViewById(R.id.si_lets_talk);


        final PrefManager prefManager = PrefManager.getInstance(SellerInform.this);
        user = prefManager.getUser();
        if(prefManager.isLoggedIn()){
            user_id=String.valueOf(user.getUser_id());

        }else{ //로그인 안 되어있을 경우

        }

        si_buy_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerInform.this, BuyList.class) ;
                intent.putExtra("user_id", si_seller_id.getText().toString()) ;
                startActivity(intent) ;
            }
        });

        si_sell_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerInform.this, SellList.class) ;
                intent.putExtra("user_id", si_seller_id.getText().toString()) ;
                startActivity(intent) ;

            }
        });

        si_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerInform.this, SellerReview.class) ;
                intent.putExtra("seller_id", si_seller_id.getText().toString()) ;
                startActivity(intent) ;
            }
        });

        si_lets_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefManager.isLoggedIn()){
                Intent intent = new Intent(SellerInform.this, TalkRoom.class) ;
                intent.putExtra("opposit_id", si_seller_id.getText().toString()) ;
                intent.putExtra("user_id", user_id);
                startActivity(intent) ;
                }else{
                    Toast.makeText(getApplicationContext(), "로그인 해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
