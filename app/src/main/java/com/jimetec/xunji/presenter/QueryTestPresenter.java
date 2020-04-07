package com.jimetec.xunji.presenter;

import android.app.Activity;

import com.jimetec.xunji.bean.HttpResult;
import com.jimetec.xunji.bean.TestUserBean;
import com.jimetec.xunji.http.HttpResultFuc;
import com.jimetec.xunji.http.ProgressSubscriber;
import com.jimetec.xunji.presenter.contract.QueryTestContract;
import com.jimetec.xunji.rx.RxPresenter;
import com.jimetec.xunji.rx.Rxutil;

/**
 * 作者:zh
 * 时间:2018/6/28 下午4:05
 * 描述:
 */
public class QueryTestPresenter extends RxPresenter<QueryTestContract.View> implements QueryTestContract.Presenter {

    private Activity mActivity;

//
    public QueryTestPresenter(Activity activity) {
        mActivity = activity;
    }



    @Override
    public void rename(long id, long targetUserId,final String targetNickName) {
        addSubscribe(mRequestClient.rename(id,targetUserId,targetNickName)
                .compose(Rxutil.<HttpResult<Object>>rxSchedulerHelper())
                .map(new HttpResultFuc<Object>())
                .subscribeWith(new ProgressSubscriber<Object>(mActivity,mView) {
                    @Override
                    public void onUINext(Object userCards) {
                        mView.backName(targetNickName);
                    }
                }));
    }

    @Override
    public void queryTest(String phone) {
        addSubscribe(mRequestClient.queryTest(phone)
                .compose(Rxutil.<HttpResult<TestUserBean>>rxSchedulerHelper())
                .map(new HttpResultFuc<TestUserBean>())
                .subscribeWith(new ProgressSubscriber<TestUserBean>(mActivity,mView) {
                    @Override
                    public void onUINext(TestUserBean userCards) {
                        mView.backTestUser(userCards);
                    }
                }));
    }
}
