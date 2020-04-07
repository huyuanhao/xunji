package com.jimetec.xunji.presenter;

import android.app.Activity;

import com.jimetec.xunji.bean.ContactBean;
import com.jimetec.xunji.bean.HttpResult;
import com.jimetec.xunji.bean.UserBean;
import com.jimetec.xunji.http.CommonSubscriber;
import com.jimetec.xunji.http.HttpResultFuc;
import com.jimetec.xunji.http.ProgressSubscriber;
import com.jimetec.xunji.presenter.contract.ContactContract;
import com.jimetec.xunji.rx.RxPresenter;
import com.jimetec.xunji.rx.Rxutil;
import com.jimetec.xunji.util.UserUtil;

import java.util.List;

/**
 * 作者:zh
 * 时间:2018/6/28 下午4:05
 * 描述:
 */
public class ContactPresenter extends RxPresenter<ContactContract.View> implements ContactContract.Presenter {

    private Activity mActivity;

//
    public ContactPresenter(Activity activity) {
        mActivity = activity;
    }


    @Override
    public void getContacts() {
        addSubscribe(mRequestClient.emergencyList()
                .compose(Rxutil.<HttpResult<List<ContactBean>>>rxSchedulerHelper())
                .map(new HttpResultFuc<List<ContactBean>>())
                .subscribeWith(new ProgressSubscriber<List<ContactBean>>(mActivity,mView) {
                    @Override
                    public void onUINext(List<ContactBean> info) {
                        mView.backContacts(info);
                    }
                }));
    }



    public void getLocContacts() {
        addSubscribe(mRequestClient.emergencyList()
                .compose(Rxutil.<HttpResult<List<ContactBean>>>rxSchedulerHelper())
                .map(new HttpResultFuc<List<ContactBean>>())
                .subscribeWith(new CommonSubscriber<List<ContactBean>>(mView) {
                    @Override
                    public void onUINext(List<ContactBean> info) {
                        mView.backContacts(info);
                    }
                }));
    }

    @Override
    public void addContact(String name, String phone, int type) {
        addSubscribe(mRequestClient.addEmergency(name,phone,type)
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
    public void deleteContact(int id) {
        addSubscribe(mRequestClient.deleteContact(id)
                .compose(Rxutil.<HttpResult<Object>>rxSchedulerHelper())
                .map(new HttpResultFuc<Object>())
                .subscribeWith(new ProgressSubscriber<Object>(mActivity,mView) {
                    @Override
                    public void onUINext(Object info) {
                        mView.backDelete(info);
                    }
                }));
    }

    @Override
    public void realName(final String name) {
        addSubscribe(mRequestClient.updateRealName(name)
                .compose(Rxutil.<HttpResult<Object>>rxSchedulerHelper())
                .map(new HttpResultFuc<Object>())
                .subscribeWith(new ProgressSubscriber<Object>(mActivity,mView) {
                    @Override
                    public void onUINext(Object info) {
                        UserBean user = UserUtil.getUser();
                        if (user!=null){
                            user.realName=name;
                            UserUtil.setUser(user);
                        }
                        mView.backrealName(info);
                    }
                }));
    }


    //信息发送成功
    public void sendMsg(String phones){
        addSubscribe(mRequestClient.sendMsg(phones,UserUtil.getRealName())
                .compose(Rxutil.<HttpResult<Object>>rxSchedulerHelper())
                .map(new HttpResultFuc<Object>())
                .subscribeWith(new CommonSubscriber<Object>(mView) {
                    @Override
                    public void onUINext(Object info) {
                        mView.backAdd(info);
                    }
                }));
    }
}
