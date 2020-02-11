package com.example.myapplication;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;


//톡방 안에 내용
public class TalkRoom extends AppCompatActivity {

    private static final String TAG = "jsonExample";
    private ListView talk_contents;
    private EditText talk_edit;
    private Button talk_send_btn;

    String URL = "ec2-54-180-99-222.ap-northeast-2.compute.amazonaws.com";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk_room);

        talk_contents = (ListView)findViewById(R.id.talk_contents);
        TalkAdapter talkAdapter = new TalkAdapter();

        talk_contents.setAdapter(talkAdapter);

        //array.add(talk_edit.getText().toString()); //버튼을 클릭하면 array에 추가
        //talkAdapter.notifyDataSetChanged(); //어댑터 새로고침
        //talk_edit.setText("");

        talk_send_btn = (Button)findViewById(R.id.talk_send_btn);
        talk_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getJSON();
            }
        });

    }


/*public void getJSON(){
    Thread thread = new Thread(new Runnable() {

        public void run() {

            String result;

            try {

                Log.d(TAG, URL);
                java.net.URL url = new URL(URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(3000);
                httpURLConnection.setConnectTimeout(3000);
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

        }

    });
    thread.start();
}*/
}

