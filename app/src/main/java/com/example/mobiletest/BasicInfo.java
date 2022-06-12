package com.example.mobiletest;

public class BasicInfo {
    public String name;
    public String address;
    public String classification;
    public String rank;
    public String phoneNum;
    public String imageURL;
    public String latitude;
    public String longitude;
    public String workTime;
    public double distance = 0.0;

    public BasicInfo (String[] s) {
        this.name = s[0];
        this.address = s[1];
        this.classification = s[2];
        this.rank = s[3];
        this.phoneNum = s[4];
        this.imageURL = s[5];
        this.latitude = s[6];
        this.longitude = s[7];
        this.workTime = s[8];
    }
}
