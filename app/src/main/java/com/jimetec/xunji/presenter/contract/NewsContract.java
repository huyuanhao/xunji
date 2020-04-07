package com.jimetec.xunji.presenter.contract;


import com.common.baseview.base.BasePresenter;
import com.common.baseview.base.IBaseView;
import com.jimetec.xunji.bean.TimeSortBean;

import java.util.List;

/**
 * 作者:zh
 * 时间:2018/6/28 下午4:03
 * 描述:
 */
public interface NewsContract {

    interface View extends IBaseView {

        void backNews(List<TimeSortBean> pageBean);

        void backAgree(Object obj);

    }

    interface Presenter extends BasePresenter<View> {

        void getNews();
        void agree(long id);
    }
}