package com.example.myapplication;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;


//톡방 안에 내용
public class TalkRoom extends AppCompatActivity {

    private static final String TAG = "jsonExample";
    public static final int LOAD_SUCCESS = 101;

    private ListView talk_contents;
    private EditText talk_edit;
    private Button talk_send_btn;
    private TextView tr_opposit_id;
    private Socket socket;
    TalkAdapter talkAdapter = new TalkAdapter();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk_room);

        /*socket connect*/
        try {
            socket = IO.socket(URLS.URL_TALK);
             socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
               public void call(Object... args) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "connect", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "disconnect", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(), "connect timeout", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(), "connect error", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    });

            socket.connect();

            //데이터를 받아온다

            /* 이거 이용해서 내가 쓴거랑 남이쓴거 분류해서 어댑터에 넣기 (서버코드)*/
            //socket.on('say', function(msg){ ... })누군가 채팅을 했을 때
            //socket.broadcast.emit('chat message', nickName+'  :  '+msg); - 누군가 채팅을 했을 때 그것을 화자 외에게 전달
            //socket.emit('mySaying', 'ME  :  '+msg);	 - 화자에게 내용 다시 보냄

            socket.on("otherSaying", new Emitter.Listener() {//on은 서버에서 받아오는거 //send라는 이름의 이벤트 받아오는거
                @Override
                public void call(final Object... args) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject data = (JSONObject) args[0];
                                String opposit_id = data.getString("opposit_id");
                                String owner_id = data.getString("owner_id");
                                String contents = data.getString("contents");
                                String time = data.getString("time");
                                talkAdapter.addOppositTalkItem(owner_id ,opposit_id,contents,time);
                                talkAdapter.notifyDataSetChanged();//

                            } catch(Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });

            socket.on("mySaying", new Emitter.Listener() {//on은 서버에서 받아오는거 //send라는 이름의 이벤트 받아오는거
                @Override
                public void call(final Object... args) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject data = (JSONObject) args[0];
                                String opposit_id = data.getString("opposit_id");
                                String owner_id = data.getString("owner_id");
                                String contents = data.getString("contents");
                                String time = data.getString("time");
                                talkAdapter.addMyTalkItem(owner_id ,opposit_id,contents,time);
                                talkAdapter.notifyDataSetChanged();//

                            } catch(Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });

            socket.connect();

        } catch(URISyntaxException e) {
            e.printStackTrace();
        }

        tr_opposit_id = (TextView)findViewById(R.id.tr_opposit_id);
        talk_contents = (ListView)findViewById(R.id.talk_contents);
        talk_contents.setAdapter(talkAdapter);

        talkAdapter.addOppositTalkItem("나","상대방","안녕하신가~","12:01");
        talkAdapter.addMyTalkItem("나","상대방","반갑군~","12:53");

        talkAdapter.notifyDataSetChanged(); //어댑터 새로고침--------------->위치 옮겨야할듯

    /* 보내기 버튼 눌렸을때*/
        talk_edit = (EditText)findViewById(R.id.talk_edit);
        talk_send_btn = (Button)findViewById(R.id.talk_send_btn);

        talk_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (socket != null) {
                    JSONObject data = new JSONObject();

                    try {

                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
                        String formatDate = dateFormat.format(date);

                        data.put("owner_id", "owner_id");
                        data.put("opposit_id", tr_opposit_id.getText().toString());
                        data.put("contents", talk_edit.getText().toString());
                        data.put("time", formatDate);
                        socket.emit("say", data);//이게 서버에 데이터 보낸거

                        //위에꺼랑 둘중하나만해도 되는지 보기(DB에도 위에껄로 넣어지는지)
                        //TalkContents tc = new TalkContents("owner_id", tr_opposit_id.getText().toString(),talk_edit.getText().toString(),formatDate);
                        //tc.execute();


                        talkAdapter.addMyTalkItem("나", "상대방", talk_edit.getText().toString(), formatDate);
                        //talkAdapter.notifyDataSetChanged();-->여긴아냐ㅠ

                        talk_edit.setText("");


                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast
                                .LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else {

                    Toast.makeText(TalkRoom.this, "내용을 입력해 주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        talkAdapter.registerDataSetObserver(new DataSetObserver() {

            @Override

            public void onChanged() {

                super.onChanged();

                //talk_contents.setSelection(TalkAdapter.getCount()-1);
                talk_contents.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

            }

        });
    }




    private static class MyHandler extends Handler {
        private final WeakReference<TalkRoom> weakReference;

        public MyHandler(TalkRoom talkRoom) {
            weakReference = new WeakReference<TalkRoom>(talkRoom);
        }

        @Override
        public void handleMessage(Message msg) {

            TalkRoom talkRoom = weakReference.get();

            if (talkRoom != null) {
                switch (msg.what) {

                    case LOAD_SUCCESS:
                        //talkRoom.progressDialog.dismiss();

                        String jsonString = (String)msg.obj;

                        talkRoom.tr_opposit_id.setText(jsonString);//되나볼라고 텍스트뷰암꺼나해봄
                        break;
                }
            }
        }
    }


//
    private class TalkContents extends AsyncTask<Void, Void, String> {

        private String owner_id, opposit_id, contents, time;

        TalkContents(String owner_id, String opposit_id, String contents, String time) {

            this.owner_id = owner_id;
            this.opposit_id = opposit_id;
            this.contents = contents;
            this.time = time;

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {

            RequestHandler requestHandler = new RequestHandler();

            HashMap<String, String> params = new HashMap<>();
            params.put("owner_id", owner_id);
            params.put("opposit_id", opposit_id);
            params.put("contents", contents);
            params.put("time", time);

            socket.emit(String.valueOf(params));
            String json = requestHandler.sendPostRequest(URLS.URL_TALK, params);


            return json;

        }

        public void onProgressUpdate(Void... values){
        super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            Log.i("talk", "Info" + s);

            try {

                JSONObject obj = new JSONObject(s);

                if (!obj.getString("code").equals(404)) {

                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    //JSONObject userJson = obj.getJSONObject("user");


                } else {

                    Toast.makeText(getApplicationContext(), "Some error occur", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }




}

