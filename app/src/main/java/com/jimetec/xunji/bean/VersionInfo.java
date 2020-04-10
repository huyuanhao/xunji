package com.jimetec.xunji.bean;

import java.util.List;

/**
 * 作者:zh
 * 时间:2019-05-23 14:22
 * 描述:
 */
public class VersionInfo {

    public static final String TAG = "VersionInfo";

    /**
     * wechat :
     * qq :
     * appDomainUrl : null
     * regulation :
     * apiUrl : null
     * imagePath : https://skt-sz.oss-cn-shenzhen.aliyuncs.com/
     * shareUrl :
     * shareTitle :
     * shareRemrak :
     * versionCode : null
     * isMust : null
     * updateDes : null
     * packageSize : null
     * creatTime : null
     * versionName : null
     * downloadUrl : null
     * applicationId : null
     * channel : null
     * iosVersionCode : null
     * iosUpdateVersion : null
     * iosUpdateText : null
     * iosForceUpdate : null
     */
    public String appDomainUrl = "";
    public String downloadUrl = "";
    public String versionName = "";
    public int versionCode;
    public int isMust;
    public boolean toh5 = false;
    public List<String> noUpgradeList;
    public List<String> noH5List;
    public String updateDes = "";
    public String packageSize = "";
    public String creatTime = "";
    public String loanApiIp = "";
    public String loanEventIp = "";
    public String qq = "";
    public String wx = "";
    public String shareTitle = "";
    public String shareContent = "";
    public String shareUrl = "";
    public String useren="";
    public String userprivate = "";
    public String userpayment = "";
    public List<MyModuleBean> information;

//"information": [{
//        "wordUrl": "https://www.baidu.com",
//                "useWord": "使用教程"
//    }],



}
