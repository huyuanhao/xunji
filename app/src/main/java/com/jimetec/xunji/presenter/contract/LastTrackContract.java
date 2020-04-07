package com.jimetec.xunji.presenter.contract;


import com.common.baseview.base.BasePresenter;
import com.common.baseview.base.IBaseView;

/**
 * 作者:zh
 * 时间:2018/6/28 下午4:03
 * 描述:
 */
public interface LastTrackContract {

    interface View extends IBaseView {

        void backName(String info);
    }

    interface  Presenter extends BasePresenter<View> {
        void  rename(long id,long targetUserId ,String targetNickName);

    }
}
