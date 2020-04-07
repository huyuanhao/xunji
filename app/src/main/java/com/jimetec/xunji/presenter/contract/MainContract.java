package com.jimetec.xunji.presenter.contract;


import com.common.baseview.base.BasePresenter;
import com.common.baseview.base.IBaseView;
import com.jimetec.xunji.bean.FriendBean;
import com.jimetec.xunji.bean.LocationWarnBean;
import com.jimetec.xunji.bean.UpLocateTimeBean;
import com.jimetec.xunji.rx.event.HomeIndexEvent;

import java.util.List;

/**
 * 作者:zh
 * 时间:2018/6/28 下午4:03
 * 描述:
 */
public interface MainContract {

    interface View extends IBaseView {

        void dealEvent(HomeIndexEvent indexEvent);
        void backLocateTime(UpLocateTimeBean bean);
        void backFriendSettingWarns(List<LocationWarnBean> list);
        void backRemind();
        void backNews(List<FriendBean> pageBean);
    }

    interface Presenter extends BasePresenter<View> {

        void registerEvent();
        void aimRemind(LocationWarnBean bean,long id,long tagetUserId, String friendName, String location,  int remindWay,  int status);
        void getFriendSettingWarns();
        void upLocateTime();
        void updateLocation(double lng,double lat,String address);
        void getNews();
    }
}