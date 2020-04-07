package com.jimetec.xunji.presenter;

import android.app.Activity;

import com.jimetec.xunji.bean.HttpResult;
import com.jimetec.xunji.bean.LocationWarnBean;
import com.jimetec.xunji.http.CommonSubscriber;
import com.jimetec.xunji.http.HttpResultFuc;
import com.jimetec.xunji.http.ProgressSubscriber;
import com.jimetec.xunji.presenter.contract.LocationWarnContract;
import com.jimetec.xunji.rx.RxPresenter;
import com.jimetec.xunji.rx.Rxutil;

import java.util.List;


/**
 * 作者:zh
 * 时间:11/27/18 4:16 PM
 * 描述:
 */
public class LocationWarnPresenter extends RxPresenter<LocationWarnContract.View> implements LocationWarnContract.Presenter {


    private Activity mActivity;

    public LocationWarnPresenter(Activity activity) {
        mActivity = activity;
    }




    @Override
    public void getLocationWarns(long friendId) {
        addSubscribe(mRequestClient.getLocationWarns(friendId)
                .compose(Rxutil.<HttpResult<List<LocationWarnBean>>>rxSchedulerHelper())
                .map(new HttpResultFuc<List<LocationWarnBean>>())
                .subscribeWith(new CommonSubscriber<List<LocationWarnBean>>(mView) {
                    @Override
                    public void onUINext(List<LocationWarnBean> info) {
                        mView.backLocationWarns(info);
                    }
                }));
    }

    @Override
    public void addFriendWarn(long friendId, String friendName,String remark, String location, double longitude, double latitude, int remindWay) {
        addSubscribe(mRequestClient.addFriendWarn(friendId,friendName,remark,location,longitude,latitude,remindWay)
                .compose(Rxutil.<HttpResult<Object>>rxSchedulerHelper())
                .map(new HttpResultFuc<Object>())
                .subscribeWith(new ProgressSubscriber<Object>(mActivity,mView) {
                    @Override
                    public void onUINext(Object info) {
                        mView.backAddWarn(info);

                    }
                }));
    }

    @Override
    public void deleteFriendWarn(long id) {
        addSubscribe(mRequestClient.deleteFriendWarn(id)
                .compose(Rxutil.<HttpResult<Object>>rxSchedulerHelper())
                .map(new HttpResultFuc<Object>())
                .subscribeWith(new ProgressSubscriber<Object>(mActivity,mView) {
                    @Override
                    public void onUINext(Object info) {
                        mView.backDeleteWarn(info);

                    }
                }));
    }
}
