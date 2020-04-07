package com.jimetec.basin.event;

import android.text.TextUtils;

import com.common.lib.utils.GsonUtil;
import com.jimetec.basin.utils.AppData;

/**
 * 作者:zh
 * 时间:2018/9/18 下午3:56
 * 描述:
 */
public class CommonsBean {
    public String udid="";
    public String phone="";
    public String ip="";

    public CommonsBean(){
        udid= LoanEventDataUtil.getUtid();

//        if (AppData.getInstance().isLogin()) {
//            if (!TextUtils.isEmpty(AppData.getInstance().getUser().phone))
                phone = AppData.getInstance().getUser().phone;
//        }
        ip = LoanEventDataUtil.getIp();

    }


    public static String getCommonsBeaan() {
        CommonsBean commonsBean = new CommonsBean();
        commonsBean.udid = LoanEventDataUtil.getUtid();
        if (AppData.getInstance().isLogin()) {
            if (!TextUtils.isEmpty(AppData.getInstance().getUser().phone))
                commonsBean.phone = AppData.getInstance().getUser().phone;
        }
        commonsBean.ip = LoanEventDataUtil.getIp();
        return  GsonUtil.toGsonString(commonsBean);
    }

    @Override
    public String toString() {
        return "CommonsBean{" +
                "udid='" + udid + '\'' +
                ", phone='" + phone + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
