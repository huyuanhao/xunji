package com.jimetec.xunji.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者:capTain
 * 时间:2019-07-17 11:38
 * 描述:
 */
public class ContactBean extends  TimeSortBean implements Serializable {

    public static final String TAG = "ContactBean";
    public int id ;
    public  long userId ;
    public String emergencyName;
    public  String emergencyPhone;
    public int type;   //  服务器含义(紧急报警)1 本地好友    2  手机好友
    // 本地含义 (添加好友) 0  未添加  1, 已添加
//      "id": 1,
//              "userId": 1,
//              "emergencyName": "asda",
//              "emergencyPhone": "asd",
//              "createTime": "2019-08-20T03:32:03.000+0000",
//              "type": 1




    public ContactBean() {
    }

    public ContactBean(String name, String phone) {
        this.emergencyName = name;
        this.emergencyPhone = phone;
    }

    public String getEmergencyName() {
        return emergencyName;
    }

    public String getPhone() {
        return emergencyPhone;
    }

    public static List<ContactBean> getContacts(){
        List<ContactBean> beans = new ArrayList<>();
        new ContactBean("小一","68520887380");
        new ContactBean("小二","28520887380");
        new ContactBean("小三","38520887380");
        return beans;
    }

}

