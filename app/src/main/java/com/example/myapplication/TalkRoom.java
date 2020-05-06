package com.example.myapplication;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;



//톡방 안에 내용
public class TalkRoom extends AppCompatActivity {


    private ListView talk_contents;
    private EditText talk_edit;
    private Button talk_send_btn;
    private TextView tr_opposit_id;
    private LinearLayout talkbackground;


    String owner_id;
    String opposit_id;
    //ArrayList<TalkItem> arrayList;
    User user;
    TalkAdapter talkAdapter = new TalkAdapter();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk_room);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        /*유저 아이디*/
        final PrefManager prefManager = PrefManager.getInstance(TalkRoom.this);
        user = prefManager.getUser();
        owner_id = String.valueOf(user.getUser_id());

        /*상대 아이디*/
        opposit_id = getIntent().getStringExtra("opposit_id");
        tr_opposit_id = findViewById(R.id.tr_opposit_id);
        tr_opposit_id.setText(opposit_id);

        /*리스트뷰에 어댑터 연결*/
        //arrayList = new ArrayList<>();
        talkAdapter = new TalkAdapter();
        tr_opposit_id = (TextView) findViewById(R.id.tr_opposit_id);
        talk_contents = (ListView) findViewById(R.id.talk_contents);
        talk_contents.setAdapter(talkAdapter);

        /*테스트*/
        //talkAdapter.addOppositTalkItem("나", "상대방", "안녕하신가~", "12:01");
        //talkAdapter.addMyTalkItem("나", "상대방", "반갑군~", "12:53");

        talkbackground= findViewById(R.id.talkbackground);
        talkbackground.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();

                switch(action) {

                    case MotionEvent.ACTION_DOWN :    //화면을 터치했을때
                        talkAdapter = new TalkAdapter(); //이거 새로해서 리스트뷰 초기화
                        talk_contents.setAdapter(talkAdapter);
                        talkReceiveExecute();
                        break;

                    case MotionEvent.ACTION_UP :    //화면을 터치했다 땠을때

                        break;

                    case MotionEvent.ACTION_MOVE :    //화면을 터치하고 이동할때

                        break;

                }
                return true;
            }
        });

        talk_contents.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        talkAdapter = new TalkAdapter(); //이거 새로해서 리스트뷰 초기화
                        talk_contents.setAdapter(talkAdapter);
                        //talk_contents.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                        talkReceiveExecute();

                    }
                }
        );

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                talkAdapter = new TalkAdapter(); //이거 새로해서 리스트뷰 초기화
                talk_contents.setAdapter(talkAdapter);
                talkReceiveExecute();
            }
        });

        /* 보내기 버튼 눌렸을때*/
        talk_edit = (EditText) findViewById(R.id.talk_edit);
        talk_send_btn = (Button) findViewById(R.id.talk_send_btn);

        talk_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(talk_edit.getText())) {
                    Toast.makeText(TalkRoom.this, "내용을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    talkAdapter = new TalkAdapter(); //이거 새로해서 리스트뷰 초기화
                    talk_contents.setAdapter(talkAdapter);
                    //talk_contents.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    talkSendExecute();
                }

            }
        });

        /* 리스트뷰 아이템 추가 시 자동 스크롤*/
        talkAdapter.registerDataSetObserver(new DataSetObserver() {

            @Override

            public void onChanged() {
                super.onChanged();
                talk_contents.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            }

        });


    }



    private void talkReceiveExecute() {

        TalkRoom.talkReceive tr = new TalkRoom.talkReceive(owner_id, opposit_id);
        tr.execute();
    }

    private void talkSendExecute() {

        /*mysql 에서 자동으로 현재시간 저장해줌*/
        /*long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String time = dateFormat.format(date);*/
        TalkRoom.talkSend ts = new TalkRoom.talkSend(owner_id, opposit_id, talk_edit.getText().toString());
        ts.execute();
        talk_edit.setText("");
    }


    private class talkSend extends AsyncTask<Void, Void, String> {
        private String owner_id;
        private String opposit_id;
        //String time;
        String contents;

        talkSend(String owner_id, String opposit_id, String contents) {
            this.owner_id = owner_id;
            this.opposit_id = opposit_id;
            //this.time = time;
            this.contents = contents;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {

                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("owner_id", owner_id);
                params.put("opposit_id", opposit_id);
                params.put("contents", contents);

                return requestHandler.sendPostRequest(URLS.URL_TALK_SEND, params);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            Log.d("talkSend 온포스트", "실행");

            try {

                JSONObject obj = new JSONObject(s);

                if (!obj.getString("code").equals(404)) {

                    //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                } else if (!obj.getString("code").equals(200)) {
                    //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                } else {

                    //Toast.makeText(getApplicationContext(), "Some error occur", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            talkReceiveExecute();
        }
    }

    private class talkReceive extends AsyncTask<Void, Void, String> {
        private String owner_id;
        private String opposit_id;

        talkReceive(String owner_id, String opposit_id) {
            this.owner_id = owner_id;
            this.opposit_id = opposit_id;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {

                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("owner_id", owner_id);
                params.put("opposit_id", opposit_id);

                return requestHandler.sendPostRequest(URLS.URL_TALK_RECEIVE, params);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            Log.d("onPost실행", "onPost실행");

            try {

                JSONObject object = new JSONObject(s);

                JSONArray jsonArray = object.getJSONArray("talks");

                int count = 0;

                while (count < jsonArray.length()) {

                    JSONObject json = jsonArray.getJSONObject(count);
                    String owner_id = json.getString("owner_id");
                    String opposit_id = json.getString("opposit_id");
                    String time = json.getString("time");
                    String contents = json.getString("contents");

                    if (owner_id.equals(this.owner_id)) {
                        talkAdapter.addMyTalkItem(owner_id,opposit_id,contents,time);

                    } else{
                        talkAdapter.addOppositTalkItem(opposit_id,owner_id,contents,time);
                    }
                    count++;
                }


                //finish();
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }

    }
}

