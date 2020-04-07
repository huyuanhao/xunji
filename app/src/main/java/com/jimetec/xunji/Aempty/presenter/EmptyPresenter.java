package com.jimetec.xunji.Aempty.presenter;

import android.app.Activity;

import com.jimetec.xunji.Aempty.presenter.contract.EmptyContract;
import com.jimetec.xunji.rx.RxPresenter;
/**
 * 作者:zh
 * 时间:11/27/18 4:16 PM
 * 描述:
 */
public class EmptyPresenter extends RxPresenter<EmptyContract.View> implements EmptyContract.Presenter {


    private Activity mActivity;

    public EmptyPresenter(Activity activity) {
        mActivity = activity;
    }


}
