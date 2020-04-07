package com.jimetec.basin.excption;


/**
 * @作者 zh
 * @时间 2018/8/7 下午4:26
 * @描述 异常类，当获取的数据不是我们需要时，抛出异常
 */
public class LoanException extends RuntimeException {
    public static final String DES_UNKOWN ="服务器维修中！";
    public static final String DES_NET_BREAK ="网络中断，请检查您的网络状态！";

    public static final String STATUS_LOCAL_UI = "-50";//本地自定义   网络数据返回  数据填充错误
    public static final String DES_LOCAL_UI= "页面数据显示错误";//本地自定义   网络数据返回  数据填充错误

    public static final String STATUS_NO_NET = "-80";//本地自定义   网络数据返回  数据填充错误
    public static final String DES_NO_NET = "手机没有网络";//本地自定义   网络数据返回  数据填充错误

    public String type;
    /**
     * 异常信息
     * @param detailMessage
     */
    public LoanException(String detailMessage) {
        super(detailMessage);
    }


    /**
     * 异常信息
     * @param detailMessage
     */
    public LoanException(String type, String detailMessage) {
        super(detailMessage);
        this.type = type;
    }
}
