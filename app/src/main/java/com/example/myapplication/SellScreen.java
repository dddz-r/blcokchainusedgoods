package com.example.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import java.util.concurrent.ExecutionException;

//for making insert image function
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static android.content.Intent.ACTION_GET_CONTENT;
import static com.example.myapplication.URLS.URL_UPLOAD;

public class SellScreen extends AppCompatActivity {

    //for making insert image function
    ApiService apiService;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    Bitmap mBitmap;

    Button addItems;
    Button sell_Ok;
    CheckBox checkBox;
    EditText device_name;
    EditText device_price;
    Spinner device_category;
    EditText device_inform;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;


    int conuti =0;

    /* oItems랑 ob에 같은 인덱스에 같은 레지스터번호 있음 oItems는 ob 축소ver 표기용으로 이용 */
    ArrayList<ObjectBlock> ob = new ArrayList<>();
    ArrayList <String> oItems = new ArrayList<>();
    String objectNumber = " ";
    String user_id;
    String category;
    String register_number;
    User user;
    Uri picUri;

    private  ArrayList<Bitmap> bitmapImageList = new ArrayList<>();
    private ArrayList<Integer> imageList;
    private ArrayList<String> StringImageList = new ArrayList<>();
    //ViewPager ss_viewpager;

    private Boolean isPermission = true;
    private static final int PICK_FROM_ALBUM = 1;


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(SellScreen.this, MainActivity.class));
        finish();
    }
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_screen);

        askPermission();
        initRetrofitClient();
        /*유저 아이디 들고오는 코드*/
        final PrefManager prefManager = PrefManager.getInstance(SellScreen.this);
        user = prefManager.getUser();

        //askPermissions();
        //initRetrofitClient();
        if (prefManager.isLoggedIn()) {
            user_id = String.valueOf(user.getUser_id());
        }

        pd = new ProgressDialog(SellScreen.this);

        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);

        /*에딧텍스트들 인풋필터는 그냥 글자 수 제한이다*/
        device_name = (EditText)findViewById(R.id.device_name);
        InputFilter[] device_nameFilter = new InputFilter[1];
        device_nameFilter[0]=new InputFilter.LengthFilter(20);
        device_name.setFilters(device_nameFilter);

        device_price = (EditText)findViewById(R.id.device_price);

        device_category = (Spinner) findViewById(R.id.device_category);

        device_inform = (EditText)findViewById(R.id.device_inform);
        InputFilter[] device_informFilter = new InputFilter[1];
        device_informFilter[0]=new InputFilter.LengthFilter(1000);
        device_inform.setFilters(device_informFilter);

        //ss_viewpager = (ViewPager)findViewById(R.id.ss_viewpager);

        /*
        /*뷰페이저 구간 (사용x)
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        //ss_viewpager.setAdapter(viewPagerAdapter);
        this.initializeData();//<-----이거가 사진을 추가하는 함수임! 갤러리에서 불러온이미지를 여기서 add하기
        //ss_viewpager.setClipToPadding(false);

        //크기조절 적당히 고치기
        int dpValue = 16;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);
        //ss_viewpager.setPadding(margin, 0, margin, 0);
        //ss_viewpager.setPageMargin(margin/2);

        // FragmentAdapter에 Fragment 추가, Image 개수만큼 추가
        for (int i = 0; i < imageList.size(); i++) {
            Frag_1 imageFragment = new Frag_1();
            Bundle bundle = new Bundle();
            //bundle.putString("imgRes", imageList.get(i));
            bundle.putInt("imgRes", imageList.get(i));
            imageFragment.setArguments(bundle);
            viewPagerAdapter.addItem(imageFragment);
        }
        viewPagerAdapter.notifyDataSetChanged();
      */


        /*사진 추가 버튼*/
        addItems = (Button)findViewById(R.id.addItems);
        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isPermission) goToAlbum();
                else Toast.makeText(v.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                conuti++;
                if(conuti==5){
                    conuti=0;
                }
            }

        });

        /*판매하기 버튼*/
        sell_Ok = (Button)findViewById(R.id.sell_Ok);
        sell_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                String formatedDate = dateFormat.format(date);*/
                //물건넘버, 이름, 설명, 가격, 카테고리, 판매자, 등록시간
                SellScreen.insertObject io = new SellScreen.insertObject(objectNumber, device_name.getText().toString(), device_inform.getText().toString(), device_price.getText().toString(),category, user_id);
                io.execute();

                //SellScreen.insertImage ii = new SellScreen.insertImage(StringImageList);
                //ii.execute();
                startActivity(new Intent(SellScreen.this, MainActivity.class));
                finish();
            }
        });
        SellScreen.selectObjectPop sop = new SellScreen.selectObjectPop(user_id);
        sop.execute(); //여기서 oItems가 세팅된다.

        /*체크박스 */
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkBox.isChecked()){

                    /* oItems 랑 ob 에 같은 인덱스에 같은 레지스터번호 있음 */
                    //final CharSequence[] oItems = {"예", "를", "들", "어", "서"};
                    CharSequence [] ooItems = oItems.toArray (new String[oItems.size()]);
                    AlertDialog.Builder oDialog = new AlertDialog.Builder(SellScreen.this,
                            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

                    oDialog.setTitle("판매할 물품을 선택하세요")
                            .setItems(ooItems, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int i)
                                {
                                    /*String pickedObject = ooItems[i].toString(); //고른아이템을 스트링으로
                                    String data[] = pickedObject.split(":"); //data[0]은 레지스터 넘버, data[1]은 오브젝트네임
                                    String registerNumber = data[0];*/

                                    /* 오브젝트넘버설정, 상품이름, 설명만 자동으로 채워줌*/
                                    objectNumber = ob.get(i).getObject_number();
                                    String object_name = ob.get(i).getObject_name();
                                    String object_info = ob.get(i).getObject_information();

                                    device_name.setText(object_name);
                                    device_inform.setText(object_info);
                                }
                            })
                            .setCancelable(true)
                            .show();

                }else{

                }
            }
        });

        /*카테고리 스피너*/
        device_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String) parent.getItemAtPosition(position);
                ((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    /*
    //뷰페이저 세팅함수
    public void initializeData()
    {
        imageList = new ArrayList();

        //imageList.add(Integer.toString(R.drawable.onlydog));
        //imageList.add(Integer.toString(R.drawable.onlydog));
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.onlydog);
        Bitmap bitmap = drawable.getBitmap();
        Drawable drawablee = new BitmapDrawable(bitmap);
        //imageList.add(drawablee);
        imageList.add(R.drawable.onlydog);
    }*/

    /*go앨범 하고나서 실행된다*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String imagePath = null;
        ArrayList<String> imageListUri = new ArrayList<>();
        ArrayList<Uri> realUri = new ArrayList<>();
        Bitmap bitmap = null;


        if (requestCode == PICK_FROM_ALBUM) {
            if (data == null) {
                //Toast.makeText(SellScreen.this, "선택된 사진이 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                if (data.getClipData() == null) {
                    //Toast.makeText(SellScreen.this, "한장씩 선택.", Toast.LENGTH_SHORT).show();

                    //StringImageList.add(data.getData().toString()); // 스트링 어레이
                    if (conuti == 1) {
                        imageView1.setImageURI(data.getData());

                        // 스트링 어레이
                        Toast.makeText(SellScreen.this, data.getData().toString(), Toast.LENGTH_SHORT).show();
                        //StringImageList.add(String.valueOf(data.getData()));

                        // 비트맵 어레이
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                            bitmapImageList.add(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        //절대경로
                       /* String[] projection = {MediaStore.Images.Media.DATA};
                        Cursor c = getContentResolver().query(Uri.parse(data.getData().toString()), projection, null, null, null);
                        //c.moveToNext();
                        int column_index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        c.moveToFirst();
                        //String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                        String absolutePath = c.getString(column_index);
                        StringImageList.add(absolutePath);
                        Toast.makeText(SellScreen.this, absolutePath, Toast.LENGTH_SHORT).show();*/
                    } else if (conuti == 2) {
                        imageView2.setImageURI(data.getData());
                        StringImageList.add(String.valueOf(data.getData()));
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                            bitmapImageList.add(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (conuti == 3) {
                        imageView3.setImageURI(data.getData());
                        StringImageList.add(String.valueOf(data.getData()));
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                            bitmapImageList.add(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (conuti == 4) {
                        imageView4.setImageURI(data.getData());
                        StringImageList.add(String.valueOf(data.getData()));
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                            bitmapImageList.add(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (conuti == 5) {
                        imageView5.setImageURI(data.getData());
                        StringImageList.add(String.valueOf(data.getData()));
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                            bitmapImageList.add(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        conuti = 0;
                    }


                } else {
                    ClipData clipData = data.getClipData();

                    if (clipData.getItemCount() > 5) {
                        Toast.makeText(SellScreen.this, "사진은 최대 5장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show();
                    } else if (clipData.getItemCount() == 1) {

                        Uri tempUri;
                        tempUri = clipData.getItemAt(0).getUri();
                        imagePath = tempUri.toString();

                        String dataStr = String.valueOf(clipData.getItemAt(0).getUri());
                        //imageView1.setImageURI(clipData.getItemAt(0).getUri());
                        StringImageList.add(dataStr); // 스트링 어레이

                        bitmapImageList.add(BitmapFactory.decodeFile(imagePath));
                        imageView1.setImageBitmap(bitmapImageList.get(0));

                    } else if (clipData.getItemCount() > 1 && clipData.getItemCount() < 6) {
                        int i;
                        for (i = 0; i < clipData.getItemCount(); i++) {


                            Uri tempUri;
                            tempUri = clipData.getItemAt(i).getUri();
                            Log.i("temp: ", i + " " + tempUri.toString());
                            imageListUri.add(tempUri.toString());
                            realUri.add(tempUri);
                            //mBitmap.add(i, BitmapFactory.decodeFile(imageListUri.get(i)));

                            try {

                                bitmapImageList.add(i,BitmapFactory.decodeStream(getContentResolver().openInputStream(realUri.get(i))));

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            //imageList.add(String.valueOf(clipData.getItemAt(i).getUri()));

                            if (i == 0) imageView1.setImageBitmap(bitmapImageList.get(i));
                            else if (i == 1) imageView2.setImageBitmap(bitmapImageList.get(i));
                            else if (i == 2) imageView3.setImageBitmap(bitmapImageList.get(i));
                            else if (i == 3) imageView4.setImageBitmap(bitmapImageList.get(i));
                            else if (i == 4) imageView5.setImageBitmap(bitmapImageList.get(i));
                        }
                        i = 0;
                    }
                }
            }
        }

    }
    /*펄미션*/
    private void askPermission() {

        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);

        }

    }

    private void initRetrofitClient() {

        OkHttpClient client = new OkHttpClient.Builder().build();

        apiService = new Retrofit.Builder().baseUrl(URL_UPLOAD).client(client).build().create(ApiService.class);
    }

    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //Intent intent = new Intent(Intent.ACTION_PICK);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);//여러장
        //intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        //Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/이미지/");
        //intent.setDataAndType(uri, "image/*");
        intent.setAction(ACTION_GET_CONTENT);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_FROM_ALBUM);

    }

    //이미지 촬영 후 Uri데 안쓸거 같지만 일단 놔두자
    private Uri getCaptureImageOutputUri() {

        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    /*이미지 부분*/
    private String getImageFromFilePath(Intent data) {
        return getPathFromUri(data.getData());
    }

    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }

    private String getPathFromUri(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle
            outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        picUri = savedInstanceState.getParcelable("pic_uri");
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {

        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermissions(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermissions(String permissions) {

        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permissions) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {

        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:

                for (String perms : permissionsToRequest) {

                    if (!hasPermissions(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                        }
                                    });
                            return;
                        }
                    }
                }

                break;
        }

    }

    //실제 이미지를 업로드 하는 파트
    //여기서 파일의 이름을 바꿔야함
    private void multipartImageUpload(int index) {

        try {

            File filesDir = getApplicationContext().getFilesDir();
            //여기서 png 앞에를 유저 id + 레지스터 넘버 이런식으로 바꿀 것
            File file = new File(filesDir, register_number+"_" + index + ".png");
//filesDir.getName() +
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmapImageList.get(index).compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();


            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");

            Call<ResponseBody> req = apiService.postImage(body, name);

            req.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.code() == 200) {
                        //textView.setText("uploaded success");
                        //textView.setTextColor(Color.BLUE);
                    }

                   // Toast.makeText(getApplicationContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    //textView.setText("uploaded fail");
                    //textView.setTextColor(Color.RED);
                    Toast.makeText(getApplicationContext(), "요청에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /*여기밑엔 이미지뺀 물건정보*/
    private class selectObjectPop extends AsyncTask<Void, Void, String> { //DB
        private String user_id;

        selectObjectPop(String user_id) {
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

                return requestHandler.sendPostRequest(URLS.URL_GET_TRANSACTION_BUY_LIST, params);


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

                JSONArray jsonArray = object.getJSONArray("buyList");
                //Toast.makeText(getApplicationContext(), object.getString("reviews"), Toast.LENGTH_SHORT).show();

                int count = 0;

                while(count < jsonArray.length()){

                    JSONObject json = jsonArray.getJSONObject(count);
                    String registerNumber = json.getString("register_number");
                    String transaction_time = json.getString("complete_time");
                    //oItems.add(registerNumber+"  ("+transaction_time+") ");

                    SellScreen.selectObjectBlock sob = new SellScreen.selectObjectBlock(registerNumber);
                    sob.execute();

                    count++;

                }
                //finish();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "JSONException", Toast.LENGTH_SHORT).show();

            }

        }
    }
/*여기 중간에 물건 디비 거치도록 수정할 것!*/
    private class selectObjectBlock extends AsyncTask<Void, Void, String> { //블록체인->db
        private String registerNumber;


        selectObjectBlock(String registerNumber) {
            this.registerNumber = registerNumber;
        }

        @Override
        protected void onPreExecute() {
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("잠시만 기다려주세요.");
            pd.show();
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
            pd.dismiss();
            super.onPostExecute(s);
            Log.d("onPost실행", "onPost실행");

            try {

                JSONObject object = new JSONObject(s);

                //JSONArray jsonArray = object.getJSONArray("obj");
                //Toast.makeText(getApplicationContext(), object.getString("reviews"), Toast.LENGTH_SHORT).show();

                int count = 0;

                //while(count < jsonArray.length()){

                    JSONObject json = object.getJSONObject("obj");
                    String registerNumber = json.getString("registerNumber");
                    String object_number = json.getString("originObjectNumber");
                    String object_name = json.getString("objectName");
                    String object_cost = json.getString("objectCost");
                    String object_owner = json.getString("objectOwner");
                    String time = json.getString("registerTime");
                    String object_information = json.getString("objectInformation");
                    //String object_state = json.getString("objectState");
                    //String object_category = json.getString("category");

                String year = time.substring(11,15);
                String month = time.substring(4,7);
                String day = time.substring(8,10);
                String hour = time.substring(16,18);
                int hours = Integer.parseInt(hour);
                hours = hours + 9;

                if(hours >= 24) {
                    hours = hours - 24;
                }

                hour = Integer.toString(hours);
                String minutes = time.substring(19,21);

                String newTime =  month+ " " + day + " " + year +"." + hour + ":" + minutes;




                    ObjectBlock inform = new ObjectBlock(registerNumber,object_number,object_name,object_information,object_cost,object_owner,newTime);
                    ob.add(inform);
                    oItems.add("등록번호 : " + registerNumber+" ,물품명 : "+object_name+" (거래날짜 : "+newTime+") ");
                    //count++;
                //}
                //finish();
            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), "JSONException", Toast.LENGTH_SHORT).show();

            }

        }
    }


    private class insertObject extends AsyncTask<Void, Void, String> {

        private String objectNumber, objectCategory, objectName, objectInformation, objectCost, objectOwner;
        //registerNumber는 자동으로 서버에서 매겨짐
        //오브젝트 넘버도기본적으로 자동으로 증가하게 하지만, 만약 체크박스에 체크가 됐을 경우에는 기존의 오브젝트 넘버로 등록.

        insertObject(String objectNumber, String objectName, String objectInformation, String objectCost, String objectCategory, String objectOwner) {

            this.objectName = objectName;
            this.objectCost = objectCost;
            this.objectOwner = objectOwner;
            this.objectNumber = objectNumber;
            this.objectCategory = objectCategory;
            this.objectInformation = objectInformation;

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
                params.put("object_number",objectNumber);
                params.put("object_name", objectName);
                params.put("object_cost", objectCost);
                params.put("object_owner", objectOwner);
                params.put("category",objectCategory);
                params.put("object_information",objectInformation);

                return requestHandler.sendPostRequest(URLS.URL_INSERT_OBJECT, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            try {

                JSONObject obj = new JSONObject(s);

                if (!obj.getString("code").equals(404)) {

                    //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                } else if (!obj.getString("code").equals(200)) {

                    //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                } else {

                    //Toast.makeText(getApplicationContext(), "Some error occur", Toast.LENGTH_SHORT).show();

                }
                SellScreen.getRegisterNumber grn = new SellScreen.getRegisterNumber(user_id);
                grn.execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    private class getRegisterNumber extends AsyncTask<Void, Void, String> { //DB
        private String object_owner;


        getRegisterNumber(String object_owner) {
            this.object_owner = object_owner;
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
                params.put("object_owner", object_owner);

                return requestHandler.sendPostRequest(URLS.URL_GET_RECENT_REGISTER_NUMBER, params);


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
            Log.d("onPost---@@@@@@@@@@@@@@@getobjectdb실행", "onPost실행");

            try {

                JSONObject object = new JSONObject(s);

                JSONObject json = object.getJSONObject("num");


                register_number = json.getString("register_number");

                if (bitmapImageList != null) {

                    for (int i = 0; i < bitmapImageList.size(); i++)
                        multipartImageUpload(i);

                } else {
                    //Toast.makeText(getApplicationContext(), "Bitmap is null. Try again", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), "서버연결ㄴㄴ", Toast.LENGTH_SHORT).show();

            }

        }
    }


}
