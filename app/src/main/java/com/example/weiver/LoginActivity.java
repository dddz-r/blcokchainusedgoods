package com.example.weiver;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    TextView join, find_id, change_password, go_main;
    EditText user_id, user_password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        join = (TextView)findViewById(R.id.join);
        find_id = (TextView)findViewById(R.id.find_id);
        change_password = (TextView)findViewById(R.id.change_password);
        go_main = (TextView)findViewById(R.id.go_main);

        login = findViewById(R.id.login);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, JoinActivity.class));
                finish();
            }
        });

        find_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, FindIdActivity.class));
                finish();
            }
        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ChangePasswordActivity.class));
                finish();
            }
        });

        go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                user_id = findViewById(R.id.userId);
                user_password = findViewById(R.id.password);
                //여기서 메소드 실행
                login();
            }
        });
    }

    private void login() {

        String userId = user_id.getText().toString().trim();
        String userPassword = user_password.getText().toString().trim();

        if(TextUtils.isEmpty(userId)) {
            user_id.setError("please Enter your ID");
            user_id.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(userPassword)) {
            user_password.setError("please Enter your password");
            user_password.requestFocus();
            return;
        }

        //여기서 쓰레드 실행
        Login login = new Login(userId, userPassword);
        login.execute();

    }

    private class Login extends AsyncTask<Void, Void, String> {

        private String user_id;
        private String user_password;

        Login(String user_id, String user_password) {

            this.user_id = user_id;
            this.user_password = user_password;

        }

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        @Override
        protected String doInBackground(Void... voids) {

            RequestHandler requestHandler = new RequestHandler();

            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", user_id);
            params.put("user_password", user_password);

            String json = requestHandler.sendPostRequest(URLS.URL_LOGIN, params);
            return json;
        }

        @Override
        protected void onPostExecute(String json) {

            Log.i("login", "info" + json);

            try {

                JSONObject obj = new JSONObject(json);

                if(!obj.getString("code").equals("404")) {

                    if(obj.getString("code").equals("204")) { //정보 불일치

                        //Toast.makeText(getApplicationContext(),  obj.getString("message"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "로그인 정보가 일치하지 않습니다" , Toast.LENGTH_SHORT).show();

                    } else { //정보 일치

                       // Toast.makeText(getApplicationContext(),  "로그인성공", Toast.LENGTH_SHORT).show();

                        JSONObject userJson = obj.getJSONObject("user");

                        User user = new User(
                                userJson.getString("user_id"),
                                userJson.getString("user_name"),
                                userJson.getString("user_phone_number"),
                                userJson.getString("user_address"),
                                userJson.getString("user_account")
                                //                      userJson.getString("user_token")
                        );

                        PrefManager.getInstance(getApplicationContext()).setUserLogin(user);
                        finish();


                        String inputId = user.getUser_id();
                        String inputName = user.getUser_name();
                        String inputPhoneNumber = user.getUser_phone_number();
                        String inputAddress = user.getUser_address();
                        String inputAccount = user.getUser_account();
                        //               String inputToken = user.getUser_token();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("inputId", inputId);
                        intent.putExtra("inputName", inputName);
                        intent.putExtra("inputPhoneNumber", inputPhoneNumber);
                        intent.putExtra("inputAddress", inputAddress);
                        intent.putExtra("inputAccount", inputAccount);
                        //              intent.putExtra("inputToken", inputToken);

                        startActivity(intent);


                        /*

    public String getUser_id() { return user_id; }
    public String getUser_name() { return user_name; }
    public String getUser_phone_number() { return user_phone_number; }
    public String getUser_address() { return user_address; }
    public String getUser_account() { return user_account; }

                         */

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
