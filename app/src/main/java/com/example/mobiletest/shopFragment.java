package com.example.mobiletest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class shopFragment extends Fragment {

    ImageView imageView;
    String url;
    ImageLoadTask task;
    int x;

    /*private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;*/

    public shopFragment(String url) {
        // Required empty public constructor
        this.url = url;
    }

    /*public static shopFragment newInstance(String param1, String param2) {
        shopFragment fragment = new shopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_shop, container, false);

       // Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();

        imageView = (ImageView)rootView.findViewById(R.id.shopImage);
        task = new ImageLoadTask(url, imageView);
        task.execute();
        //뒤로 가면 오류뜸


        //x = ((MainActivity)MainActivity.context).num_page;
        //Toast.makeText(getActivity(), String.valueOf(x), Toast.LENGTH_SHORT).show();

        return rootView;
    }
}