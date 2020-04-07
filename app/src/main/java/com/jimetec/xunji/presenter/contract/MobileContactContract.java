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
public interface MobileContactContract {

    interface View extends IBaseView {
        void backAdd(ContactBean obj);
        void backDatas(List<ContactBean> datas);
    }

    interface  Presenter extends BasePresenter<View> {
        void getContacts();
        void addFriend(String nickNmae, ContactBean bean);

    }
}
