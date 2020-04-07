package com.jimetec.xunji;

import com.jimetec.basin.DefaultLoanHelp;
import com.jimetec.xunji.util.VersionManager;

/**
 * 作者:capTain
 * 时间:2019-05-23 19:21
 * 描述:
 */
public class MyLoanHelp extends DefaultLoanHelp {


    @Override
    public boolean isUpdateApp() {
        return VersionManager.getInstance().isUpdateApp();
    }

    @Override
    public boolean isMustUpdate() {
        return VersionManager.getInstance().isMustUpdate();
    }

    @Override
    public String getUpgradeUrl() {
        return  VersionManager.getInstance().getUpgradeUrl();
    }
}
