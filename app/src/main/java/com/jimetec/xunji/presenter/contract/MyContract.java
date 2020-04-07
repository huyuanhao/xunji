package com.jimetec.xunji.presenter.contract;


import com.common.baseview.base.BasePresenter;
import com.common.baseview.base.IBaseView;
import com.jimetec.xunji.bean.UserBean;

/**
 * 作者:zh
 * 时间:2018/6/28 下午4:03
 * 描述:
 */
public interface MyContract {

    interface View extends IBaseView {

        void backIcon(String info);
        void backNickName(String nickname);

        void backMyInfo(UserBean info);

    }

    interface  Presenter extends BasePresenter<View> {
        void  upIcon(String base64);
        void  updateName(String nick);
        void my();
    }
}
