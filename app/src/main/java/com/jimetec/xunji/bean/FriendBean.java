package com.jimetec.xunji.bean;

import android.text.TextUtils;

import com.jimetec.xunji.util.UserUtil;

import java.io.Serializable;

/**
 * 作者:capTain
 * 时间:2019-07-17 11:38
 * 描述:
 */
public class FriendBean extends  TimeSortBean implements Serializable {

    public static final String TAG = "FriendBean";
    /**
     * id : 4
     * createDate : 2019-07-18T02:46:47.000+0000
     * userId : 16
     * userPhone : 15917834072
     * userNickName :
     * targetUserId : 14
     * targetPhone : 15917834073
     * targetNickName :
     * status : 1
     * times : 1563418007000
     * sid : 42998
     * tid : 168183741
     * longitude : 113.95277
     * latitude : 22.541113
     * lastLocation : 广东省东莞市南城区莞太大道113号
     * lastLocationTime : 1563444629000
     * headImage :
     */

    public int id;
    public String createDate;
    public long userId;
    public String userPhone;
    public String userName;
    public String userNickName;
    public long targetUserId;
    public String targetPhone;
    public String targetNickName;
    public int status;
//    public long times;
    public int sid;
    public int tid;
    public int trid;
    public double longitude;
    public double latitude;
    public String lastLocation;
    public long lastLocationTimes;
    public String headImage;
    public int onlineStatus ;//    1 在线 2  下线




//    /**
//     * id : 8
//     * createDate : 2019-07-19T03:01:30.000+0000
//     * userId : 13
//     * userPhone : 18520887381
//     * targetUserId : 12
//     * targetUserPhone : 18520887380
//     * status : 1
//     * type : 0
//     * remark :
//     * times : 1563505290000
//     *
//     */
//
//    public int id;
//    public String createDate;
//    public int userId;
//    public String userPhone;
//    public int targetUserId;
//    public String targetUserPhone;
//    public int status;
//    public int type;
//    public String remark;
//    public long times;


    public String  getNewsNickName() {
        String name = "";
        if (UserUtil.getUserId() == userId) {
            name = targetPhone;
            if (!TextUtils.isEmpty(userName)) {
                name = userName;
            }

        } else {
            name = userPhone;
            if (!TextUtils.isEmpty(userName)) {
                name = userName;
            }
        }
        return name;
    }



    public String  getFriendNickName() {
        String name = "";
        if (UserUtil.getUserId() == userId) {
            name = targetPhone;
            if (!TextUtils.isEmpty(targetNickName)) {
                name = targetNickName;
            }

        } else {
            name = userPhone;
            if (!TextUtils.isEmpty(userNickName)) {
                name = userNickName;
            }
        }
        return name;
    }


    public String  getFriendPhone() {
        String name = "";
        if (UserUtil.getUserId() == userId) {
            name = targetPhone;

        } else {
            name = userPhone;

        }
        return name;
    }





    public long getFriendUserId(){
        if (UserUtil.getUserId() == userId) {
            return targetUserId;
        }else {
            return  userId;
        }
    }


    public void setFriendNickName(String info) {
        if (UserUtil.getUserId() == userId) {
            targetNickName = info;
        } else {
            userNickName = info;
        }
    }

    @Override
    public String toString() {
        return "FriendBean{" +
                "id=" + id +
                ", createDate='" + createDate + '\'' +
                ", userId=" + userId +
                ", userPhone='" + userPhone + '\'' +
                ", userName='" + userName + '\'' +
                ", userNickName='" + userNickName + '\'' +
                ", targetUserId=" + targetUserId +
                ", targetPhone='" + targetPhone + '\'' +
                ", targetNickName='" + targetNickName + '\'' +
                ", status=" + status +
                ", sid=" + sid +
                ", tid=" + tid +
                ", trid=" + trid +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", lastLocation='" + lastLocation + '\'' +
                ", lastLocationTimes=" + lastLocationTimes +
                ", headImage='" + headImage + '\'' +
                '}';
    }
}

