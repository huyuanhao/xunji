package com.jimetec.xunji.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 作者:capTain
 * 时间:2019-07-17 11:38
 * 描述:
 */
public class TimeSortBean  extends LitePalSupport implements Serializable {


    public long times;

    public long getTimes() {
        return times;
    }
}

