package com.jimetec.xunji.bean;

/**
 * 作者:zh
 * 时间:2018/6/28 下午5:21
 * 描述:
 */
public class HttpResult<T> {
    public String resultCode;
    public String code;
    public String message;
    public T datas;
    public T data;
    public boolean memberStatus;
    public boolean isNeed;

    @Override
    public String toString() {
        return "LoanHttpResult{" +
                "resultCode='" + resultCode + '\'' +
                ", message='" + message + '\'' +
                ", datas=" + datas +
                '}';
    }
}
