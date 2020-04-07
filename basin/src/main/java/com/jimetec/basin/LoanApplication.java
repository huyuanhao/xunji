package com.jimetec.basin;

import android.app.Application;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebView;

import com.common.lib.utils.NetworkUtils;
import com.common.lib.utils.Utils;
import com.jimetec.basin.event.LoanEventDataUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * 作者:zh
 * 时间:2019-05-22 14:40
 * 描述:只作为测试启动
 */
@Deprecated
public class LoanApplication  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        if (BuildConfig.DEBUG){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }

        init();

    }



    private void init() {

        initip();
 
        UMConfigure.init(this, "5badcff1b465f5df02000026","guangfang", UMConfigure.DEVICE_TYPE_PHONE, "");
        UMConfigure.setLogEnabled(false);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
 
    }







    private void initip() {
//       final String ip = NetworkUtils.getIPAddress(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!TextUtils.isEmpty(NetworkUtils.getOutNetIP( 0))){
                       String ip =NetworkUtils.getOutNetIP( 0)  ;
                        LoanEventDataUtil.setIp(ip);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
