package com.common.baseview.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.common.lib.utils.ToastUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者:zh
 * 时间:2018/8/14 下午6:39
 * 描述:
 */
public abstract  class AbsCommonActivity<T extends BasePresenter> extends BaseActivity implements IBaseView {

    protected T mPresenter;
    protected Unbinder mUnbinder;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        mUnbinder = ButterKnife.bind(this);
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }else{
            throw new RuntimeException("mPresenter  不能为null  请初始化");
        }
        inOncreate();
    }

    protected  void inOncreate(){
        initViewAndData();
    }

    public abstract T getPresenter();
    public abstract int getLayoutId();
    public abstract void initViewAndData();


    @Override
    public void loadingNetData() {
        showLoadingPage();
    }

    @Override
    public void showLoadingPage() {

    }

    @Override
    public void showEmptyPage() {

    }

    @Override
    public void showSuccessPage() {

    }

    @Override
    public void showErrorMsg(String code ,String msg) {
        ToastUtil.showShort(msg);
//        SnackbarUtil.show(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), msg);
    }

    @Override
    public void showErrorPage(String code,String msg) {

    }

    @Override
    public void onFinish() {}

    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.detachView();
        if (mUnbinder != null)
            mUnbinder.unbind();
        super.onDestroy();
    }




}
