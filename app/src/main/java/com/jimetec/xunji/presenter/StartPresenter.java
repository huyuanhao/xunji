package com.jimetec.xunji.presenter;

import android.app.Activity;

import com.jimetec.basin.bean.LoanInfo;
import com.jimetec.basin.http.LoanClient;
import com.jimetec.basin.http.LoanHttpResult;
import com.jimetec.basin.http.LoanRxutil;
import com.jimetec.xunji.bean.HttpResult;
import com.jimetec.xunji.bean.SplashInfo;
import com.jimetec.xunji.http.CommonSubscriber;
import com.jimetec.xunji.http.HttpResultFuc;
import com.jimetec.xunji.presenter.contract.StartContract;
import com.jimetec.xunji.rx.RxPresenter;
import com.jimetec.xunji.rx.Rxutil;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 作者:zh
 * 时间:2018/6/28 下午4:05
 * 描述:
 */
public class StartPresenter extends RxPresenter<StartContract.View> implements StartContract.Presenter {

    private Activity mActivity;

    //
    public StartPresenter(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void startup(String applicationId, String channel) {
        addSubscribe(mRequestClient.startup(applicationId, channel).
                compose(Rxutil.<HttpResult<SplashInfo>>rxSchedulerHelper())
                .compose(Rxutil.<SplashInfo>handleResult())
                .subscribeWith(new CommonSubscriber<SplashInfo>(mView) {
                    @Override
                    public void onUINext(SplashInfo info) {
                        mView.backSplashInfo(info);
                    }
                }));

        addSubscribe(LoanClient.getInstance()
                .queryMarketing(applicationId)
                .compose(LoanRxutil.<LoanHttpResult<LoanInfo>>rxSchedulerHelper())
                .compose(LoanRxutil.<LoanInfo>handleResult())
                .subscribeWith(new ResourceSubscriber<LoanInfo>() {
                    @Override
                    public void onNext(LoanInfo loanInfo) {
                        mView.backLoanInfo(loanInfo);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));

    }

    @Override
    public void exitUdid() {
        addSubscribe(mRequestClient.exist()
                .compose(Rxutil.<HttpResult<Object>>rxSchedulerHelper())
                .map(new HttpResultFuc<Object>())
                .subscribeWith(new ResourceSubscriber<Object>() {
                    @Override
                    public void onNext(Object o) {
                        mView.backExit(o);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));


    }


}
