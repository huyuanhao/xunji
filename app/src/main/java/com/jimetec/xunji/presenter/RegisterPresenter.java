package com.jimetec.xunji.presenter;

import android.app.Activity;

import com.jimetec.xunji.http.ProgressSubscriber;
import com.jimetec.xunji.bean.HttpResult;
import com.jimetec.xunji.bean.UserBean;
import com.jimetec.xunji.http.HttpResultFuc;
import com.jimetec.xunji.rx.RxPresenter;
import com.jimetec.xunji.presenter.contract.RegisterContract;
import com.jimetec.xunji.rx.Rxutil;

/**
 * 作者:capTain
 * 时间:2019-06-19 12:08
 * 描述:
 */
public class RegisterPresenter extends RxPresenter<RegisterContract.View> implements RegisterContract.Presenter {
    private Activity mActivity;


    public RegisterPresenter(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void getCode(String phone) {
        addSubscribe(mRequestClient.getCode(phone)
                .compose(Rxutil.<HttpResult<Object>>rxSchedulerHelper())
                .map(new HttpResultFuc<Object>())
                .subscribeWith(new ProgressSubscriber<Object>(mActivity, mView) {
                    @Override
                    public void onUINext(Object info) {
                        mView.backGetCode();

                    }
                }));
    }


    @Override
    public void register(String phone, String code) {
        addSubscribe(mRequestClient.login(phone,code)
                .compose(Rxutil.<HttpResult<UserBean>>rxSchedulerHelper())
                .map(new HttpResultFuc<UserBean>())
                .subscribeWith(new ProgressSubscriber<UserBean>(mActivity, mView) {
                    @Override
                    public void onUINext(UserBean info) {
                        mView.backRegister(info);

                    }
                }));
    }
}
