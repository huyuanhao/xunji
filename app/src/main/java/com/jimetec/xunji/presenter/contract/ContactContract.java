package com.jimetec.xunji.presenter.contract;


import com.common.baseview.base.BasePresenter;
import com.common.baseview.base.IBaseView;
import com.jimetec.xunji.bean.ContactBean;

import java.util.List;

/**
 * 作者:zh
 * 时间:2018/6/28 下午4:03
 * 描述:
 */
public interface ContactContract {

    interface View extends IBaseView {

        void backContacts(List<ContactBean> beans);
        void backAdd(Object obj);
        void backDelete(Object obj);
        void backrealName(Object obj);
    }

    interface Presenter extends BasePresenter<View> {
        void getContacts();
        void addContact(String name ,String phone ,int type);
        void deleteContact (int id );
        void realName(String name);

    }
}
