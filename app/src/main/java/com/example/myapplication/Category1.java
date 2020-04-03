package com.example.myapplication;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;

public class Category1  extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private View drawerView;
    private GridView category_grid;

    TextView categoryTitle;
    ArrayList<MainGridItem> gridItems;
    MainGridAdapter gridViewAdapter;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(Category1.this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_grid1);

        category_grid = (GridView)findViewById(R.id.category_grid);

        /*제목*/
        categoryTitle = (TextView)findViewById(R.id.categoryTitle);
        categoryTitle.setText("스마트폰 / 태블릿");

        /* 그리드 뷰 (상품목록) */
        /*그리드 뷰 파싱해오기*/
        gridItems = new ArrayList<>();
        gridViewAdapter = new MainGridAdapter(gridItems);
        //이거 되면 카테고리 별로 다 복붙하고 여기 *대신 카테고리만 넣으면 됨
        //디비에서 카테고리에 해당하는 등록번호 다 들고오기(상태가 onSale인거)->서버에서 이미지/블록체인에서 이름,가격들고옴
        Category1.objectGrid og = new Category1.objectGrid("스마트폰 / 태블릿"); //카테고리 전체라서 일단 *넣어둠
        og.execute();

        category_grid = (GridView)findViewById(R.id.category_grid);
        category_grid.setAdapter(gridViewAdapter);


        category_grid.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(Category1.this, BuyScreen.class);
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
                        startActivity(new Intent(Category1.this, BuyScreen.class));

                    }
                }
        );

    }


    private class objectGrid extends AsyncTask<Void, Void, String> {
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
                    String object_price = json.getString("object_cost"); /// 여기 이름 잘보기!

                    String object_number = json.getString("object_number");
                    String object_information = json.getString("object_information");
                    String object_owner = json.getString("object_owner");
                    String register_time = json.getString("register_time");

                    MainGridItem inform = new MainGridItem(register_number, object_name, object_price);
                    gridItems.add(inform);

                    gridViewAdapter.notifyDataSetChanged();

                    count++;
                }
                gridViewAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "서버가 꺼져있어요^^", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private class objectGridBlock extends AsyncTask<Void, Void, String> {
        private String registerNumber;

        objectGridBlock(String registerNumber) {
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

                int count = 0;

                //while(count < jsonArray.length()){

                JSONObject json = object.getJSONObject("obj");

                String register_number = json.getString("registerNumber");
                String object_name = json.getString("objectName");
                String object_price = json.getString("objectCost"); /// 여기 이름 잘보기!

                String object_number = json.getString("originObjectNumber");
                String object_information = json.getString("objectInformation");
                String object_owner = json.getString("objectOwner");
                String register_time = json.getString("registerTime");




                    MainGridItem inform = new MainGridItem(register_number, object_name, object_price);
                    gridItems.add(inform);

                    //ObjectBlock informm = new ObjectBlock(registerNumber,object_number,object_name,object_information,object_price, object_owner,register_time);
                    //objectBlocks.add(informm);


                //count++;
                //}
                gridViewAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "서버가 꺼져있어요^^", Toast.LENGTH_SHORT).show();

            }

        }
    }





}
