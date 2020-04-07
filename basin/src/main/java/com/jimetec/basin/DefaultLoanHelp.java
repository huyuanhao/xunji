package com.jimetec.basin;

import com.jimetec.basin.http.LoanClient;
import com.jimetec.basin.utils.LoanSplashUtil;

import java.util.Map;

/**
 * 作者:zh
 * 时间:2019-05-23 12:18
 * 描述:
 */
public class DefaultLoanHelp  implements  LoanHelp{

    @Override
    public boolean isUpdateApp() {
        return false;
//        return LoanSplashUtil.getInstance().isUpdateApp() ;
    }

    @Override
    public boolean isMustUpdate() {
//        return LoanSplashUtil.getInstance().isMustUpdate();
        return false;
    }

    @Override
    public String getUpgradeUrl() {
        return  LoanSplashUtil.getInstance().getUpgradeUrl();
    }

    @Override
    public String geth5HomePageUrl() {
        return LoanSplashUtil.getInstance().geth5HomePageUrl();
    }

    @Override
    public long getApplistReportTime() {
//        return 0;
        return LoanSplashUtil.getInstance().getApplistReportTime();
    }

    @Override
    public long getonfidePopFrameTime() {
//        return 0;
        return LoanSplashUtil.getInstance().getonfidePopFrameTime();
    }

    @Override
    public void submitGson(String json) {
        LoanClient.getInstance().submitJsonEvent(json);
    }

    @Override
    public void submitScreenshot(String json) {
        LoanClient.getInstance().submitScreenshot(json);
    }

    @Override
    public void submitUserAppList(String json) {
        LoanClient.getInstance().submitUserAppList(json);
    }

    @Override
    public void submitProdStayTime(String json) {
//        return 0;
        LoanClient.getInstance().prodStayTime(json);
    }

    @Override
    public void feed(Map<String, String> map) {
        LoanClient.getInstance().feed(map);
    }
}
