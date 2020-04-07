package com.jimetec.xunji.http;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 作者:capTain
 * 时间:2019-09-05 11:00
 * 描述:
 */
public abstract class NotipSubcriber <T> extends ResourceSubscriber<T> {

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }
}
