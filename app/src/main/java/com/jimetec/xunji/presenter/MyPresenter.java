package com.jimetec.xunji.presenter;

import android.app.Activity;

import com.jimetec.xunji.bean.HttpResult;
import com.jimetec.xunji.bean.UserBean;
import com.jimetec.xunji.http.CommonSubscriber;
import com.jimetec.xunji.http.HttpResultFuc;
import com.jimetec.xunji.http.ProgressSubscriber;
import com.jimetec.xunji.presenter.contract.MyContract;
import com.jimetec.xunji.rx.RxPresenter;
import com.jimetec.xunji.rx.Rxutil;

/**
 * 作者:zh
 * 时间:2018/6/28 下午4:05
 * 描述:
 */
public class MyPresenter extends RxPresenter<MyContract.View> implements MyContract.Presenter {

    private Activity mActivity;

//
    public MyPresenter(Activity activity) {
        mActivity = activity;
    }



    @Override
    public void upIcon(String base64) {
        addSubscribe(mRequestClient.updateIcon(base64)
                .compose(Rxutil.<HttpResult<String>>rxSchedulerHelper())
                .map(new HttpResultFuc<String>())
                .subscribeWith(new ProgressSubscriber<String>(mActivity,mView) {
                    @Override
                    public void onUINext(String userCards) {
                        mView.backIcon(userCards);
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

    @Override
    public void my() {
        addSubscribe(mRequestClient.my()
                .compose(Rxutil.<HttpResult<UserBean>>rxSchedulerHelper())
                .map(new HttpResultFuc<UserBean>())
                .subscribeWith(new CommonSubscriber<UserBean>(mView) {
                    @Override
                    public void onUINext(UserBean info) {
                        mView.backMyInfo(info);
                    }
                }));

    }

}
