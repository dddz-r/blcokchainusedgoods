package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;//State빼면 정해진 수의 프레그먼트

//물건 구매화면에서 사진넘기는거
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return Frag_1.newInstance();
        // 해당하는 page의 Fragment를 생성합니다.
        //return PageFragment.create(position);

        /*switch (position){
            case 0:
                return Frag_1.newInstance();

            default:
                return null;
        }*/
    }

    @Override
    public int getCount() {//사진개수 리턴해야함
        return 5;
    }

    //상단의 타이틀
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }
}
