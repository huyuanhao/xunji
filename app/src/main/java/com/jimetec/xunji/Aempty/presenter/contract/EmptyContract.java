package com.jimetec.xunji.Aempty.presenter.contract;

import com.common.baseview.base.BasePresenter;
import com.common.baseview.base.IBaseView;

/**
 * 作者:zh
 * 时间:2018/6/28 下午4:03
 * 描述:
 */
public interface EmptyContract {

    interface View extends IBaseView {
    }

    interface Presenter extends BasePresenter<View> {
    }
}