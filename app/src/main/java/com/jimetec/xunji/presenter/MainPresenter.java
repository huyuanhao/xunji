package com.jimetec.xunji.presenter;

import android.app.Activity;

import com.common.lib.utils.SpUtil;
import com.jimetec.xunji.Constants;
import com.jimetec.xunji.bean.FriendBean;
import com.jimetec.xunji.bean.HttpResult;
import com.jimetec.xunji.bean.LocationWarnBean;
import com.jimetec.xunji.bean.UpLocateTimeBean;
import com.jimetec.xunji.http.CommonSubscriber;
import com.jimetec.xunji.http.HttpResultFuc;
import com.jimetec.xunji.http.NotipSubcriber;
import com.jimetec.xunji.presenter.contract.MainContract;
import com.jimetec.xunji.rx.RxBus;
import com.jimetec.xunji.rx.RxPresenter;
import com.jimetec.xunji.rx.Rxutil;
import com.jimetec.xunji.rx.event.HomeIndexEvent;
import com.jimetec.xunji.util.UserUtil;

import java.util.List;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 作者:capTain
 * 时间:2019-06-19 12:08
 * 描述:
 */
public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {
    private Activity mActivity;


    public MainPresenter(Activity activity) {
        mActivity = activity;

    }

    @Override
    public void attachView(MainContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    @Override
    public void registerEvent() {
        addSubscribe(
                RxBus.getDefault().toFlowable(HomeIndexEvent.class)
                        .compose(Rxutil.<HomeIndexEvent>rxSchedulerHelper())
                        .subscribeWith(
                                new ResourceSubscriber<HomeIndexEvent>() {

                                    @Override
                                    public void onNext(HomeIndexEvent homeIndexEvent) {
                                        mView.dealEvent(homeIndexEvent);
                                    }

                                    @Override
                                    public void onError(Throwable t) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                }
                        ));

    }

    @Override
    public void aimRemind(final  LocationWarnBean bean, long id, long tagetUserId, String friendName, String location, int remindWay, final int status) {
        addSubscribe(mRequestClient.aimRemind(id,tagetUserId,friendName,location,remindWay,status)
                .compose(Rxutil.<HttpResult<Object>>rxSchedulerHelper())
                .map(new HttpResultFuc<Object>())
                .subscribeWith(new NotipSubcriber<Object>() {
                    @Override
                    public void onNext(Object o) {
                        mView.backRemind();
                        bean.status = status;
                    }
                }));
    }

    @Override
    public void getFriendSettingWarns() {
            addSubscribe(mRequestClient.getFriendSettingWarns()
                    .compose(Rxutil.<HttpResult<List<LocationWarnBean>>>rxSchedulerHelper())
                    .map(new HttpResultFuc<List<LocationWarnBean>>())
                    .subscribeWith(new CommonSubscriber<List<LocationWarnBean>>(mView) {
                        @Override
                        public void onUINext(List<LocationWarnBean> info) {
                            mView.backFriendSettingWarns(info);
                        }
                    }));
    }

    @Override
    public void upLocateTime() {
        addSubscribe(mRequestClient.upLocateTime()
                .compose(Rxutil.<HttpResult<UpLocateTimeBean>>rxSchedulerHelper())
                .map(new HttpResultFuc<UpLocateTimeBean>())
                .subscribeWith(new ResourceSubscriber<UpLocateTimeBean>() {
                    @Override
                    public void onNext(UpLocateTimeBean time) {
                        mView.backLocateTime(time);
                    }

                    @Override
                    public void onError(Throwable t) {
                        mView.backLocateTime(new UpLocateTimeBean());
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void updateLocation(final double lng, final double lat, final String address) {
        addSubscribe(mRequestClient.updateLocation(lng, lat, address)
                .compose(Rxutil.<HttpResult<Long>>rxSchedulerHelper())
                .map(new HttpResultFuc<Long>())
                .subscribeWith(new ResourceSubscriber<Long>() {
                    @Override
                    public void onNext(Long time) {
                        if (UserUtil.isLogined()){
                            SpUtil.putString(Constants.longitude, lng + "");
                            SpUtil.putString(Constants.latitude, lat + "");
                            SpUtil.putString(Constants.locationAddress, address);
                            UserUtil.getUser().latitude = lat;
                            UserUtil.getUser().longitude = lng;
                            UserUtil.getUser().lastLocationTimes = time;
                            UserUtil.getUser().lastLocation = address;
                            UserUtil.save();
                        }


                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }



    @Override
    public void getNews() {

        if (UserUtil.isLogined()){
            addSubscribe(mRequestClient.getNews()
                    .compose(Rxutil.<HttpResult<List<FriendBean>>>rxSchedulerHelper())
                    .map(new HttpResultFuc<List<FriendBean>>())
                    .subscribeWith(new CommonSubscriber<List<FriendBean>>(mView) {
                        @Override
                        public void onUINext(List<FriendBean> info) {
                            mView.backNews(info);
                        }
                    }));
        }

    }

}
