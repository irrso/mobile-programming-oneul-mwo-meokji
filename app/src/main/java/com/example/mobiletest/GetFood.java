package com.example.mobiletest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class GetFood {
    String []idxToName = ((MainActivity)MainActivity.context).idxToName;
    Vector<Vector<BasicInfo>> basicInfo = ((MainActivity)MainActivity.context).basicInfo;
    HashMap<String, Integer> nameToIdx = ((MainActivity)MainActivity.context).nameToIdx;

    boolean[] dislike = ((MainActivity)MainActivity.context).dislike.clone();
    boolean[] category = ((MainActivity)MainActivity.context).category;
    boolean[] categoryFoods = ((MainActivity)MainActivity.context).categoryFoods.clone();

    Random random = new Random();

    String food;
    int SIZE;
    Vector<BasicInfo> randomInfo = new Vector<BasicInfo>();
    Vector<BasicInfo> rankInfo = new Vector<BasicInfo>();
    Vector<BasicInfo> distanceInfo = new Vector<BasicInfo>();

    public GetFood(Context context) { // 입력(double latitude, double longitude)   =>  distanceInfo

        // 랜덤하게 음식 하나 뽑기
        int foodIdx;
        {
            // 카테고리 음식 & 비선호 음식 처리
            boolean isNotSelected = true;
            for (int i = 0; i < 6; i++) {
                if (category[i]) isNotSelected = false;
            }
            if (isNotSelected) {
                for (int i = 0; i < 37; i++) categoryFoods[i] = true;
            }

            // 예외인 경우 처리 (뽑힐 후보가 없는 경우)
            boolean flag = false;
            for (int i = 0; i < 37; i++) {
                if (!dislike[i] && categoryFoods[i]) flag = true;
            }
            if (!flag) { // 예외 처리 (비선호가 37개인 경우)
                Toast.makeText(context, "카테고리와 비선호 음식을 확인해주세요.", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < 37; i++) dislike[i] = false;
            }

            // 후보 중에서 랜덤하게 선택
            while (true) {
                foodIdx = random.nextInt(37);
                if (!dislike[foodIdx] && categoryFoods[foodIdx]) break;
            }
        }
        food = idxToName[foodIdx];
        SIZE = basicInfo.get(foodIdx).size();
        Log.d("getFood", food + " / " + foodIdx + " / " + SIZE); //


        // 거리 계산해서 basicInfo 의 distance 에 넣기
        {
            double cur_lat = ((MainActivity)MainActivity.context).longitude;
            double cur_lon = ((MainActivity)MainActivity.context).latitude;
            for (int i=0; i<SIZE; i++) {
                double shop_lat = Double.parseDouble(basicInfo.get(foodIdx).get(i).latitude);
                double shop_lon = Double.parseDouble(basicInfo.get(foodIdx).get(i).longitude);
                double dist = calculateDistance(cur_lat, cur_lon, shop_lat, shop_lon);
                dist = Math.round(dist*1000);
                basicInfo.get(foodIdx).get(i).distance = dist;
            }
        }


        // 랜덤 배열 만들기
        int[] randomIdx = new int[SIZE]; {
            Integer[] array = new Integer[SIZE];
            for (int i = 0; i < SIZE; i++) array[i] = i;
            List<Integer> list = Arrays.asList(array);

            Collections.shuffle(list);
            list.toArray(array);
            for (int i=0; i<SIZE; i++) randomIdx[i] = array[i];
        }
        for (int i=0; i<SIZE; i++) randomInfo.add(basicInfo.get(foodIdx).get(randomIdx[i]));


        // 평점 배열 만들기
        int[] rankIdx = new int[SIZE]; {
            double[][] array = new double[SIZE][2];
            for (int i=0; i<SIZE; i++) {
                double rank;
                {
                    String rankStr = basicInfo.get(foodIdx).get(i).rank;
                    try {
                        rank = Double.parseDouble(rankStr);
                    } catch (Exception e) {
                        rank = 0.0;
                    }
                }

                int idx = i;

                array[i][0] = rank;
                array[i][1] = idx;
            }
            Arrays.sort(array, Comparator.comparingDouble(o1 -> o1[0]));

            double[][] array2 = new double[SIZE][2];
            for (int i=0; i<SIZE; i++) {
                array2[i][0] = array[SIZE-i-1][0];
                array2[i][1] = array[SIZE-i-1][1];
            }

            for (int i=0; i<SIZE; i++) rankIdx[i] = (int) array2[i][1];
        }
        for (int i=0; i<SIZE; i++) rankInfo.add(basicInfo.get(foodIdx).get(rankIdx[i]));


        // 거리 배열 만들기
        int[] distanceIdx = new int[SIZE]; {
            // 나중에 하기
            double[][] array = new double[SIZE][2];
            for (int i=0; i<SIZE; i++) {
                array[i][0] = basicInfo.get(foodIdx).get(i).distance;
                array[i][1] = i;
            }
            Arrays.sort(array, Comparator.comparingDouble(o1 -> o1[0]));

//            for (int i=0; i<SIZE; i++) distanceIdx[i] = i;
            for (int i=0; i<SIZE; i++) distanceIdx[i] = (int) array[i][1];
        }
        for (int i=0; i<SIZE; i++) distanceInfo.add(basicInfo.get(foodIdx).get(distanceIdx[i]));







//        for (int i=0; i<SIZE; i++) {
//            Log.d("getFood", rankInfo.get(i).name);
//        }
    }



    double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // TODO Auto-generated method stub
        final int R = 6371; // Radious of the earth
        Double latDistance = toRad(lat2-lat1);
        Double lonDistance = toRad(lon2-lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        Double distance = R * c;

        return distance;
    }

    Double toRad(Double value) {
        return value * Math.PI / 180;
    }
}