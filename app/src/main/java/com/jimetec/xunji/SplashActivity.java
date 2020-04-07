package com.jimetec.xunji;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.common.baseview.base.AbsCommonActivity;
import com.common.lib.utils.SpUtil;
import com.jimetec.basin.bean.LoanInfo;
import com.jimetec.basin.event.LoanEventDataUtil;
import com.jimetec.basin.http.LoanUrl;
import com.jimetec.basin.ui.AllWebActivity;
import com.jimetec.basin.utils.LoanSplashUtil;
import com.jimetec.xunji.bean.SplashInfo;
import com.jimetec.xunji.presenter.StartPresenter;
import com.jimetec.xunji.presenter.contract.StartContract;
import com.jimetec.xunji.ui.MyWebViewActivity;
import com.jimetec.xunji.ui.WelcomeActivity;
import com.jimetec.xunji.util.VersionManager;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.List;

public class SplashActivity extends AbsCommonActivity<StartPresenter> implements StartContract.View {
    long mDelayed = 1000;

    boolean isPreVip = false;

    @Override
    public StartPresenter getPresenter() {
        return new StartPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public boolean isAutoSubmitViewEvent() {

        return false;
    }

    @Override
    public void initViewAndData() {

        mPresenter.startup(LoanEventDataUtil.getApplicationId(), LoanEventDataUtil.getEventChannel());
        if (VersionManager.getInstance().isToH5()) {
            if (SpUtil.getBoolean(Constants.ISFIRST, true)) {
                requestPermission(Permission.READ_PHONE_STATE, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE);
            } else {
                goTo(mDelayed);
            }

        } else {
            mPresenter.exitUdid();
            requestPermission(Permission.READ_PHONE_STATE, Permission.ACCESS_FINE_LOCATION,
                    Permission.ACCESS_COARSE_LOCATION, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE);
        }
    }


    private void requestPermission(String... permissions) {
        AndPermission.with(this)
                .runtime()
                .permission(permissions)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        if (VersionManager.getInstance().isToH5()) {
                            goTo(0);
                        } else {
                            goTo(mDelayed);
//                            System.exit(0);//正常退出
                        }
//                        goTo(0);
//                        toast(R.string.sss);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        if (VersionManager.getInstance().isToH5()) {
                            goTo(0);
                        } else {
                            goTo(0);
//                            System.exit(0);//正常退出
                        }

                    }
                })
                .start();
    }


    public void goTo(long delayed) {


        SpUtil.putBoolean(Constants.ISFIRST, false);
//        getLocation();
        handler.sendEmptyMessageDelayed(200, delayed);

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
//                    startActivity(new Intent(mActivity,AdDemoActivity.class));
                    startToPage();
                    break;
            }
        }
    };


    public void startToPage() {
//        Intent intent = new Intent(this, CardListActivity.class);
//        startActivity(intent);
        if (VersionManager.getInstance().isToH5()) {
            Intent intent = new Intent(this, AllWebActivity.class);
            startActivity(intent);
        } else {

            Intent intent;
//            startActivity();
//            intent = new Intent(this, TestActivity.class);

            //是否 进入过欢迎页面
            if (SpUtil.getBoolean(Constants.BEFORE_SHOW_WELCOME, false)) {
                //进入过欢迎页面
                if (isPreVip) {
                    MyWebViewActivity.startToPreVip(this);
                    finish();
                    return;
                } else {
                    intent = new Intent(this, MainActivity.class);
//                    intent = new Intent(this, TestActivity.class);
                }

            } else {
                //未进入过进入过欢迎页面
                intent = new Intent(this, WelcomeActivity.class);
                intent.putExtra(WelcomeActivity.isPreVip, isPreVip);

//                intent = new Intent(this, MainActivity.class);

            }

//            intent = new Intent(this, WelcomeActivity.class);

//            if (UserUtil.isLogined()) {
//                intent = new Intent(this, TestActivity.class);
//            } else {
//                intent = new Intent(this, RegisterActivity.class);
//            }

            startActivity(intent);
            finish();
        }
        finish();
    }


    @Override
    public void backSplashInfo(SplashInfo info) {
//        UserManager.getInstance().setUserBean(info.userInfo);
        VersionManager.getInstance().setVersionInfo(info.dataDictionary);


        try {
            String loanApiIp = info.dataDictionary.loanApiIp;
            if (!TextUtils.isEmpty(loanApiIp)) {
                SpUtil.putString(LoanUrl.PATH_API, loanApiIp);
            }

            String loanEventIp = info.dataDictionary.loanEventIp;
            if (!TextUtils.isEmpty(loanEventIp)) {
                SpUtil.putString(LoanUrl.PATH_EVENT, loanEventIp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void backLoanInfo(LoanInfo info) {
        LoanSplashUtil.getInstance().setLoanInfo(info);
    }

    @Override
    public void backExit(Object object) {
        isPreVip = true;

    }

    @Override
    public String getEventMode() {
        return "启动页";
    }


}
