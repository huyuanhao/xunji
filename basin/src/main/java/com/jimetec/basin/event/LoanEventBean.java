package com.jimetec.basin.event;


import com.common.lib.utils.GsonUtil;
import com.common.lib.utils.NetworkUtils;

/*  String url;
String etype;
String mode;
String title;
int position;//上传点击产品的位置的数字编号
int productId;//产品id，当点击特卖新品下的具体产品时上传
String inner_media;//内部媒体参数，从进入的URL链接中解析动态参数inner_media对应的取值，并记录下来，内媒时效为1个小时，即1小时内没有新的内媒访问，则清空当前cookies中记录的内媒信息
String outer_media;//外部媒体参数，从访前链接url中获取动态参数outer_media对应的值，并记录下来，外媒时效一个小时，即1小时内没有新的外媒访问，则清空当前cookies中记录的外媒信息
String REFERRER;
EventBean eventBean = new EventBean();
eventBean.etype = eventBean.etypeClick;
eventBean.title = productBean.prodName;
eventBean.mode = "首页";
eventBean.position=position;
eventBean.productId=productBean.prodId;
* */
public class LoanEventBean {

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

    public String url = "";//页面URL/对象链接URL, 浏览页面行为时上传页面URL,点击对象行为时上传对象链接URL
   
    public String efrom = LoanEventDataUtil.getEfrom();//

//    public String userid="";//用户注册id，数据库自增长id，非手机号码

    /**
     * 动作类型, 浏览页面还是点击对象
     * 浏览：view
     * 点击：click
     */
    public String etype = "";
    /**
     * 页面标题/点击对象标题：浏览页面行为时上传页面标题；
     * 点击对象行为时上传点击对象标题，命名规则为页面名称_点击对象名称
     *  
     */
    public String title = "";

    public LoanEventBean setUrl(String url) {
        this.url = url;
        return this;
    }

    public LoanEventBean setEtype(String etype) {
        this.etype = etype;
        return this;

    }

    public LoanEventBean setTitle(String title) {
        this.title = title;
        return this;
    }

    public LoanEventBean setMode(String mode) {
        this.mode = mode;
        return this;
    }

    public LoanEventBean setPosition(int position) {
        this.position = position;
        return this;
    }

    public LoanEventBean setProductId(int productId) {
        this.productId = productId;
        return this;
    }

    public LoanEventBean setInner_media(String inner_media) {
        this.innerMedia = inner_media;
        return this;
    }

    public LoanEventBean setOuter_media(String outer_media) {
        this.outerMedia = outer_media;
        return this;
    }

    public LoanEventBean setREFERRER(String REFERRER) {
        this.referrer = REFERRER;
        return this;
    }

    public LoanEventBean setRefererUrl(String refererUrl) {
        this.refererUrl = refererUrl;
        return this;
    }


    public String mode = "";
    public int position = 0;//上传点击产品的位置的数字编号
    public int productId = 0;//产品id，当点击特卖新品下的具体产品时上传，比如点击广发聪明卡对应的产品ID
    public String innerMedia = "";//内部媒体参数，从进入的URL链接中解析动态参数inner_media对应的取值，并记录下来，内媒时效为1个小时，即1小时内没有新的内媒访问，则清空当前cookies中记录的内媒信息
    public String outerMedia = "";//外部媒体参数，从访前链接url中获取动态参数outer_media对应的值，并记录下来，外媒时效一个小时，即1小时内没有新的外媒访问，则清空当前cookies中记录的外媒信息
    public String referrer = "";  //访前页面链接URL，即前一个请求的页面URL(包括动态参数)，包括外部网站
    public String refererUrl = "";  //访前页面链接URL，即前一个请求的页面URL(包括动态参数)，包括外部网站
    public String channel = LoanEventDataUtil.getEventChannel();//渠道包，如jinritoutiao、360、oppo等（ios统一上传空），数据来源调用对应客户端接口
    public String networkType = NetworkUtils.getNetworkType().getState(); //网络类型，比如移动2g、联通3g、4g、 WiFi等
    public String systemName = "android";//系统名（iPhone OS等）
    public String systemVersion = "Android " + android.os.Build.VERSION.RELEASE + " " + android.os.Build.MODEL; //系统版本，如8.4.1、4.4.4，数据来源调用对应客户端接口  +" "+ android.os.Build.BRAND
    public String productVersion = LoanEventDataUtil.getVersionName(); //终端版本号，如9.7.5，数据来源调用对应客户端接口
    public long longEventTime = System.currentTimeMillis();//当前操作时间
    public String gyro = LoanEventDataUtil.getGyro();  //用户当前角度，陀螺仪数据，例：（18,18,18）
    public int power = 0; //用户当前电量，例：18
    public int batteryStatus = 0; //当前充电状态，在充电为1，未充电为0
    public int batteryType = 0; //当前充电类型，充电器充电 1 在usb充电为2，无线充电 4
    public String mac = LoanEventDataUtil.getMac();
    public String imei = LoanEventDataUtil.getImei();
    public String androidid = LoanEventDataUtil.getAndroidId();
    public String idfa = "";
    public String applicationId = LoanEventDataUtil.getApplicationId();
    public String tag = LoanEventDataUtil.getEfrom();
    public String UMChannel = LoanEventDataUtil.getmUMChannel();


    //    mac	String	否 	events	MAC 地址， 适用于 Android 系统 （陌陌上报的为MD5加密）
//    imei	String	否	events	用户终端 IMEI， 适用于 Android 系统
//（陌陌上报的为MD5加密）
//    idfa	String	否	events	IDFA，明文。适用于 iOS 系统
    public LoanEventBean() {

        try {
            batteryStatus = LoanEventDataUtil.getPower()[0];
            power = LoanEventDataUtil.getPower()[1];
            batteryType = LoanEventDataUtil.getPower()[2];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//      Log.e("getPhoneImei ",AppPhoneMgr.getPhoneImei(this)) ;
//       Log.e("getPhoneModel ",AppPhoneMgr.getPhoneModel()) ;
//       Log.e("getDeviceInfo ",AppPhoneMgr.getDeviceInfo()) ;
//       Log.e("getSDKVersionNumber ",AppPhoneMgr.getSDKVersionNumber()+"") ;
////       Log.e("getiP ", NetworkUtils.getIpAddress(MyApplication.mContext)+"") ;
//       Log.e("getnet ", EventUtil. GetNetworkType());


    @Override
    public String toString() {
        return "EventBean{" +
                "url='" + url + '\'' +
                ", efrom='" + efrom + '\'' +
                ", etype='" + etype + '\'' +
                ", title='" + title + '\'' +
                ", mode='" + mode + '\'' +
                ", position=" + position +
                ", productId=" + productId +
                ", innerMedia='" + innerMedia + '\'' +
                ", outerMedia='" + outerMedia + '\'' +
                ", referrer='" + referrer + '\'' +
                ", refererUrl='" + refererUrl + '\'' +
                ", channel='" + channel + '\'' +
                ", networkType='" + networkType + '\'' +
                ", systemName='" + systemName + '\'' +
                ", systemVersion='" + systemVersion + '\'' +
                ", productVersion='" + productVersion + '\'' +
                ", longEventTime=" + longEventTime +
                ", gyro='" + gyro + '\'' +
                ", power=" + power +
                ", batteryStatus=" + batteryStatus +
                ", batteryType=" + batteryType +
                ", mac='" + mac + '\'' +
                ", imei='" + imei + '\'' +
                ", idfa='" + idfa + '\'' +
                '}';
    }

    public String getJson() {
        return GsonUtil.toGsonString(this);
    }
}
