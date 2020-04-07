package com.jimetec.xunji.http;

/**
 * 作者:zh
 * 时间:2019-05-15 18:19
 * 描述:
 */
public class ApiException extends RuntimeException {
    public static final String DES_UNKOWN ="服务器维修中！";
    public static final String DES_NET_BREAK ="网络中断，请检查您的网络状态！";

    public static final String STATUS_LOCAL_UI = "-50";//本地自定义   网络数据返回  数据填充错误
    public static final String STATUS_TOKEN_OUTTIME = "-10";//本地自定义   网络数据返回  数据填充错误
    public static final String DES_LOCAL_UI= "页面数据显示错误";//本地自定义   网络数据返回  数据填充错误

    public static final String STATUS_NO_NET = "-80";//本地自定义   网络数据返回  数据填充错误
    public static final String DES_NO_NET = "手机没有网络";//本地自定义   网络数据返回  数据填充错误


    public static final String code_303="303";
    public static final String code_303_des="登录验证过期,请重新登录";

    public String code="";

    /**
     * 异常信息
     * @param detailMessage
     */
    public ApiException(String detailMessage) {
        super(detailMessage);
    }


    /**
     * 异常信息
     * @param detailMessage
     */
    public ApiException(String code, String detailMessage) {
        super(detailMessage);
        this.code = code;
    }
}
