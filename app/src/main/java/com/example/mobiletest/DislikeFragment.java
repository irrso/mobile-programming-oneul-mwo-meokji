package com.example.mobiletest;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DislikeFragment extends DialogFragment {

    boolean[] dislike = new boolean[37];
    Button[] disBtn = new Button[37];
    int[] dId = {R.id.bibimbap, R.id.calgooksu, R.id.gooksu, R.id.gookbap, R.id.haejanggook, R.id.gopchang, R.id.ssapbap, R.id.budaezzigae,
            R.id.zzimdark, R.id.soondae, R.id.naengmyen, R.id.gogi, R.id.zockbal, R.id.dosirock, R.id.pasta, R.id.dongas, R.id.steak, R.id.pizza,
            R.id.pilaf, R.id.hamburger, R.id.sandwitch, R.id.toast, R.id.kebap, R.id.soba, R.id.ramen, R.id.chobap, R.id.hoe, R.id.yeoneh, R.id.oodong,
            R.id.kare, R.id.zzazangmyen, R.id.zzamppong, R.id.maratang, R.id.tangsuyook, R.id.tteokboki, R.id.kimbap, R.id.chicken};
    int[] cId;

    public DislikeFragment() {
        // Required empty public constructor
    }

    public static DislikeFragment newInstance(String param1, String param2) {
        DislikeFragment fragment = new DislikeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.custom_dialog);
        //isCancelable = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dislike, container, false);

        //비선호 버튼 클릭
        for(int i = 0; i < 37; i++){
            disBtn[i] = view.findViewById(dId[i]);
            int index = i;
            disBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setClicked(index);
                }
            });
        }
        return view;
    }

    public void setClicked(int i){
        if(dislike[i] == false){ disBtn[i].setBackgroundResource(R.drawable.button_round_clicked); disBtn[i].setTextColor(Color.WHITE); dislike[i] = true;}
        else{ disBtn[i].setBackgroundResource(R.drawable.button_round); disBtn[i].setTextColor(Color.parseColor("#919191")); dislike[i] = false; }
    }
}