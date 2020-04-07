package com.common.baseview.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.lib.utils.ToastUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者:zh
 * 时间:2018/8/14 下午6:39
 * 描述:
 */
public abstract class AbsCommonFragment<T extends BasePresenter> extends BaseFragment implements IBaseView {

    public T mPresenter;
    public Unbinder mUnBinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(getLayoutId(), container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = getPresenter();
        mUnBinder = ButterKnife.bind(this, view);
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }else{
            throw new RuntimeException("mPresenter  不能为null  请初始化");
        }

    }

    @Override
    protected void onFragmentResume(boolean isFirst, boolean isViewDestroyed) {
        super.onFragmentResume(isFirst,isViewDestroyed);
        if (isFirst){
            viewCreated();
        }

    }

    public  void viewCreated(){
        initViewAndData();
    }
    public abstract int getLayoutId();
    public abstract T getPresenter();
    public abstract void initViewAndData();


    @Override
    public void loadingNetData() { }

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
    }

    @Override
    public void showErrorPage(String code ,String msg) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) mPresenter.detachView();
        if (mUnBinder != null) mUnBinder.unbind();
        super.onDestroyView();
    }

}
