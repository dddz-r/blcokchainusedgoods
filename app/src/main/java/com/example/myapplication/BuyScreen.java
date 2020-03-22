package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class BuyScreen extends AppCompatActivity {

    Button bs_device_inform_btn;
    Button bs_seller_inform_btn;
    Button bs_buyOk;
    Button bs_sendOk;
    ViewPager bs_viewpager;
    TextView bs_device_name;
    TextView bs_device_price;
    TextView bs_device_description;
    User user;
    private ArrayList<Integer> imageList;

    String object_owner = "수민";
    String firstDate = "20.02.22";
    String numTransaction = "222"; // 총 거래된 횟수
    String register_number = getIntent().getStringExtra("register_number"); //혹시나 에러뜨면 온크리에이트안에넣어!

    //물건정보 들고 올 거
    String object_name;
    String object_cost;
    String object_information;
    String object_number;
    String register_time;

    //거래정보 입력 할 거
    String buyer_id;
    String object_num;
    String transction_num;
    String transction_time;
    String object_state;

    ArrayList<ObjectBlock> objectBlocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_screen);

        /*물건정보 들고오기*/
        BuyScreen.getObject go = new BuyScreen.getObject(register_number);
        go.execute();

        bs_device_name = findViewById(R.id.bs_device_name);
        bs_device_name.setText(object_name);
        bs_device_price = findViewById(R.id.bs_device_price);
        bs_device_price.setText(object_cost);
        bs_device_description = findViewById(R.id.bs_device_description);
        bs_device_description.setText(object_information);

        /* buyer id */
        final PrefManager prefManager = PrefManager.getInstance(BuyScreen.this);
        user = prefManager.getUser();
        if(prefManager.isLoggedIn()){

            buyer_id = String.valueOf(user.getUser_id());

        }else{ //로그인 안 되어있을 경우

        }

        /*물품 정보 팝업창 띄우기*/
        bs_device_inform_btn = (Button)findViewById(R.id.bs_device_inform_btn);

        bs_device_inform_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyScreen.getObjectPopup gop = new BuyScreen.getObjectPopup(register_number);
                gop.execute();
                AlertDialog.Builder ad = new AlertDialog.Builder(BuyScreen.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                ad.setTitle("상품 정보");
                ad.setMessage("최초 등록일 : "+firstDate+"\n거래 횟수 : "+numTransaction+"\n판매자 : "+object_owner);

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
                //ad.setNegativeButton();//취소버튼 사용x
            }
        });


        /*본인이 올린 글에서는 구매하기 버튼 안보인다*/
        //대신 배송확인 눌리는 버튼이 있어야한다.
        if(buyer_id.equals(object_owner)){
            if(object_state.equals("onSale")){//구매요청전에는 택배보내면 안되지
                bs_sendOk.setVisibility(View.GONE);
            }
            bs_buyOk.setVisibility(View.GONE);
            bs_seller_inform_btn.setVisibility(View.GONE);
        }else {
            bs_sendOk.setVisibility(View.GONE);
        }

        if(object_state.equals("onBuy")){
            bs_buyOk.setText("구매중");
        }else if(object_state.equals("onTransportation")){
            bs_sendOk.setText("배송중");//클릭할수없도록 내일 설정하자
            bs_buyOk.setText("수취 확인");
        }

        /* 배송확인버튼 - 판매자가 택배를 보낸 후 클릭해야하는 버튼 */
        bs_sendOk = findViewById(R.id.bs_send_Ok);
        bs_sendOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(BuyScreen.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                ad.setTitle("배송 확인");
                ad.setMessage("물건을 발송 하였습니까?");
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        BuyScreen.setObjectState sos = new BuyScreen.setObjectState(register_number, "onTransportation");
                        sos.execute();
                        bs_buyOk.setText("배송중");
                    }
                });
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }

        });



        /*구매버튼*/
        //onSale-onBuy-onTransportation-endSale 물건상태에따라 변해야함
        bs_buyOk = findViewById(R.id.bs_buyOK);
        bs_buyOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (object_state.equals("onSale")) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(BuyScreen.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                    ad.setTitle("구매 확인");
                    ad.setMessage("물건을 구매하시겠습니까 ?");
                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            BuyScreen.setObjectState sos = new BuyScreen.setObjectState(register_number, "onBuy");
                            sos.execute();
                            bs_buyOk.setText("구매중");//onBuy 상태
                        }
                    });
                    ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                }else if(object_state.equals("onTransportation")){
                    AlertDialog.Builder ad = new AlertDialog.Builder(BuyScreen.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                    ad.setTitle("수취 확인");
                    ad.setMessage("물건을 수령하셨습니까?");
                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            BuyScreen.setObjectState sos = new BuyScreen.setObjectState(register_number, "endSale");
                            sos.execute();

                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                            String formatedDate = dateFormat.format(date);
                            BuyScreen.insertTransaction it = new BuyScreen.insertTransaction(register_number,object_owner,buyer_id,formatedDate);
                            it.execute();
                            bs_buyOk.setText("구매 완료");//onBuy 상태
                        }
                    });
                    ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                }
            }
        });

        /*판매자 정보*/
        bs_seller_inform_btn = findViewById(R.id.bs_seller_inform_btn);
        bs_seller_inform_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyScreen.this, SellerInform.class);
                intent.putExtra("seller_id", object_owner);
                startActivity(intent);
            }
        });


        /*뷰페이저 구간*/
        bs_viewpager = findViewById(R.id.bs_viewpager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        bs_viewpager.setAdapter(viewPagerAdapter);
        this.initializeData();//<-----이거가 사진을 추가하는 함수임! 갤러리에서 불러온이미지를 여기서 add하기
        bs_viewpager.setClipToPadding(false);

        //크기조절 적당히 고치기
        int dpValue = 16;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);
        bs_viewpager.setPadding(margin, 0, margin, 0);
        bs_viewpager.setPageMargin(margin/2);

        // FragmentAdapter에 Fragment 추가, Image 개수만큼 추가
        for (int i = 0; i < imageList.size(); i++) {
            Frag_1 imageFragment = new Frag_1();
            Bundle bundle = new Bundle();
            bundle.putInt("imgRes", imageList.get(i));
            imageFragment.setArguments(bundle);
            viewPagerAdapter.addItem(imageFragment);
        }
        viewPagerAdapter.notifyDataSetChanged();


    }

    //뷰페이저 세팅함수
    public void initializeData()
    {
        imageList = new ArrayList();

        imageList.add(R.drawable.onlydog);
        imageList.add(R.drawable.onlydog);
        imageList.add(R.drawable.onlydog);
        imageList.add(R.drawable.onlydog);
        imageList.add(R.drawable.onlydog);
    }

    private class getObjectPopup extends AsyncTask<Void, Void, String> { //블록체인
        private String registerNumber;

        getObjectPopup(String registerNumber) {
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
                params.put("registerNumber", registerNumber);

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

                JSONArray jsonArray = object.getJSONArray("popup");
                //Toast.makeText(getApplicationContext(), object.getString("reviews"), Toast.LENGTH_SHORT).show();

                int count = 0;

                while(count < jsonArray.length()){

                    JSONObject json = jsonArray.getJSONObject(count);
                    //소유자, 거래횟수, 처음거래일자 //다가져와서 안스에서 세야할수도 잇음
                    String objectOwner = json.getString("objectOwner"); //마지막블럭꺼
                    String firstRegister = json.getString("firstRegister"); //처음 블럭꺼
                    String transferCount = json.getString("transferCount"); //개수


                    count++;
                }
                //finish();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "JSONException", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private class getObject extends AsyncTask<Void, Void, String> { //블록체인
        private String registerNumber;

        getObject(String registerNumber) {
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
                params.put("registerNumber", registerNumber);

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

                JSONArray jsonArray = object.getJSONArray("obj");
                //Toast.makeText(getApplicationContext(), object.getString("reviews"), Toast.LENGTH_SHORT).show();

                int count = 0;

                while(count < jsonArray.length()){

                    JSONObject json = jsonArray.getJSONObject(count);

                    //+이미지도 들고와야함
                    //String registerNumber = json.getString("registerNumber");
                    object_number = json.getString("originObjectNumber");
                    object_name = json.getString("objectName");
                    object_cost = json.getString("objectInformation");
                    object_owner = json.getString("objectCost");
                    register_time = json.getString("objectOwner");
                    object_information = json.getString("objectInformation");
                    //object_state = json.getString("object_state");//<-블록체인이 아님! 따로 만들것


                    count++;
                }
                //finish();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "JSONException", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private class setObjectState extends AsyncTask<Void, Void, String> { //데이터베이스

        private String register_number, object_state;

        setObjectState(String register_number, String object_state) {

            this.register_number = register_number;
            this.object_state = object_state;

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
                params.put("register_number", register_number);
                params.put("object_state", object_state);


                return requestHandler.sendPostRequest(URLS.URL_REVIEW_WRITE, params);//주소 설정
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void onProgressUpdate(Void... values){
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            try {

                JSONObject obj = new JSONObject(s);

                if (!obj.getString("code").equals(404)) {

                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

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

    private class insertTransaction extends AsyncTask<Void, Void, String> {

        private String register_number, seller_id, buyer_id, completeTime;
//타임지우기
        insertTransaction(String register_number, String seller_id, String buyer_id, String completeTime) {

            this.register_number = register_number;
            this.seller_id = seller_id;
            this.buyer_id = buyer_id;
            this.completeTime = completeTime;
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
                params.put("register_number", register_number);
                params.put("seller_id", seller_id);
                params.put("buyer_id", buyer_id);
                params.put("completeTime", completeTime);


                return requestHandler.sendPostRequest(URLS.URL_REVIEW_WRITE, params);//
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void onProgressUpdate(Void... values){
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            try {

                JSONObject obj = new JSONObject(s);

                if (!obj.getString("code").equals(404)) {

                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

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
