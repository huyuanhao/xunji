package com.jimetec.basin.http;

import com.common.lib.utils.SpUtil;

/**
 * 作者:zh
 * 时间:2019-05-23 10:55
 * 描述:
 */
public class LoanUrl {
    public static final String PATH_API = "PATH_API";
    public static final String PATH_API_DEFAULT = "http://109.23.68.8:8088";
    public static final String PATH_EVENT = "PATH_EVENT";
    public static final String PATH_EVENT_DEFAULT = "http://139.108.87.194:8088";

    public static String getApiBaseUrl() {
        return SpUtil.getString(PATH_API, PATH_API_DEFAULT);
    }

    public static String getEventBaseUrl() {
           return SpUtil.getString(PATH_EVENT, PATH_EVENT_DEFAULT);
    }

//    public static String getAppDomainUrlPath() {
//        return SplashBeanUtil.getAppDomainUrl()+"/";
//    }
}
