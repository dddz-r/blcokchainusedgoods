package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private View drawerView;
    private GridView main_gridView;

    Button person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View
        main_gridView = (GridView)findViewById(R.id.main_gridView);
        //Data
        List<String> gredView_data = new ArrayList<>();
        ArrayAdapter<String> gerdViewAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,gredView_data);

        main_gridView.setAdapter(gerdViewAdapter);

        //테스트
        for(int i =0;i<15;i++) {
            gredView_data.add("data"+i);
        }

        // 리스트 아이템 눌렸을 때 이벤트 (쓸지안쓸지모름)
        main_gridView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String buy_data = String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(MainActivity.this, buy_data, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        person = (Button)findViewById(R.id.person);

        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
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

        Button btn_menu_close = (Button)findViewById(R.id.menu_close);
        btn_menu_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
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
