package com.jimetec.xunji.presenter;

import android.app.Activity;

import com.jimetec.xunji.bean.HttpResult;
import com.jimetec.xunji.http.HttpResultFuc;
import com.jimetec.xunji.http.ProgressSubscriber;
import com.jimetec.xunji.presenter.contract.IdeaContract;
import com.jimetec.xunji.rx.RxPresenter;
import com.jimetec.xunji.rx.Rxutil;


/**
 * 作者:zh
 * 时间:11/27/18 4:16 PM
 * 描述:
 */
public class IdeaPresenter extends RxPresenter<IdeaContract.View> implements IdeaContract.Presenter {


    private Activity mActivity;

    public IdeaPresenter(Activity activity) {
        mActivity = activity;
    }


    @Override
    public void leaveWord(String content) {
        addSubscribe(mRequestClient.leaveWord(content)
                .compose(Rxutil.<HttpResult<Object>>rxSchedulerHelper())
                .map(new HttpResultFuc<Object>())
                .subscribeWith(new ProgressSubscriber<Object>(mActivity,mView) {
                    @Override
                    public void onUINext(Object info) {
                        mView.backLeaveWord(info);

                    }
                }));

    }
}
