package com.jimetec.xunji.http;


import com.jimetec.xunji.util.VersionManager;

/**
 * 作者:zh
 * 时间:2018/6/28 上午10:44
 * 描述:
 */
public class URLs {



    public static final String appDomainUrl = "https://xunjiapi.jimetec.com"; //pro
    public static final String START_IP = "http://47.113.86.139:8080"; //pro

  
    public static String getApiUrlPath() {
//        return  START_IP;
        return VersionManager.getInstance().getAppDomainUrl();
//        return START_IP;
    }
}


