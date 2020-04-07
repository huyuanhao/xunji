package com.jimetec.basin.event;



import com.jimetec.basin.bean.ProductBean;

import java.io.Serializable;

/**
 * 作者:zh
 * 时间:2018/9/21 下午5:53
 * 描述:
 */
public class ProdBean implements Serializable{


    public static final String TAG = "ProdBean";
    public ProductBean prod;
    public String refererUrl;
    public String referer;
    public String phone;
}
