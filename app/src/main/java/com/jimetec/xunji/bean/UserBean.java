package com.jimetec.xunji.bean;

import android.text.TextUtils;

import java.util.List;

/**
 * 作者:capTain
 * 时间:2019-07-17 11:38
 * 描述:
 */
public class UserBean {


    /**
     * userId : 12
     * udid : 638419ac44ea93c7
     * phone : 18520887380
     * createDate : 2019-07-17T03:49:51.000+0000
     * updateDate : 2019-07-17T03:49:51.000+0000
     * code : null
     * sid : 0
     * tid : 0
     */

    public long userId;
    public String udid;
    public String phone;
    public String createDate;
    public String updateDate;
    public String code;
    public String userName;
    public String realName;
    public int sid ;
    public int tid;
    public int trid;
    public double longitude;
    public double latitude;
    public long  lastLocationTimes;
    public String lastLocation;

    public String name = "";
    public long expireTimes;
    public String headImage = "";
    public List<MyModuleBean> information;


    public String getNickName() {

        if (TextUtils.isEmpty(userName)){
            return phone;
        }
        return userName;
    }

    public String getAvatar() {
        return headImage;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "userId=" + userId +
                ", udid='" + udid + '\'' +
                ", phone='" + phone + '\'' +
                ", createDate='" + createDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", code='" + code + '\'' +
                ", sid=" + sid +
                ", tid=" + tid +
                ", trid=" + trid +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", lastLocationTimes=" + lastLocationTimes +
                ", lastLocation='" + lastLocation + '\'' +
                ", emergencyName='" + name + '\'' +
                ", headImage='" + headImage + '\'' +
                '}';
    }
}
