package com.common.baseview.event;

import android.os.Build;
import android.text.TextUtils;


/**
 * 作者:zh
 * 时间:12/19/18 12:01 PM
 * 描述:
 */
public class EventHeadData {
    private long userId ;
    private String phone = "";
    public String udid = EventDataUtil.getUtid();
    public long createTime = System.currentTimeMillis();

    public long lastTime;

    public String firstIp = "";

    public String lastIp;


    public String parter = EventDataUtil.getEventChannel();

    public String sysOs = "android";

    public String productName;

    public String productVersion = EventDataUtil.getVersionName();
    public int productCode = EventDataUtil.getVersionCode();

    public String networktype = EventDataUtil.getNetworktype();

    public String sysVersion = Build.VERSION.RELEASE;

    public String deviceBrand = Build.BRAND;

    public String deviceModel = Build.MODEL;

    public String imei = EventDataUtil.getImei();

    public String mac = EventDataUtil.getMac();

    public String androidId = EventDataUtil.getAndroidId();

    public String idfa;

    public String idfv;
    public String umengPid;


    public EventHeadData() {
//        if (UserUtil.isLogined()) {
        phone = EventDataUtil.getPhone();
        userId = EventDataUtil.getUserId();


        if (!TextUtils.isEmpty(EventDataUtil.getUmDeviceToken())){
            umengPid = EventDataUtil.getUmDeviceToken();
        }
//        }
    }
}
