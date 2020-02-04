package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class BuyScreen extends AppCompatActivity {

    private FragmentPagerAdapter fragmentPagerAdapter;

    Button device_inform_btn;
    Button seller_inform_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_screen);

        //물품 정보 팝업창 띄우기
        device_inform_btn = (Button)findViewById(R.id.bs_device_inform_btn);

        device_inform_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(BuyScreen.this);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("상품 정보");
                ad.setMessage("최초 등록일 : ");
                ad.setMessage("거래 횟수 : ");

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
                //ad.setNegativeButton();
            }
        });

        //판매자 정보 화면으로 넘어가기
        seller_inform_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BuyScreenIntent = new Intent(getApplicationContext(), BuyScreen.class);
                startActivity(BuyScreenIntent);
            }
        });

        //뷰페이저 세팅
        ViewPager viewPager = findViewById(R.id.bs_viewpager);
        //fragmentPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));


    }



}
