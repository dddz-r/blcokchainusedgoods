package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


//톡방의 목록
public class TalkList extends AppCompatActivity {

    public ListView tl_listView;
    User user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk_list);


        /*유저 아이디 들고오기*/
        final PrefManager prefManager = PrefManager.getInstance(TalkList.this);
        user = prefManager.getUser();

        String owner_id = String.valueOf(user.getUser_id()) ;

        if(prefManager.isLoggedIn()){


        }else{ //로그인 안 되어있을 경우
            Toast.makeText(TalkList.this, "로그인 하세요", Toast.LENGTH_SHORT).show();
        }


        tl_listView = (ListView)findViewById(R.id.tl_listView);
        TalkListAdapter ItemAdapter = new TalkListAdapter();

        tl_listView.setAdapter(ItemAdapter);

        //테스트//순서 : 그림 이름 가격 상태
        ItemAdapter.addTalkListItem("상대방","02:41");
        //List<String> buy_data = new ArrayList<>();

        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,buy_data);
        //bl_listView.setAdapter(adapter);

        //adapter.notifyDataSetChanged();

        tl_listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            startActivity(new Intent(TalkList.this, TalkRoom.class));
                    }
                }
        );




    }
}
