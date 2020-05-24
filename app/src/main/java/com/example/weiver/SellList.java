package com.example.weiver;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SellList extends AppCompatActivity {

    private ListView sl_listView;

    ArrayList<BuyListItem> buyItems;
    BuyListAdapter buyListAdapter;// = new BuyListAdapter(buyItems);

    String user_id;
    String device_name;
    String device_price;
    String device_condition;
    Drawable image;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(SellList.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_list);



        user_id = getIntent().getStringExtra("user_id");


        buyItems = new ArrayList<>();
        buyListAdapter = new BuyListAdapter(buyItems);

        sl_listView = (ListView)findViewById(R.id.sl_listView);
        sl_listView.setAdapter(buyListAdapter);

        SellList.getBuyList gbl = new SellList.getBuyList(user_id);
        gbl.execute();



        //테스트//순서 : 이름 가격 상태 그림
        //buyListAdapter.addBuyItem(ContextCompat.getDrawable(this,R.drawable.onlydog),"멍멍이","999억","판매중");
        //List<String> buy_data = new ArrayList<>();

        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,buy_data);
        //bl_listView.setAdapter(adapter);

        //adapter.notifyDataSetChanged();

        // 리스트 아이템 눌렸을 때 이벤트 (쓸지안쓸지모름)
        sl_listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String buy_data = String.valueOf(parent.getItemAtPosition(position));
                        //Toast.makeText(SellList.this, buy_data, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private class getBuyList extends AsyncTask<Void, Void, String> {//트렌젝션디비
        private String buyer_id;//서버에서 SELECT * FROM transaction_info where buyer_id = "";

        getBuyList(String buyer_id) {
            this.buyer_id = buyer_id;
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
                params.put("user_id", buyer_id);

                return requestHandler.sendPostRequest(URLS.URL_GET_TRANSACTION_SELL_LIST, params);


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

                JSONArray jsonArray = object.getJSONArray("sellList");
                //Toast.makeText(getApplicationContext(), object.getString("reviews"), Toast.LENGTH_SHORT).show();

                int count = 0;

                while(count < jsonArray.length()){

                    JSONObject json = jsonArray.getJSONObject(count);
                    String transactionNumber = json.getString("transaction_number");

                    SellList.getTransaction gt = new SellList.getTransaction(transactionNumber);
                    gt.execute();


                    count++;
                }
                //finish();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "JSONException", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private class getTransaction extends AsyncTask<Void, Void, String> { //트렌젝션 블록체인
        private String transactionNumber;

        getTransaction(String transactionNumber) {
            this.transactionNumber = transactionNumber;
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
                params.put("transaction_number", transactionNumber);

                return requestHandler.sendPostRequest(URLS.URL_GET_TRANSACTION_BLOCK, params);


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

                //JSONArray jsonArray = object.getJSONArray("transaction");
                //Toast.makeText(getApplicationContext(), object.getString("reviews"), Toast.LENGTH_SHORT).show();

                //int count = 0;

                //while(count < jsonArray.length()){

                JSONObject json = object.getJSONObject("transaction");

                String transactionNumber = json.getString("transactionNumber");
                String registerNumber = json.getString("registerNumber");
                String completeTime = json.getString("completeTime");

                SellList.getObjectBlock gob = new SellList.getObjectBlock(registerNumber);
                gob.execute();


                //count++;
                //}
                //finish();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "JSONException", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private class getObjectBlock extends AsyncTask<Void, Void, String> {//물건 블록체인
        private String registerNumber;

        getObjectBlock(String registerNumber) {
            this.registerNumber = registerNumber;
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
                params.put("register_number", registerNumber);

                return requestHandler.sendPostRequest(URLS.URL_GET_OBJECT_BLOCK, params);


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

                //JSONArray jsonArray = object.getJSONArray("obj");
                //Toast.makeText(getApplicationContext(), object.getString("reviews"), Toast.LENGTH_SHORT).show();

                //int count = 0;

                //while(count < jsonArray.length()){

                JSONObject json = object.getJSONObject("obj");

                //+이미지도 들고와야함
                String register_number = json.getString("registerNumber");
                String object_name = json.getString("objectName");
                String object_coast = json.getString("objectCost");
                String object_condition = "거래완료";

                BuyListItem item = new BuyListItem(register_number,object_name, object_condition, object_coast); //,img
                buyItems.add(item);


                //count++;
                //}
                buyListAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "서버가 꺼져있어요^^", Toast.LENGTH_SHORT).show();

            }

        }
    }

}
