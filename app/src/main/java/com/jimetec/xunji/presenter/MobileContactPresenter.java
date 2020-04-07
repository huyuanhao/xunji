package com.jimetec.xunji.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.common.lib.utils.SpUtil;
import com.common.lib.utils.Utils;
import com.jimetec.xunji.Constants;
import com.jimetec.xunji.bean.ContactBean;
import com.jimetec.xunji.bean.HttpResult;
import com.jimetec.xunji.http.CommonSubscriber;
import com.jimetec.xunji.http.HttpResultFuc;
import com.jimetec.xunji.http.ProgressSubscriber;
import com.jimetec.xunji.presenter.contract.MobileContactContract;
import com.jimetec.xunji.rx.RxPresenter;
import com.jimetec.xunji.rx.Rxutil;
import com.jimetec.xunji.util.ContactUtil;
import com.jimetec.xunji.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * 作者:capTain
 * 时间:2019-06-19 12:08
 * 描述:
 */
public class MobileContactPresenter extends RxPresenter<MobileContactContract.View> implements MobileContactContract.Presenter {
    private Activity mActivity;

    public MobileContactPresenter(Activity activity) {
        mActivity = activity;
    }


    @Override
    public void getContacts() {
        addSubscribe(
                Flowable.create(new FlowableOnSubscribe<List<ContactBean>>() {
                    @Override
                    public void subscribe(FlowableEmitter<List<ContactBean>> emitter) throws Exception {
                        String history = SpUtil.getString(Constants.ADD_FRIENDs_HISTORY, "");

                        List<ContactBean> contactBeans = ContactUtil.getContactList(Utils.getApp());
                        List<ContactBean> filter = new ArrayList<>();
                        for (int i = 0; i < contactBeans.size(); i++) {
                            ContactBean contactBean = contactBeans.get(i);
                            if(!TextUtils.isEmpty(contactBean.emergencyPhone) &&contactBean.emergencyPhone.length()>10  ){
                                if (history.contains(contactBean.emergencyPhone)){
                                    contactBean.type =1;
                                }
                                if (!contactBean.emergencyPhone.equals(UserUtil.getUserPhone()))
                                filter.add(contactBean);
                            }

                        }
                        emitter.onNext(filter);
                    }
                }, BackpressureStrategy.DROP)
                        .compose(Rxutil.<List<ContactBean>>rxSchedulerHelper())
                        .subscribeWith(new CommonSubscriber<List<ContactBean>>(mView) {
                            @Override
                            public void onUINext(List<ContactBean> contactBeans) {
                                mView.backDatas(contactBeans);
                            }
                        }));

    }

    @Override
    public void addFriend(String nickName, final ContactBean bean) {
        addSubscribe(mRequestClient.addFriend(UserUtil.getUserPhone(),nickName,bean.emergencyPhone)
                .compose(Rxutil.<HttpResult<Object>>rxSchedulerHelper())
                .map(new HttpResultFuc<Object>())
                .subscribeWith(new ProgressSubscriber<Object>(mActivity,mView) {
                    @Override
                    public void onUINext(Object info) {
                        mView.backAdd(bean);

                    }

                    @Override
                    public void showError(String code, String msg) {
                        super.showError(code, msg);
                        if ("300".equals(code)){
                            mView.backAdd(bean);
                        }
                    }
                }));
    }
}
