package com.jimetec.xunji.bean;

/**
 * 作者:capTain
 * 时间:2019-08-07 17:40
 * 描述:
 */
public class UpLocateTimeBean {


//    {"result":"true","resultCode":"200","message":"操作成功","datas":
//{"report":30,  //位置上报周期
//"playback":10, //轨迹回放时间
//"collect":10,//位置采集周期
//"lastReport":60//最后位置上报周期
//}}

    public int report = 30;
    public int playback = 15;
    public int collect = 10;
    public int lastReport = 60;
    public int locationRemind = 50;


}
