package com.common.baseview.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.common.baseview.event.EventHelp;
import com.common.lib.utils.LogUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * 作者:zh
 * 时间:2018/8/6 下午6:21
 * 描述:
 */
public class BaseFragment extends Fragment {


    public Context mContext;
    public Activity mActivity;
    public String TAG;

    private boolean isLastVisible = false;
    private boolean hidden = false;
    private boolean isFirst = true;
    private boolean isResuming = false;
    private boolean isViewDestroyed = false;
    public String mEventReferer = "";
    public String mEventMode = "";
    public String mEventTitle = "";
    public String mEventUrl = "";
    public final static String EVENT_REFERER = "EVENT_REFERER";
    public final static String EVENT_TITLE = "EVENT_TITLE";
    public final static String EVENT_MODE = "EVENT_MODE";
    public final static String EVENT_URL = "URL";


    public static <T extends Fragment> T getInstance(Context context, Class<T> clazz, Bundle bundle) {
        T t = null;
        try {
            t = clazz.newInstance();
            t.setArguments(bundle);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            String referer = getArguments().getString(EVENT_REFERER);
            if (!TextUtils.isEmpty(referer)) {
                mEventReferer = referer;
            }
            String mode = getArguments().getString(EVENT_MODE);
            if (!TextUtils.isEmpty(mode)) {
                mEventMode = mode;
            }
            String title = getArguments().getString(EVENT_TITLE);
            if (!TextUtils.isEmpty(title)) {
                mEventTitle = title;
            }
            String url = getArguments().getString(EVENT_URL);
            if (!TextUtils.isEmpty(url)) {
                mEventUrl = url;
            }
        }
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();
        isLastVisible = false;
        hidden = false;
        isFirst = true;
        isViewDestroyed = false;
        mContext = getContext();
        mActivity = getActivity();
        LogUtils.e("onCreate--BaseFragment--" + TAG);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        isResuming = true;
        tryToChangeVisibility(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        isResuming = false;
        tryToChangeVisibility(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewDestroyed = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        setUserVisibleHintClient(isVisibleToUser);
    }

    private void setUserVisibleHintClient(boolean isVisibleToUser) {
        tryToChangeVisibility(isVisibleToUser);
        if (isAdded()) {
            // 当Fragment不可见时，其子Fragment也是不可见的。因此要通知子Fragment当前可见状态改变了。
            List<Fragment> fragments = getChildFragmentManager().getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    if (fragment instanceof BaseFragment) {
                        ((BaseFragment) fragment).setUserVisibleHintClient(isVisibleToUser);
                    }
                }
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        onHiddenChangedClient(hidden);
    }

    public void onHiddenChangedClient(boolean hidden) {
        this.hidden = hidden;
        tryToChangeVisibility(!hidden);
        if (isAdded()) {
            List<Fragment> fragments = getChildFragmentManager().getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    if (fragment instanceof BaseFragment) {
                        ((BaseFragment) fragment).onHiddenChangedClient(hidden);
                    }
                }
            }
        }
    }

    private void tryToChangeVisibility(boolean tryToShow) {
        // 上次可见
        if (isLastVisible) {
            if (tryToShow) {
                return;
            }
            if (!isFragmentVisible()) {
                onFragmentPause();
                isLastVisible = false;
            }
            // 上次不可见
        } else {
            boolean tryToHide = !tryToShow;
            if (tryToHide) {
                return;
            }
            if (isFragmentVisible()) {
                onFragmentResume(isFirst, isViewDestroyed);
                isLastVisible = true;
                isFirst = false;
            }
        }
    }

    /**
     * Fragment是否可见
     *
     * @return
     */
    public boolean isFragmentVisible() {
        if (isResuming()
                && getUserVisibleHint()
                && !hidden) {
            return true;
        }
        return false;
    }

    /**
     * Fragment 是否在前台。
     *
     * @return
     */
    private boolean isResuming() {
        return isResuming;
    }


//    /**
//     * Fragment 可见时回调
//     *
//     * @param isFirst       是否是第一次显示
//     * @param isViewDestroyed Fragment中的View是否被回收过。
//     * 存在这种情况：Fragment 的 View 被回收，但是Fragment实例仍在。
//     */
//    protected void onFragmentResume(boolean isFirst, boolean isViewDestroyed) {
//        LogUtil.e("onFragmentResume-- "  + getClass().getSimpleName());
//
//    }
//
//    /**
//     * Fragment 不可见时回调
//     */
//    protected void onFragmentPause() {
//        LogUtil.e("onFragmentPause-- "  + getClass().getSimpleName());
//
//    }

    //    private boolean isLastVisible = false;
//    private boolean hidden = false;
//    private boolean isFirst = true;
//    private boolean isResuming = false;
//    private boolean isViewDestroyed = false;
    protected void onFragmentResume(boolean isFirst, boolean isViewDestroyed) {
        if (isFirst) {
            if (isAutoSubmitViewEvent()) {
                submitViewEvent();
            }
            //MobclickAgent.onPageStart(TAG); //统计页面("MainScreen"为页面名称，可自定义)
            LogUtils.e(TAG + "--onFragmentResume");


        }
    }

    protected void onFragmentPause() {
        //MobclickAgent.onPageEnd(TAG);
        LogUtils.e(TAG + "--onFragmentPause");
    }

    public void toLogin() {
//        startActivity(new Intent(getContext(), LoginActivity.class));

    }

    public boolean isAutoSubmitViewEvent() {
        return true;
    }


    public void submitViewEvent() {
        EventHelp.submitViewEvent(getEventReferer(), getEventMode(), getEventTitle(), mEventUrl);
    }

    public void submitClickEvent(String mode,String title,String url) {
        EventHelp.submitClickEvent(getEventReferer(),mode,title,url);
    }

    public void submitClickProEvent(String mode,String title,String url,int id) {
        EventHelp.submitClickProEvent(getEventReferer(),mode,title,url,id);
    }



    public String getEventMode() {
        return mEventMode;
    }

    public String getEventTitle() {
        if (TextUtils.isEmpty(mEventTitle)) {
            return getEventMode();
        }
        return mEventTitle;
    }

    public String getEventReferer() {
        return mEventReferer;
    }


//    public void  startEventIntent(Class clazz){
//        Intent intent = new Intent(getContext() , clazz);
//        intent.putExtra(EVENT_REFERER,getEventRefererDes()+getEventMode());
//        startActivity(intent);
//    }


    public void  startEventIntent(Class clazz,String module){
        Intent intent = new Intent(getContext() , clazz);
        intent.putExtra(EVENT_REFERER,getEventRefererDes()+getEventMode()+"_"+module);
        startActivity(intent);
    }


    public String  getEventRefererDes(){
        String eventReferer = getEventReferer();
        if (TextUtils.isEmpty(eventReferer)){
            return "";
        }else {
            return eventReferer+"_";
        }
    }
}
