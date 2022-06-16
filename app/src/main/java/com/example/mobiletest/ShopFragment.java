package com.example.mobiletest;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        rankText= rootView.findViewById(R.id.rank);
        if(rank.charAt(1) != '.') rankText.setText("0.00");
        else rankText.setText(rank);
        distanceText = rootView.findViewById(R.id.distance);
        if(distance >= 1000) { double a = Math.round(distance/100); a /= 10; distanceText.setText(a + "km"); }
        else { distanceText.setText(Integer.parseInt(String.valueOf(Math.round(distance))) + "m"); }
        imageView = (ImageView) rootView.findViewById(R.id.shopImage);

        //가게 정보 팝업
        LinearLayout shopLayout = rootView.findViewById(R.id.shopLayout);
        shopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new InfoFragment();
                dialogFragment.show(getFragmentManager(), "InfoDialog");
            }
        });

        if (imageURL.charAt(0) == 'd') {
            imageView.setImageResource(R.drawable.no_image);
        }
        else {
            task = new ImageLoadTask(imageURL, imageView);
            task.execute();
        }

        return rootView;
    }
}