package com.jimetec.xunji.bean;



import com.jimetec.xunji.util.VersionManager;

import java.io.Serializable;

/**
 * 作者:zh
 * 时间:12/22/18 3:47 PM
 * 描述:
 */
public class ShareBean implements Serializable{

    public String title ="" ;
    public String content="";
    public String url="" ;
    public String phone="" ;
    public String sms="" ;

    public ShareBean() {
        title= VersionManager.getInstance().getShareTitle();
        content= VersionManager.getInstance().getShareContent();
        url= VersionManager.getInstance().getShareUrl();
//        String baseurl = SplashInfoUtil.getBaseShareUrl();
//        url =baseurl+"?invitorId="+ UserUtil.getUserId();
    }

//    public String getBaseUrl (){
//        String baseurl = SplashInfoUtil.getBaseShareUrl();
//       String shareUrl =baseurl+"?invitorId="+ UserUtil.getUserId()+"&invitorChannel="+2;
//        return shareUrl;
//    }

    @Override
    public String toString() {
        return "ShareBean{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", phone='" + phone + '\'' +
                ", sms='" + sms + '\'' +
                '}';
    }
}
