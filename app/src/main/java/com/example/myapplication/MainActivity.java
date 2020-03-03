package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private View drawerView;
    private GridView main_gridView;

    Button add_item;
    Button talk;

    Button bought_product;
    Button sold_product;

    User user;


    //버튼을 누른 시간
    private long backBtnTime = 0;

    @Override
    public void onBackPressed() {

        long curTime = System.currentTimeMillis();
        long getTime = curTime - backBtnTime ;

        if( 0 <= getTime && 1000 >= getTime) {

            android.os.Process.killProcess(android.os.Process.myPid());
            super.onBackPressed();

        } else {

            backBtnTime = curTime;
            Toast.makeText(this, "\'뒤로\' 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //로그인 된 현재 유저 정보를 저장
        final PrefManager prefManager = PrefManager.getInstance(MainActivity.this);
        user = prefManager.getUser();


        //View *그리드뷰 아직테스트안해봄
        main_gridView = (GridView)findViewById(R.id.main_gridView);
        //Data

        MainGridAdapter gridViewAdapter = new MainGridAdapter();

        main_gridView.setAdapter(gridViewAdapter);
        //테스트
        for(int i =0;i<16;i++) {
            gridViewAdapter.addGridItem(ContextCompat.getDrawable(this,R.drawable.onlydog),"멍멍이","999억");
        }

        main_gridView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        startActivity(new Intent(MainActivity.this, BuyScreen.class));

                    }
                }
        );

        //이거 나중에 톡방리스트로 넘어가게 바꿔야함
        talk = (Button)findViewById(R.id.talk);

        talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TalkList.class));
            }
        });

        add_item = (Button)findViewById(R.id.add_item);

        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SellScreen.class));
            }
        });

        bought_product = (Button)findViewById(R.id.bought_product);
        sold_product = (Button)findViewById(R.id.sold_product);


        bought_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BuyList.class));
            }
        });

        sold_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(Main2Activity.this, BuyList.class));
            }
        });

        //드로워
        drawerLayout = (DrawerLayout)findViewById(R.id.main_drawer_layout);
        drawerView = (View)findViewById(R.id.drawer_layout);

        Button btn_menu = (Button)findViewById(R.id.menu);
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        Button login_btn = (Button)findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Login2Activity.class));
            }
        });

        drawerLayout.setDrawerListener(listner);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }

    DrawerLayout.DrawerListener listner = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

}
