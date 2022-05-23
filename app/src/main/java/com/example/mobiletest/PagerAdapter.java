package com.example.mobiletest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PagerAdapter extends FragmentStateAdapter {
    public int num_page = 3; //크롤링해서 받은 가게 개수
    public String[] url;
            //= new String[num_page];

    public PagerAdapter(FragmentActivity fa) {
        super(fa);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        url = ((MainActivity)MainActivity.context).url;
        for(int i = 0; i < num_page; i++){
            if(position == i){ return new shopFragment(url[i]);}
        }
        return new shopFragment("http://www.urbanbrush.net/web/wp-content/uploads/edd/2018/09/urbanbrush-20180920005324420964.png");
        //if(position == 0){ return new shopFragment("https://www.readersnews.com/news/photo/202201/104872_72982_4017.jpg");}
       // else return new shopFragment("https://media.cdnandroid.com/item_images/1097514/imagen-a-a-a-a-a-a-a-a-o-0thumb.jpeg");
    }

    @Override
    public int getItemCount() { return num_page; }
}