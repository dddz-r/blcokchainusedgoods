package com.example.myapplication;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class BuyList extends AppCompatActivity {

    private ListView bl_listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_list);

        String user_id;
        String device_name;
        String device_price;
        String device_condition;
        Drawable image;

        user_id = getIntent().getStringExtra("user_id");

        bl_listView = (ListView)findViewById(R.id.bl_listView);
        BuyListAdapter ItemAdapter = new BuyListAdapter();

        bl_listView.setAdapter(ItemAdapter);
        
        //테스트//순서 : 그림 이름 가격 상태
        ItemAdapter.addBuyItem(ContextCompat.getDrawable(this,R.drawable.onlydog),"멍멍이","999억","판매중");
        //List<String> buy_data = new ArrayList<>();

        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,buy_data);
        //bl_listView.setAdapter(adapter);

         //adapter.notifyDataSetChanged();

         // 리스트 아이템 눌렸을 때 이벤트 (쓸지안쓸지모름)
        bl_listView.setOnItemClickListener(
                 new AdapterView.OnItemClickListener() {
                     @Override
                     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                         String buy_data = String.valueOf(parent.getItemAtPosition(position));
                         Toast.makeText(BuyList.this, buy_data, Toast.LENGTH_SHORT).show();
                     }
                 }
         );
    }

}
