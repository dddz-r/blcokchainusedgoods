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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;


//톡방 안에 내용
public class TalkRoom extends AppCompatActivity {

    public static final int LOAD_SUCCESS = 101;

    private ListView talk_contents;
    private EditText talk_edit;
    private Button talk_send_btn;
    private TextView tr_opposit_id;

    private Socket socket;

    String owner_id;
    String opposit_id;
    //ArrayList<TalkItem> arrayList;
    User user;
    TalkAdapter talkAdapter = new TalkAdapter();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk_room);

        /*유저 아이디*/
        final PrefManager prefManager = PrefManager.getInstance(TalkRoom.this);
        user = prefManager.getUser();
        owner_id = String.valueOf(user.getUser_name());

        /*상대 아이디*/
        opposit_id = getIntent().getStringExtra("opposit_id");
        tr_opposit_id = findViewById(R.id.tr_opposit_id);
        tr_opposit_id.setText(opposit_id);

        /*소켓에러 해결 뭔지모름*/
        int SDK_INT = android.os.Build.VERSION.SDK_INT;

        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        /*socket connect*/
        SocketThread socketThread = new SocketThread();
        //socketThread.start();

        /*리스트뷰에 어댑터 연결*/
        //arrayList = new ArrayList<>();
        talkAdapter = new TalkAdapter();
        tr_opposit_id = (TextView) findViewById(R.id.tr_opposit_id);
        talk_contents = (ListView) findViewById(R.id.talk_contents);
        talk_contents.setAdapter(talkAdapter);

        /*테스트*/
        //talkAdapter.addOppositTalkItem("나", "상대방", "안녕하신가~", "12:01");
        //talkAdapter.addMyTalkItem("나", "상대방", "반갑군~", "12:53");
         //어댑터 새로고침--------------->위치 옮겨야할듯
        talkAdapter.notifyDataSetChanged();
        /*톡 불러오기*/
        talkReceiveExecute();

        talk_contents.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();

                switch(action) {

                    case MotionEvent.ACTION_DOWN :    //화면을 터치했을때
                        talkAdapter = new TalkAdapter();
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

        /* 보내기 버튼 눌렸을때*/
        talk_edit = (EditText) findViewById(R.id.talk_edit);
        talk_send_btn = (Button) findViewById(R.id.talk_send_btn);

        talk_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(talk_edit.getText())) {
                    Toast.makeText(TalkRoom.this, "내용을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    talkSendExecute();
                    //sendtalk();
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


    private void sendtalk() {
        if (socket != null) {

            Log.d("sendtalk", "소켓 널 아냐");
            try {
                JSONObject data = new JSONObject();
                //지금 날짜,시간
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                String formatDate = dateFormat.format(date);


                data.put("owner_id", "주인");
                data.put("opposit_id", "반대사람");
                data.put("contents", talk_edit.getText().toString());
                data.put("time", formatDate);

                socket.emit("say", data);//이게 서버에 데이터 보낸거

                Toast.makeText(getApplicationContext(), data.toString(), Toast.LENGTH_LONG).show();

                talk_edit.setText("");


            } catch (Exception e) {

                e.printStackTrace();
            }

        } else {
            Log.d("sendtalk", "소켓 널 이야");
        }
    }

    public void setSocket(String uri) throws IOException {

        try {
            socket = IO.socket(uri);
            socket.connect();
            Log.d("셋소켓", "실행댐");
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
            }).on("hi", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Toast.makeText(getApplicationContext(), "하이!", Toast.LENGTH_SHORT).show();
                }
            }).on("otherSaying", new Emitter.Listener() {//on은 서버에서 받아오는거 //send라는 이름의 이벤트 받아오는거
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
                                talkAdapter.addOppositTalkItem(owner_id, opposit_id, contents, time);
                                talkAdapter.notifyDataSetChanged();//

                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }).on("mySaying", new Emitter.Listener() {//on은 서버에서 받아오는거 //send라는 이름의 이벤트 받아오는거
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
                                talkAdapter.addMyTalkItem(owner_id, opposit_id, contents, time);
                                talkAdapter.notifyDataSetChanged();//

                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });

            socket.connect();


        } catch (URISyntaxException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "set소켓 예외", Toast.LENGTH_SHORT).show();
            Log.d("set소켓 예외", "URISyntaxException");

        }

    }


    class SocketThread extends Thread {
        public void run() {
            try {
                setSocket("http://ec2-13-125-217-33.ap-northeast-2.compute.amazonaws.com:5000/api");
                Log.d("소켓스레드실행", "try");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("소켓스레드에러", "IO익셉션");
            }
        }
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
                    talkAdapter.notifyDataSetChanged();
                    count++;
                }


                //finish();
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }

    }
}

