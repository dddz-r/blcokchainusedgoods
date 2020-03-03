package com.example.myapplication;

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

public class SellList extends AppCompatActivity {

    private ListView sl_listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_list);

        sl_listView = (ListView)findViewById(R.id.sl_listView);
        BuyListAdapter ItemAdapter = new BuyListAdapter();

        sl_listView.setAdapter(ItemAdapter);

        //테스트//순서 : 그림 이름 가격 상태
        ItemAdapter.addBuyItem(ContextCompat.getDrawable(this,R.drawable.onlydog),"멍멍이","999억","판매완료");
        //List<String> buy_data = new ArrayList<>();

        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,buy_data);
        //bl_listView.setAdapter(adapter);

        //adapter.notifyDataSetChanged();

        // 리스트 아이템 눌렸을 때 이벤트 (쓸지안쓸지모름)
        sl_listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String buy_data = String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(SellList.this, buy_data, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

}
