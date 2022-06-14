package com.example.mobiletest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Vector;

import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {

    public static Context context;
    Button prefBtn, infoBtn, selectBtn;
    public int num_page; //크롤링해서 받은 가게 개수
    ViewPager2 pager;
    CircleIndicator3 indicator;
    String[] idxToName = {
            "비빔밥", "칼국수", "국수", "국밥", "해장국", "곱창", "쌈밥", "부대찌개", "찜닭", "순대",
            "냉면", "고기", "족발", "도시락", "파스타", "돈가스", "스테이크", "피자", "필라프", "햄버거",
            "샌드위치", "토스트", "케밥", "소바", "라면", "초밥", "회", "연어", "우동", "카레",
            "짜장면", "짬뽕", "마라탕", "탕수육", "떡볶이", "김밥", "치킨"
    };

    boolean[] dislike = new boolean[37];
    LinearLayout[] categLayout = new LinearLayout[6];
    int lId[] = { R.id.HansikLayout, R.id.YangsikLayout, R.id.IllsikLayout, R.id.JungsikLayout, R.id.BunsikLayout, R.id.FastfoodLayout };
    Button[] categBtn = new Button[6];
    int cId[] = { R.id.hansik, R.id.yangsik, R.id.illsik, R.id.jungsik, R.id.bunsik, R.id.fastfood };
    boolean[] category = new boolean[6];
    int ctId[] = {R.id.hansik_t, R.id.yangsik_t, R.id.illsik_t, R.id.jungsik_t, R.id.bunsik_t, R.id.fastfood_t };
    TextView[] categText = new TextView[6];

    public String[] name = new String[100];
    public String[] classfication = new String[100];
    public String[] rank = new String[100];
    public String[] phoneNum = new String[100];
    public String[] imageURL = new String[100];
    public String[] worktime = new String[100];
    public double[] distance = new double[100];

    Vector<Vector<BasicInfo>> basicInfo;
    HashMap<String, Integer> nameToIdx;
    GetFood GF;
    Typeface customFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //폰트 가져오기
        customFont = ResourcesCompat.getFont(this, R.font.font_regular);

        //비선호 팝업
        prefBtn = findViewById(R.id.dislikeBtn);
        prefBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new DislikeFragment();
                dialogFragment.show(getSupportFragmentManager(), "DislikeDialog");
            }
        });

        //카테고리 레이아웃
        for(int i = 0; i < 6; i++){
            categLayout[i] = findViewById(lId[i]);
            int index = i;
            categLayout[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setClicked(index);
                }
            });
        }

        //카테고리 버튼
        for(int i = 0; i < 6; i++){
            categBtn[i] = findViewById(cId[i]);
            int index = i;
            categBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setClicked(index);
                }
            });
        }

        //카테고리 텍스트
        for(int i = 0; i < 6; i++) {
            categText[i] = findViewById(ctId[i]);
        }

        //가게 정보 팝업
        infoBtn = findViewById(R.id.infoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new InfoFragment();
                dialogFragment.show(getSupportFragmentManager(), "InfoDialog");
            }
        });

        // selectBtn 클릭
        selectBtn = findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GF = new GetFood();
                num_page = GF.SIZE;
                Log.d("DD", "num_page:"+num_page);
                setShop();
                setPager();
            }
        });

        context = this; //MainActivity 변수 사용

        CreateBasicInfo CBI =  new CreateBasicInfo();
        basicInfo = CBI.basicInfo;
        nameToIdx = CBI.nameToIdx;
    }

    //ViewPager, Indicator 설정
    public void setPager(){
        //ViewPager2
        pager = findViewById(R.id.viewPager);
        PagerAdapter adapter = new PagerAdapter(this);
        pager.setAdapter(adapter);

        //pager 설정
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                indicator.animatePageSelected(position);
            }
        });

        //Indicator
        indicator = findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        indicator.createIndicators(num_page, 0);
    }

    public void setShop(){
        for (int i=0; i<GF.SIZE; i++) {
            name[i] = GF.randomInfo.get(i).name;
            classfication[i] = GF.randomInfo.get(i).classification;
            rank[i] = GF.randomInfo.get(i).rank;
            phoneNum[i] = GF.randomInfo.get(i).phoneNum;
            imageURL[i] = GF.randomInfo.get(i).imageURL;
            worktime[i] = GF.randomInfo.get(i).workTime;
            distance[i] = GF.randomInfo.get(i).distance;
        }
    }

    //레이아웃 클릭
    public void setClicked(int i){
        /*for(int j = 0; j < 6; j++){
            if(category[j] == true){ categLayout[j].setBackgroundResource(R.drawable.layout_round); category[j] = false; }
        }*/
        if(category[i] == false){
            categLayout[i].setBackgroundResource(R.drawable.layout_round_clicked);
            categText[i].setTypeface(customFont, Typeface.BOLD); category[i] = true; }
        else{ categLayout[i].setBackgroundResource(R.drawable.layout_round);
            categText[i].setTypeface(customFont); category[i] = false; }
    }

    //크롤링 불러오기
    public InputStream get_path() {
        try {
            InputStream temp_is = getBaseContext().getResources().getAssets().open("crawling.xls");
            return temp_is;
        } catch (Exception e){
            e.printStackTrace();
            Log.d("crawling", "안됨");
            return null;
        }
    }
}