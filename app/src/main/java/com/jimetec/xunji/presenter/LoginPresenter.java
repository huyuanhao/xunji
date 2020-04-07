package com.jimetec.xunji.presenter;

import android.app.Activity;

import com.jimetec.xunji.rx.RxBus;
import com.jimetec.xunji.rx.RxPresenter;
import com.jimetec.xunji.presenter.contract.LoginContract;
import com.jimetec.xunji.rx.event.LoginEvent;

import io.reactivex.functions.Consumer;

/**
 * 作者:capTain
 * 时间:2019-06-19 12:08
 * 描述:
 */
public class LoginPresenter extends RxPresenter<LoginContract.View>  implements LoginContract.Presenter {
    private Activity mActivity;

    public LoginPresenter(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void attachView(LoginContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    @Override
    public void login(String phone, String password) {
        mView.backlogin();
    }

    @Override
    public void registerEvent() {
        addSubscribe(RxBus.getDefault()
                .toFlowable(LoginEvent.class)
                .subscribe(new Consumer<LoginEvent>() {
                    @Override
                    public void accept(LoginEvent loginEvent) throws Exception {
                        mView.dealLoginEvent(loginEvent);
                    }
                }));
    }
}
