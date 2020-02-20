package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Text;

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
    }
}
