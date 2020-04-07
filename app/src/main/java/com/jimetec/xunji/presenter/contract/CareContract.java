package com.jimetec.xunji.presenter.contract;


import com.common.baseview.base.BasePresenter;
import com.common.baseview.base.IBaseView;
import com.jimetec.xunji.bean.FriendBean;
import com.jimetec.xunji.rx.event.HomeIndexEvent;

import java.util.List;

/**
 * 作者:zh
 * 时间:2018/6/28 下午4:03
 * 描述:
 */
public interface CareContract {

    interface View extends IBaseView {

        void backFriends(List<FriendBean>  pageBean);

        void backAdd(Object obj);
        void backUnbinder(Object obj);
        void backNickName(String nickname);

        void updateViewEvent(HomeIndexEvent event);
    }

    interface Presenter extends BasePresenter<View> {

        void getFriends();
        void unBinderFriend(long id);
        void addFriend(String phone, String nickNmae, String targetUserPhone);
        void  updateName(String nick);

    }
}