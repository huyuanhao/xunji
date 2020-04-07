package com.jimetec.xunji.presenter;

import android.app.Activity;

import com.jimetec.xunji.bean.FriendBean;
import com.jimetec.xunji.bean.HttpResult;
import com.jimetec.xunji.http.CommonSubscriber;
import com.jimetec.xunji.http.HttpResultFuc;
import com.jimetec.xunji.presenter.contract.TrackContract;
import com.jimetec.xunji.rx.RxPresenter;
import com.jimetec.xunji.rx.Rxutil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者:capTain
 * 时间:2019-06-19 12:08
 * 描述:
 */
public class TrackPresenter extends RxPresenter<TrackContract.View> implements TrackContract.Presenter {
    private Activity mActivity;

    public TrackPresenter(Activity activity) {
        mActivity = activity;
    }


    @Override
    public void getFriends() {

//        mView.backFriends();
//        mRequestClient.getFriends()

        addSubscribe(mRequestClient.getFriends()
                .compose(Rxutil.<HttpResult<List<FriendBean>>>rxSchedulerHelper())
                .map(new HttpResultFuc<List<FriendBean> >())
                .subscribeWith(new CommonSubscriber<List<FriendBean> >(mView) {
                    @Override
                    public void onUINext(List<FriendBean>  info) {
                        List<FriendBean> beans = new ArrayList<>();
                        for (FriendBean bean:info) {
                            if (bean.lastLocationTimes>0){
                                beans.add(bean);
                            }
                        }
                        mView.backFriends(beans);
                    }
                }));

    }
}
