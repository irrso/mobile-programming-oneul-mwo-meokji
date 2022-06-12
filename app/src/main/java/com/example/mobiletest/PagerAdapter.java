package com.example.mobiletest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PagerAdapter extends FragmentStateAdapter {
    public int num_page = 100; //크롤링해서 받은 가게 개수
    public String[] url; //크롤링해서 받은 사진 url Mainactivity에서 받아옴

    public PagerAdapter(FragmentActivity fa) {
        super(fa);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        url = ((MainActivity)MainActivity.context).imageURL;
        num_page = ((MainActivity)MainActivity.context).num_page;
        for(int i = 0; i < num_page; i++){
            if(position == i){ return new ShopFragment(url[i]);}
        }
        return new ShopFragment("http://www.urbanbrush.net/web/wp-content/uploads/edd/2018/09/urbanbrush-20180920005324420964.png");
    }

    @Override
    public int getItemCount() { return num_page; }
}