package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


//톡방의 목록
public class TalkList extends AppCompatActivity {

    public ListView tl_listView;
    User user;
    String owner_id;
    String opposit_id;
    ArrayList<TalkListItem> arrayList;
    TalkListAdapter ItemAdapter;// = new TalkListAdapter(arrayList);

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(TalkList.this, MainActivity.class));
        finish();
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk_list);


        /*유저 아이디 들고오기*/
        final PrefManager prefManager = PrefManager.getInstance(TalkList.this);
        user = prefManager.getUser();

        owner_id = String.valueOf(user.getUser_id()) ;

        if(prefManager.isLoggedIn()){


        }else{ //로그인 안 되어있을 경우
            Toast.makeText(TalkList.this, "로그인 하세요.", Toast.LENGTH_SHORT).show();
        }


        /*리스트뷰에 어댑터 연결*/
        arrayList = new ArrayList<>();
        ItemAdapter = new TalkListAdapter(arrayList);

        tl_listView = (ListView)findViewById(R.id.tl_listView);
        tl_listView.setAdapter(ItemAdapter);
        arrayList.clear();
        talklistExecute();


        /*테스트*/
        //ItemAdapter.addTalkListItem("상대방","02:41");


        tl_listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(TalkList.this, TalkRoom.class);
                        intent.putExtra("opposit_id",arrayList.get(position).getOpposit_id());
                        startActivity(intent);
                        finish();
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
        ProgressDialog pd = new ProgressDialog(TalkList.this);
        talklist(String owner_id) {
            this.owner_id = owner_id;
        }

        @Override
        protected void onPreExecute() {
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("잠시만 기다려주세요.");
            pd.show();
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
            pd.dismiss();
            super.onPostExecute(s);
            Log.d("onPost실행", "onPost실행");

            try {

                JSONObject object = new JSONObject(s);

                JSONArray jsonArray = object.getJSONArray("talklists");
                //Toast.makeText(getApplicationContext(), object.getString("reviews"), Toast.LENGTH_SHORT).show();

                int count = 0;

                while(count < jsonArray.length()) {

                    JSONObject json = jsonArray.getJSONObject(count);

                    //반대사람 인텐트로 넘겨야해서 위에서 선언함

                    String get_owner_id = json.getString("owner_id");
                    opposit_id = json.getString("opposit_id");
                    String time = json.getString("time");

                    String year = time.substring(0,4);
                    String month = time.substring(5,7);
                    String day = time.substring(8,10);
                    String hour = time.substring(11,13);
                    int hours = Integer.parseInt(hour);
                    hours = hours + 9;

                    if(hours >= 24) {
                        hours = hours - 24;
                    }

                    hour = Integer.toString(hours);
                    String minutes = time.substring(14,16);

                    String newTime = year + "-" + month + "-" + day + "." + hour + ":" + minutes;

                    if(arrayList.size()==0){
                        TalkListItem inform = new TalkListItem(opposit_id, newTime);
                        arrayList.add(inform);
                    }else{

                    if(opposit_id.equals(owner_id)){
                        //opposit_id=get_owner_id;
                        for(int i =0;i<arrayList.size();i++) {
                            if(arrayList.get(i).getOpposit_id().equals(get_owner_id)){
                                //Toast.makeText(getApplicationContext(), arrayList.get(i).getOpposit_id(), Toast.LENGTH_SHORT).show();
                                break;
                            }
                            if(i==arrayList.size()-1){
                                //Toast.makeText(getApplicationContext(), "중복노!", Toast.LENGTH_SHORT).show();
                                    TalkListItem inform = new TalkListItem(get_owner_id, newTime);
                                    arrayList.add(inform);
                            }
                        }


                    }else{
                        for(int i =0;i<arrayList.size();i++) {
                            if (arrayList.get(i).getOpposit_id().equals(opposit_id)) {
                                //Toast.makeText(getApplicationContext(), arrayList.get(i).getOpposit_id() + "왓와오앗", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            if (i == arrayList.size() - 1) {
                                //Toast.makeText(getApplicationContext(), "중복노!와와앙", Toast.LENGTH_SHORT).show();
                                TalkListItem inform = new TalkListItem(opposit_id, newTime);
                                arrayList.add(inform);
                            }
                        }

                    }
                    }
                    //ItemAdapter.addTalkListItem(opposit_id, time);


                    count++;
                }
                //finish();
                ItemAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), "서버가 꺼져있어요^^", Toast.LENGTH_SHORT).show();

            }

        }
    }

}
