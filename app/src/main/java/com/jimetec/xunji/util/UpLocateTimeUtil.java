package com.jimetec.xunji.util;

import android.text.TextUtils;

import com.common.lib.utils.LogUtils;
import com.common.lib.utils.SpUtil;
import com.google.gson.Gson;
import com.jimetec.xunji.Constants;
import com.jimetec.xunji.bean.UpLocateTimeBean;


/**
 * 作者:zh
 * 时间:12/8/18 5:21 PM
 * 描述:
 */
public class UpLocateTimeUtil {
    /**
     * 登录成功保存的用户信息
     **/
    private static UpLocateTimeBean mBean;


    public static UpLocateTimeBean getInstance() {

        if (null != mBean) {//&& !TextUtils.iEmpty(temp.token)
            LogUtils.e("get   user" + mBean.toString());
            return mBean;
        }
        //判断本地用户json数据是否存在(这一步判断也是又必要的，user在缓存有可能被free掉，所以)
        String userJson = SpUtil.getString(Constants.UPLOCATE_TIME_BEAN);
        if (!TextUtils.isEmpty(userJson)) {
            UpLocateTimeBean temp = new Gson().fromJson(userJson, UpLocateTimeBean.class);

            if (null != temp) {//&& !TextUtils.isEmpty(temp.token)
                //登录成功
                mBean = temp;
                return mBean;
            }
        }

        return new UpLocateTimeBean();
    }


    /**
     * 获取用户信息
     *
     * @return
     */
    public static String getUpLocateTimeBeanJson() {


        //判断本地用户json数据是否存在(这一步判断也是又必要的，user在缓存有可能被free掉，所以)
        String userJson = null;
        try {
            userJson = SpUtil.getString(Constants.USER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userJson;
    }


    public static void setUpLocateTimeBean(UpLocateTimeBean user) {
        if (null != user) {
            //将user保存到本地
            mBean = user;
//            LogUtils.e("save   user" + user.toString());
            save();
        }
    }


    public static void save() {
        if (null != mBean) {
            //将user保存到本地
            LogUtils.e("save   UPLOCATE_TIME_BEAN " + mBean.toString());
            String string = new Gson().toJson(mBean);
            SpUtil.putString(Constants.UPLOCATE_TIME_BEAN, string);
        }
    }


 

}
