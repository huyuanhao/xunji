package com.common.baseview.event;

import android.os.Build;

import com.common.lib.utils.NetworkUtils;

/**
 * 作者:zh
 * 时间:1/18/19 10:22 AM
 * 描述:
 */
public class EventDataBean {



    /*
     *
     String url;
     String etype;
     String mode;
     String title;
     String mode;
     int position;//上传点击产品的位置的数字编号
     int productId;//产品id，当点击特卖新品下的具体产品时上传
     String inner_media;//内部媒体参数，从进入的URL链接中解析动态参数inner_media对应的取值，并记录下来，内媒时效为1个小时，即1小时内没有新的内媒访问，则清空当前cookies中记录的内媒信息
     String outer_media;//外部媒体参数，从访前链接url中获取动态参数outer_media对应的值，并记录下来，外媒时效一个小时，即1小时内没有新的外媒访问，则清空当前cookies中记录的外媒信息
     String REFERRER;
     * */

    public static final String etypeView = "view";
    public static final String etypeClick = "click";
    public String udid = "";
    public long userId;
    private String phone = "";
    
//    public String efrom = BuildConfig.DEF_EFROM;//
    public String efrom = "";//


    public String url = "";//页面URL/对象链接URL, 浏览页面行为时上传页面URL,点击对象行为时上传对象链接URL
    /**
     * 页面标题/点击对象标题：浏览页面行为时上传页面标题；
     * 点击对象行为时上传点击对象标题，命名规则为页面名称_点击对象名称
     *  
     */
    public String title = "";

    /**
     * 动作类型, 浏览页面还是点击对象
     * 浏览：view
     * 点击：click
     */
    public String etype = "";
    public String mode = "";
    public String referer = "";  //访前页面链接URL，即前一个请求的页面URL(包括动态参数)，包括外部网站

    public String innerMedia = "";//内部媒体参数，从进入的URL链接中解析动态参数inner_media对应的取值，并记录下来，内媒时效为1个小时，即1小时内没有新的内媒访问，则清空当前cookies中记录的内媒信息
    public String outerMedia = "";//外部媒体参数，从访前链接url中获取动态参数outer_media对应的值，并记录下来，外媒时效一个小时，即1小时内没有新的外媒访问，则清空当前cookies中记录的外媒信息
    public String channel = "";//渠道包，如jinritoutiao、360、oppo等（ios统一上传空），数据来源调用对应客户端接口
    public String networktype = NetworkUtils.getNetworkType().getState(); //网络类型，比如移动2g、联通3g、4g、 WiFi等
    public String systemname = "android";//系统名（iPhone OS等）
    public String systemversion = Build.VERSION.RELEASE; //系统版本，如8.4.1、4.4.4，数据来源调用对应客户端接口  +" "+ android.os.Build.BRAND

    public String productversion = EventDataUtil.getVersionName(); //终端版本号，如9.7.5，数据来源调用对应客户端接口
    public int power = 0; //用户当前电量，例：18
    public String gyo = EventDataUtil.getGyro();  //用户当前角度，陀螺仪数据，例：（18,18,18）
    public String mac = "";
    public String imei = "";
    private String deviceBrand = Build.BRAND;
    private String deviceModel = Build.MODEL;
    public String idfa = "";
    public long eventTime = System.currentTimeMillis();
    public String androidId = EventDataUtil.getAndroidId();
    public int productId = 0;//产品id
    public String msgId = "";//消息id
    public int productCode = EventDataUtil.getVersionCode();
    private int systemSdk = Build.VERSION.SDK_INT;
    public String applicationId = EventDataUtil.getApplicationId();
    public String umDeviceId = "";
    public String umDeviceToken = "";
    public String umMac = "";
    public String reallyChannel = "";
    public int batteryStatus;
    public int batteryType;


    //    mac	String	否 	events	MAC 地址， 适用于 Android 系统 （陌陌上报的为MD5加密）
//    imei	String	否	events	用户终端 IMEI， 适用于 Android 系统
//（陌陌上报的为MD5加密）
//    idfa	String	否	events	IDFA，明文。适用于 iOS 系统
    public EventDataBean() {
//        if (UserUtil.isLogined()) {
        phone = EventDataUtil.getPhone();
        userId = EventDataUtil.getUserId();
//        }
        udid = EventDataUtil.getUtid();
        imei = EventDataUtil.getImei();
        mac = EventDataUtil.getMac();
        efrom = EventDataUtil.getEfrom();

        try {
            batteryStatus = EventDataUtil.getPower()[0];
            power = EventDataUtil.getPower()[1];
            batteryType = EventDataUtil.getPower()[2];
        } catch (Exception e) {
            e.printStackTrace();
        }

        reallyChannel = EventDataUtil.getReallyChannel();
        channel = EventDataUtil.getEventChannel();
        umDeviceId = EventDataUtil.getUmDeviceId();
        umMac = EventDataUtil.getUmMac();
        gyo = EventDataUtil.getGyro();
        umDeviceToken = EventDataUtil.getUmDeviceToken();
    }

    public EventDataBean setUrl(String url) {
        this.url = url;
        return this;
    }

    public EventDataBean setTitle(String title) {
        this.title = title;

        return this;
    }

    public EventDataBean setEtype(String etype) {
        this.etype = etype;
        return this;
    }

    public EventDataBean setMode(String mode) {
        this.mode = mode;
        return this;
    }

    public EventDataBean setReferer(String referer) {
        this.referer = referer;
        return this;
    }

    public EventDataBean setMsgId(String msgId) {
        this.msgId = msgId;
        return this;
    }

    public EventDataBean setProductId(int productId) {
        this.productId = productId;
        return this;
    }


//    static double[] angle = new double[3];
//    public static void  setGyro(double x,double y,double z){
//        if (Math.abs(x - angle[0])>0.001|| Math.abs(y - angle[1])>0.001|| Math.abs(z - angle[2])>0.001){
//            angle[0]=x;
//            angle[1]=y;
//            angle[2]=z;
//            LogUtils.e("Orientation: " + angle[0] + ", " +  angle[1] + ", " +  angle[2]);
////            AppData.getInstance().setGyro("("+angle[0]+","+angle[1]+","+angle[2]+")");
//        }
//
//    }
//
//    public static String getGyro(){
//        return "("+angle[0]+","+angle[1]+","+angle[2]+")";
//    }
}
