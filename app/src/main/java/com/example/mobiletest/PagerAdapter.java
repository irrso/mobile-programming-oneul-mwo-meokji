package com.example.mobiletest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.apache.log4j.chainsaw.Main;

public class PagerAdapter extends FragmentStateAdapter {

    public PagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        for(int i = 0; i < ((MainActivity)MainActivity.context).num_page; i++){
            if(position == i){ return new ShopFragment(((MainActivity)MainActivity.context).name[i],
                    ((MainActivity)MainActivity.context).rank[i], ((MainActivity)MainActivity.context).distance[i],
                    ((MainActivity)MainActivity.context).imageURL[i]);}
        }
        return new ShopFragment("초기", "초기", 0.0, "초기");
    }

    @Override
    public boolean containsItem(long itemId) {
        return super.containsItem(itemId);
    }

    @Override
    public int getItemCount() {
        return ((MainActivity)MainActivity.context).num_page;
    }
}