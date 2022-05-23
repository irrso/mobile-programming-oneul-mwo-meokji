package com.example.mobiletest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {

    public static Context context;
    ScrollView scrollView;
    Button prefBtn;
    int[] index = {0, 0, 0, 0, 0};
    public int num_page = 3; //크롤링해서 받은 가게 개수
    public String[] url = new String[num_page];
    ViewPager2 pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrollView = findViewById(R.id.ScrollView);
        scrollView.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        prefBtn = findViewById(R.id.prefBtn);
        prefBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = (scrollView.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(scrollView, new AutoTransition());
                scrollView.setVisibility(v);
            }
        });

        setUrl();

        pager = findViewById(R.id.viewPager);
        PagerAdapter adapter = new PagerAdapter(this);
        pager.setAdapter(adapter);

        context = this;
    }

    public void setClicked(View view){
        int idx;
        if(view.getId() == R.id.button2){ idx = 0; }
        else if(view.getId() == R.id.button3){ idx = 1; }
        else if(view.getId() == R.id.button4){ idx = 2; }
        else if(view.getId() == R.id.button5){ idx = 3; }
        else{ idx = 4; }
        if(index[idx] == 0){ view.setBackgroundColor(Color.parseColor("#595959")); index[idx] += 1;} //"#595959"
        else if(index[idx] == 1){view.setBackgroundColor(Color.parseColor("#919191")); index[idx] -= 1;}
    }

    public void setUrl(){ //크롤링해서 저장
        url[0] = "https://mblogthumb-phinf.pstatic.net/MjAyMDAxMTZfMTAg/MDAxNTc5MTU4NDUxMDA2.BNqfqi8nQV_zznxzcp0Stlv8gScRcRIOSvAU7Hf-jIAg.17JmbOB3ry4Dt10zrM8BkaCbUp90d-C8WpKQHTKk4jsg.JPEG.3761726/4.jpg?type=w800";
        url[1] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRwpFJiRPnW2-i2a3cZBv15scpEfdiwz98r6g&usqp=CAU";
        url[2] = "https://www.readersnews.com/news/photo/202201/104872_72982_4017.jpg";
    }
}