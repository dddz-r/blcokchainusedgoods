package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {

    EditText user_name, user_id, user_phone_number, user_password, user_password_confirm, user_address, user_account;
    Button check_id, auth_phone_number, join, back;
    private boolean afterCheckId = false;
    private boolean afterAuth = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        //final Intent back = getIntent();
        //final String after_phone_number;
        //final boolean afterAuth;

        check_id = findViewById(R.id.check_user_id);
        auth_phone_number = findViewById(R.id.authPhoneNumberButton);
        join = findViewById(R.id.button);
        back = findViewById(R.id.backButton);

        //아이디 중복검사 버튼 클릭 리스너
        check_id.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                user_id = findViewById(R.id.userIdEditText);
                checkIdDuplicaton();
            }

        });

        //회원가입 버튼 클릭 리스너
        join.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                user_id = findViewById(R.id.userIdEditText);
                user_password = findViewById(R.id.userPasswordEditText);
                user_name = findViewById(R.id.userNameEditText);
                user_phone_number = findViewById(R.id.phoneNumber);
                user_address = findViewById(R.id.userAddressEditText);
                user_account = findViewById(R.id.userAccountEditText);
                user_password_confirm = findViewById(R.id.userPasswordConfirmEditText);

                /*
                user_id = findViewById(R.id.userIdEditText);
                InputFilter[] userIdFileter = new InputFilter[1];
                userIdFileter[0] = new InputFilter.LengthFilter(20);
                user_id.setFilters(userIdFileter);

                user_password = findViewById(R.id.userPasswordEditText);
                InputFilter[] userPasswordFilter = new InputFilter[1];
                userPasswordFilter[0] = new InputFilter.LengthFilter(40);
                user_password.setFilters(userPasswordFilter);

                user_name = findViewById(R.id.userNameEditText);
                InputFilter[] userNameFilter = new InputFilter[1];
                userNameFilter[0] = new InputFilter.LengthFilter(30);
                user_name.setFilters(userNameFilter);

                user_phone_number = findViewById(R.id.userPhoneNumberEditText);
                InputFilter[] userPhoneNumberFilter = new InputFilter[1];
                userPhoneNumberFilter[0] = new InputFilter.LengthFilter(30);
                user_phone_number.setFilters(userPhoneNumberFilter);

                user_address = findViewById(R.id.userAddressEditText);
                InputFilter[] userAddressFilter = new InputFilter[1];
                userAddressFilter[0] = new InputFilter.LengthFilter(100);
                user_address.setFilters(userAddressFilter);

                user_account = findViewById(R.id.userAccountEditText);
                InputFilter[] userAccountFilter = new InputFilter[1];
                userAccountFilter[0] = new InputFilter.LengthFilter(140);
                user_account.setFilters(userAccountFilter);

                user_password_confirm = findViewById(R.id.userPasswordConfirmEditText);
                InputFilter[] userPasswordConfirmFilter = new InputFilter[1];
                userPasswordConfirmFilter[0] = new InputFilter.LengthFilter(40);
                user_password_confirm.setFilters(userPasswordConfirmFilter);

                */

                joinUser();

            }


        });
    }

    //아이디 중복검사 버튼 클릭 시 실행하는 메소드
    private void checkIdDuplicaton() {

        final String userId = user_id.getText().toString().trim();

        if(TextUtils.isEmpty(userId)) {
            user_id.setError("please Enter Id");
            user_id.requestFocus();
            return;
        }

        CheckIdDuplication ci = new CheckIdDuplication(userId);
        ci.execute();

    }

    //회원가입 버튼 클릭 시 실행하는 메소드
    private void joinUser() {

        final String userId = user_id.getText().toString().trim();
        final String userPassword = user_password.getText().toString().trim();
        final String userName = user_name.getText().toString().trim();
        final String userPhoneNumber = user_phone_number.getText().toString().trim();
        final String userAddress = user_address.getText().toString().trim();
        final String userAccount = user_account.getText().toString().trim();
        final String userPasswordConfirm = user_password_confirm.getText().toString().trim();

        if(TextUtils.isEmpty(userId)) {
            user_id.setError("please Enter this component");
            user_id.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(userPassword)) {
            user_password.setError("please Enter this component");
            user_password.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(userName)) {
            user_name.setError("please Enter this component");
            user_name.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(userPhoneNumber)) {
            user_phone_number.setError("please Enter this component");
            user_phone_number.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(userPasswordConfirm)) {
            user_password_confirm.setError("please Enter this component");
            user_password_confirm.requestFocus();
            return;
        }

        if (checkPasswordSecurity(userPassword)) {

            if (!userPassword.equals(userPasswordConfirm)) {

                Toast.makeText(this, "비밀번호가 일치하지 않습니다. 재입력 해주시기 바랍니다.", Toast.LENGTH_SHORT).show();

            } else {

                //여기다가 final boolean 타입의 유무 확인 두 변수가 true일 경우 를 만들어야 함
                //만들 것 1. 아이디 중복 체크 하는 AsyncTask
                //만들 것 2. 문자 인증하는 AsyncTask

                if(afterAuth == true && afterCheckId == true) {

                    JoinUser ju = new JoinUser(userId, userPassword, userName, userPhoneNumber, userAddress, userAccount);
                    ju.execute();

                } else if(afterCheckId == false) {

                    Toast.makeText(this, "아이디 중복검사를 진행해 주세요!", Toast.LENGTH_SHORT).show();

                } else if(afterAuth == false) {

                    Toast.makeText(this, "휴대폰 본인인증을 진행해 주세요!", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(this, "에러 발생", Toast.LENGTH_SHORT).show();

                }

            }
        } else {

            Toast.makeText(this, "비밀번호가 안전성 정책에 어긋납니다. 규칙대로 비밀번호를 생성해주세요.", Toast.LENGTH_SHORT).show();

        }

    }

    //회원가입 클래스
    private class JoinUser extends AsyncTask<Void, Void, String> {

        private String user_id, user_password, user_name, user_phone_number, user_address, user_account;

        JoinUser(String user_id, String user_password, String user_name, String user_phone_number, String user_address, String user_account) {

            this.user_id = user_id;
            this.user_password = user_password;
            this.user_name = user_name;
            this.user_phone_number = user_phone_number;
            this.user_address = user_address;
            this.user_account = user_account;

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {

            RequestHandler requestHandler = new RequestHandler();

            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", user_id);
            params.put("user_password", user_password);
            params.put("user_name", user_name);
            params.put("user_phone_number", user_phone_number);
            params.put("user_address", user_address);
            params.put("user_account", user_account);

            String json =  requestHandler.sendPostRequest(URLS.URL_JOIN, params);

            Log.i("Join", "Info" + json);

            try {

                JSONObject obj = new JSONObject(json);

                if (!obj.getString("code").equals(404)) {

                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONObject userJson = obj.getJSONObject("user");

                    User user = new User(

                            userJson.getString("user_id"),
                            userJson.getString("user_name"),
                            userJson.getString("user_phone_number"),
                            userJson.getString("user_address"),
                            userJson.getString("user_account")
                    );

                    PrefManager.getInstance(getApplicationContext()).setUserLogin(user);
                    finish();


                    Intent intent = new Intent(JoinActivity.this, MainActivity.class);

                    String inputUserId = user.getUser_id();
                    String inputUserName = user.getUser_name();
                    String inputUserPhoneNumber = user.getUser_phone_number();
                    String inputUserAddress = user.getUser_address();
                    String inputUserAccount = user.getUser_account();

                    startActivity(intent);

                } else {

                    Toast.makeText(getApplicationContext(), "Some error occur", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return json;
        }

        /*@Override
        protected void onPostExecute(String json) {

            super.onPostExecute(json);
            Log.i("Join", "Info" + json);

            try {

                JSONObject obj = new JSONObject(json);

                if (!obj.getString("code").equals(404)) {

                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONObject userJson = obj.getJSONObject("user");


                } else {

                    Toast.makeText(getApplicationContext(), "Some error occur", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }*/
    }

    //아이디 중복 검사
    private class  CheckIdDuplication extends AsyncTask<Void, Void, String> {

        private String user_id;
        CheckIdDuplication(String user_id) { this.user_id = user_id; }

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        @Override
        protected String doInBackground(Void... voids) {

            RequestHandler requestHandler = new RequestHandler();

            HashMap<String, String > params = new HashMap<>();
            params.put("user_id", user_id);

            String json = requestHandler.sendPostRequest(URLS.URL_CHECK_ID_DUPLICATION, params);

            Log.i("check_id_duplication", "Info" + json);

            try {

                JSONObject obj = new JSONObject(json);

                if (!obj.getString("code").equals(404)) {

                    if (obj.getString("code").equals(204)){//해당 아이디가 이미 존재 할 경우

                        Toast.makeText(getApplicationContext(), "해당 아이디는 이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();

                    } else { //code:200 해당 아이디가 사용 가능할 경우

                        afterCheckId = true;
                        Toast.makeText(getApplicationContext(), "사용가능한 아이디 입니다.", Toast.LENGTH_SHORT).show();
                    }

                } else { //code:404 에러 발생

                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return json;
        }

    }

    //비밀번호 안전성 검사
    public boolean checkPasswordSecurity(String user_password){

        String passwordPattern = "^(?=.*\\d)(?=.*[~!@#$%^&*()-])(?=.*[a-z]).{8,14}$";
        Matcher matcher = Pattern.compile(passwordPattern).matcher(user_password);

        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    //뒤로가기 버튼
    public void onClick_Back(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
