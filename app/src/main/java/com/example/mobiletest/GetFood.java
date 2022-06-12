package com.example.mobiletest;

import android.util.Log;

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
    Random random = new Random();

    String food;
    int SIZE;
    Vector<BasicInfo> randomInfo = new Vector<BasicInfo>();
    Vector<BasicInfo> rankInfo = new Vector<BasicInfo>();
    Vector<BasicInfo> distanceInfo = new Vector<BasicInfo>();

    public GetFood() { // 입력(double latitude, double longitude)   =>  distanceInfo
        // 랜덤하게 음식 하나 뽑기
        int foodIdx = random.nextInt(37);
        food = idxToName[foodIdx];
        SIZE = basicInfo.get(foodIdx).size();
        Log.d("getFood", food + " / " + foodIdx + " / " + SIZE); //


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
            for (int i=0; i<SIZE; i++) distanceIdx[i] = i;
        }
        for (int i=0; i<SIZE; i++) distanceInfo.add(basicInfo.get(foodIdx).get(distanceIdx[i]));


        for (int i=0; i<SIZE; i++) {
            Log.d("getFood", rankInfo.get(i).name);
        }
    }
}
