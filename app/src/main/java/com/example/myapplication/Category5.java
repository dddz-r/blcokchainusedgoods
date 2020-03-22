package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class Category5  extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private View drawerView;
    private GridView category5_grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_grid5);

        category5_grid = (GridView)findViewById(R.id.category5_grid);

       //MainGridAdapter gridViewAdapter = new MainGridAdapter();

        //category5_grid.setAdapter(gridViewAdapter);
        //테스트
        for(int i =0;i<16;i++) {
            //gridViewAdapter.addGridItem(ContextCompat.getDrawable(this,R.drawable.onlydog),"멍멍이","999억");
        }

        category5_grid.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        startActivity(new Intent(Category5.this, BuyScreen.class));

                    }
                }
        );

    }
}
