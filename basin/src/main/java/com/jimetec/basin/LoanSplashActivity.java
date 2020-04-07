package com.jimetec.basin;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jimetec.basin.bean.LoanInfo;
import com.jimetec.basin.event.LoanDataBean;
import com.jimetec.basin.event.LoanEventBean;
import com.jimetec.basin.event.LoanEventDataUtil;
import com.jimetec.basin.http.LoanClient;
import com.jimetec.basin.http.LoanHttpResult;
import com.jimetec.basin.http.LoanRxutil;
import com.jimetec.basin.ui.AllWebActivity;
import com.jimetec.basin.utils.LoanSplashUtil;

import io.reactivex.subscribers.ResourceSubscriber;

public class LoanSplashActivity extends AppCompatActivity {

    long mDelayed = 200;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    startActivity(new Intent(LoanSplashActivity.this, AllWebActivity.class));
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_loan_splash);
        init();
        LoanEventBean eventBean = new LoanEventBean();
        eventBean.etype = eventBean.etypeView;
        eventBean.mode = "启动页";
        eventBean.title = "启动页";
        LoanClient.getInstance().submitJsonEvent(new LoanDataBean().setEvents(eventBean).toJson());
    }

    private void init(){


        LoanClient.getInstance().queryMarketing(LoanEventDataUtil.getApplicationId())
       .compose(LoanRxutil.<LoanHttpResult<LoanInfo>>rxSchedulerHelper())
                .compose(LoanRxutil.<LoanInfo>handleResult())
                .subscribeWith(new ResourceSubscriber<LoanInfo>() {
                    @Override
                    public void onNext(LoanInfo loanInfo) {
                        LoanSplashUtil.getInstance().setLoanInfo(loanInfo);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        handler.sendEmptyMessageDelayed(200, mDelayed);
    }
}
