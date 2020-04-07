package com.jimetec.xunji.bean;

/**
 * 作者:capTain
 * 时间:2019-08-07 18:23
 * 描述:
 */
public class TempLocationBean {

    public  double longitude;
    public  double latitude;
    public  String address;
    public  long time;


    public TempLocationBean(double longitude, double latitude, String address) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.time = System.currentTimeMillis();
    }



    public boolean isNeedUpload(){
        long cur = System.currentTimeMillis();
        if (cur -time <1000*60*5){
            return true;
        }

        return false;
    }

}
