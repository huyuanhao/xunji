package com.jimetec.basin.bean;


/**
 * 作者:zh
 * 时间:2018/9/8 下午3:29
 * 描述:
 */
public class User {
    public static final String TAG = "User";

    public String token="";
    public String icon="";
    public String phone="";

    @Override
    public String toString() {
        return "User{" +
                "token='" + token + '\'' +
                ", icon='" + icon + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
