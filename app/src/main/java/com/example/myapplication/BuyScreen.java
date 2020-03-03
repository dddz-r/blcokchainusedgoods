package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class BuyScreen extends AppCompatActivity {

    Button bs_device_inform_btn;
    Button bs_seller_inform_btn;
    Button bs_buyOk;
    ViewPager bs_viewpager;
    TextView bs_device_name;
    TextView bs_device_price;
    TextView bs_device_description;
    String firstDate = "20.02.22";
    String numTransaction = "222";
    String seller_id = "수민";

    private ArrayList<Integer> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_screen);

        /*물품 정보 팝업창 띄우기*/
        bs_device_inform_btn = (Button)findViewById(R.id.bs_device_inform_btn);

        bs_device_inform_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(BuyScreen.this);
                ad.setTitle("상품 정보");
                ad.setMessage("최초 등록일 : "+firstDate+"\n거래 횟수 : "+numTransaction+"\n판매자 : "+seller_id);

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
                //ad.setNegativeButton();
            }
        });


        /*구매버튼*/

        /*판매자 정보*/
        bs_seller_inform_btn = findViewById(R.id.bs_seller_inform_btn);
        bs_seller_inform_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyScreen.this, SellerInform.class);
                intent.putExtra("seller_id", seller_id);
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
}
