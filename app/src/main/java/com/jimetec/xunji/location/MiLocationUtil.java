package com.jimetec.xunji.location;

import android.content.Context;

/**
 * 作者:capTain
 * 时间:2019-10-21 10:20
 * 描述:
 */
public class MiLocationUtil {
    static WifiAutoCloseDelegate sDelegate = new WifiAutoCloseDelegate();

    public static void initOnServiceStarted(Context context) {
        if (sDelegate.isUseful(context)){
            sDelegate.initOnServiceStarted(context);
        }

    }

    public static void onLocateSuccess(Context context, int mapErrorCode) {
        if (sDelegate.isUseful(context)){
            sDelegate.onLocateSuccess(context.getApplicationContext(), PowerManagerUtil.getInstance().isScreenOn(context.getApplicationContext()), NetUtil.getInstance().isMobileAva(context.getApplicationContext()));
        }

    }


    public static void onLocateFail(Context context, int mapErrorCode) {
        if (sDelegate.isUseful(context)){
            sDelegate.onLocateFail(context.getApplicationContext(),mapErrorCode, PowerManagerUtil.getInstance().isScreenOn(context.getApplicationContext()), NetUtil.getInstance().isMobileAva(context.getApplicationContext()));
        }

    }
}
