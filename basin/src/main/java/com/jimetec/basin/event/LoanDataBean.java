package com.jimetec.basin.event;

import com.common.lib.utils.GsonUtil;

/**
 * 作者:zh
 * 时间:2018/9/21 下午5:53
 * 描述:
 */
public class LoanDataBean {

    public  CommonsBean commons = new CommonsBean();
    public LoanEventBean events;

    public LoanDataBean(){
    }


    public LoanDataBean setEvents(LoanEventBean events) {
        this.events = events;
        return this;
    }

    public String toJson(){
        return GsonUtil.toGsonString(this);
    }
}
