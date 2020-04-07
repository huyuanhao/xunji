package com.jimetec.xunji.http;


import com.jimetec.xunji.MyApplication;
import com.jimetec.xunji.bean.HttpResult;
import com.jimetec.xunji.util.UserUtil;

import io.reactivex.functions.Function;

public class HttpResultFuc<T> implements Function<HttpResult<T>, T> {


    @Override
    public T apply(HttpResult<T> httpResult) throws Exception {
//        if(!httpResult.error){
        if (httpResult.isNeed) {
            UserUtil.saveVip(httpResult.memberStatus);
        }


        if ("200".equals(httpResult.code)) {
            return httpResult.data;
        } else{
            if (!"200".equals(httpResult.resultCode)) {
                if ("810".equals(httpResult.resultCode)) {
                    MyApplication.freeVipDes=httpResult.message;
                }
                //非正常返回结构处理
//            throw new LoanException(httpResult.type, httpResult.msg);
                throw new ApiException(httpResult.resultCode, httpResult.message);
            }
        }

//        System.out.println(httpResult.toString());
        return httpResult.datas;
    }
}
