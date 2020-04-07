package com.jimetec.basin.event;

import com.common.lib.utils.NetworkUtils;

/**
 * 作者:zh
 * 时间:2018/10/16 下午3:36
 * 描述:
 */
public class JsBean {

    public String efrom = LoanEventDataUtil.getEfrom();//
    public String udid= LoanEventDataUtil.getUtid();
    public String channel = LoanEventDataUtil.getEventChannel();//渠道包，如jinritoutiao、360、oppo等（ios统一上传空），数据来源调用对应客户端接口
    public String networkType = NetworkUtils.getNetworkType().getState(); //网络类型，比如移动2g、联通3g、4g、 WiFi等
    public String systemName = "android";//系统名（iPhone OS等）
    public String systemVersion = "Android "+android.os.Build.VERSION.RELEASE  +" "+ android.os.Build.MODEL; //系统版本，如8.4.1、4.4.4，数据来源调用对应客户端接口  +" "+ android.os.Build.BRAND
    public String productVersion = LoanEventDataUtil.getVersionName(); //终端版本号，如9.7.5，数据来源调用对应客户端接口
    public int power=0; //用户当前电量，例：18
    public String gyro = LoanEventDataUtil.getGyro();  //用户当前角度，陀螺仪数据，例：（18,18,18）
    public int batteryStatus=0; //当前充电状态，在充电为1，未充电为0
    public int batteryType=0; //当前充电类型，充电器充电 1 在usb充电为2，无线充电 4
    public String tag = LoanEventDataUtil.getEfrom();
    public String UMChannel = LoanEventDataUtil.getReallyChannel();
    public String mac= LoanEventDataUtil.getMac();
    public String imei= LoanEventDataUtil.getImei();
    public String androidid= LoanEventDataUtil.getAndroidId();

    public String idfa="";

    public JsBean() {

        try {
            batteryStatus = LoanEventDataUtil.getPower()[0];
            power = LoanEventDataUtil.getPower()[1];
            batteryType = LoanEventDataUtil.getPower()[2];
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
