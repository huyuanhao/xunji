package com.jimetec.basin.event;


import android.text.TextUtils;

import com.jimetec.basin.bean.AppInfo;
import com.jimetec.basin.utils.AppData;

import java.util.List;

/**
 * 作者:zh
 * 时间:2018/10/10 下午2:15
 * 描述:
 */
public class AppList {

    public String ip;
    public String phone;
    public String systemName ="android";
    public String udid;
    public long createTimeLong;
    public List<AppInfo> userApps;
    public AppList(){
        udid= LoanEventDataUtil.getUtid();
        if (AppData.getInstance().isLogin()) {
            if (!TextUtils.isEmpty(AppData.getInstance().getUser().phone))
                phone = AppData.getInstance().getUser().phone;
        }
        ip = LoanEventDataUtil.getIp();
        createTimeLong =System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "AppList{" +
                "ip='" + ip + '\'' +
                ", phone='" + phone + '\'' +
                ", systemName='" + systemName + '\'' +
                ", udid='" + udid + '\'' +
                ", createTimeLong=" + createTimeLong +
                ", userApps=" + userApps +
                '}';
    }
}
