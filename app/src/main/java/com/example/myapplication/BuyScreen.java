package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    ImageView bs_test1;
    ImageView bs_test2;
    ImageView bs_test3;
    ImageView bs_test4;
    ImageView bs_test5;

    private ArrayList<Integer> imageList;

    String object_owner;
    String firstRegister;
    String numTransaction; // 총 거래된 횟수
    String register_number;

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
    private  ArrayList<Bitmap> bitmapImageList = new ArrayList<>();
    int img_cnt = 0;

    ArrayList<ObjectBlock> objectBlocks;


    Thread stateThread = new Thread(

            /*본인이 올린 글에서는 구매하기 버튼 안보인다*/
            //대신 배송확인 눌리는 버튼이 있어야한다.
            new Runnable() {
                public void run() {

                    if (object_state.equals("onSale")) {//구매요청전에는 택배보내면 안되지
                        //bs_sendOk.setVisibility(View.GONE);
                        Message msg = handler1.obtainMessage();
                        handler1.sendMessage(msg);
                    } else if (object_state.equals("onBuy")) {
                        bs_buyOk.setText("구매중");
                        bs_sendOk.setText("배송 확인");
                    } else if (object_state.equals("onTransportation")) {
                        bs_sendOk.setText("배송중");//클릭할수없도록 내일 설정하자
                        bs_sendOk.setEnabled(false);
                        bs_buyOk.setText("수취 확인");
                    }
                }
            }
    );

    Thread whoThread = new Thread(
            new Runnable() {
                public void run() {
                    if(buyer_id == null){
                        Message msg = handler3.obtainMessage();
                        handler1.sendMessage(msg);
                    }else if (buyer_id.equals(object_owner)) {
                        if (object_state.equals("onSale")) {//구매요청전에는 택배보내면 안되지
                            Message msg = handler1.obtainMessage();
                            handler1.sendMessage(msg);
                        }
                        Message msg = handler2.obtainMessage();
                        handler2.sendMessage(msg);
                        /*bs_sendOk.setVisibility(View.VISIBLE);
                        bs_buyOk.setVisibility(View.GONE);
                        bs_seller_inform_btn.setVisibility(View.GONE);*/
                    }else {
                        Message msg = handler1.obtainMessage();
                        handler1.sendMessage(msg);
                    }

                }
            }
    );

    final Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            bs_sendOk.setVisibility(View.GONE);
            bs_buyOk.setVisibility(View.VISIBLE);
            bs_seller_inform_btn.setVisibility(View.VISIBLE);
        }
    };

    final Handler handler2 = new Handler() {
        public void handleMessage(Message msg) {
            bs_sendOk.setVisibility(View.VISIBLE);
            bs_buyOk.setVisibility(View.GONE);
            bs_seller_inform_btn.setVisibility(View.GONE);
        }
    };

    final Handler handler3 = new Handler() {
        public void handleMessage(Message msg) {
            bs_sendOk.setVisibility(View.GONE);
            bs_buyOk.setVisibility(View.GONE);
            bs_seller_inform_btn.setVisibility(View.GONE);
        }
    };
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(BuyScreen.this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_screen);

        bs_test1 = findViewById(R.id.bs_test1);
        bs_test2 = findViewById(R.id.bs_test2);
        bs_test3 = findViewById(R.id.bs_test3);
        bs_test4 = findViewById(R.id.bs_test4);
        bs_test5 = findViewById(R.id.bs_test5);

        /*물건정보 들고오기*/
        register_number = getIntent().getStringExtra("register_number");
        //object_state = getIntent().getStringExtra("object_state");
        //Toast.makeText(getApplicationContext(),  register_number, Toast.LENGTH_SHORT).show();
        BuyScreen.getObjectState gos = new BuyScreen.getObjectState(register_number);
        gos.execute();

        BuyScreen.getObject go = new BuyScreen.getObject(register_number);
        go.execute();

        for(img_cnt =0 ; img_cnt <5 ;img_cnt++){
        BuyScreen.getImg gi = new BuyScreen.getImg(register_number,String.valueOf(img_cnt));
        gi.execute();
        }


        bs_device_name = findViewById(R.id.bs_device_name);
        bs_device_price = findViewById(R.id.bs_device_price);
        bs_device_description = findViewById(R.id.bs_device_description);

        bs_sendOk = findViewById(R.id.bs_send_Ok);
        bs_buyOk = findViewById(R.id.bs_buyOK);
        bs_seller_inform_btn = findViewById(R.id.bs_seller_inform_btn);



        /* buyer id */
        final PrefManager prefManager = PrefManager.getInstance(BuyScreen.this);
        user = prefManager.getUser();
        if (prefManager.isLoggedIn()) {

            buyer_id = String.valueOf(user.getUser_id());

        } else { //로그인 안 되어있을 경우
            buyer_id = null;
        }


        /*물품 정보 팝업창 띄우기*/
        bs_device_inform_btn = (Button) findViewById(R.id.bs_device_inform_btn);

        bs_device_inform_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BuyScreen.getObjectPopup gop = new BuyScreen.getObjectPopup(object_number);
                //gop.execute();
                AlertDialog.Builder ad = new AlertDialog.Builder(BuyScreen.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                ad.setTitle("상품 정보");
                ad.setMessage("최초 등록일 : " + firstRegister + "\n거래 횟수 : " + numTransaction + "\n판매자 : " + object_owner);

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

        bs_sendOk = findViewById(R.id.bs_send_Ok);
        bs_buyOk = findViewById(R.id.bs_buyOK);
        bs_seller_inform_btn = findViewById(R.id.bs_seller_inform_btn);


        /* 배송확인버튼 - 판매자가 택배를 보낸 후 클릭해야하는 버튼 */
        bs_sendOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (object_state.equals("onSale")) {
                    Toast.makeText(getApplicationContext(), "구매요청이없습니다.", Toast.LENGTH_SHORT).show();
                } else {
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
            }

        });



        /*구매버튼*/
        //onSale-onBuy-onTransportation-endSale 물건상태에따라 변해야함
        bs_buyOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefManager.isLoggedIn()) {
                    if (object_state.equals("onSale")) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(BuyScreen.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                        ad.setTitle("구매 확인");
                        ad.setMessage("물건을 구매하시겠습니까 ?");
                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                object_state = "onBuy";
                                BuyScreen.setObjectState sos = new BuyScreen.setObjectState(register_number, object_state);
                                sos.execute();

                                BuyScreen.setBuyReq sbr = new BuyScreen.setBuyReq(register_number, buyer_id);
                                sbr.execute();

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
                    } else if (object_state.equals("onTransportation")) {
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
                                BuyScreen.insertTransaction it = new BuyScreen.insertTransaction(register_number, object_owner, buyer_id, formatedDate);
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
                    } else if (object_state.equals("onBuy")) { //구매중이 아닌사람은 어쩌피 이 화면 볼수없어서 구매하기누른 사람만 취소할 수 있음
                        AlertDialog.Builder ad = new AlertDialog.Builder(BuyScreen.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                        ad.setTitle("구매요청중인 물건 입니다.");
                        ad.setMessage("구매요청을 취소하겠습니까?");
                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                BuyScreen.setObjectState sos = new BuyScreen.setObjectState(register_number, "onSale");
                                sos.execute();

                                BuyScreen.setBuyReq sbr = new BuyScreen.setBuyReq(register_number, null);
                                sbr.execute();

                                long now = System.currentTimeMillis();
                                Date date = new Date(now);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String formatedDate = dateFormat.format(date);
                                BuyScreen.insertTransaction it = new BuyScreen.insertTransaction(register_number, object_owner, buyer_id, formatedDate);
                                it.execute();
                                bs_buyOk.setText("구매하기");//onSale 상태
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
                }else{
                    Toast.makeText(getApplicationContext(), "로그인 해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*판매자 정보*/
        bs_seller_inform_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyScreen.this, SellerInform.class);
                intent.putExtra("seller_id", object_owner);
                startActivity(intent);
            }
        });


        /*뷰페이저 구간*/
        /*bs_viewpager = findViewById(R.id.bs_viewpager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        bs_viewpager.setAdapter(viewPagerAdapter);
        this.initializeData();
        bs_viewpager.setClipToPadding(false);

        //크기조절 적당히 고치기
        int dpValue = 16;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);
        bs_viewpager.setPadding(margin, 0, margin, 0);
        bs_viewpager.setPageMargin(margin / 2);

        // FragmentAdapter에 Fragment 추가, Image 개수만큼 추가
        for (int i = 0; i < imageList.size(); i++) {
            Frag_1 imageFragment = new Frag_1();
            Bundle bundle = new Bundle();
            bundle.putInt("imgRes", imageList.get(i));
            imageFragment.setArguments(bundle);
            viewPagerAdapter.addItem(imageFragment);
        }
        viewPagerAdapter.notifyDataSetChanged();

*/
    }

    //뷰페이저 세팅함수
    public void initializeData() {
        imageList = new ArrayList();

        imageList.add(R.drawable.onlydog);
        imageList.add(R.drawable.onlydog);
        imageList.add(R.drawable.onlydog);
        imageList.add(R.drawable.onlydog);
        imageList.add(R.drawable.onlydog);
    }
    private class getImg extends AsyncTask<String, Integer, Bitmap> { //서버
        private String register_number;
        String img_cnt;
        Bitmap bitmapImg = null;

        getImg(String register_number, String img_cnt) {
            this.register_number = register_number;
            this.img_cnt = img_cnt;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            /*try {
                URL myFileUrl = new URL(URLS.URL_GETIMG);
                HttpURLConnection connection = (HttpURLConnection) myFileUrl.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();*/


                HashMap<String, String> params = new HashMap<>();
                params.put("register_number", register_number);
                params.put("img_cnt", img_cnt);


                /*OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write(getPostDataString(params));

                Log.d("request 입력", getPostDataString(params));
                writer.flush();
                writer.close();
                os.close();



                int responseCode = connection.getResponseCode();

                Log.d("이미지리스폰스",Integer.toString(responseCode));



                InputStream is = ThisSendPostRequest(URLS.URL_GETIMG, params);


                //InputStream is = conn.getInputStream();


                bitmapImg = BitmapFactory.decodeStream(is);
            }catch (IOException e) {
                e.printStackTrace();
            }*/

            InputStream is = ThisSendPostRequest(URLS.URL_GETIMG, params);
            //Log.d("is",is.toString());
            bitmapImg = BitmapFactory.decodeStream(is);
            return bitmapImg;

        }

        protected void onPostExecute (Bitmap img){

            bitmapImageList.add(img);
            Log.d("aaaaaaaaaaaaasssssssssssdddddddddfffff", String.valueOf(bitmapImageList.size()));
            if(bitmapImageList.size()==1){
                bs_test1.setImageBitmap(bitmapImageList.get(0));
            }else if(bitmapImageList.size()==2){
                bs_test2.setImageBitmap(bitmapImageList.get(1));
            }else if(bitmapImageList.size()==3){
                bs_test3.setImageBitmap(bitmapImageList.get(2));
            }else if(bitmapImageList.size()==4){
                bs_test4.setImageBitmap(bitmapImageList.get(3));
            }else if(bitmapImageList.size()==5){
                bs_test5.setImageBitmap(bitmapImageList.get(4));
            }





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









    private class getObjectPopup extends AsyncTask<Void, Void, String> { //DB
        private String object_number;

        getObjectPopup(String object_number) {
            this.object_number = object_number;
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
                params.put("object_number", object_number); //오브젝트 넘버가 같은 레지스터넘버 갯수, 젤 처음블록 레지스터 넘버

                return requestHandler.sendPostRequest(URLS.URL_GET_OBJECT_REGISTER_NUMBER, params);


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("doInBackground 에러", "doInBackground Exception");
            }
            return null;

        }

        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            Log.d("onPost실행", "onPost실행");

            try {

                JSONObject object = new JSONObject(s);

                JSONArray jsonArray = object.getJSONArray("objdb");
                //Toast.makeText(getApplicationContext(), object.getString("reviews"), Toast.LENGTH_SHORT).show();

                int count = 0;

                while (count < jsonArray.length()) {

                    JSONObject json = jsonArray.getJSONObject(count);

                    String register_number = json.getString("register_number");
                    //오브젝트 넘버가 같은 레지스터넘버 갯수, 젤 처음블록 레지스터 넘버

                    if (count == 0) { //처음 등록 날짜 가져오기 위함
                        BuyScreen.getObjectPopupBlock gopb = new BuyScreen.getObjectPopupBlock(register_number);
                        gopb.execute();
                    }

                    count++;
                    numTransaction = String.valueOf(count);
                }
                //finish();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "JSONException", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private class getObjectPopupBlock extends AsyncTask<Void, Void, String> { //블록체인
        private String registerNumber;

        getObjectPopupBlock(String registerNumber) {
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

        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            Log.d("onPost실행", "onPost실행");

            try {

                JSONObject object = new JSONObject(s);


                JSONObject json = object.getJSONObject("obj");


                firstRegister = json.getString("registerTime");


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
                params.put("register_number", registerNumber);


                return requestHandler.sendPostRequest(URLS.URL_GET_OBJECT_BLOCK, params);


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("doInBackground 에러", "doInBackground Exception");
            }
            return null;

        }

        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            Log.d("onPost실행", "onPost실행");

            try {

                JSONObject object = new JSONObject(s);

                JSONObject json = object.getJSONObject("obj");
                //JSONArray jsonArray = object.getJSONArray("obj");
                //Toast.makeText(getApplicationContext(), object.getString("obj"), Toast.LENGTH_SHORT).show();

                int count = 0;

                //while(count < jsonArray.length()){

                //JSONObject json = jsonArray.getJSONObject(count);

                //+이미지도 들고와야함
                register_number = json.getString("registerNumber");
                object_number = json.getString("originObjectNumber");

                object_name = json.getString("objectName");
                bs_device_name.setText(object_name);

                object_information = json.getString("objectInformation");
                bs_device_description.setText(object_information);

                object_cost = json.getString("objectCost");
                bs_device_price.setText(object_cost);

                object_owner = json.getString("objectOwner");

                register_time = json.getString("registerTime");
                //object_state = json.getString("object_state");//<-블록체인이 아님! 따로 만들것

                whoThread.start();
                BuyScreen.getObjectPopup gop = new BuyScreen.getObjectPopup(object_number);
                gop.execute();
                //BuyScreen.getObjectState gos = new BuyScreen.getObjectState(register_number);
                //gos.execute();

                //count++;
                //}
                //finish();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "JSONException", Toast.LENGTH_SHORT).show();

            }

        }
    }


    private class getObjectState extends AsyncTask<Void, Void, String> { //DB
        private String registerNumber;


        getObjectState(String registerNumber) {
            this.registerNumber = registerNumber;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {

                Log.d("tag", "--------doInBackground실행");

                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("register_number", registerNumber);

                return requestHandler.sendPostRequest(URLS.URL_GET_OBJECT_DB, params);


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("doInBackground 에러", "doInBackground Exception");
            }
            return null;

        }

        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            Log.d("onPost---getobjectdb실행", "onPost실행");

            try {

                JSONObject object = new JSONObject(s);

                JSONObject json = object.getJSONObject("objdb");

                //JSONArray jsonArray = object.getJSONArray("obj");
                //Toast.makeText(getApplicationContext(), object.getString("obj"), Toast.LENGTH_SHORT).show();

                int count = 0;

                //while(count < jsonArray.length()){

                //JSONObject json = jsonArray.getJSONObject(count);

                //+이미지도 들고와야함
                String register_number = json.getString("register_number");
                //object_number = json.getString("originObjectNumber");

                object_state = json.getString("object_state");
                String object_category = json.getString("object_category");
                stateThread.start();


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "서버연결ㄴㄴ", Toast.LENGTH_SHORT).show();

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


                return requestHandler.sendPostRequest(URLS.URL_UPDATE_OBJECT_STATE, params);
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


    private class setBuyReq extends AsyncTask<Void, Void, String> { //데이터베이스

        private String register_number, buy_req_id;

        setBuyReq(String register_number, String buy_req_id) {

            this.register_number = register_number;
            this.buy_req_id = buy_req_id;

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
                params.put("buy_req_id", buy_req_id);


                return requestHandler.sendPostRequest(URLS.URL_UPDATE_BUY_REQ_ID, params);
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
                params.put("complete_time", completeTime);


                return requestHandler.sendPostRequest(URLS.URL_INSERT_TRANSACTION, params);//
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
