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

public class JoinActivity extends AppCompatActivity {

    EditText user_name, user_id, user_phone_number, user_password, user_password_confirm, user_address, user_account;
    Button check_id, auth_phone_number, join;

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

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {

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

        JoinUser ju = new JoinUser(userId, userPassword, userName, userPhoneNumber, userAddress, userAccount);
        ju.execute();

    }

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

            return requestHandler.sendPostRequest(URLS.URL_JOIN, params);

        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            Log.i("Join", "Info" + s);

            try {

                JSONObject obj = new JSONObject(s);

                if (!obj.getString("code").equals(404)) {

                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONObject userJson = obj.getJSONObject("user");


                } else {

                    Toast.makeText(getApplicationContext(), "Some error occur", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
