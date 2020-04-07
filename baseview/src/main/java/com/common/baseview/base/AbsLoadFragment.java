package com.common.baseview.base;

import android.view.View;
import android.view.ViewGroup;

import com.common.baseview.R;
import com.common.lib.utils.LogUtils;
import com.common.lib.utils.ToastUtil;


/**
 * 作者:zh
 * 时间:2018/8/14 下午7:47
 * 描述:
 */
public abstract class AbsLoadFragment<T extends BasePresenter> extends AbsCommonFragment<T> implements IBaseView {


    public View viewError;
    public View viewEmpty;
    public View viewLoading;
    public View baseView;
    public ViewGroup mParent;

    boolean hasSuccessed;  //只要展示过成功布局      以后其他view多失效
    public ResultStatus mCurrentState = ResultStatus.LOADING;     //   当前状态
    public int mEmptyResId;
    public int mLoadingResId;
    public int mErrorResId;


    @Override
    public void viewCreated() {
        hasSuccessed =false;
        LogUtils.e("initViewAndData");
        mEmptyResId = getEmptyResId() > 0 ? getEmptyResId() : R.layout.load_base_empty;
        mLoadingResId = getLoadingResId() > 0 ? getLoadingResId() : R.layout.load_base_loading;
        mErrorResId = getErrorResId() > 0 ? getErrorResId() : R.layout.load_base_error;
        if (getView() == null)
            return;
//        if (baseView == null)
        baseView = getView().findViewById(R.id.base_view);
        if (baseView == null) {
            throw new IllegalStateException(
                    "AbsLoadFragment子类 必须含有命名为'base_view'的View");
        }
        if (!(baseView.getParent() instanceof ViewGroup)) {
            throw new IllegalStateException(
                    "base_view's 必须在包裹在 ViewGroup里面");
        }
        mParent = (ViewGroup) baseView.getParent();
        if (viewLoading != null) {
            mParent.removeView(viewLoading);
//            LogUtils.e("removeView" + "viewLoading");
        }
        if (viewError != null) {
            mParent.removeView(viewError);
//            LogUtils.e("removeView" + "viewError");
        }
        if (viewEmpty != null) {
            mParent.removeView(viewEmpty);
//            LogUtils.e("removeView" + "viewEmpty");
        }
        View.inflate(mActivity, mLoadingResId, mParent);
        viewLoading = mParent.findViewById(R.id.view_loading);
        View.inflate(mActivity, mErrorResId, mParent);
        viewError = mParent.findViewById(R.id.view_error);
        View btError = viewError.findViewById(R.id.bt_error_again);
        if (btError!=null)
            btError.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    onFirstLoad();
                    showLoadingPage();
                    loadingNetData();
                }
            });

        View.inflate(mActivity, mEmptyResId, mParent);
        viewEmpty = mParent.findViewById(R.id.view_empty);
        hideCurrentView();
//            viewEmpty = mParent.findViewById(R.id.view_empty);
        viewLoading.setVisibility(View.VISIBLE);
//        viewLoading.setVisibility(View.VISIBLE);
        initViewAndData();
//        onFirstLoad();
        showLoadingPage();
    }

//    public void onFirstLoad(){
//        showLoadingPage();
//    }



    @Override
    public void showErrorPage(String code,String msg) {
        if (hasSuccessed&&isOnlyFirst()) return;
        if (mCurrentState == ResultStatus.ERROR)
            return;
        if (viewError == null) throw new IllegalStateException("需要一个错误的布局");
        hideCurrentView();
        mCurrentState = ResultStatus.ERROR;
        viewError.setVisibility(View.VISIBLE);
        if (viewError.getVisibility() == View.VISIBLE) {
            LogUtils.e("viewError VISIBLE");
        } else {
            LogUtils.e("viewError GONE");
        }
    }


    @Override
    public void showErrorMsg(String code ,String msg) {
        ToastUtil.showShort(msg);
    }


    @Override
    public void showLoadingPage() {
        if (hasSuccessed&&isOnlyFirst()) return;
        if (mCurrentState == ResultStatus.LOADING)
            return;
        hideCurrentView();
        mCurrentState = ResultStatus.LOADING;
        viewLoading.setVisibility(View.VISIBLE);
        if (viewLoading.getVisibility() == View.VISIBLE) {
            LogUtils.e("viewLoading VISIBLE");
        } else {
            LogUtils.e("viewLoading GONE");
        }
    }

    @Override
    public void showEmptyPage() {
        if (hasSuccessed&&isOnlyFirst()) return;
        if (mCurrentState == ResultStatus.EMPTY)
            return;
        if (viewEmpty == null) throw new IllegalStateException("需要一个空布局");
        hideCurrentView();
        mCurrentState = ResultStatus.EMPTY;
        viewEmpty.setVisibility(View.VISIBLE);
        if (viewEmpty.getVisibility() == View.VISIBLE) {
            LogUtils.e("viewEmpty VISIBLE");
        } else {
            LogUtils.e("viewEmpty GONE");
        }
    }


    @Override
    public void showSuccessPage() {
        hasSuccessed = true;
        if (mCurrentState == ResultStatus.SUCCESS)
            return;
        hideCurrentView();
        mCurrentState = ResultStatus.SUCCESS;
        baseView.setVisibility(View.VISIBLE);
//        baseView.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.page_fade_in));

        if (baseView.getVisibility() == View.VISIBLE) {
            LogUtils.e("baseView VISIBLE");
        }
    }


    private void hideCurrentView() {
        if (viewEmpty != null)
            viewEmpty.setVisibility(View.GONE);
        if (baseView != null)
            baseView.setVisibility(View.GONE);
        if (viewLoading != null){
            viewLoading.setVisibility(View.GONE);
//            viewLoading.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.page_fade_out));
        }

        if (viewError != null)
            viewError.setVisibility(View.GONE);


    }


    @Override
    public void onFinish() {

    }

    public int getEmptyResId() {
        return 0;
    }

    public int getLoadingResId() {
        return 0;
    }

    public int getErrorResId() {
        return 0;
    }




    public enum ResultStatus {
        UNDO,       // 未处理
        EMPTY,       // 数据为空
        LOADING,     // 正在加载
        ERROR,       // 加载失败
        SUCCESS      // 加载成功
    }


    public boolean  isOnlyFirst () {
        return true;
    }

}
