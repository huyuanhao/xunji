package com.jimetec.xunji.presenter.contract;


import com.common.baseview.base.BasePresenter;
import com.common.baseview.base.IBaseView;
import com.jimetec.xunji.bean.FriendBean;

import java.util.List;

/**
 * 作者:zh
 * 时间:2018/6/28 下午4:03
 * 描述:
 */
public interface TrackContract {

    interface View extends IBaseView {
        void backFriends(List<FriendBean> pageBean);
    }

    interface Presenter extends BasePresenter<View> {
        void getFriends();
    }
}