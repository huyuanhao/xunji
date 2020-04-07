package com.jimetec.basin.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.common.lib.utils.LogUtils;
import com.common.lib.utils.SpUtil;
import com.jimetec.basin.bean.LoanInfo;
import com.jimetec.basin.event.LoanEventDataUtil;

/**
 * 作者:zh
 * 时间:2019-05-23 12:05
 * 描述:
 */
public class LoanSplashUtil {
    private static volatile LoanSplashUtil sSplashUtil;
    private LoanInfo mLoanInfo;

    public static LoanSplashUtil getInstance() {
        if (null == sSplashUtil) {
            synchronized (LoanSplashUtil.class) {
                if (null == sSplashUtil) {
                    sSplashUtil = new LoanSplashUtil();
                }
            }
        }
        return sSplashUtil;
    }

    /**
     **/
    public void setLoanInfo(LoanInfo loanInfo) {
        if(null != loanInfo) {
            //将user保存到本地
            this.mLoanInfo = loanInfo;
            LogUtils.e("save   user" +loanInfo.toString());
            save();
        }
    }

    public void save(){
        if(null != mLoanInfo) {
            //将user保存到本地
            String string = new Gson().toJson(mLoanInfo);
            SpUtil.putString(LoanInfo.TAG, string);
        }
    }


    /**
     * 获取信息
     *
     * @return
     */
    public LoanInfo getLoanInfo() {
        if (null != mLoanInfo) {//&& !TextUtils.isEmpty(temp.token)
            LogUtils.e("get   mLoanInfo" + mLoanInfo.toString());
            return mLoanInfo;
        }
        //判断本地用户json数据是否存在(这一步判断也是又必要的，mLoanInfo在缓存有可能被free掉，所以)
        String mLoanInfoJson = SpUtil.getString(LoanInfo.TAG);
        if (!TextUtils.isEmpty(mLoanInfoJson)) {
            LoanInfo temp = new Gson().fromJson(mLoanInfoJson, LoanInfo.class);

            if (null != temp) {//&& !TextUtils.isEmpty(temp.token)
                //登录成功
                this.mLoanInfo = temp;
            }
        }
        if (mLoanInfo == null) mLoanInfo = new LoanInfo();
        return mLoanInfo;
    }


    public boolean isUpdateApp() {
        try {
            if (mLoanInfo == null) getLoanInfo();
            //判断缓存用户是否存在
            if (null != mLoanInfo) {
                if (mLoanInfo != null) {
                    int versionCode = LoanEventDataUtil.getVersionCode();
                    if (mLoanInfo.androidVersion > versionCode) {
                        if (mLoanInfo.noUpgradeList != null && mLoanInfo.noUpgradeList.size() > 0) {
                            if (mLoanInfo.noUpgradeList.contains(LoanEventDataUtil.getEventChannel())) {
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

    public boolean isMustUpdate() {
        if (mLoanInfo == null) getLoanInfo();
        //判断缓存用户是否存在
        if (null != mLoanInfo) {
            return mLoanInfo.isUpgrade == 1;
        }
        return false;
    }



    public int getNewVersion() {
        if (mLoanInfo == null) getLoanInfo();
        if (null != mLoanInfo) {
            int versionCode = LoanEventDataUtil.getVersionCode();
            if (mLoanInfo.androidVersion > versionCode) {
                return mLoanInfo.androidVersion;
            }
        }
        return LoanEventDataUtil.getVersionCode();
    }



    public String getUpgradeUrl() {
        if (mLoanInfo == null) getLoanInfo();
        if (null != mLoanInfo) {
            return mLoanInfo.upgradeUrl;
        }
        return "";
    }



    public String geth5HomePageUrl() {
        if (mLoanInfo == null) getLoanInfo();
        if (null != mLoanInfo) {
            return mLoanInfo.h5HomePageUrl;
        }
        return "";
    }


    public long getApplistReportTime() {
        try {
            if (mLoanInfo == null) getLoanInfo();
            //判断缓存用户是否存在
            if (null != mLoanInfo) {
                if (mLoanInfo != null) {
                    if (mLoanInfo.userApplistReportTime > 0) {
                        return mLoanInfo.userApplistReportTime;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 2592000000L;
    }


    public String showBlackList() {
        if (mLoanInfo == null) getLoanInfo();
        if (null != mLoanInfo) {
            if (mLoanInfo.isOpenBlacklist == 1 && (!TextUtils.isEmpty(mLoanInfo.blackForwardAdrees))) {
                return mLoanInfo.blackForwardAdrees;
            }

        }
        return "";
    }

    public long getonfidePopFrameTime() {
        try {
            if (mLoanInfo == null) getLoanInfo();
            if (mLoanInfo != null) {
                return mLoanInfo.confidePopFrameTime;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 60000;
    }


}
