package com.jimetec.basin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者:zh
 * 时间:2019-05-23 11:04
 * 描述:
 *
 */
public class LoanInfo implements Serializable {
    public static final String TAG = "LoanInfo";


    public int isUpgrade;//是否强制升级 1 是 2否
    public int androidVersion;//安卓版本号
    public String upgradeUrl;//安卓升级url
    //    public int appServerNumber; //app服务端版本号
    public int isOpenBlacklist;//是否打开黑名单
    public String blackForwardAdrees;//黑名单跳转地址
    public String h5HomePageUrl;//h5地址
    public long confidePopFrameTime=60000;//html5页面  离开 时间
    public long userApplistReportTime=2592000000L;//上传已安装apk时间戳
    public boolean toh5;
    public List<String> noUpgradeList;
}
