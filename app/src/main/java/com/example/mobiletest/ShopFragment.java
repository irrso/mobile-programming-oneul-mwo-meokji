package com.example.mobiletest;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopFragment extends Fragment {

    ImageView imageView;
    String name, rank, imageURL;
    Double distance;
    TextView nameText, rankText, distanceText;
    ImageLoadTask task;

    /*private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;*/

    public ShopFragment(String name, String rank, Double distance, String imageURL){
        this.name = name;
        this.rank = rank;
        this.distance = distance;
        this.imageURL = imageURL;

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
        View rootView = (ViewGroup) inflater.inflate(R.layout.fragment_shop, container, false);

        nameText = rootView.findViewById(R.id.name); nameText.setText(name);
        rankText= rootView.findViewById(R.id.rank); rankText.setText(rank);
        distanceText = rootView.findViewById(R.id.distance); distanceText.setText(""+distance);
        imageView = (ImageView) rootView.findViewById(R.id.shopImage);
        task = new ImageLoadTask(imageURL, imageView);
        task.execute();

        return rootView;
    }
}