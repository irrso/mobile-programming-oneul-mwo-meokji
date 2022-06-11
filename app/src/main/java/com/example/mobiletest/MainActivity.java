package com.example.mobiletest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {

    public static Context context;
    ScrollView scrollView;
    Button prefBtn;
    //int[] index = {0, 0, 0, 0, 0};
    public int num_page = 3; //크롤링해서 받은 가게 개수
    public String[] url = new String[num_page]; //크롤링해서 받은 이미지 url
    ViewPager2 pager;
    TextView infoTxt, textView;
    public String[] info = new String[num_page]; //크롤링해서 받은 가게정보
    LinearLayout TextLayout;
    CircleIndicator3 indicator;
    boolean[] dislike = new boolean[37];
    Button[] disBtn = new Button[37];
    int[] dId = {R.id.bibimbap, R.id.calgooksu, R.id.gooksu, R.id.gookbap, R.id.haejanggook, R.id.gopchang, R.id.ssapbap, R.id.budaezzigae,
    R.id.zzimdark, R.id.soondae, R.id.naengmyen, R.id.gogi, R.id.zockbal, R.id.dosirock, R.id.pasta, R.id.dongas, R.id.steak, R.id.pizza,
    R.id.pilaf, R.id.hamburger, R.id.sandwitch, R.id.toast, R.id.kebap, R.id.soba, R.id.ramen, R.id.chobap, R.id.hoe, R.id.yeoneh, R.id.oodong,
    R.id.kare, R.id.zzazangmyen, R.id.zzamppong, R.id.maratang, R.id.tangsuyook, R.id.tteokboki, R.id.kimbap, R.id.chicken};
    int[] cId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //비선호 버튼 클릭
       /* for(int i = 0; i < 37; i++){
            disBtn[i] = findViewById(dId[i]);
            int index = i;
            disBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setClicked(index);
                }
            });
        }*/

        //비선호 expandable
        /*scrollView = findViewById(R.id.ScrollView);
        scrollView.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        prefBtn = findViewById(R.id.dislikeBtn);
        prefBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = (scrollView.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(scrollView, new AutoTransition());
                scrollView.setVisibility(v);
            }
        });*/
        // 비선호 팝업
        prefBtn = findViewById(R.id.dislikeBtn);
        prefBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new DislikeFragment();
                dialogFragment.show(getSupportFragmentManager(), "DislikeDialog");
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

        readExcel();
    }

    public void setClicked(int i){
        if(dislike[i] == false){ disBtn[i].setBackgroundResource(R.drawable.button_round_clicked); disBtn[i].setTextColor(Color.WHITE); dislike[i] = true;}
        else{ disBtn[i].setBackgroundResource(R.drawable.button_round); disBtn[i].setTextColor(Color.parseColor("#919191")); dislike[i] = false; }
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

    //음식점 불러오기
    public void readExcel(){
        try{
            InputStream is = getBaseContext().getResources().getAssets().open("chicken.xls");
            POIFSFileSystem fs = new POIFSFileSystem(is);
            HSSFWorkbook wb = new HSSFWorkbook(fs);

            int rowIdx = 0;
            int colIdx = 0;
            //시트 수
            Sheet sheet = wb.getSheet("치킨");
            //행의 수
            int rowTotal = sheet.getPhysicalNumberOfRows();
            for(rowIdx = 0; rowIdx < rowTotal; rowIdx++){
                Row row = sheet.getRow(rowIdx);
                if(row != null){
                    //셀의 수
                    int cells = row.getPhysicalNumberOfCells();
                    for(colIdx = 0; colIdx <= cells; colIdx++){
                        Cell cell = row.getCell(colIdx);
                        String value = "";
                        if(cell == null){
                            continue;
                        }else{
                            switch(cell.getCellType()){
                                case FORMULA:
                                    value = cell.getCellFormula();
                                    break;
                                case NUMERIC:
                                    value = cell.getNumericCellValue()+"";
                                    break;
                                case STRING:
                                    value = cell.getStringCellValue()+"";
                                    break;
                                case BLANK:
                                    value = cell.getBooleanCellValue()+"";
                                    break;
                                case ERROR:
                                    value = cell.getErrorCellValue()+"";
                                    break;
                            }
                        }
                        //Log.d("chicken", value);
                    }
                }
            }
            Log.d("excel", "됨");
        }catch (Exception e){
            e.printStackTrace();
            Log.d("excel", "안됨");
        }
    }

}