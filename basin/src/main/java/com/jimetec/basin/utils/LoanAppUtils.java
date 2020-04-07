package com.jimetec.basin.utils;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.common.lib.utils.Utils;
import com.jimetec.basin.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者:zh
 * 时间:2018/10/9 下午3:53
 * 描述:
 */
public class LoanAppUtils {

    private static Application sApplication=Utils.getApp();
    private static int mainThreadId;
    private static Handler sHandler;
    public static  String sUtid;


//    public static Application getApp() {
//        if (sApplication != null) return sApplication;
//        return null;
//    }
//    LoanAppUtils.getApp()



    public static String getUtid(){
        return sUtid;
    }


    public static Application getApplication() {
        return sApplication;
    }

    public static void setApplication(Application application) {
        sApplication = application;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }

    public static void setMainThreadId(int threadId) {
        mainThreadId = threadId;
    }

    public static Handler getHandler() {
        return sHandler;
    }

    public static void setHandler(Handler handler) {
        sHandler = handler;
    }

    public static void setUtid(String utid) {
        sUtid= utid;
    }

    public static List<AppInfo> getAppsInfo() {
        List<AppInfo> list = new ArrayList<>();
        PackageManager pm = sApplication.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo pi : installedPackages) {
            AppInfo ai = getBean(pm, pi);
            if (ai == null) continue;
            list.add(ai);
        }
        return list;
    }


    private static AppInfo getBean(final PackageManager pm, final PackageInfo pi) {
        if (pm == null || pi == null) return null;
        ApplicationInfo ai = pi.applicationInfo;
        String packageName = pi.packageName;
        String name = ai.loadLabel(pm).toString();
        Drawable icon = ai.loadIcon(pm);
        String packagePath = ai.sourceDir;
        String versionName = pi.versionName;
        long time =  pi.firstInstallTime;
        int versionCode = pi.versionCode;
//        boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0;
        return new AppInfo(name, packageName,  versionName, time, versionCode);
    }



}
