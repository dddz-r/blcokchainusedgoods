package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
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

    User user;
    //메인2
    Button login_btn;
    TextView main2_user_name;
    TextView main2_user_id;
    Button bought_product;
    Button sold_product;

    Button category1;
    Button category2;
    Button category3;
    Button category4;
    Button category5;
    Button category6;
    Button category7;

    //String user_name = getIntent().getStringExtra("inputName");
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

        //main2_user_name.setText(user_name);
        /*작성자 아이디 들고오기*/
        //로그인 된 현재 유저 정보를 저장
        final PrefManager prefManager = PrefManager.getInstance(MainActivity.this);
        user = prefManager.getUser();


        //View *그리드뷰 아직테스트안해봄
        main2_user_name = (TextView)findViewById(R.id.main2_user_name);
        main2_user_id = (TextView)findViewById(R.id.main2_user_id);
        login_btn = (Button)findViewById(R.id.login_btn);

        if(prefManager.isLoggedIn()){
            main2_user_name.setText(String.valueOf(user.getUser_name()));
            main2_user_id.setText(String.valueOf(user.getUser_id()));
            login_btn.setText("로그아웃");
            login_btn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    finish();
                    prefManager.logout();
                }
            });

        }else{ //로그인 안 되어있을 경우
            login_btn.setText("로그인");
            login_btn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });
        }

        /*카테고리*/
        category1=(Button)findViewById(R.id.category1);
        category1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Category1.class));
            }
        });
        category2=(Button)findViewById(R.id.category2);
        category2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Category2.class));
            }
        });
        category3=(Button)findViewById(R.id.category3);
        category3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Category3.class));
            }
        });
        category4=(Button)findViewById(R.id.category4);
        category4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Category4.class));
            }
        });
        category5=(Button)findViewById(R.id.category5);
        category5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Category5.class));
            }
        });
        category6=(Button)findViewById(R.id.category6);
        category6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Category6.class));
            }
        });
        category7=(Button)findViewById(R.id.category7);
        category7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Category7.class));
            }
        });


        main_gridView = (GridView)findViewById(R.id.main_gridView);

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
                Intent intent = new Intent(MainActivity.this, BuyList.class) ;
                intent.putExtra("user_id", main2_user_id.getText().toString()) ;
                startActivity(intent) ;
            }
        });

        sold_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SellList.class) ;
                intent.putExtra("user_id", main2_user_id.getText().toString()) ;
                startActivity(intent) ;
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
