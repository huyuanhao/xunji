package com.jimetec.basin.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.common.lib.utils.LogUtils;
import com.common.lib.utils.Utils;

/**
 * 作者:zh
 * 时间:4/18/19 3:12 PM
 * 描述:
 */
public class WebUtil {

    public static boolean isWifiProxy(){

       
        if ( LogUtils.getConfig().isLogSwitch() ||isCanProxy()){
            return false;
        }

      return isWifiProxy(Utils.getApp());
    }

    /*
     * 判断设备 是否使用代理上网
     * */
    public static boolean isWifiProxy(Context context) {

        final boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;

        String proxyAddress;

        int proxyPort;

        if (IS_ICS_OR_LATER) {

            proxyAddress = System.getProperty("http.proxyHost");

            String portStr = System.getProperty("http.proxyPort");

            proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));

        } else {

            proxyAddress = android.net.Proxy.getHost(context);

            proxyPort = android.net.Proxy.getPort(context);

        }

        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);

    }





    public static boolean isCanProxy(){
       return true;
    }


    public static void handleReceivedSslError(String curUrl, String hostUrl, android.webkit.SslErrorHandler handler) {

        if (isDebug()   || isCanProxy()){
            handler.proceed();// 接受所有网站的证书
            return;
        }

        try {
            Uri hostUri = Uri.parse(hostUrl);
            Uri curUri = Uri.parse(curUrl);
//            LogUtil.e("onReceivedSslError",curUrl);
//            LogUtil.e("onReceivedSslError",hostUrl);
            String hostHost = hostUri.getHost();
            String curHost = curUri.getHost();
            if (hostHost.equalsIgnoreCase(curHost)) {

            }else {
                handler.proceed();// 接受所有网站的证书
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void handleReceivedSslError( android.webkit.SslErrorHandler handler) {

        if (isDebug() || isCanProxy() || !isWifiProxy()){
            handler.proceed();// 接受所有网站的证书
            return;
        }
    }
    
    public static boolean  isDebug(){
        return LoanUtil.isDebug();
    }
}
