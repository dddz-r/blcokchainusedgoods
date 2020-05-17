package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Category2  extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private View drawerView;
    private GridView category_grid;

    TextView categoryTitle;
    ArrayList<MainGridItem> gridItems;
    MainGridAdapter gridViewAdapter;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(Category2.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_grid1);

        category_grid = (GridView)findViewById(R.id.category_grid);

        /*제목*/
        categoryTitle = (TextView)findViewById(R.id.categoryTitle);
        categoryTitle.setText("PC / 노트북");

        /* 그리드 뷰 (상품목록) */
        /*그리드 뷰 파싱해오기*/
        gridItems = new ArrayList<>();
        gridViewAdapter = new MainGridAdapter(gridItems);
        //이거 되면 카테고리 별로 다 복붙하고 여기 *대신 카테고리만 넣으면 됨
        //디비에서 카테고리에 해당하는 등록번호 다 들고오기(상태가 onSale인거)->서버에서 이미지/블록체인에서 이름,가격들고옴
        Category2.objectGrid og = new Category2.objectGrid("PC / 노트북"); //카테고리 전체라서 일단 *넣어둠
        og.execute();

        category_grid = (GridView)findViewById(R.id.category_grid);
        category_grid.setAdapter(gridViewAdapter);


        category_grid.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(Category2.this, BuyScreen.class);
                        intent.putExtra("register_number",gridItems.get(position).getRegister_number());
                        //Toast.makeText(getApplicationContext(), gridItems.get(position).getRegister_number(), Toast.LENGTH_SHORT).show();
                        startActivity(intent);

                    }
                }
        );

        category_grid.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(Category2.this, BuyScreen.class);
                        intent.putExtra("register_number",gridItems.get(position).getRegister_number());
                        //Toast.makeText(getApplicationContext(), gridItems.get(position).getRegister_number(), Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }
        );

    }


    private class objectGrid extends AsyncTask<Void, Void, String> { //DB
        private String object_category;

        objectGrid(String object_category) {
            this.object_category = object_category;
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
                params.put("object_category", object_category);

                return requestHandler.sendPostRequest(URLS.URL_GET_OBJECT_GRIDVIEW, params);


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

                JSONArray jsonArray = object.getJSONArray("objs");
                //

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


                    Category2.getImg gi = new Category2.getImg(register_number, object_name, object_price, "0");
                    gi.execute();


                    count++;
                }
                gridViewAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "서버가 꺼져있어요ㅠ-ㅠ", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private class getImg extends AsyncTask<String, Integer, Bitmap> { //서버
        private String register_number;
        String object_name;
        String object_price;
        String img_cnt;
        Bitmap bitmapImg = null;

        getImg(String register_number, String object_name, String object_price, String img_cnt) {
            this.register_number = register_number;
            this.object_name = object_name;
            this.object_price = object_price;
            this.img_cnt = img_cnt;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... urls) {

            HashMap<String, String> params = new HashMap<>();
            params.put("register_number", register_number);
            params.put("img_cnt", img_cnt);


            InputStream is = ThisSendPostRequest(URLS.URL_GETIMG, params);

            bitmapImg = BitmapFactory.decodeStream(is);
            return bitmapImg;

        }

        protected void onPostExecute (Bitmap img){

            MainGridItem inform = new MainGridItem(register_number, object_name, object_price, img);
            gridItems.add(inform);

            gridViewAdapter.notifyDataSetChanged();




        }


        public void onProgressUpdate(Void... values){
            super.onProgressUpdate();
        }

    }


    InputStream ThisSendPostRequest(String requestURL, HashMap<String, String> postDataParams) {

        URL url;
        StringBuilder sb = new StringBuilder();
        InputStream is = null;

        try{
            Log.d("url",requestURL);
            Log.d("params",postDataParams.toString());
            url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();//

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            writer.write(getPostDataString(postDataParams));

            Log.d("request 입력", getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();



            int responseCode = connection.getResponseCode();

            Log.d("이미지리스폰스",Integer.toString(responseCode));

            if (responseCode == HttpURLConnection.HTTP_OK) {
                is = connection.getInputStream();

                /*BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                sb = new StringBuilder();

                String response;

                while ((response = br.readLine()) != null) {

                    sb.append(response);
                }*/
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d("asdf", "ThisSendPostRequest: E11111111");

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("asdf", "ThisSendPostRequest: E222222222");
        }

        return is;

    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {

        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {

            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));

            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        Log.d("getPostData", result.toString());
        return result.toString();
    }



}
