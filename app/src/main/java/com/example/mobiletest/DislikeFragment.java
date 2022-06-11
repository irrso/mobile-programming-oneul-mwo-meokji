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
import android.widget.TextView;

public class DislikeFragment extends DialogFragment {

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
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_dislike, container, false);

        View view = inflater.inflate(R.layout.fragment_dislike, container, false);
        //Dialog dialog = getDialog();
        return view;
    }
}