package com.jimetec.xunji.bean;

import java.io.Serializable;

/**
 * 作者:capTain
 * 时间:2019-08-08 17:53
 * 描述:
 */
public class LocationWarnBean implements Serializable {

    public static final String TAG = "LocationWarnBean";

    /**
     * id : 2
     * userId : 30
     * friendId : 44
     * friendName :
     * longitude : 113.952812
     * latitude : 22.541529
     * location : 广东省东莞市南城区莞太大道113号靠近中威大厦
     * remindWay : 2  //提醒方式1.手机振动（两次 一秒）2.本地消息推送3.短信通知用户
     */

    public int id;
    public int userId;
    public int targetUserId;
    public int friendId;
    public String friendName;
    public double longitude;
    public double latitude;
    public String location;
    public String remark;
    public int remindWay;
    public int status;
    public long lastUpdateTimes;

//    "status": 2,    //1 未进入 2 已进入
//            “”:1565854553552



    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
