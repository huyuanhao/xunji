package com.jimetec.xunji.util;

import android.text.TextUtils;

import com.common.lib.utils.LogUtils;
import com.common.lib.utils.SpUtil;
import com.google.gson.Gson;
import com.jimetec.xunji.BuildConfig;
import com.jimetec.xunji.Constants;
import com.jimetec.xunji.bean.MyModuleBean;
import com.jimetec.xunji.bean.UserBean;

import java.util.List;


/**
 * 作者:zh
 * 时间:12/8/18 5:21 PM
 * 描述:
 */
public class UserUtil {
    /**
     * 登录成功保存的用户信息
     **/
    private static UserBean mUserBean;

    public static String getUserPhone() {
        if (getUser() != null) {
            return mUserBean.phone;
        }
        return "";
    }

//    public static String getShareId() {
//        if (getUser() != null) {
//            return mUserBean.shareId;
//        }
//        return "";
//    }

    public static String getNickName() {
        if (getUser() != null) {
            return mUserBean.getNickName();
        }
        return "";
    }

    public static String getUserName() {
        if (getUser() != null) {
            return mUserBean.userName;
        }
        return "";
    }


    public static long getUserId() {
        if (getUser() != null) {
            return mUserBean.userId;
        }
        return 0;
    }



    public static int getUserSid() {
        if (getUser() != null) {
            return mUserBean.sid;
        }
        return 0;
    }

    public static int getUserTid() {
        if (getUser() != null) {
            return mUserBean.tid;
        }
        return 0;
    }

    public static int getUserTrid() {
        if (getUser() != null) {
            return mUserBean.trid;
        }
        return 0;
    }


//    public static String getToken() {
//        if (getUser() != null) {
//            return mUserBean.token;
//        }
//        return "";
//    }

//
//    public static String getSignToken() {
//        String signToke = "001";
//        if (getUser() != null) {
//            if (!TextUtils.isEmpty(mUserBean.token))
//                signToke = mUserBean.token;
//        }
//        return signToke;
//    }


     public static boolean isLogined() {
        if (getUser() != null && mUserBean.userId > 0) return true;
        return false;
    }


    public static boolean isVip() {
        if (UserUtil.isLogined()){
            return SpUtil.getBoolean(Constants.USER_STATUS_VIP,false);
        }
        return false;
//
//        return true;
    }


    /**
     * 获取用户信息
     *
     * @return
     */
    public static UserBean getUser() {

        if (null != mUserBean) {//&& !TextUtils.iEmpty(temp.token)
            LogUtils.e("get   user" + mUserBean.toString());
            return mUserBean;
        }
        //判断本地用户json数据是否存在(这一步判断也是又必要的，user在缓存有可能被free掉，所以)
        String userJson = SpUtil.getString(Constants.USER);
        LogUtils.e("userJson",userJson);
        if (!TextUtils.isEmpty(userJson)) {
            UserBean temp = new Gson().fromJson(userJson, UserBean.class);

            if (null != temp) {//&& !TextUtils.isEmpty(temp.token)
                //登录成功
                mUserBean = temp;
                return mUserBean;
            }
        }

        return null;
    }


    /**
     * 获取用户信息
     *
     * @return
     */
    public static String getUserJson() {


        //判断本地用户json数据是否存在(这一步判断也是又必要的，user在缓存有可能被free掉，所以)
        String userJson = null;
        try {
            userJson = SpUtil.getString(Constants.USER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userJson;
    }


    public static void setUser(UserBean user) {
        if (null != user) {
            //将user保存到本地
            mUserBean = user;
//            LogUtils.e("save   user" + user.toString());
            save();
        }
    }


    public static void save() {
        if (null != mUserBean) {
            //将user保存到本地
            LogUtils.e("save   user" + mUserBean.toString());
            String string = new Gson().toJson(mUserBean);
            SpUtil.putString(Constants.USER, string);
        }
    }


    public  static String getRealName(){
        if (UserUtil.isLogined()){
            return mUserBean.realName;
        }
        return "";
    }


    /**
     * 退出登录
     */
    public static void loginOut() {
        //清空token
        mUserBean = null;
        SpUtil.putString(Constants.USER, null);
    }


    public  static  void  saveVip(boolean vip){
        if (UserUtil.isLogined()){
            SpUtil.putBoolean(Constants.USER_STATUS_VIP, vip);
        }
    }

    public  static  boolean  isTest(){
       return  SpUtil.getBoolean(Constants.TEST_DEBUG, BuildConfig.DEBUG ) ;
    }

    public static List<MyModuleBean> getInformation() {
        if (getUser() != null) {
            return mUserBean.information;
        }
        return null;
    }


}
