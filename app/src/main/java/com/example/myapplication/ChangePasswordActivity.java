package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

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
import org.w3c.dom.Text;

import java.util.HashMap;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText user_id, user_phone_number;
    Button next, back;
    //private boolean match = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        next = findViewById(R.id.changePasswordButton);
        back = findViewById(R.id.backButton);

        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                user_id = findViewById(R.id.userIdEditText);
                user_phone_number = findViewById(R.id.userPhoneNumberEditText);

                changePasswordFirst();
            }
        });
    }

    private void changePasswordFirst() {

        final String userId = user_id.getText().toString().trim();
        final String userPhoneNumber = user_phone_number.getText().toString().trim();

        if(TextUtils.isEmpty(userId)) {
            user_id.setError("please Enter your ID");
            user_id.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(userPhoneNumber)) {
            user_phone_number.setError("please Enter your phone number");
            user_phone_number.requestFocus();
            return;
        }

        ChangePasswordFirst cp = new ChangePasswordFirst(userId, userPhoneNumber);
        cp.execute();

    }

    private class ChangePasswordFirst extends AsyncTask<Void, Void, String> {

        private String user_id, user_phone_number;

        ChangePasswordFirst(String user_id, String user_phone_number) {

            this.user_id = user_id;
            this.user_phone_number = user_phone_number;

        }

        @Override
        protected  void onPreExecute() { super.onPreExecute(); }

        @Override
        protected String doInBackground(Void... voids) {

            RequestHandler requestHandler = new RequestHandler();

            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", user_id);
            params.put("user_phone_number", user_phone_number);

            String json = requestHandler.sendPostRequest(URLS.URL_CHANGE_PASSWORD_FIRST, params);

            Log.i("change_password_first", "info" + json);

            try {

                JSONObject obj = new JSONObject(json);

                if(!obj.getString("code").equals("404")) {

                    if(obj.getString("code").equals("204")) {

                        Toast.makeText(getApplicationContext(), "입력하신 정보에 해당하는 계정이 없습니다", Toast.LENGTH_SHORT).show();

                    } else { //입력한 정보에 해당하는 계정 존재

                        Toast.makeText(getApplicationContext(), "다음 페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                        //match = true;
                        Intent intent = new Intent(ChangePasswordActivity.this, ChangePasswordActivity2.class);
                        intent.putExtra("user_id", user_id);

                        startActivity(intent);

                }
            }
        } catch (JSONException e) {

                e.printStackTrace();

            }

            return json;
        }
    }
}
