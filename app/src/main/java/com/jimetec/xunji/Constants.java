package com.jimetec.xunji;

import com.jimetec.xunji.util.UserUtil;

public class Constants {

    /**
     * 终端名称，该名称可以根据使用方业务而定，比如可以是用户名、用户手机号等唯一标识
     *
     * 通常应该使用该名称查询对应终端id，确定该终端是否存在，如果不存在则创建，然后就可以开启轨迹上报，将上报的轨迹关联
     * 到该终端
     */
    public static final String TERMINAL_NAME = "12";

    public static final String NOTIFY_FRIEND = "CHANNEL_ID_Friend";

    /**
     * 服务id，请修改成您创建的服务的id
     *
     * 猎鹰轨迹服务，同一个开发者账号下的key可以直接使用同账号下的sid，不再需要人工绑定
     */
    public static final long SERVICE_ID = 42998;
    public static final String USER = "UserBean";
    public static final String  UPLOCATE_TIME_BEAN = "UpLocateTimeBean";
    public static final String ISFIRST = "ISFIRST";

    public static final String longitude = "longitude";
    public static final String latitude = "latitude";
    public static final String locationAddress = "locationAddress";
    public static final String BEFORE_SHOW_WELCOME = "BEFORE_SHOW_WELCOME";//曾经是否展示过欢迎页面
    public static final String USER_STATUS_VIP = "USER_STATUS_VIP";//曾经是否展示过欢迎页面

//    public static final String LOGIN_URL_USE = "https://xunji.jimetec.com/dist/userAgreement.html";//曾经是否展示过欢迎页面
//    public static final String LOGIN_URL_COST = "https://xunji.jimetec.com/dist/paymentAgreement.html";//曾经是否展示过欢迎页面


    public static final String LOGIN_URL_USE = "https://xunji.jimetec.com/dist/userAgreement.html";//曾经是否展示过欢迎页面
    public static final String LOGIN_URL_COST = "https://xunji.jimetec.com/dist/paymentAgreement.html";//曾经是否展示过欢迎页面
    public static final String AGREEMENT_URL_PRIVACY = "https://xunji.jimetec.com/dist/agreement.html";//隐私协议
    public static final String AGREEMENT_URL_USER = "https://xunji.jimetec.com/dist/userAgreement.html";//用户协议




    public static final String TEST_DEBUG = "TEST_DEBUG";//曾经是否展示过欢迎页面
    public static final String ADD_FRIENDs_HISTORY = "ADD_FRIENDs_HISTORY" + UserUtil.getUserId();//曾经是否展示过欢迎页面

    public static final String LOGIN_REFRESH_NEWS = "LOGIN_REFRESH_NEWS";//曾经是否展示过欢迎页面




}
