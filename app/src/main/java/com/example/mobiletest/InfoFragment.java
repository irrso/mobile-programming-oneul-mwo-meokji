package com.example.mobiletest;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
public class InfoFragment extends DialogFragment {

    Button closeBtn2;
    TextView name, classification, phoneNum, worktime;

    /*public InfoFragment() {
        // Required empty public constructor
    }

    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        View view =  inflater.inflate(R.layout.fragment_info, container, false);

        name = view.findViewById(R.id.name);
        classification = view.findViewById(R.id.classification);
        phoneNum = view.findViewById(R.id.phoneNum);
        worktime = view.findViewById(R.id.worktime);

        transContext();

        closeBtn2 = view.findViewById(R.id.closeBtn2);
        closeBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }

    public void transContext(){
        for (int i = 0; i < ((MainActivity)MainActivity.context).num_page; i++){
            if(((MainActivity)MainActivity.context).pager.getCurrentItem() == i){
                name.setText(((MainActivity)MainActivity.context).name[i]);
                classification.setText(((MainActivity)MainActivity.context).classfication[i]);
                phoneNum.setText(((MainActivity)MainActivity.context).phoneNum[i]);
                worktime.setText(((MainActivity)MainActivity.context).worktime[i]);

            }
        }
    }
}