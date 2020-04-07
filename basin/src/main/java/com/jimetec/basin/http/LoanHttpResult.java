package com.jimetec.basin.http;

/**
 * 作者:zh
 * 时间:2018/6/28 下午5:21
 * 描述:
 */
public class LoanHttpResult<T> {
    public boolean error;
//    public T results;
    public String code="";
    public String msg="";
    public String message="";
    public T data;

    @Override
    public String toString() {
        return "LoanHttpResult{" +
                "error=" + error +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
