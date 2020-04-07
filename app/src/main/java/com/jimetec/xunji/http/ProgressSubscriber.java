package com.jimetec.xunji.http;

import android.content.Context;
import android.content.Intent;

import com.common.baseview.base.IBaseView;
import com.common.baseview.progress.ProgressDialogHandler;
import com.common.baseview.progress.ProgressDialogListener;
import com.common.lib.utils.LogUtils;
import com.common.lib.utils.NetworkUtils;
import com.common.lib.utils.Utils;
import com.jimetec.xunji.ui.RegisterActivity;
import com.jimetec.xunji.util.UserUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.subscribers.ResourceSubscriber;


public abstract class ProgressSubscriber<T> extends ResourceSubscriber<T> implements ProgressDialogListener {

    /**
     * 上下文
     */
    private Context context;
    private ProgressDialogHandler mProgressDialogHandler;
    private boolean showDialog = true;
    private boolean showError = true;
    public IBaseView mIBaseView;
    public ProgressSubscriber(Context context, IBaseView view) {
        this.context = context;
        mIBaseView = view;
        this.mProgressDialogHandler = new ProgressDialogHandler(false, this, context);
    }
    public ProgressSubscriber(Context context, IBaseView view, String des) {
        this.context = context;
        mIBaseView = view;
        this.mProgressDialogHandler = new ProgressDialogHandler(false, this, context,des);
    }


    public ProgressSubscriber(Context context, IBaseView view, boolean isCancelable) {
        this.context = context;
        mIBaseView = view;
        this.mProgressDialogHandler = new ProgressDialogHandler(isCancelable, this, context);
    }

    public ProgressSubscriber(Context context, IBaseView view, boolean isCancelable, boolean showError) {
        this.context = context;
        mIBaseView = view;
        this.mProgressDialogHandler = new ProgressDialogHandler(isCancelable, this, context);
//        this.showDialog = showDialog;
        this.showError = showError;
    }


    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }


    @Override
    public void onStart() {

        if (showDialog) {
            showProgressDialog();
        }


        super.onStart();
    }


    @Override
    public void onNext(T t)  {

        try {
            onUINext(t);
        } catch (Exception e) {
            try {
                LogUtils.e("onError",e.toString());
//                CrashReport.postCatchedException(new Exception(LoanException.DES_LOCAL_UI+e.getMessage(),e));
                onError(new ApiException(ApiException.STATUS_LOCAL_UI, ApiException.DES_LOCAL_UI));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /*@Override
    public void onNext(T t) {
        try {
            onUINext(t);
        } catch (Exception e) {
            try {
                LogUtil.e("onError",e.toString());
                CrashReport.postCatchedException(new Exception(LoanException.DES_LOCAL_UI+"----"+e.getMessage()));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
*/
    public abstract void onUINext(T t);

    @Override
    public void onComplete() {
        dismissProgressDialog();
        onFinish();
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        LogUtils.e(e);
        if (e instanceof SocketTimeoutException) {
            showError("", ApiException.DES_NET_BREAK);
        } else if (e instanceof ConnectException) {
            showError("", ApiException.DES_NET_BREAK);
        } else if (e instanceof ApiException) {
            ApiException ae = (ApiException) e;
            LogUtils.e("ApiException",e.toString());


            if ("303".equalsIgnoreCase(ae.code)){
                if (UserUtil.isLogined()){
                    Intent intent = new Intent(Utils.getApp(), RegisterActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Utils.getApp().startActivity(intent);
                    UserUtil.loginOut();
                }
            }

            showError(ae.code,ae.getMessage());
            if (ApiException.STATUS_TOKEN_OUTTIME.equalsIgnoreCase(ae.code)){
                if (UserUtil.isLogined()){
                    Intent intent = new Intent(Utils.getApp(), RegisterActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Utils.getApp().startActivity(intent);
                    UserUtil.loginOut();
                }
            }
        } else {
            if (!NetworkUtils.isConnected()) {
                showError("", ApiException.DES_NET_BREAK);
//                return;
            }else {
                showError("", ApiException.DES_UNKOWN);
            }
//            showError("",LoanException.DES_UNKOWN);
        }

        onFinish();
        if (this.isDisposed()) {
            this.dispose();
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isDisposed()) {
            this.dispose();
        }
    }

    // 处理因网络请求状态异常而不能关闭列表刷新状态的问题
    public void onFinish() {
        if (mIBaseView != null)
            mIBaseView.onFinish();
        //根据具体业务场景重写该方法以实现自己的需求
    }


    public void showError(String code ,String msg) {
        if (showError)
            if (mIBaseView != null)
                mIBaseView.showErrorMsg(code,msg);
    }
}
