package com.jimetec.xunji.push;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.common.baseview.base.BaseActivity;
import com.common.baseview.event.EventHelp;
import com.common.lib.utils.GsonUtil;
import com.common.lib.utils.LogUtils;
import com.jimetec.xunji.MainActivity;
import com.jimetec.xunji.bean.NewsBean;
import com.jimetec.xunji.ui.MyWebViewActivity;
import com.jimetec.xunji.ui.NewDetailActivity;
import com.jimetec.xunji.util.UserUtil;
import com.umeng.message.UmengNotifyClickActivity;

import org.android.agoo.common.AgooConstants;
import org.json.JSONObject;

public class MiPushActivity extends UmengNotifyClickActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_mi_push);
        //设定一像素的activity
        Window window = getWindow();
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);
    }

    NewsBean mNewsBean;
    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        LogUtils.e("onMessage", "onMessage");
        try {
            if (intent == null) {
                //            finish();
                Bundle bun = getIntent().getExtras();
                String bunmap = GsonUtil.toGsonString(bun);
                LogUtils.e("onMessage", bunmap);
                JSONObject jsonObject = new JSONObject(bunmap);
                String josn = jsonObject.getString("mMap");
                mNewsBean = GsonUtil.jsonToBean(josn, NewsBean.class);
            } else {
                String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
                LogUtils.e("onMessage", body);
                JSONObject jsonObject = new JSONObject(body);
                String josn = jsonObject.getString("extra");
                mNewsBean = GsonUtil.jsonToBean(josn, NewsBean.class);
                startActivity(new Intent(this, MainActivity.class));
            }

            if (mNewsBean ==null ||mNewsBean.type ==0){
                if (mNewsBean!=null){
                    EventHelp.submitViewEvent("", "离线通知",mNewsBean.title, mNewsBean.url);
                }
                finish();
                return;
            }
            dealNewsBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void dealNewsBean() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (mNewsBean != null) {
                                if (mNewsBean.type == 1) {
                                    mNewsBean.hasRead = 1;
                                    if (mNewsBean.targetUserId == 0) {
                                        mNewsBean.targetUserId = UserUtil.getUserId();
                                    }
                                    mNewsBean.times = mNewsBean.timestamp;
                                    mNewsBean.save();
                                }

                                if (!TextUtils.isEmpty(mNewsBean.url)) {
                                    MyWebViewActivity.startTo(MiPushActivity.this, mNewsBean.url, "离线通知", mNewsBean.title);
                                } else {
                                    if (mNewsBean.type == 1) {
                                        Intent nfIntent = new Intent(MiPushActivity.this, NewDetailActivity.class);
                                        nfIntent.putExtra(NewsBean.TAG, mNewsBean);
                                        nfIntent.putExtra(BaseActivity.EVENT_MODE,"离线通知");
                                        nfIntent.putExtra(BaseActivity.EVENT_TITLE,mNewsBean.title);
                                        nfIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(nfIntent);
                                    }

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                }, 700);
            }
        });
    }

}
