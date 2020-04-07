package com.jimetec.xunji.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.common.baseview.base.BaseActivity;
import com.jimetec.xunji.MyApplication;
import com.jimetec.xunji.bean.NewsBean;
import com.jimetec.xunji.rx.RxBus;
import com.jimetec.xunji.rx.event.HomeIndexEvent;
import com.jimetec.xunji.ui.MyWebViewActivity;
import com.jimetec.xunji.ui.NewDetailActivity;
import com.jimetec.xunji.ui.NewsActivity;
import com.jimetec.xunji.util.UserUtil;

public class NotificationBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MyApplication.hasGloalNews =false;
        if (intent != null) {
            NewsBean bean = (NewsBean) intent.getSerializableExtra(NewsBean.TAG);
            if (bean.type == 1) {
                bean.hasRead = 1;
                if (bean.targetUserId ==0){
                    bean.targetUserId = UserUtil.getUserId();
                }
                bean.save();
            }
            dealData(context, bean);
        }

    }


    private void dealData(Context context, NewsBean bean) {
        //1.文本消息类型  2.请求加好友    3.已成为好友通知
        Intent nfIntent;
        if (bean.type == 2) {
            nfIntent = new Intent(context, NewsActivity.class);
            nfIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

        } else if (bean.type == 3) {
//            nfIntent = new Intent(context, TestActivity.class);
//            nfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK );
//            nfIntent.putExtra(TestActivity.TAB_INDEX, 1);
            RxBus.getDefault().post(new HomeIndexEvent(1));
            return;
        } else {

            if (!TextUtils.isEmpty(bean.url)) {
//                nfIntent = new Intent();
//                nfIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                nfIntent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse(bean.url);
//                nfIntent.setData(content_url);
                MyWebViewActivity.startTo(context,bean.url,"通知栏",bean.title);
                return;

            } else {
                nfIntent = new Intent(context, NewDetailActivity.class);
                nfIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                nfIntent.putExtra(BaseActivity.EVENT_TITLE,bean.title);
                nfIntent.putExtra(NewsBean.TAG, bean);
            }
        }
        context.startActivity(nfIntent);

    }

}
