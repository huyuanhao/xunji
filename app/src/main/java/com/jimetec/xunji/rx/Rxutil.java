package com.jimetec.xunji.rx;


import com.jimetec.xunji.MyApplication;
import com.jimetec.xunji.bean.HttpResult;
import com.jimetec.xunji.http.ApiException;
import com.jimetec.xunji.util.UserUtil;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * @作者 zh
 * @时间 2018/8/6 下午7:36
 * @描述
 */
public class Rxutil {

    /**
     * 子线程 网络请求数据处理   主线程 ui
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> rxSchedulerHelper() {


        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 统一返回结果处理
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<HttpResult<T>, T> handleResult() {   //compose判断结果

        return new FlowableTransformer<HttpResult<T>, T>() {
            @Override
            public Flowable<T> apply(Flowable<HttpResult<T>> httpResponseFlowable) {
                return httpResponseFlowable.flatMap(new Function<HttpResult<T>, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(HttpResult<T> result) {

                        if (result.isNeed) {
                            UserUtil.saveVip(result.memberStatus);
                        }
                        if("200".equals(result.resultCode)){
                            return createData(result.datas);
                        } else {
                            if ("810".equals(result.resultCode)) {
                                MyApplication.freeVipDes=result.message;
                            }
                            return Flowable.error(new ApiException(result.resultCode,result.message));
                        }
                    }
                });
            }
        };
    }

    /**
     * 生成Flowable
     * @param <T>
     * @return
     */
    public static <T> Flowable<T> createData(final T t) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }, BackpressureStrategy.BUFFER);
    }



    public static <T> ObservableTransformer<T, T> localSchedulerHelper() {

       return new ObservableTransformer<T, T>(){

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }






}
