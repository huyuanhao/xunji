package com.jimetec.basin.event;

import android.text.TextUtils;


import com.jimetec.basin.utils.AppData;

import java.io.Serializable;

public class ProdStay implements Serializable {


    public int prodId;

    public long stayTime;

    public String udid;

    public String phone;

    public String channel;


    public ProdStay() {
        udid = LoanEventDataUtil.getUtid();
        if (!TextUtils.isEmpty(AppData.getInstance().getUser().phone))
            phone = AppData.getInstance().getUser().phone;
        channel = LoanEventDataUtil.getEventChannel();
    }
}