package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SellScreen extends AppCompatActivity {

    Button addItems;
    Button sell_Ok;
    EditText device_name;
    Spinner device_category;
    EditText device_inform;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    int conuti =0;
    String category;

    //constant
    final int PICTURE_REQUEST_CODE = 100;

    //private  ArrayList<Bitmap> imageList;
    private ArrayList<Integer> imageList;
    //private ArrayList<String> imageList;
    ViewPager ss_viewpager;

    private Boolean isPermission = true;
    private static final int PICK_FROM_ALBUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_screen);

        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        addItems = (Button)findViewById(R.id.addItems);

        device_name = (EditText)findViewById(R.id.device_name);
        InputFilter[] device_nameFilter = new InputFilter[1];
        device_nameFilter[0]=new InputFilter.LengthFilter(20);
        device_name.setFilters(device_nameFilter);

        device_category = (Spinner) findViewById(R.id.device_category);

        device_inform = (EditText)findViewById(R.id.device_inform);
        InputFilter[] device_informFilter = new InputFilter[1];
        device_informFilter[0]=new InputFilter.LengthFilter(1000);
        device_inform.setFilters(device_informFilter);

        sell_Ok = (Button)findViewById(R.id.sell_Ok);
        //ss_viewpager = (ViewPager)findViewById(R.id.ss_viewpager);

        /*뷰페이저 구간*/
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


        /*갤러리 사용자권한? 안드버전따라서 필요할수도있는데 일단쓰면 꺼짐*/
        //tedPermission();

        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isPermission) goToAlbum();
                else Toast.makeText(v.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                conuti++;
            }

        });

        sell_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellScreen.this, BuyScreen.class));
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

        /*유저 이름 들고오는 코드*/
        /*final PrefManager prefManager = PrefManager.getInstance(SellScreen.this);
        User user = prefManager.getUser();

        if (prefManager.isLoggedIn()) {
            cg_writer.setText(String.valueOf(user.getEmail()));
        }*/
    }

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
    }

    /*go앨범 하고나서 실행된다*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;
        // Check which request we're responding to
        if (requestCode == PICK_FROM_ALBUM) {
           if(data == null){
               Toast.makeText(SellScreen.this, "선택된 사진이 없습니다.", Toast.LENGTH_SHORT).show();
            }else {
                if(data.getClipData()==null){
                    Toast.makeText(SellScreen.this, "다중선택이 불가능한 기기입니다.", Toast.LENGTH_SHORT).show();
                    //imageList.add(String.valueOf(data.getData()));
                        if(conuti==1) imageView1.setImageURI(data.getData());
                        else if(conuti==2) imageView2.setImageURI(data.getData());
                        else if(conuti==3) imageView3.setImageURI(data.getData());
                        else if(conuti==4) imageView4.setImageURI(data.getData());
                        else if(conuti==5) {imageView5.setImageURI(data.getData());conuti=0;}

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        //imageList.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    ClipData clipData = data.getClipData();

                    if (clipData.getItemCount()>5){
                        Toast.makeText(SellScreen.this, "사진은 최대 5장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show();
                    }else if (clipData.getItemCount() == 1){
                        String dataStr = String.valueOf(clipData.getItemAt(0).getUri());
                        imageView1.setImageURI(clipData.getItemAt(0).getUri());
;                       try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), clipData.getItemAt(0).getUri());
                            Drawable drawable = new BitmapDrawable(bitmap);
                            //imageList.add(getResources().getDrawable(drawable));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //imageList.add(dataStr);
                    }else if (clipData.getItemCount() >1 && clipData.getItemCount() < 5){
                        int i;
                        for(i = 0 ; i < clipData.getItemCount() ; i++){
                            try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), clipData.getItemAt(i).getUri());
                                //imageList.add(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //imageList.add(String.valueOf(clipData.getItemAt(i).getUri()));
                            if(i==0)imageView1.setImageURI(clipData.getItemAt(i).getUri());
                            else if(i==1)imageView2.setImageURI(clipData.getItemAt(i).getUri());
                            else if(i==2)imageView3.setImageURI(clipData.getItemAt(i).getUri());
                            else if(i==3)imageView4.setImageURI(clipData.getItemAt(i).getUri());
                            else if(i==4)imageView5.setImageURI(clipData.getItemAt(i).getUri());
                        }
                        i=0;
                    }
                }
           }
        }

    }

    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);//여러장
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_FROM_ALBUM);

    }



    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }
}
