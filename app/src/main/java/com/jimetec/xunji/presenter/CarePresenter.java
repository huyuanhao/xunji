package com.jimetec.xunji.presenter;

import android.app.Activity;

import com.jimetec.xunji.bean.FriendBean;
import com.jimetec.xunji.bean.HttpResult;
import com.jimetec.xunji.http.CommonSubscriber;
import com.jimetec.xunji.http.HttpResultFuc;
import com.jimetec.xunji.http.ProgressSubscriber;
import com.jimetec.xunji.presenter.contract.CareContract;
import com.jimetec.xunji.rx.RxBus;
import com.jimetec.xunji.rx.RxPresenter;
import com.jimetec.xunji.rx.Rxutil;
import com.jimetec.xunji.rx.event.HomeIndexEvent;

import java.util.List;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 作者:capTain
 * 时间:2019-06-19 12:08
 * 描述:
 */
public class CarePresenter extends RxPresenter<CareContract.View> implements CareContract.Presenter {
    private Activity mActivity;

    public CarePresenter(Activity activity) {
        mActivity = activity;
    }



//    @Override
//    public void attachView(View view) {
//        super.attachView(view);
//        registerEvent();
//    }


    @Override
    public void attachView(CareContract.View view) {
        super.attachView(view);
        registerEvent();
    }



    public void registerEvent() {
        addSubscribe(
                RxBus.getDefault().toFlowable(HomeIndexEvent.class)
                        .compose(Rxutil.<HomeIndexEvent>rxSchedulerHelper())
                        .subscribeWith(
                                new ResourceSubscriber<HomeIndexEvent>() {

                                    @Override
                                    public void onNext(HomeIndexEvent homeIndexEvent) {
                                        mView.updateViewEvent(homeIndexEvent);
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
    public void getFriends() {

//        mView.backFriends();
//        mRequestClient.getFriends()
        addSubscribe(mRequestClient.getFriends()
                .compose(Rxutil.<HttpResult<List<FriendBean>>>rxSchedulerHelper())
                .map(new HttpResultFuc<List<FriendBean>>())
                .subscribeWith(new CommonSubscriber<List<FriendBean> >(mView) {
                    @Override
                    public void onUINext(List<FriendBean>  info) {
                        mView.backFriends(info);
                    }
                }));

    }




    @Override
    public void unBinderFriend(long id) {
        addSubscribe(mRequestClient.unBinderFriend(id)
                .compose(Rxutil.<HttpResult<Object>>rxSchedulerHelper())
                .map(new HttpResultFuc<Object>())
                .subscribeWith(new ProgressSubscriber<Object>(mActivity,mView) {
                    @Override
                    public void onUINext(Object info) {
                        mView.backUnbinder(info);

                    }
                }));
    }

    @Override
    public void addFriend(String phone, String nickNmae, String targetUserPhone) {
        addSubscribe(mRequestClient.addFriend(phone,nickNmae,targetUserPhone)
                .compose(Rxutil.<HttpResult<Object>>rxSchedulerHelper())
                .map(new HttpResultFuc<Object>())
                .subscribeWith(new ProgressSubscriber<Object>(mActivity,mView) {
                    @Override
                    public void onUINext(Object info) {
                        mView.backAdd(info);

                    }
                }));
    }

    @Override
    public void updateName(String userName) {
        //用户更改自己的用户名

        addSubscribe(mRequestClient.updateName(userName)
                .compose(Rxutil.<HttpResult<String>>rxSchedulerHelper())
                .map(new HttpResultFuc<String>())
                .subscribeWith(new ProgressSubscriber<String>(mActivity,mView) {
                    @Override
                    public void onUINext(String nickname) {
                        mView.backNickName(nickname);
                    }
                }));
    }
}
