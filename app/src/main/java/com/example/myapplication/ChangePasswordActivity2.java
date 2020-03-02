package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ChangePasswordActivity2 extends AppCompatActivity {

    EditText new_password, password_confirm;
    Button change, back;

    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password2);

        change = findViewById(R.id.changePasswordButton);
        back = findViewById(R.id.backButton);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");

        change.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new_password = findViewById(R.id.newPasswordEditText);
                password_confirm = findViewById(R.id.passwordConfirmEditText);

                changePasswordSecond();

            }
        });
    }

    private void changePasswordSecond() {

        final String user_password = new_password.getText().toString().trim();
        final String user_password_confirm = password_confirm.getText().toString().trim();

        if(TextUtils.isEmpty(user_password)) {
            new_password.setError("please Enter new Password");
            new_password.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(user_password_confirm)) {
            password_confirm.setError("please Enter new Password one more time for confirming");
            password_confirm.requestFocus();
            return;
        }

        if(!user_password.equals(user_password_confirm)) {

            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다. 다시 확인해 주세요.", Toast.LENGTH_SHORT).show();

        } else {

            //여기서 쓰레드 실행
            ChangePasswordSecond cp = new ChangePasswordSecond(user_password, user_id);
            cp.execute();

        }
    }

    private class ChangePasswordSecond extends AsyncTask<Void, Void, String> {

        private String user_id, user_password;

        ChangePasswordSecond(String user_id, String user_password) {

            this.user_id = user_id;
            this.user_password = user_password;

        }

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        //doInBackground 와 onPostExecute는 나누어주어야함. 하나의 쓰레드로 처리 하는 듯
        @Override
        protected String doInBackground(Void... voids) {

            RequestHandler requestHandler = new RequestHandler();

            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", user_id);
            params.put("user_password", user_password);

            return requestHandler.sendPostRequest(URLS.URL_CHANGE_PASSWORD_SECOND, params);

        }

        @Override
        protected void onPostExecute(String s) {

            //Log.i("change_password_second", "info" + json);
            super.onPostExecute(s);

            try {

                JSONObject obj = new JSONObject(s);

                if(!obj.getString("code").equals("404")) {

                    AlertDialog.Builder alt = new AlertDialog.Builder(ChangePasswordActivity2.this);

                    alt.setMessage("비밀번호 변경에 성공했습니다. 변경하신 정보로 로그인 해주시기 바랍니다.")
                            .setCancelable(false)
                            .setPositiveButton("OK",

                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent intent = new Intent(ChangePasswordActivity2.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    });

                    AlertDialog alert = alt.create();
                    alert.setTitle("비밀번호 변경");

                    alert.show();

                } else {

                    //Log.e("error here", json);
                    Toast.makeText(getApplicationContext(), "에러발생", Toast.LENGTH_SHORT).show();

                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
            //return json;
        }
    }
}
