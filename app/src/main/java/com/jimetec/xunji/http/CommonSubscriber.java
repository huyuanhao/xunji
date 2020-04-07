package com.jimetec.xunji.http;


import android.content.Intent;
import android.text.TextUtils;

import com.common.baseview.base.IBaseView;
import com.common.lib.utils.LogUtils;
import com.common.lib.utils.NetworkUtils;
import com.common.lib.utils.Utils;
import com.jimetec.xunji.ui.RegisterActivity;
import com.jimetec.xunji.util.UserUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.subscribers.ResourceSubscriber;


public abstract class CommonSubscriber<T> extends ResourceSubscriber<T> {

    private boolean showError = true;
    private boolean ishowErrorPage = true;
    private IBaseView mIBaseView;

    public CommonSubscriber(IBaseView view) {
        mIBaseView =view;
    }

    public CommonSubscriber(IBaseView view , boolean showError) {
        mIBaseView =view;
        this.showError = showError;
    }


    protected CommonSubscriber(IBaseView view, boolean showError, boolean ishowErrorPage){
        this.mIBaseView = view;
        this.showError = showError;
        this.ishowErrorPage = ishowErrorPage;

    }

    @Override
    protected void onStart() {

        super.onStart();
    }


    @Override
    public void onNext(T t)  {

        try {
             onUINext(t);
        } catch (Exception e) {
            try {
//                LogUtils.e("onError",e.toString());
//                CrashReport.postCatchedException(new Exception(LoanException.DES_LOCAL_UI+e.getMessage(),e));
                onError(new ApiException(ApiException.STATUS_LOCAL_UI, ApiException.DES_LOCAL_UI));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
//            throw new LoanException(LoanException.STATUS_LOCAL_UI, LoanException.DES_LOCAL_UI);
        }
    }



    public abstract  void onUINext(T t);

    @Override
    public void onComplete() {
        onFinish();
    }

    @Override
    public void onError(Throwable e) {

        LogUtils.e("onError"+e.toString());
        if (e instanceof SocketTimeoutException) {
            showError("", ApiException.DES_NET_BREAK);
        } else if (e instanceof ConnectException) {
            showError("", ApiException.DES_NET_BREAK);
        } else if (e instanceof ApiException) {
            ApiException ae = (ApiException) e;
            LogUtils.e("LoanException",e.toString());

            if ("303".equalsIgnoreCase(ae.code)){
                if (UserUtil.isLogined()){
                    Intent intent = new Intent(Utils.getApp(), RegisterActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Utils.getApp().startActivity(intent);
                    UserUtil.loginOut();
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConstantAction.ACTION_LOGIN));
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    LocationUtils.getApp().startActivity(intent);


//
                }
            }

            showError(ae.code,ae.getMessage());
            if (ApiException.STATUS_TOKEN_OUTTIME.equalsIgnoreCase(ae.code)){
                if (UserUtil.isLogined()){
                    Intent intent = new Intent(Utils.getApp(), RegisterActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Utils.getApp().startActivity(intent);
                    UserUtil.loginOut();
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConstantAction.ACTION_LOGIN));
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    LocationUtils.getApp().startActivity(intent);


//
                }
            }
        } else {
            if (!NetworkUtils.isConnected()) {
                showError("", ApiException.DES_NET_BREAK);
//                return;
            }else {
                showError("", ApiException.DES_UNKOWN);
            }
        }
        onFinish();
        if (!this.isDisposed()) {
            this.dispose();
        }
    }

    // 处理因网络请求状态异常而不能关闭列表刷新状态的问题
    public void onFinish() {

        if (mIBaseView!=null)
        mIBaseView.onFinish();
    }

    public void showError(String code ,String msg){
        if (mIBaseView==null)return;
        if(TextUtils.isEmpty(msg))return;
        if (showError)
            mIBaseView.showErrorMsg(code,msg);
        if (ishowErrorPage)
            mIBaseView.showErrorPage(code,msg);
    }

}
