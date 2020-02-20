package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    TextView join, find_id,  change_password;
    EditText user_id, user_password;
    Button login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        join = (TextView)findViewById(R.id.join);
        find_id = (TextView)findViewById(R.id.find_id);
        change_password = (TextView)findViewById(R.id.change_password);

        login = findViewById(R.id.login);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, JoinActivity.class));
            }
        });

        find_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, FindIdActivity.class));
            }
        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ChangePasswordActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                user_id = findViewById(R.id.userId);
                user_password = findViewById(R.id.password);

                //여기서 메소드 실행
            }
        });
    }

    
}
