package com.jimetec.basin.event;

import android.text.TextUtils;

import com.jimetec.basin.utils.AppData;


/**
 * 作者:zh
 * 时间:2018/10/11 上午9:30
 * 描述:
 */
public class ScreenBean {
    public String ip;

    public String phone;

    public String systemName="android";

    public String udid;

    public long screenshotTimeLong=System.currentTimeMillis();

    public String pageClassName;

    public String pageUrl;

    public ScreenBean(){
        udid= LoanEventDataUtil.getUtid();
        if (AppData.getInstance().isLogin()) {
            if (!TextUtils.isEmpty(AppData.getInstance().getUser().phone))
                phone = AppData.getInstance().getUser().phone;
        }
        ip = LoanEventDataUtil.getIp();
    }

}
