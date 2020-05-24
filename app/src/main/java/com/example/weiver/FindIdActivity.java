package com.example.weiver;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FindIdActivity extends AppCompatActivity {

    EditText user_name, user_phone_number;
    Button find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        find = findViewById(R.id.findIdButton);

        find.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                user_name = findViewById(R.id.userNameEditText);
                user_phone_number = findViewById(R.id.userPhoneNumberEditText);

                findId();
            }
        });
    }

    private void findId() {

        final String userName = user_name.getText().toString().trim();
        final String userPhoneNumber = user_phone_number.getText().toString().trim();

        if(TextUtils.isEmpty(userName)) {
            user_name.setError("please Enter your name");
            user_name.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(userPhoneNumber)) {
            user_phone_number.setError("please Enter your phone number");
            user_phone_number.requestFocus();
            return;
        }

        //FindId 쓰레드를 실행하는 코드
        FindId fi = new FindId(userName, userPhoneNumber);
        fi.execute();

    }

    private class FindId extends AsyncTask<Void, Void, String > {

        private String user_name, user_phone_number;

        FindId(String user_name, String user_phone_number) {

            this.user_name = user_name;
            this.user_phone_number = user_phone_number;

        }

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        @Override
        protected String doInBackground(Void... voids) {

            RequestHandler requestHandler = new RequestHandler();

            HashMap<String, String> params = new HashMap<>();
            params.put("user_name", user_name);
            params.put("user_phone_number", user_phone_number);

            return requestHandler.sendPostRequest(URLS.URL_FIND_ID, params);

        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            try {

                JSONObject obj = new JSONObject(s);

                if(!obj.getString("code").equals("404")) {

                    if(obj.getString("code").equals("200")) { //유저존재

                        //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONObject userJson = obj.getJSONObject("user");

                        String user_id = userJson.getString("user_id");

                        AlertDialog.Builder alt = new AlertDialog.Builder(FindIdActivity.this);

                        alt.setMessage("입력하신 성명과 휴대전화 번호에 해당하는 아이디는 \n" + user_id + "입니다.")
                                .setCancelable(false)
                                .setPositiveButton("OK",

                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent intent = new Intent(FindIdActivity.this, LoginActivity.class);
                                                startActivity(intent);

                                            }
                                        });

                        AlertDialog alert = alt.create();
                        alert.setTitle("아이디 찾기");

                        alert.show();
                    }

                } else { //code 204

                    //Log.e("error here", json);
                    Toast.makeText(getApplicationContext(), "입력하신 이름과 휴대폰 번호에 해당하는 아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {

                e.printStackTrace();

            }
            //return json;
        }
    }
}
