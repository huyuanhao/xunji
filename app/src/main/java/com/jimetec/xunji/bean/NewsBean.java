package com.jimetec.xunji.bean;


import java.io.Serializable;

/**
 * 作者:capTain
 * 时间:2019-07-17 11:38
 * 描述:
 */
public class NewsBean extends TimeSortBean implements Serializable {

    public static final String TAG = "NewsBean";
    public long id;
//    public long times;//时间戳
    public String title ="";
    public String text =""; //通知栏描述（summary）
    public String content ="";//文本详情
    public int type; //1.文本消息类型  2.请求加好友    3.已成为好友通知  4 地理位置提醒
    public String url ="";
    public long targetUserId; //接受消息的用户
    public int hasRead; //是否已读 0 未读  1 已读
    public String msgId; //是否已读 0 未读  1 已读
    public long timestamp;


}

