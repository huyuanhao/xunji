package com.jimetec.xunji.presenter;

import android.app.Activity;

import com.jimetec.xunji.bean.FriendBean;
import com.jimetec.xunji.bean.HttpResult;
import com.jimetec.xunji.http.CommonSubscriber;
import com.jimetec.xunji.http.HttpResultFuc;
import com.jimetec.xunji.presenter.contract.SelectContactContract;
import com.jimetec.xunji.rx.RxPresenter;
import com.jimetec.xunji.rx.Rxutil;

import java.util.List;

/**
 * 作者:capTain
 * 时间:2019-06-19 12:08
 * 描述:
 */
public class SelectContactPresenter extends RxPresenter<SelectContactContract.View> implements SelectContactContract.Presenter {
    private Activity mActivity;

    public SelectContactPresenter(Activity activity) {
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
                        mView.backFriends(info);
                    }
                }));

    }


}
