package com.common.baseview.base;

/**
 * @作者 zh 
 * @时间 2018/8/6 下午3:02 
 * @描述 
 */
public interface BasePresenter<T extends IBaseView> {
    void  attachView(T view);
    void  detachView();
}
