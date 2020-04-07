package com.jimetec.xunji.presenter;

import android.app.Activity;

import com.jimetec.xunji.bean.FriendBean;
import com.jimetec.xunji.bean.HttpResult;
import com.jimetec.xunji.bean.NewsBean;
import com.jimetec.xunji.bean.TimeSortBean;
import com.jimetec.xunji.http.CommonSubscriber;
import com.jimetec.xunji.http.HttpResultFuc;
import com.jimetec.xunji.http.ProgressSubscriber;
import com.jimetec.xunji.presenter.contract.NewsContract;
import com.jimetec.xunji.rx.RxPresenter;
import com.jimetec.xunji.rx.Rxutil;
import com.jimetec.xunji.util.UserUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.functions.Function;

/**
 * 作者:capTain
 * 时间:2019-06-19 12:08
 * 描述:
 */
public class NewsPresenter extends RxPresenter<NewsContract.View> implements NewsContract.Presenter {
    private Activity mActivity;

    public NewsPresenter(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void getNews() {

        if (UserUtil.isLogined()){
            addSubscribe(mRequestClient.getNews()
                    .compose(Rxutil.<HttpResult<List<FriendBean>>>rxSchedulerHelper())
                    .map(new HttpResultFuc<List<FriendBean>>())
                    .map(new Function<List<FriendBean>, List<TimeSortBean>>() {
                        @Override
                        public List<TimeSortBean> apply(List<FriendBean> friendBeans) throws Exception {
                            List<TimeSortBean> beans = new ArrayList<>();
                            List<NewsBean> newsBeans = LitePal.where("targetUserId = ? ", UserUtil.getUserId() + "").find(NewsBean.class);

                            if (newsBeans!=null){
                                beans.addAll(newsBeans);
                            }

                            beans.addAll(friendBeans);
                            Collections.sort(beans, new Comparator<TimeSortBean>() {
                                @Override
                                public int compare(TimeSortBean o1, TimeSortBean o2) {
                                    return o2.times - o1.times > 0 ? 1 : -1;
                                }
                            });
                            return beans;
                        }
                    })
                    .subscribeWith(new CommonSubscriber<List<TimeSortBean>>(mView) {
                        @Override
                        public void onUINext(List<TimeSortBean> info) {


                            mView.backNews(info);
                        }
                    }));
        }else {
            List<TimeSortBean> beans = new ArrayList<>();
            List<NewsBean> newsBeans = LitePal.where("targetUserId = ? ", UserUtil.getUserId() + "").find(NewsBean.class);
            if (newsBeans!=null){
                beans.addAll(newsBeans);
            }
            Collections.sort(beans, new Comparator<TimeSortBean>() {
                @Override
                public int compare(TimeSortBean o1, TimeSortBean o2) {
                    return o2.times - o1.times > 0 ? 1 : -1;
                }
            });

            mView.backNews(beans);
        }


    }

    @Override
    public void agree(long id) {
        addSubscribe(mRequestClient.agree(id)
                .compose(Rxutil.<HttpResult<Object>>rxSchedulerHelper())
                .map(new HttpResultFuc<Object>())
                .subscribeWith(new ProgressSubscriber<Object>(mActivity, mView) {
                    @Override
                    public void onUINext(Object info) {
                        mView.backAgree(info);
                    }
                }));
    }


//    @Override
//    public void getFriends() {
//
////        mView.backFriends();
////        mRequestClient.getFriends()
//
//        addSubscribe(mRequestClient.getFriends()
//                .compose(Rxutil.<HttpResult<List<UserBean>>>rxSchedulerHelper())
//                .map(new HttpResultFuc<List<UserBean> >())
//                .subscribeWith(new CommonSubscriber<List<UserBean> >(mView) {
//                    @Override
//                    public void onUINext(List<UserBean>  info) {
//                        mView.backFriends(info);
//                    }
//                }));
//
//    }
//
//    @Override
//    public void addFriend(String phone, String targetUserPhone) {
//        addSubscribe(mRequestClient.addFriend(phone,targetUserPhone)
//                .compose(Rxutil.<HttpResult<Object>>rxSchedulerHelper())
//                .map(new HttpResultFuc<Object>())
//                .subscribeWith(new ProgressSubscriber<Object>(mActivity,mView) {
//                    @Override
//                    public void onUINext(Object info) {
//                        mView.backAdd(info);
//
//                    }
//                }));
//    }
}
