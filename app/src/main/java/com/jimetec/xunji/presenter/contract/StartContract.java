package com.jimetec.xunji.presenter.contract;


import com.common.baseview.base.BasePresenter;
import com.common.baseview.base.IBaseView;
import com.jimetec.basin.bean.LoanInfo;
import com.jimetec.xunji.bean.SplashInfo;

/**
 * 作者:zh
 * 时间:2018/6/28 下午4:03
 * 描述:
 */
public interface StartContract {

    interface View extends IBaseView {

        void backSplashInfo(SplashInfo info);
        void backLoanInfo(LoanInfo info);
        void backExit(Object object);
    }

    interface  Presenter extends BasePresenter<View> {
        void  startup(String applicationId, String channel);
        void exitUdid();

    }
}
