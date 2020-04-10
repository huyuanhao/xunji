package com.jimetec.xunji.util;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.common.baseview.event.EventDataBean;
import com.common.baseview.event.EventHeadData;
import com.common.lib.sign.AESUtil;
import com.common.lib.utils.GsonUtil;
import com.jimetec.xunji.Constants;
import com.jimetec.xunji.MainActivity;
import com.jimetec.xunji.ui.MyWebViewActivity;
import com.jimetec.xunji.ui.RegisterActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者:zh
 * 时间:4/18/19 2:12 PM
 * 描述:
 */
public class AndroidXunJS {
    public Activity mActivity;

    public AndroidXunJS(Activity context) {
        mActivity = context;
    }

    //获取用户信息
    @JavascriptInterface
    public String getUserInfo() {
        String userJson = null;
        try {
            userJson = UserUtil.getUserJson();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userJson;

    }

    @JavascriptInterface
    public String getDeviceData() {
        String json = "";
        try {
            EventHeadData eventDataBean = new EventHeadData();
            json = GsonUtil.toGsonString(eventDataBean);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    @JavascriptInterface
    public String getEventData() {
        String json = "";
        try {
//            EventHeadData eventDataBean = new e();
            EventDataBean dataBean = new EventDataBean();
            json = GsonUtil.toGsonString(dataBean);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    @JavascriptInterface
    public String getAttach(String phone) {
        String json = "";
        try {

            String attach = AESUtil.encryptAttach(phone);
            Map<String, String> map = new HashMap<>();
            map.put("attach", attach);
            json = GsonUtil.toGsonString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    @JavascriptInterface
    public void toConfirmOrder(String json) {
//        try {
//            OrderBean orderBean = GsonUtil.jsonToBean(json, OrderBean.class);
//            if (!TextUtils.isEmpty(orderBean.orderNo)) {
//                Intent intent = new Intent(mActivity, ConfirmOrderActivity.class);
//                intent.putExtra(OrderBean.TAG, orderBean);
//                mActivity.startActivity(intent);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    //去主页
    @JavascriptInterface
    public void toMainPage() {
        try {
            mActivity.startActivity(new Intent(mActivity, MainActivity.class));
            mActivity.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //位置信息
    @JavascriptInterface
    public void toLoginPage(boolean isAgree) {
        try {
            Intent intent = new Intent(mActivity, RegisterActivity.class);
            intent.putExtra(RegisterActivity.IS_AGREE_VIP, isAgree);
            mActivity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
    @JavascriptInterface
    public void setVip(boolean vip) {
        try {
            UserUtil.saveVip(vip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 

    @JavascriptInterface
    public void toWebUser() {
        MyWebViewActivity.startTo(mActivity, Constants.AGREEMENT_URL_USER, "用户协议", "用户协议");
    }


    @JavascriptInterface
    public void toWebCost() {
        String useUrl = "";
        MyWebViewActivity.startTo(mActivity, Constants.LOGIN_URL_COST, "付费会员服务协议", "付费会员服务协议");
    }


    //位置信息
    @JavascriptInterface
    public void closePage() {
        try {
            mActivity.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
