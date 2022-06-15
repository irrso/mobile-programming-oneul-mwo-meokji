package com.example.mobiletest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {

    public static Context context;
    Button prefBtn, infoBtn, selectBtn, refreshBtn, mapBtn, leftBtn, rightBtn;
    TextView food_t;
    boolean[] option = new boolean[2];
    int num_page;
    ViewPager2 pager;
    CircleIndicator3 indicator;
    String[] idxToName = {
            "비빔밥", "칼국수", "국수", "국밥", "해장국", "곱창", "쌈밥", "부대찌개", "찜닭", "순대",
            "냉면", "고기", "족발", "도시락", "파스타", "돈가스", "스테이크", "피자", "필라프", "햄버거",
            "샌드위치", "토스트", "케밥", "소바", "라면", "초밥", "회", "연어", "우동", "카레",
            "짜장면", "짬뽕", "마라탕", "탕수육", "떡볶이", "김밥", "치킨"
    };
    int POSITION;

    boolean[] dislike = new boolean[37];
    LinearLayout[] categLayout = new LinearLayout[6];
    int lId[] = { R.id.HansikLayout, R.id.YangsikLayout, R.id.IllsikLayout, R.id.JungsikLayout, R.id.BunsikLayout, R.id.FastfoodLayout };
    Button[] categBtn = new Button[6];
    int cId[] = { R.id.hansik, R.id.yangsik, R.id.illsik, R.id.jungsik, R.id.bunsik, R.id.fastfood };
    boolean[] category = new boolean[6];
    int ctId[] = {R.id.hansik_t, R.id.yangsik_t, R.id.illsik_t, R.id.jungsik_t, R.id.bunsik_t, R.id.fastfood_t };
    TextView[] categText = new TextView[6];

    String[] name = new String[100];
    String[] classfication = new String[100];
    String[] rank = new String[100];
    String[] phoneNum = new String[100];
    String[] imageURL = new String[100];
    String[] worktime = new String[100];
    double[] distance = new double[100];

    Vector<Vector<BasicInfo>> basicInfo;
    HashMap<String, Integer> nameToIdx;
    GetFood GF;
    Typeface customFont;

    TextView location_t;
    GpsTracker gpsTracker;
    double latitude, longitude;
    String curAddress = null;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    boolean[] categoryFoods = new boolean[37];
    Vector<Vector<Integer>> categoryList = new Vector<Vector<Integer>>(6);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //폰트 가져오기
        customFont = ResourcesCompat.getFont(this, R.font.font_regular);

        //현재주소 업데이트
        ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS, 100);
        setAddress();
        refreshBtn = findViewById(R.id.refresh);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAddress();
            }
        });

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

        //거리순, 평점순
        leftBtn = findViewById(R.id.leftBtn);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(option[0] == true){ leftBtn.setBackgroundResource(R.drawable.left_clicked_button); leftBtn.setTextColor(Color.WHITE); option[0] = false;}
                else { leftBtn.setBackgroundResource(R.drawable.left_button); leftBtn.setTextColor(Color.parseColor("#919191")); option[0] = true;}
            }
        });
        rightBtn = findViewById(R.id.rightBtn);
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(option[1] == true){ rightBtn.setBackgroundResource(R.drawable.right_clicked_button); rightBtn.setTextColor(Color.WHITE); option[1] = false;}
                else { rightBtn.setBackgroundResource(R.drawable.right_button); rightBtn.setTextColor(Color.parseColor("#919191")); option[1] = true;}
            }
        });

        //가게 정보 팝업
        infoBtn = findViewById(R.id.infoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new InfoFragment();
                dialogFragment.show(getSupportFragmentManager(), "InfoDialog");
            }
        });

        //지도 url
        mapBtn = findViewById(R.id.map);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = name[POSITION].replace(" ", "+");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://map.naver.com/v5/search/"+str));
                startActivity(intent);
            }
        });

        // selectBtn 클릭
        selectBtn = findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 카테고리 업데이트
                for (int i=0; i<37; i++) categoryFoods[i] = false;
                for (int i=0; i<6; i++) {
                    if (category[i]) {
                        for (Integer j : categoryList.get(i)) categoryFoods[j] = true;
                    }
                }
                GF = new GetFood(context);
                num_page = GF.SIZE;
                setShop();
                setPager();
                mapBtn.setVisibility(View.VISIBLE); infoBtn.setVisibility(View.VISIBLE);
                leftBtn.setVisibility(View.VISIBLE); rightBtn.setVisibility(View.VISIBLE);
                food_t = findViewById(R.id.food_t); food_t.setText(GF.food);
                Log.d("dist", latitude+","+longitude);
            }
        });

        context = this; //MainActivity 변수 사용

        CreateBasicInfo CBI =  new CreateBasicInfo();
        basicInfo = CBI.basicInfo;
        nameToIdx = CBI.nameToIdx;

        // 카테고리별 음식 분류
        {
            // 벡터 6개 생성
            categoryList.add(new Vector<Integer>());
            categoryList.add(new Vector<Integer>());
            categoryList.add(new Vector<Integer>());
            categoryList.add(new Vector<Integer>());
            categoryList.add(new Vector<Integer>());
            categoryList.add(new Vector<Integer>());

            // 한식
            categoryList.get(0).add(nameToIdx.get("비빔밥"));
            categoryList.get(0).add(nameToIdx.get("국수"));
            categoryList.get(0).add(nameToIdx.get("칼국수"));
            categoryList.get(0).add(nameToIdx.get("국밥"));
            categoryList.get(0).add(nameToIdx.get("해장국"));
            categoryList.get(0).add(nameToIdx.get("곱창"));
            categoryList.get(0).add(nameToIdx.get("쌈밥"));
            categoryList.get(0).add(nameToIdx.get("부대찌개"));
            categoryList.get(0).add(nameToIdx.get("찜닭"));
            categoryList.get(0).add(nameToIdx.get("순대"));
            categoryList.get(0).add(nameToIdx.get("냉면"));
            categoryList.get(0).add(nameToIdx.get("고기"));
            categoryList.get(0).add(nameToIdx.get("족발"));
            categoryList.get(0).add(nameToIdx.get("도시락"));

            // 양식
            categoryList.get(1).add(nameToIdx.get("파스타"));
            categoryList.get(1).add(nameToIdx.get("돈가스"));
            categoryList.get(1).add(nameToIdx.get("스테이크"));
            categoryList.get(1).add(nameToIdx.get("피자"));
            categoryList.get(1).add(nameToIdx.get("필라프"));
            categoryList.get(1).add(nameToIdx.get("햄버거"));
            categoryList.get(1).add(nameToIdx.get("샌드위치"));
            categoryList.get(1).add(nameToIdx.get("토스트"));
            categoryList.get(1).add(nameToIdx.get("케밥"));

            // 일식
            categoryList.get(2).add(nameToIdx.get("소바"));
            categoryList.get(2).add(nameToIdx.get("라면"));
            categoryList.get(2).add(nameToIdx.get("초밥"));
            categoryList.get(2).add(nameToIdx.get("회"));
            categoryList.get(2).add(nameToIdx.get("연어"));
            categoryList.get(2).add(nameToIdx.get("우동"));
            categoryList.get(2).add(nameToIdx.get("돈가스"));
            categoryList.get(2).add(nameToIdx.get("카레"));

            // 중식
            categoryList.get(3).add(nameToIdx.get("짜장면"));
            categoryList.get(3).add(nameToIdx.get("짬뽕"));
            categoryList.get(3).add(nameToIdx.get("마라탕"));
            categoryList.get(3).add(nameToIdx.get("탕수육"));

            // 분식
            categoryList.get(4).add(nameToIdx.get("떡볶이"));
            categoryList.get(4).add(nameToIdx.get("순대"));
            categoryList.get(4).add(nameToIdx.get("김밥"));
            categoryList.get(4).add(nameToIdx.get("토스트"));

            // 패스트푸드
            categoryList.get(5).add(nameToIdx.get("치킨"));
            categoryList.get(5).add(nameToIdx.get("피자"));
            categoryList.get(5).add(nameToIdx.get("햄버거"));
        }
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
                POSITION = position;
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

    //주소 불러오기
    public String getCurrentAddress( double latitude, double longitude) throws IOException {
        //GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> address;

        try {
            address = geocoder.getFromLocation(latitude, longitude, 7);
        } catch (IOException ioException) {
            //네트워크 문제
            return "네트워크 확인";
        } catch (IllegalArgumentException illegalArgumentException) {
            return "잘못된 GPS 좌표";
        }

        if (address == null || address.size() == 0) {
            return "주소 미발견";

        }
        Address curAddress = address.get(0);
        String str = curAddress.getAddressLine(0).toString()+"\n";
        //str = str.substring(str.indexOf("도 ")+2);
        return str;
    }

    public void setAddress(){
        gpsTracker = new GpsTracker(MainActivity.this);
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        try {
            curAddress = getCurrentAddress(latitude, longitude);
        } catch (IOException e) {
            Log.d("location", "실행 안됨");
        }
        location_t = findViewById(R.id.location_t);
        location_t.setText(curAddress);
    }
}