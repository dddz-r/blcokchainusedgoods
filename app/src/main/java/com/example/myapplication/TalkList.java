package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


//톡방의 목록
public class TalkList extends AppCompatActivity {

    public ListView tl_listView;
    User user;
    String owner_id;
    TalkListAdapter ItemAdapter = new TalkListAdapter(); //어댑터 선언 꼭 위에서 해야함

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk_list);


        /*유저 아이디 들고오기*/
        final PrefManager prefManager = PrefManager.getInstance(TalkList.this);
        user = prefManager.getUser();

        owner_id = String.valueOf(user.getUser_id()) ;

        if(prefManager.isLoggedIn()){


        }else{ //로그인 안 되어있을 경우
            Toast.makeText(TalkList.this, "로그인 하세요", Toast.LENGTH_SHORT).show();
        }


        /*리스트뷰에 어댑터 연결*/
        tl_listView = (ListView)findViewById(R.id.tl_listView);
        tl_listView.setAdapter(ItemAdapter);

        talklistExecute();

        /*테스트*/
        //ItemAdapter.addTalkListItem("상대방","02:41");


        tl_listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            startActivity(new Intent(TalkList.this, TalkRoom.class));
                    }
                }
        );

    }

    private void talklistExecute() {
        TalkList.talklist tl = new TalkList.talklist(owner_id);
        tl.execute();
        Log.d("tag", "AsyncTask실행");
    }


    private class talklist extends AsyncTask<Void, Void, String> {
        private String owner_id;

        talklist(String owner_id) {
            this.owner_id = owner_id;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {

                Log.d("tag", "doInBackground실행");

                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("owner_id", owner_id);

                return requestHandler.sendPostRequest(URLS.URL_TALK_LIST, params);


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("doInBackground 에러", "doInBackground Exception");
            }
            return null;

        }

        public void onProgressUpdate(Void... values){
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            Log.d("onPost실행", "onPost실행");

            try {

                JSONObject object = new JSONObject(s);

                JSONArray jsonArray = object.getJSONArray("talklists");
                //Toast.makeText(getApplicationContext(), object.getString("reviews"), Toast.LENGTH_SHORT).show();

                int count = 0;

                while(count < jsonArray.length()){

                    JSONObject json = jsonArray.getJSONObject(count);

                    String opposit_id = json.getString("opposit_id");
                    String time = json.getString("time");

                    ItemAdapter.addTalkListItem(opposit_id, time);
                    ItemAdapter.notifyDataSetChanged();

                    count++;
                }
                //finish();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "JSONException", Toast.LENGTH_SHORT).show();

            }

        }
    }

}
