package com.example.myapplication;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;


//톡방 안에 내용
public class TalkRoom extends AppCompatActivity {

    private static final String TAG = "jsonExample";
    public static final int LOAD_SUCCESS = 101;

    private ListView talk_contents;
    private EditText talk_edit;
    private Button talk_send_btn;
    private TextView tr_opposit_id;

    String URL = "http://ec2-54-180-99-222.ap-northeast-2.compute.amazonaws.com:5000/talk_room";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk_room);

        tr_opposit_id = (TextView)findViewById(R.id.tr_opposit_id);

        talk_contents = (ListView)findViewById(R.id.talk_contents);
        TalkAdapter talkAdapter = new TalkAdapter();

        talk_contents.setAdapter(talkAdapter);

        talkAdapter.addTalkItem("나","상대방","내용","시간");

        //array.add(talk_edit.getText().toString()); //버튼을 클릭하면 array에 추가
        //talkAdapter.notifyDataSetChanged(); //어댑터 새로고침
        //talk_edit.setText("");

        talk_send_btn = (Button)findViewById(R.id.talk_send_btn);
        talk_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getJSON();
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



    private final MyHandler mHandler = new MyHandler(this);


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

public void getJSON(){
    Thread thread = new Thread(new Runnable() {

        public void run() {

            String result;

            try {

                Log.d(TAG, URL);
                java.net.URL url = new URL(URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(3000);
                httpURLConnection.setConnectTimeout(5000);//3000
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setUseCaches(false);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {

                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();

                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;


                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();
                httpURLConnection.disconnect();

                result = sb.toString().trim();


            } catch (Exception e) {
                result = e.toString();
            }
            Message message = mHandler.obtainMessage(LOAD_SUCCESS, result);
            mHandler.sendMessage(message);
        }

    });
    thread.start();
}
}

