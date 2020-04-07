package com.common.baseview.progress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

/**
 * @作者 zh
 * @时间 2018/8/7 下午4:11
 * @描述  控制缓冲框显示与隐藏
 */
public class ProgressDialogHandler extends Handler {

    public static final int SHOW_DIALOG = 1;

    public static final int DISMISS_DIALOG =2;


    private boolean mCancelable;

    private ProgressDialogListener mProgressDialogListener;

    private Context mContext;

//    private Dialog mDialog;
    public String des;
    private  ProgressDialog mDialog;
//    private ProgressDialog mProgressDialog;

    public ProgressDialogHandler(boolean mCancelable, ProgressDialogListener mProgressDialogListener, Context context) {
        this.mCancelable = mCancelable;
        this.mProgressDialogListener = mProgressDialogListener;
        this.mContext = context;
    }
    public ProgressDialogHandler(boolean mCancelable, ProgressDialogListener mProgressDialogListener, Context context , String des) {
        this.mCancelable = mCancelable;
        this.mProgressDialogListener = mProgressDialogListener;
        this.mContext = context;
        this.des = des;
    }


    /**
     * 用于显示Dialog
     */
    private void initProgressDialog(){
        if (mDialog == null) {
            if (TextUtils.isEmpty(des)){

                mDialog = new ProgressDialog(mContext);
                mDialog.setMessage("加载中...");
//                mDialog = new ProgressDialog(mContext);
            }else {
//                mDialog = new ProgressDialog(mContext,des);
                mDialog = new ProgressDialog(mContext);
                mDialog.setMessage(des);
            }
            mDialog.setCancelable(mCancelable);
//            mDialog.setCancelable(true);
            if (mCancelable) {
                mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        if (mProgressDialogListener!=null)
                        mProgressDialogListener.onCancelProgress();
                    }
                });
            }
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }
    }


    private void dismissProgressDialog(){
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }
}
