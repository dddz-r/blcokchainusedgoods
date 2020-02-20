package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
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
import java.util.Random;

public class AuthPhoneNumber extends AppCompatActivity {

    private int certNumLength = 6;
    public static boolean personAllow = false;
    public static String phone_Number;

    EditText auth_number;
    Button auth_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_phone_number);

        auth_button = findViewById(R.id.AuthButton);
        auth_number = findViewById(R.id.authNumberEditText);

        Intent intent = getIntent();
        phone_Number = intent.getStringExtra("inputPhoneNumber");

        final AuthAttribute aa = new AuthAttribute(phone_Number, personAllow);

        String authNumber = executeGenerate();

        //액티비티가 시작하자 마자 번호를 전송
        //인증 버튼을 클릭하면 authNumber와 비교
        PhoneNumberAuth pa = new PhoneNumberAuth(phone_Number, authNumber);
        pa.execute();

        //버튼 클릭 이벤트
        auth_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String inputNumber = auth_number.getText().toString().trim();

                if(TextUtils.isEmpty(inputNumber)) {
                    auth_number.setError("please Enter authentication number");
                    auth_number.requestFocus();
                    return;
                }

                if(inputNumber.equals(auth_number)) {

                    Toast.makeText(getApplicationContext(), "휴대전화 인증 성공", Toast.LENGTH_SHORT).show();
                    aa.personAllow = true;

                    Intent back = new Intent(AuthPhoneNumber.this, JoinActivity.class);
                    back.putExtra("personAllow", aa.personAllow);
                    back.putExtra("user_phone_number", aa.phoneNumber);

                    startActivity(back);

                } else {

                    Toast.makeText(getApplicationContext(), "휴대전화 인증 실패", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    //랜덤 난수 생성 메소드
    public String executeGenerate() {

        Random random = new Random(System.currentTimeMillis());

        int range = (int) Math.pow(10, certNumLength);
        int trim = (int) Math.pow(10, certNumLength -1);
        int result  = random.nextInt(range) + trim;

        if(result > range) {

            result = result - trim;

        }

        return String.valueOf(result);

    }

    //휴대폰 번호를 가져오는 메소드
    public String getPhone_Number() { return phone_Number; }

    //인증된 사람인지를 boolean 타입으로 가져오는 메소드
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

    //휴대폰 인증 쓰레드
    //결과는 발송 되었는지 말았는지
    //EditText의 값이 랜덤 난수와 같은지는 해당 클래스에서 비교(인증 버튼 클릭 시)
    //같다면 personAllow를 true로 바꿔 Intent로 join에 전해줄 것
    class PhoneNumberAuth extends AsyncTask<Void, Void, String> {

        private String user_phone_number;
        private String randomAuthNumber;

        PhoneNumberAuth(String user_phone_number, String randomAuthNumber) {

            this.user_phone_number = user_phone_number;
            this.randomAuthNumber = randomAuthNumber;

        }

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        @Override
        protected String doInBackground(Void... voids) {

            RequestHandler requestHandler = new RequestHandler();

            HashMap<String, String> params = new HashMap<>();
            params.put("user_phone_number", user_phone_number);
            params.put("randomAuthNumber", randomAuthNumber);

            String json = requestHandler.sendPostRequest(URLS.URL_AUTH_PHONENUMBER, params);

            Log.i("authentication", "Info" + json);

            try {

                JSONObject obj = new JSONObject(json);

                if (!obj.getString("code").equals(404)) {

                    if (obj.getString("code").equals(200)) { //문자가 발송된 경우

                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } else { //문자가 발송 안 된 경우. code 가 404인 경우

                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {

                e.printStackTrace();
            }

            return json;
        }
    }
}
