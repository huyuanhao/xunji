package com.jimetec.xunji.presenter.contract;


import com.common.baseview.base.BasePresenter;
import com.common.baseview.base.IBaseView;
import com.jimetec.xunji.bean.UserBean;

/**
 * 作者:zh
 * 时间:2018/6/28 下午4:03
 * 描述:
 */
public interface RegisterContract {

    interface View extends IBaseView {
        void backRegister(UserBean user);
        void backGetCode();
    }

    interface  Presenter extends BasePresenter<View> {
        void  getCode(String phone);
        void  register(String phone, String code);
    }
}
