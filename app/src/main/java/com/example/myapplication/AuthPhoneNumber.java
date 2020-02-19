package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class AuthPhoneNumber extends AppCompatActivity {

    public static boolean personAllow = false;
    public static String phone_Number;

    EditText auth_number;
    Button auth_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_phone_number);

        Intent intent =getIntent();
        phone_Number = intent.getStringExtra("inputPhoneNumber");


    }

    //휴대폰 번호 가져오기
    public String getPhone_Number() { return phone_Number; }

    //인증된 사람인지 가져오기
    public boolean getPersonAllow() { return personAllow; }

    //인증 속성 클래스
    class AuthAttribute{

        AuthPhoneNumber ap = new AuthPhoneNumber();
        String phoneNumber = ap.getPhone_Number();
        boolean personAllow = ap.getPersonAllow();

        public AuthAttribute(String phoneNumber, boolean personAllow){
            this.phoneNumber = phoneNumber;
            this.personAllow = personAllow;
        }
    }
}
