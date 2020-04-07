package com.jimetec.basin.http;





import com.jimetec.basin.excption.LoanException;

import io.reactivex.functions.Function;

public class LoanHttpResultFuc<T> implements Function<LoanHttpResult<T>,T> {


    @Override
    public T apply(LoanHttpResult<T> httpResult) throws Exception {
//        if(!httpResult.error){
        if(!"0".equals(httpResult.code)){
            //非正常返回结构处理
//            throw new LoanException(httpResult.type, httpResult.msg);
            throw new LoanException(httpResult.code, httpResult.message);
        }
//        System.out.println(httpResult.toString());
        return httpResult.data;
    }
}
