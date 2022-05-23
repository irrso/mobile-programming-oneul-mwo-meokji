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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {

    public static Context context;
    ScrollView scrollView;
    Button prefBtn;
    int[] index = {0, 0, 0, 0, 0};
    public int num_page = 3; //크롤링해서 받은 가게 개수
    public String[] url = new String[num_page];
    ViewPager2 pager;
    TextView infoTxt, textView;
    public String[] info = new String[num_page];
    LinearLayout TextLayout;
    CircleIndicator3 indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //선호도 expandable
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

        //가게 정보 expandable
        TextLayout = findViewById(R.id.TextLayout);
        TextLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        textView = findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int vv = (TextLayout.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(TextLayout, new AutoTransition());
                TextLayout.setVisibility(vv);
            }
        });

        //
        infoTxt = findViewById(R.id.infoTxt);
        setInfoText();

        setUrl();

        //ViewPager2
        pager = findViewById(R.id.viewPager);
        PagerAdapter adapter = new PagerAdapter(this);
        pager.setAdapter(adapter);

        //Indicator
        indicator = findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        indicator.createIndicators(num_page, 0);

        //pager 설정
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                indicator.animatePageSelected(position);
                transInfo(position);
            }
        });

        context = this; //PagerAdpater에서 MainActivity 변수 사용

        //Toast.makeText(this, String.valueOf(pager.getCurrentItem()), Toast.LENGTH_LONG).show();
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

    public void setInfoText(){
        info[0] = "첫번째 가게";
        info[1] = "두번째 가게";
        info[2] = "세번째 가게";
    }

    /*public void transInfo(){
        for (int i = 0; i < num_page; i++){
            if(pager.getCurrentItem() == i){ infoTxt.setText(info[i]);}
        }
    }*/

    public void transInfo(int position){
        infoTxt.setText(info[position]);
    }
}