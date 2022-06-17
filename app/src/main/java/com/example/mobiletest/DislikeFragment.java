package com.example.mobiletest;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class DislikeFragment extends DialogFragment {

    Button closeBtn;
    Button[] disBtn = new Button[37];
    int[] dId = {
            R.id.bibimbap, R.id.calgooksu, R.id.gooksu, R.id.gookbap, R.id.haejanggook, R.id.gopchang, R.id.ssapbap, R.id.budaezzigae, R.id.zzimdark, R.id.soondae,
            R.id.naengmyen, R.id.gogi, R.id.zockbal, R.id.dosirock, R.id.pasta, R.id.dongas, R.id.steak, R.id.pizza, R.id.pilaf, R.id.hamburger,
            R.id.sandwitch, R.id.toast, R.id.kebap, R.id.soba, R.id.ramen, R.id.chobap, R.id.hoe, R.id.yeoneh, R.id.oodong, R.id.kare,
            R.id.zzazangmyen, R.id.zzamppong, R.id.maratang, R.id.tangsuyook, R.id.tteokboki, R.id.kimbap, R.id.chicken
    };

    /*public DislikeFragment() {
        // Required empty public constructor
    }

    public static DislikeFragment newInstance(String param1, String param2) {
        DislikeFragment fragment = new DislikeFragment();
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.custom_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dislike, container, false);

        closeBtn = view.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // 비선호 버튼 클릭
        for(int i = 0; i < 37; i++){
            disBtn[i] = view.findViewById(dId[i]);
            int index = i;
            disBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setClicked(index);
                }
            });
            // 길게 누르면 선호 쌉가능
            disBtn[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    setLongClicked(index);
                    return true;
                }
            });
        }

        //값 복원
        for (int i = 0; i < 37; i++) {
            if(((MainActivity)MainActivity.context).dislike[i] == true){
                disBtn[i].setBackgroundResource(R.drawable.button_round_clicked); disBtn[i].setTextColor(Color.WHITE);}
        }

        return view;
    }

    public void setClicked(int i){
        if(((MainActivity)MainActivity.context).dislike[i] == false){ disBtn[i].setBackgroundResource(R.drawable.button_round_clicked); disBtn[i].setTextColor(Color.WHITE); ((MainActivity)MainActivity.context).dislike[i] = true;}
        else{ disBtn[i].setBackgroundResource(R.drawable.button_round); disBtn[i].setTextColor(Color.parseColor("#919191")); ((MainActivity)MainActivity.context).dislike[i] = false; }
    }

    public void setLongClicked(int idx) {
        for (int i=0; i<37; i++) {
            disBtn[i].setBackgroundResource(R.drawable.button_round_clicked);
            disBtn[i].setTextColor(Color.WHITE);
            ((MainActivity)MainActivity.context).dislike[i] = true;
        }
        setClicked(idx);
    }
}