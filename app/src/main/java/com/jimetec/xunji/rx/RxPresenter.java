package com.jimetec.xunji.rx;


import com.common.baseview.base.BasePresenter;
import com.common.baseview.base.IBaseView;
import com.common.lib.utils.LogUtils;
import com.jimetec.xunji.http.client.RequestClient;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * @作者 zh
 * @时间 2018/8/6 下午3:15
 * @描述
 */
public class RxPresenter<T extends IBaseView> implements BasePresenter<T> {
    protected T mView;
    protected CompositeDisposable mDisposable;
    public static RequestClient mRequestClient;

    static {
        mRequestClient = RequestClient.getInstance();
    }

    private String mSimpleName;


    @Override
    public void attachView(T view) {
        mSimpleName = view.getClass().getSimpleName();
        mView = view;
        LogUtils.i(this.getClass().getSimpleName() + "--attachView--" + mSimpleName);
    }

    public void addSubscribe(Disposable subscription) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(subscription);
    }


    protected void unSubscribe() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
        LogUtils.i(this.getClass().getSimpleName() + "--detachView--" + mSimpleName);
        unSubscribe();
    }
}
