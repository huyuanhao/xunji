package com.jimetec.xunji.util;

import android.text.TextUtils;

import com.common.lib.utils.LogUtils;
import com.common.lib.utils.SpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jimetec.basin.event.LoanEventDataUtil;
import com.jimetec.xunji.bean.VersionInfo;
import com.jimetec.xunji.http.URLs;

/**
 * 作者:capTain
 * 时间:2019-05-23 16:36
 * 描述:
 */
public class VersionManager {


    private static volatile VersionManager mManager;
    private VersionInfo mVersionInfo;

    public static VersionManager getInstance() {
        if (null == mManager) {
            synchronized (VersionManager.class) {
                if (null == mManager) {
                    mManager = new VersionManager();
                }
            }
        }
        return mManager;
    }

    /**
     *
     **/
    public void setVersionInfo(VersionInfo loanInfo) {

        if (null != loanInfo) {
            //将user保存到本地
            this.mVersionInfo = loanInfo;
            LogUtils.e("save   user" + loanInfo.toString());
            save();
        }
    }

    public void save() {
        if (null != mVersionInfo) {
            //将user保存到本地
            String string = new Gson().toJson(mVersionInfo);
            SpUtil.putString(VersionInfo.TAG, string);
        }
    }


    /**
     *
     * 获取信息
     * @return
     *
     *
     */
    public VersionInfo getVersionInfo() {
        if (null != mVersionInfo) {//&& !TextUtils.isEmpty(temp.token)
            LogUtils.e("get   mVersionInfo" + mVersionInfo.toString());
            return mVersionInfo;
        }
        //判断本地用户json数据是否存在(这一步判断也是又必要的，mVersionInfo在缓存有可能被free掉，所以)
        String mVersionInfoJson = SpUtil.getString(VersionInfo.TAG);
        if (!TextUtils.isEmpty(mVersionInfoJson)) {
            VersionInfo temp = new Gson().fromJson(mVersionInfoJson, VersionInfo.class);

            if (null != temp) {//&& !TextUtils.isEmpty(temp.token)
                //登录成功
                this.mVersionInfo = temp;
            }
        }
        if (mVersionInfo == null) mVersionInfo = new VersionInfo();
        return mVersionInfo;
    }


    public boolean isUpdateApp() {
        try {
            if (mVersionInfo == null) getVersionInfo();
            //判断缓存用户是否存在
            if (null != mVersionInfo) {
                if (mVersionInfo != null) {
                    int versionCode = LoanEventDataUtil.getVersionCode();
                    if (mVersionInfo.versionCode > versionCode) {
                        if (mVersionInfo.noUpgradeList != null && mVersionInfo.noUpgradeList.size() > 0) {
                            if (mVersionInfo.noUpgradeList.contains(LoanEventDataUtil.getEventChannel())) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isToH5() {
        try {
            if (mVersionInfo == null) getVersionInfo();
            //判断缓存用户是否存在
            if (mVersionInfo.toh5) {
                String channel = LoanEventDataUtil.getEventChannel();
                if (mVersionInfo.noH5List != null && mVersionInfo.noH5List.contains(channel)) {
                    return false;
                }
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public String getAppDomainUrl() {
        try {
            if (mVersionInfo == null) getVersionInfo();
            if (mVersionInfo != null) {
                if (!TextUtils.isEmpty(mVersionInfo.appDomainUrl)) {

                    return mVersionInfo.appDomainUrl;
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return URLs.appDomainUrl;
    }

    public boolean isMustUpdate() {
        if (mVersionInfo == null) getVersionInfo();
        //判断缓存用户是否存在
        if (null != mVersionInfo) {
            return mVersionInfo.isMust == 1;
        }
        return false;
    }


    public int getNewVersion() {
        if (mVersionInfo == null) getVersionInfo();
        if (null != mVersionInfo) {
            int versionCode = LoanEventDataUtil.getVersionCode();
            if (mVersionInfo.versionCode > versionCode) {
                return mVersionInfo.versionCode;
            }
        }
        return LoanEventDataUtil.getVersionCode();
    }


    public String getUpgradeUrl() {
        if (mVersionInfo == null) getVersionInfo();
        if (null != mVersionInfo) {
            return mVersionInfo.downloadUrl;
        }
        return "https://xunji.jimetec.com/xunji_V1.0.0.apk";
    }


    public String getQQ() {
        if (mVersionInfo == null) getVersionInfo();
        if (null != mVersionInfo) {
            return mVersionInfo.qq;
        }
        return "";
    }

    public String getWx() {
        if (mVersionInfo == null) getVersionInfo();
        if (null != mVersionInfo) {
            return mVersionInfo.wx;
        }
        return "";
    }



    public String getShareTitle() {
        if (mVersionInfo == null) getVersionInfo();
        if (null != mVersionInfo  &&!TextUtils.isEmpty(mVersionInfo.shareTitle)) {
            return mVersionInfo.shareTitle;
        }

        return "家人安全出行，一手掌握";
    }

    public String getShareContent() {
        if (mVersionInfo == null) getVersionInfo();
        if (null != mVersionInfo  && !TextUtils.isEmpty(mVersionInfo.shareContent)) {
            return mVersionInfo.shareContent;
        }
        return "快速定位您的位置及行为轨迹，同时对你的好友进行自动位置跟踪和轨迹回放，让你更实时关注您所关心人的位置及安全";
    }

    public String getShareUrl() {
        if (mVersionInfo == null) getVersionInfo();
        if (null != mVersionInfo  &&!TextUtils.isEmpty(mVersionInfo.shareUrl)) {
            return mVersionInfo.shareUrl;
        }
        return "https://xunji.jimetec.com/dist/down.html";
    }

}
