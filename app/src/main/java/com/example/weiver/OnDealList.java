package com.example.weiver;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OnDealList extends AppCompatActivity { //판매

    private ListView odl_listView;

    ArrayList<BuyListItem> buyItems;
    BuyListAdapter buyListAdapter;// = new BuyListAdapter(buyItems);

    String user_id;
    String device_name;
    String device_price;
    String device_condition;
    Drawable image;
    TextView on_deal_text;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(OnDealList.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_deal_list);



        user_id = getIntent().getStringExtra("user_id");

        on_deal_text = findViewById(R.id.on_deal_text);
        on_deal_text.setText("판매중인 상품");

        buyItems = new ArrayList<>();
        buyListAdapter = new BuyListAdapter(buyItems);

        odl_listView = (ListView)findViewById(R.id.odl_listView);
        odl_listView.setAdapter(buyListAdapter);

        OnDealList.getOnDealDB getOnDealDB = new OnDealList.getOnDealDB(user_id);
        getOnDealDB.execute();



        //테스트//순서 : 이름 가격 상태 그림
        //buyListAdapter.addBuyItem(ContextCompat.getDrawable(this,R.drawable.onlydog),"멍멍이","999억","판매중");
        //List<String> buy_data = new ArrayList<>();

        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,buy_data);
        //bl_listView.setAdapter(adapter);

        //adapter.notifyDataSetChanged();

        // 리스트 아이템 눌렸을 때 이벤트 (쓸지안쓸지모름)
        odl_listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String buy_data = String.valueOf(parent.getItemAtPosition(position));
                        Intent intent = new Intent(OnDealList.this, BuyScreen.class) ;
                        intent.putExtra("register_number",buyItems.get(position).getRegister_number());
                        startActivity(intent);
                    }
                }
        );
    }

    private class getOnDealDB extends AsyncTask<Void, Void, String> {//트렌젝션디비
        private String user_id;//서버에서 SELECT * FROM transaction_info where buyer_id = "";

        getOnDealDB(String user_id) {
            this.user_id = user_id;
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
                params.put("user_id", user_id);

                return requestHandler.sendPostRequest(URLS.URL_GET_ON_DEAL_LIST, params);


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

                JSONArray jsonArray = object.getJSONArray("onDealList");
                //Toast.makeText(getApplicationContext(), object.getString("reviews"), Toast.LENGTH_SHORT).show();

                int count = 0;

                while(count < jsonArray.length()){

                    JSONObject json = jsonArray.getJSONObject(count);
                    String register_number = json.getString("register_number");
                    String object_name = json.getString("object_name");
                    String object_price = json.getString("object_cost");
                    String object_state = json.getString("object_state");
                    String object_number = json.getString("object_number");
                    String object_information = json.getString("object_information");
                    String object_owner = json.getString("object_owner");
                    String register_time = json.getString("register_time");

                    BuyListItem item = new BuyListItem(register_number,object_name, object_state,object_price); //,img
                    buyItems.add(item);

                    count++;
                }
                buyListAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), "JSONException", Toast.LENGTH_SHORT).show();

            }

        }
    }



}
