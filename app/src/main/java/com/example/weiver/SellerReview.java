package com.example.weiver;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SellerReview extends AppCompatActivity {

    ListView sr_listview;
    Button sr_write_review;
    SellerReviewAdapter sellerReviewAdapter = new SellerReviewAdapter();
    String seller_id;
    String user_id;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_review);

        final PrefManager prefManager = PrefManager.getInstance(SellerReview.this);
        user = prefManager.getUser();
        if(prefManager.isLoggedIn()){
            user_id=String.valueOf(user.getUser_id());

        }else{ //로그인 안 되어있을 경우

        }

        sr_listview = (ListView)findViewById(R.id.sr_listview);
        sr_write_review = (Button)findViewById(R.id.sr_write_review);

        sr_listview.setAdapter(sellerReviewAdapter);

        seller_id = getIntent().getStringExtra("seller_id");

        reviewExecute();


        sr_write_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefManager.isLoggedIn()){
                Intent intent = new Intent(SellerReview.this, WriteReview.class) ;
                intent.putExtra("seller_id", seller_id) ;
                startActivity(intent) ;
                }else{
                    Toast.makeText(getApplicationContext(), "로그인 해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void reviewExecute() {
        review r = new  review(seller_id);
        r.execute();
        Log.d("tag", "AsyncTask실행");
    }


    private class review extends AsyncTask<Void, Void, String> {
        private String seller_id;

        review(String seller_id) {
            this.seller_id = seller_id;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {

                Log.d("tag", "doInBackground실행");

                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("seller_id", seller_id);

                return requestHandler.sendPostRequest(URLS.URL_REVIEW_READ, params);


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("doInBackground 에러", "doInBackground Exception");
            }
                return null;

        }

        public void onProgressUpdate(Void... values){
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            Log.d("onPost실행", "onPost실행");

            try {

                JSONObject object = new JSONObject(s);

                JSONArray jsonArray = object.getJSONArray("reviews");
                //Toast.makeText(getApplicationContext(), object.getString("reviews"), Toast.LENGTH_SHORT).show();

                int count = 0;

                while(count < jsonArray.length()){

                    JSONObject json = jsonArray.getJSONObject(count);
                    String commenter_id = json.getString("commenter_id");
                    String comment = json.getString("comment");
                    String score = json.getString("score");

                    String scoreStar = "☆☆☆☆☆";
                    if (score.equals("0")) {
                        scoreStar = "☆☆☆☆☆";
                    } else if (score.equals("1")) {
                        scoreStar = "★☆☆☆☆";
                    } else if (score.equals("2")) {
                        scoreStar = "★★☆☆☆";
                    } else if (score.equals("3")) {
                        scoreStar = "★★★☆☆";
                    } else if (score.equals("4")) {
                        scoreStar = "★★★★☆";
                    } else if (score.equals("5")) {
                        scoreStar = "★★★★★";
                    }

                    sellerReviewAdapter.addSellerReview(commenter_id, scoreStar, comment);
                    sellerReviewAdapter.notifyDataSetChanged();
                    count++;
                }
                //finish();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "JSONException", Toast.LENGTH_SHORT).show();

            }

        }
    }



}
