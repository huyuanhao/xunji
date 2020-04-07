package com.jimetec.xunji.presenter.contract;


import com.common.baseview.base.BasePresenter;
import com.common.baseview.base.IBaseView;
import com.jimetec.xunji.bean.LocationWarnBean;

import java.util.List;

/**
 * 作者:zh
 * 时间:2018/6/28 下午4:03
 * 描述:
 */
public interface LocationWarnContract {

    interface View extends IBaseView {
        void backLocationWarns(List<LocationWarnBean> pageBean);
        void backAddWarn(Object object);
        void backDeleteWarn(Object object);
    }

    //设置地点提醒 /locationRemind/add post
    interface Presenter extends BasePresenter<View> {
        void getLocationWarns(long friendId);
        void addFriendWarn(long friendId,String friendName,String remark,String location,double longitude,double latitude,int remindWay);
        void deleteFriendWarn(long id);

    }
}