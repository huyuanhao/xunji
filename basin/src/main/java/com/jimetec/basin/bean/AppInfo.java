package com.jimetec.basin.bean;


/**
 * 作者:zh
 * 时间:2018/10/10 下午2:18
 * 描述:
 */
public class AppInfo {

    public String appName;
    public String packageName;
    public String versionName;
    public long appInstalltimeLong;
    public int versionCode;


    public AppInfo(String app_name, String package_name, String versionname, long app_installtime, int versioncode) {
        this.appName = app_name;
        this.packageName = package_name;
        this.versionName = versionname;
        this.appInstalltimeLong = app_installtime;
        this.versionCode = versioncode;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", appInstalltimeLong='" + appInstalltimeLong + '\'' +
                ", versionCode=" + versionCode +
                '}';
    }
}
