package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

public class JoinActivity extends AppCompatActivity {

    EditText user_name, user_id, user_phone_number, user_password, user_password_confirm, user_address, user_account;
    Button check_id, auth_phone_number, join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        user_id = findViewById(R.id.userIdEditText);
        InputFilter[] twentyCharFilter = new InputFilter[1];
        twentyCharFilter[0] = new InputFilter.LengthFilter(20);
        user_id.setFilters(twentyCharFilter);

        user_password = findViewById(R.id.userPasswordEditText);
        InputFilter[] fortyCharFilter = new InputFilter[1];
        fortyCharFilter[0] = new InputFilter.LengthFilter(40);
        user_password.setFilters(fortyCharFilter);

        user_name = findViewById(R.id.userNameEditText);
        InputFilter[] thirtyCharFilter = new InputFilter[1];
        thirtyCharFilter[0] = new InputFilter.LengthFilter(30);
        user_name.setFilters(thirtyCharFilter);

        user_phone_number = findViewById(R.id.userPhoneNumberEditText);
        user_phone_number.setFilters(thirtyCharFilter);

        user_address = findViewById(R.id.userAddressEditText);
        InputFilter[] hundredCharFilter = new InputFilter[1];
        hundredCharFilter[0] = new InputFilter.LengthFilter(100);
        user_address.setFilters(hundredCharFilter);

        user_account = findViewById(R.id.userAccountEditText);
        user_account.setFilters(fortyCharFilter);

        user_password_confirm = findViewById(R.id.userPasswordConfirmEditText);
        user_password_confirm.setFilters(fortyCharFilter);

        //final Intent back = getIntent();
        //final String after_phone_number;
        //final boolean afterAuth;

        check_id = findViewById(R.id.check_user_id);
        auth_phone_number = findViewById(R.id.authPhoneNumberButton);
        join = findViewById(R.id.button);

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
    }

    private class JoinUser extends AsyncTask<Void, Void, String> {

        private String user_id, user_password, user_name, user_phone_number, user_address, user_account;

        JoinUser(String user_id, String user_password, String user_name, String user_phone_number, String user_address, String user_account) {

            this.user_id = user_id;
            this.user_password = user_password;
            this.user_name = user_phone_number;
            this.user_address = user_address;
            this.user_account = user_account;

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {
            return null;
        }
    }
}
