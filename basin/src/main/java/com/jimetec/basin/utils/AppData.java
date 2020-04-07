package com.jimetec.basin.utils;

import android.text.TextUtils;


import com.common.lib.utils.GsonUtil;
import com.common.lib.utils.LogUtils;
import com.common.lib.utils.SpUtil;
import com.jimetec.basin.bean.User;


/**
 * 作者:zh
 * 时间:2018/9/11 下午3:40
 * 描述:
 */
public class AppData {


    private static volatile AppData appData;

    private AppData() {
        user =getUser();
    }

    public static AppData getInstance() {
        if (null == appData) {
            synchronized (AppData.class) {
                if (null == appData) {
                    appData = new AppData();
                }
            }
        }
        return appData;
    }


    /**
     * 登录成功保存的用户信息
     **/
    private User user ;
    public void setUser(User user) {
        if(null != user) {
            //将user保存到本地
            this.user = user;
            LogUtils.e("save   user" +user.toString());
            save();
        }
    }

    public void save(){
        if(null != user) {
            //将user保存到本地
            String string = GsonUtil.toGsonString(user);
            SpUtil.putString(User.TAG, string);
        }
    }


    /**
     * 退出登录
     */
    public void logOut() {

        //清空token
        user.token = "";
        save();
    }


    /**
     * 获取用户信息
     *
     * @return
     */
    public User getUser() {

        if (null != user) {//&& !TextUtils.isEmpty(temp.token)
            LogUtils.e("get   user" +user.toString());
            return user;
        }
        //判断本地用户json数据是否存在(这一步判断也是又必要的，user在缓存有可能被free掉，所以)
        String userJson = SpUtil.getString(User.TAG);
        if (!TextUtils.isEmpty(userJson)) {
            User temp = GsonUtil.jsonToBean(userJson, User.class);

            if (null != temp ) {//&& !TextUtils.isEmpty(temp.token)
                //登录成功
                this.user = temp;
            }
        }
        if (user ==null)user = new User();
        return user;
    }


    public void setToken(String token){
        if (user ==null) {
            getUser();
        }
        user.token =token;
        save();

    }
    public boolean isLogin() {
        if (user ==null)
            getUser();
        //判断缓存用户是否存在
        if (null != user && !TextUtils.isEmpty(user.token)) {
            return true;
        }
        return false;
    }


  



}

