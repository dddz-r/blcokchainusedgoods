package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class WriteReview extends AppCompatActivity {

    TextView wr_commenter_id;
    Spinner wr_score;
    EditText wr_comment;
    Button wr_reviewOk;
    User user;

    String seller_id;
    String commenter_id;
    String comment;
    String score;
    int scoreInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_review);

        wr_comment = (EditText)findViewById(R.id.wr_comment);
        wr_commenter_id = (TextView)findViewById(R.id.wr_commenter_id);
        wr_score = (Spinner)findViewById(R.id.wr_score);
        wr_reviewOk = (Button)findViewById(R.id.wr_reviewOk);

        /*판매자 아이디 들고오기*/
        seller_id = "웨에에엑";

        /*작성자 아이디 들고오기*/
        final PrefManager prefManager = PrefManager.getInstance(WriteReview.this);
        user = prefManager.getUser();

        if(prefManager.isLoggedIn()){
            wr_commenter_id.setText(String.valueOf(user.getUser_id()));

        }else{ //로그인 안 되어있을 경우

        }

        /*평점 스피너*/
        wr_score.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                score = (String) parent.getItemAtPosition(position);
                ((TextView)parent.getChildAt(0)).setTextColor(Color.YELLOW);
                if(score=="☆☆☆☆☆"){
                    scoreInt = 0;
                }else if (score=="★☆☆☆☆"){
                    scoreInt = 1;
                }else if (score=="★★☆☆☆"){
                    scoreInt = 2;
                }else if (score=="★★★☆☆"){
                    scoreInt = 3;
                }else if (score=="★★★★☆"){
                    scoreInt = 4;
                }else if (score=="★★★★★"){
                    scoreInt = 5;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*작성자 아이디들고오기*/

        /*리뷰 작성 버튼*/
        wr_reviewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = wr_comment.getText().toString();
                reviewWrite rw = new reviewWrite(seller_id, commenter_id ,comment, score);
                rw.execute();

                startActivity(new Intent(WriteReview.this, SellerReview.class));

            }
        });


    }

    private class reviewWrite extends AsyncTask<Void, Void, String> {

        private String seller_id, commenter_id, comment, score;

        reviewWrite(String seller_id, String commenter_id, String comment, String score) {

            this.seller_id = seller_id;
            this.commenter_id = commenter_id;
            this.comment = comment;
            this.score = score;

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {


            try {
                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("seller_id", seller_id);
                params.put("commenter_id", commenter_id);
                params.put("comment", comment);
                params.put("score", score);


                return requestHandler.sendPostRequest(URLS.URL_REVIEW_WRITE, params);//이거 에러
            }catch (Exception e) {
            e.printStackTrace();
            Log.d("error", "doInBackground Exception");
        }
            return null;
        }

        public void onProgressUpdate(Void... values){
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            Log.i("talk", "Info" + s);

            try {

                JSONObject obj = new JSONObject(s);

                if (!obj.getString("code").equals(404)) {

                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    //JSONObject userJson = obj.getJSONObject("user");


                }else if(!obj.getString("code").equals(200)){
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                }else
                 {

                    Toast.makeText(getApplicationContext(), "Some error occur", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }




}
