package com.jimetec.xunji.presenter.contract;


import com.common.baseview.base.BasePresenter;
import com.common.baseview.base.IBaseView;
import com.jimetec.xunji.rx.event.LoginEvent;

/**
 * 作者:zh
 * 时间:2018/6/28 下午4:03
 * 描述:
 */
public interface LoginContract {

    interface View extends IBaseView {
        void backlogin();

        void dealLoginEvent(LoginEvent event);

    }

    interface Presenter extends BasePresenter<View> {

        void login(String phone,String  password);
        void registerEvent();
    }
}