package com.common.baseview.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.common.baseview.event.EventHelp;
import com.common.lib.utils.LogUtils;
import com.umeng.message.PushAgent;


/**
 * 作者:zh
 * 时间:2018/8/6 下午2:53
 * 描述:
 */
public class BaseActivity extends AppCompatActivity {
    public Activity mActivity;
    public Context mContext;
    public String TAG;

//    public String mEventReferer = "";
//    public String mEventMode = "";
//    public String mEventTitle = "";


    public String mEventReferer = "";
    public String mEventMode = "";
    public String mEventTitle = "";
    public final static String EVENT_REFERER = "EVENT_REFERER";
    public final static String EVENT_MODE = "EVENT_MODE";
    public final static String EVENT_TITLE = "EVENT_TITLE";
    public static final String EVENT_URL = "EVENT_URL";
    public String mEventUrl = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(getApplicationContext()).onAppStart();
        TAG = getClass().getSimpleName();
        initEventData();
        mActivity = this;
        mContext = this;
    }


    //
    public void initEventData() {
        String referer = "";
        String mode = "";
        String title = "";
        String url = "";
        if (getIntent() != null) {
            Uri data = getIntent().getData();
            if (data != null) {
                LogUtils.e("Uri--" + data.toString());
                referer = data.getQueryParameter(EVENT_REFERER);
                if (!TextUtils.isEmpty(referer)) {
                    mEventReferer = referer;
                }

                mode = data.getQueryParameter(EVENT_MODE);
                if (!TextUtils.isEmpty(mode)) {
                    mEventMode = mode;
                }

                title = data.getQueryParameter(EVENT_TITLE);
                if (!TextUtils.isEmpty(title)) {
                    mEventTitle = title;
                }
                url = data.getQueryParameter(EVENT_URL);
                if (!TextUtils.isEmpty(url)) {
                    mEventUrl = url;
                }
            }


            referer = getIntent().getStringExtra(EVENT_REFERER);
            if (!TextUtils.isEmpty(referer)) {
                mEventReferer = referer;
            }
            mode = getIntent().getStringExtra(EVENT_MODE);
            if (!TextUtils.isEmpty(mode)) {
                mEventMode = mode;
            }
            title = getIntent().getStringExtra(EVENT_TITLE);
            if (!TextUtils.isEmpty(title)) {
                mEventTitle = title;
            }
            url = getIntent().getStringExtra(EVENT_URL);
            if (!TextUtils.isEmpty(url)) {
                mEventUrl = url;
            }
            if (isAutoSubmitViewEvent()) {
                submitViewEvent();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
//        hideKeyboard();

        /**
         * 隐藏键盘
         */
        if (getCurrentFocus()!=null){
            toggleKeyboard(false);
        }
    }


    public void hideKeyboard() {
        toggleKeyboard(false);
    }


    public void showkeyBoard() {
        toggleKeyboard(true);
    }


    protected void toggleKeyboard(boolean isShow) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null == imm)
                return;
            if (isShow) {
                if (getCurrentFocus() != null) {
                    //有焦点打开
                    imm.showSoftInput(getCurrentFocus(), 0);
                } else {
                    //无焦点打开
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            } else {
                if (getCurrentFocus() != null) {
                    //有焦点关闭

//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
//                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    //无焦点关闭
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
//
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean isAutoSubmitViewEvent() {
        return true;
    }


    public void submitViewEvent() {
        EventHelp.submitViewEvent(getEventReferer(), getEventMode(), getEventTitle(), mEventUrl);
    }


    public void submitClickEvent(String mode, String title, String url) {
        EventHelp.submitClickEvent(getEventReferer(), mode, title, url);
    }

    public void submitClickEvent(String title, String url) {
        EventHelp.submitClickEvent(getEventReferer(), getEventMode(), title, url);
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


    public void startEventIntent(Class clazz) {

        Intent intent = new Intent(this, clazz);
        intent.putExtra(EVENT_REFERER, getEventRefererDes() + getEventMode());
        startActivity(intent);
    }

    public void startEventIntent(Intent intent) {
        intent.putExtra(EVENT_REFERER, getEventRefererDes() + getEventMode());
        startActivity(intent);
    }


    public String getEventReferer() {
        return mEventReferer;
    }

    public String getEventRefererDes() {
        String eventReferer = getEventReferer();
        if (TextUtils.isEmpty(eventReferer)) {
            return "";
        } else {
            return eventReferer + "_";
        }
    }


    /**
     * 隐藏键盘
     */
    protected void hideInput(View view) {

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null == imm)
                return;
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


