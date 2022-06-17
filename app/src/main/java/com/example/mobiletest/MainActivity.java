package com.example.mobiletest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
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
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    boolean[] categoryFoods = new boolean[37];
    Vector<Vector<Integer>> categoryList = new Vector<Vector<Integer>>(6);

    int sortOption = 0; // 0: random / 1: dist / 2: rank


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // 폰트 가져오기
        customFont = ResourcesCompat.getFont(this, R.font.font_regular);

        /// 현재주소 업데이트
        ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS, 100);
        location_t = findViewById(R.id.location_t);
        setLocation(); location_t.setText(getaddress());
        refreshBtn = findViewById(R.id.refresh);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocation();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        location_t.setText(getaddress());
                    }
                });
            }
        });

        // 비선호 팝업
        prefBtn = findViewById(R.id.dislikeBtn);
        prefBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new DislikeFragment();
                dialogFragment.show(getSupportFragmentManager(), "DislikeDialog");
            }
        });

        // 카테고리 레이아웃
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

        // 카테고리 버튼
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

        // 카테고리 텍스트
        for(int i = 0; i < 6; i++) {
            categText[i] = findViewById(ctId[i]);
        }

        /// 거리순
        leftBtn = findViewById(R.id.leftBtn);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(option[0] == false) {
                    if (option[1]) { rightBtn.setBackgroundResource(R.drawable.right_button); rightBtn.setTextColor(Color.parseColor("#919191")); option[1] = false; }
                    leftBtn.setBackgroundResource(R.drawable.left_clicked_button);
                    leftBtn.setTextColor(Color.WHITE);
                    option[0] = true;}
                else { leftBtn.setBackgroundResource(R.drawable.left_button); leftBtn.setTextColor(Color.parseColor("#919191")); option[0] = false; }
                setSortOption();
                setSortedPage();
            }
        });

        /// 평점순
        rightBtn = findViewById(R.id.rightBtn);
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(option[1] == false){
                    if (option[0]) { leftBtn.setBackgroundResource(R.drawable.left_button); leftBtn.setTextColor(Color.parseColor("#919191")); option[0] = false; }
                    rightBtn.setBackgroundResource(R.drawable.right_clicked_button);
                    rightBtn.setTextColor(Color.WHITE);
                    option[1] = true;}
                else { rightBtn.setBackgroundResource(R.drawable.right_button); rightBtn.setTextColor(Color.parseColor("#919191")); option[1] = false; }
                setSortOption();
                setSortedPage();
            }
        });

        // 가게 정보 팝업
        infoBtn = findViewById(R.id.infoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new InfoFragment();
                dialogFragment.show(getSupportFragmentManager(), "InfoDialog");
            }
        });

        // 지도 url
        mapBtn = findViewById(R.id.map);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = name[POSITION].replace(" ", "+");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://map.naver.com/v5/search/"+str));
                startActivity(intent);
            }
        });

        /// selectBtn 클릭
        selectBtn = findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateGetFood(); // GetFood() 객체 생성
                setSortedPage(); // 옵션에 맞게 가게 띄우기
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

    // ViewPager, Indicator 설정
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
        Vector<Vector<BasicInfo>> v = new Vector<Vector<BasicInfo>>(); {
            v.add(GF.randomInfo);
            v.add(GF.distanceInfo);
            v.add(GF.rankInfo);
        }
        for (int i=0; i<GF.SIZE; i++) {
            name[i] = v.get(sortOption).get(i).name;
            classfication[i] = v.get(sortOption).get(i).classification;
            rank[i] = v.get(sortOption).get(i).rank;
            phoneNum[i] = v.get(sortOption).get(i).phoneNum;
            imageURL[i] = v.get(sortOption).get(i).imageURL;
            worktime[i] = v.get(sortOption).get(i).workTime;
            distance[i] = v.get(sortOption).get(i).distance;
        }
    }

    // 레이아웃 클릭
    public void setClicked(int i){
        if(category[i] == false){
            categLayout[i].setBackgroundResource(R.drawable.layout_round_clicked);
            categText[i].setTypeface(customFont, Typeface.BOLD); category[i] = true; }
        else{ categLayout[i].setBackgroundResource(R.drawable.layout_round);
            categText[i].setTypeface(customFont); category[i] = false; }
    }

    /// 엑셀 불러오기
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

    // 경도,위도
    public void setLocation(){
        gpsTracker = new GpsTracker(MainActivity.this);
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
    }

    /// 주소 가져오기
    public String getaddress() {
        String finalAddress = "도로명주소 미발견";
        String finalAddress2 = "지번주소 미발견";
        try {
            BufferedReader bufferedReader = null;
            StringBuilder stringBuilder = new StringBuilder();
            String coord = longitude+","+latitude;
            Log.d("coord", coord);
            String query = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?request=coordsToaddr&coords="
                    + coord + "&sourcecrs=epsg:4326&output=json&orders=roadaddr&output=xml";
            URL url = null;
            HttpURLConnection conn = null;

            BufferedReader bufferedReader2 = null;
            StringBuilder stringBuilder2 = new StringBuilder();
            String query2 = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?request=coordsToaddr&coords="
                    + coord + "&sourcecrs=epsg:4326&output=json&orders=addr&output=xml";
            URL url2 = null;
            HttpURLConnection conn2 = null;

            try {
                url = new URL(query);
                url2 = new URL(query2);
                Log.d("request", "URL 됨");
            } catch (MalformedURLException e) {
                Log.d("request", "URL 안됨");
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn2 = (HttpURLConnection) url2.openConnection();
            } catch (IOException e) {
                Log.d("request", "http 안됨");
            }

            //도로명 주소
            if (conn != null) {
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                try {
                    conn.setRequestMethod("GET");
                    Log.d("request", "conn 됨");
                } catch (ProtocolException e) {
                    Log.d("request", "conn 안됨");
                }
                conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "3hxuop6xkd");
                conn.setRequestProperty("X-NCP-APIGW-API-KEY", "illyoSwD97UiNVfZs4SI4eso09HNJ0CHjHAeRgh2");
                conn.setDoInput(true);

                int responseCode = 0;
                try {
                    responseCode = conn.getResponseCode();
                    Log.d("request", responseCode + "");
                } catch (IOException e) {
                    Log.d("request", "responseCode 안됨");
                }

                if (responseCode == 200) {
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    Log.d("request", "if responseCode 안됨");
                }

                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                Gson gson = new Gson();
                Log.d("request", String.valueOf(stringBuilder));
                Addresses address = gson.fromJson(String.valueOf(stringBuilder), Addresses.class);
                if (address.results.length != 0) {
                    finalAddress = address.results[0].region.area2.name+" ";
                    finalAddress += address.results[0].region.area3.name+" ";
                    finalAddress += address.results[0].land.name+" ";
                    finalAddress += address.results[0].land.number1;
                }
                Log.d("request", finalAddress);
                bufferedReader.close();
                conn.disconnect();
            }

            //지번 주소
            if (conn2 != null) {
                conn2.setConnectTimeout(5000);
                conn2.setReadTimeout(5000);
                try {
                    conn2.setRequestMethod("GET");
                    Log.d("request2", "conn 됨");
                } catch (ProtocolException e) {
                    Log.d("request2", "conn 안됨");
                }
                conn2.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "3hxuop6xkd");
                conn2.setRequestProperty("X-NCP-APIGW-API-KEY", "illyoSwD97UiNVfZs4SI4eso09HNJ0CHjHAeRgh2");
                conn2.setDoInput(true);

                int responseCode2 = 0;
                try {
                    responseCode2 = conn.getResponseCode();
                    Log.d("request2", responseCode2 + "");
                } catch (IOException e) {
                    Log.d("request2", "responseCode 안됨");
                }

                if (responseCode2 == 200) {
                    bufferedReader2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
                } else {
                    bufferedReader2 = new BufferedReader(new InputStreamReader(conn2.getErrorStream()));
                    Log.d("request", "if responseCode 안됨");
                }

                String line2 = null;
                while ((line2 = bufferedReader2.readLine()) != null) {
                    stringBuilder2.append(line2 + "\n");
                }

                Gson gson2 = new Gson();
                Log.d("request2", String.valueOf(stringBuilder2));
                Addresses address2 = gson2.fromJson(String.valueOf(stringBuilder2), Addresses.class);
                finalAddress2 = address2.results[0].region.area2.name+" ";
                finalAddress2 += address2.results[0].region.area3.name+" ";
                finalAddress2 += address2.results[0].land.number1;
                if (address2.results[0].land.number2.length() != 0) finalAddress2 += "-" + address2.results[0].land.number2;
                Log.d("request2", finalAddress2);
                bufferedReader2.close();
                conn2.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(finalAddress != "도로명주소 미발견"){ return finalAddress; }
        else{ return finalAddress2; }
    }

    // 정렬 옵션 선택
    public void setSortOption() {
        // false, false : random
        if (!option[0] && !option[1]) sortOption = 0;
        // true, false : dist 정렬
        if (option[0] && !option[1]) sortOption = 1;
        // false, true : rank 정렬
        if (!option[0] && option[1]) sortOption = 2;
    }

    // GetFood() 객체 생성
    public void CreateGetFood() {
        // 카테고리 업데이트
        for (int i=0; i<37; i++) categoryFoods[i] = false;
        for (int i=0; i<6; i++) {
            if (category[i]) {
                for (Integer j : categoryList.get(i)) categoryFoods[j] = true;
            }
        }
        // GetFood 객체 생성
        GF = new GetFood(context);
    }

    // 정렬 옵션에 맞게 띄우기
    public void setSortedPage() {
        num_page = GF.SIZE; // 페이지 개수 지정
        setShop();
        setPager();
        mapBtn.setVisibility(View.VISIBLE); infoBtn.setVisibility(View.VISIBLE);
        leftBtn.setVisibility(View.VISIBLE); rightBtn.setVisibility(View.VISIBLE);
        food_t = findViewById(R.id.food_t); food_t.setText(GF.food);
        Log.d("dist", latitude+","+longitude);
    }

}