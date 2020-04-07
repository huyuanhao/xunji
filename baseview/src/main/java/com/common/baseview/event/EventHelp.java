package com.common.baseview.event;


import com.common.lib.utils.GsonUtil;

/**
 * 作者:zh
 * 时间:1/23/19 8:27 PM
 * 描述:
 */
public class EventHelp {

    static EventHelpClient sClient;

    public static  void setClient(EventHelpClient client){
        sClient =client;
    }


    public static void submitViewEvent(String referer,String mode,String title,String url) {
        EventDataBean eventBean = new EventDataBean()
                .setEtype(EventDataBean.etypeView)
                .setReferer(referer)
                .setTitle(title)
                .setMode(mode)
                .setUrl(url);
        sClient.submitJsonEvent(GsonUtil.toGsonString(eventBean));
    }
    public static void submitViewEvent(String referer,String mode,String title,String url,int id) {
        EventDataBean eventBean = new EventDataBean()
                .setEtype(EventDataBean.etypeView)
                .setReferer(referer)
                .setTitle(title)
                .setMode(mode)
                .setUrl(url)
                .setProductId(id);
        sClient.submitJsonEvent(GsonUtil.toGsonString(eventBean));
    }


    public static void submitClickEvent(String referer,String mode,String title,String url) {
        EventDataBean eventBean = new EventDataBean()
                .setEtype(EventDataBean.etypeClick)
                .setReferer(referer)
                .setMode(mode)
                .setTitle(title)
                .setUrl(url);
       sClient.submitJsonEvent(GsonUtil.toGsonString(eventBean));
    }

    public static void submitClickProEvent(String referer,String mode,String title,String url,int id) {
        EventDataBean eventBean = new EventDataBean()
                .setEtype(EventDataBean.etypeClick)
                .setReferer(referer)
                .setMode(mode)
                .setTitle(title)
                .setUrl(url)
                .setProductId(id);
        sClient.submitJsonEvent(GsonUtil.toGsonString(eventBean));
    }
}
