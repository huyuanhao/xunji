package com.jimetec.xunji.presenter.contract;


import com.common.baseview.base.BasePresenter;
import com.common.baseview.base.IBaseView;
import com.jimetec.xunji.bean.TestUserBean;

/**
 * 作者:zh
 * 时间:2018/6/28 下午4:03
 * 描述:
 */
public interface QueryTestContract {

    interface View extends IBaseView {

        void backName(String info);
        void backTestUser(TestUserBean bean);
    }

    interface  Presenter extends BasePresenter<View> {
        void  rename(long id, long targetUserId, String targetNickName);
        void queryTest(String  phone);
    }
}
