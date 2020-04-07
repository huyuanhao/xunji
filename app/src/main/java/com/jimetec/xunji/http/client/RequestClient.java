package com.jimetec.xunji.http.client;


import com.common.baseview.event.EventDataUtil;
import com.common.lib.utils.LogUtils;
import com.jimetec.xunji.BuildConfig;
import com.jimetec.xunji.bean.ContactBean;
import com.jimetec.xunji.bean.FriendBean;
import com.jimetec.xunji.bean.HttpResult;
import com.jimetec.xunji.bean.LocationWarnBean;
import com.jimetec.xunji.bean.SplashInfo;
import com.jimetec.xunji.bean.TestUserBean;
import com.jimetec.xunji.bean.UpLocateTimeBean;
import com.jimetec.xunji.bean.UserBean;
import com.jimetec.xunji.http.ApiServer;
import com.jimetec.xunji.http.URLs;
import com.jimetec.xunji.http.interceptor.CommonInterceptor;
import com.jimetec.xunji.http.interceptor.LoggingInterceptor;
import com.jimetec.xunji.rx.Rxutil;
import com.jimetec.xunji.util.UserUtil;

import java.net.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者:zh
 * 时间:2018/8/14 下午7:06
 * 描述:
 */
public class RequestClient {


    /**
     * 超时时间(秒)
     */
    public static final int DEFAULT_TIMEOUT = 30;
    public static final int CONNECT_TIMEOUT = 30;

    /**
     * 单例
     */
    private static RequestClient requestClient;

    private Retrofit mRetrofit;
//    private Retrofit mEventRetrofit;

    private ApiServer mApiServer;

    private RequestClient() {
//
        OkHttpClient okHttpClient = creatBuilder().build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(URLs.getApiUrlPath() + "/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mApiServer = mRetrofit.create(ApiServer.class);


    }

    public static OkHttpClient.Builder creatBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
//        builder.retryOnConnectionFailure(false);
        //拦截器－添加公共字段
//        builder.addInterceptor(new CacheInterceptor());
        builder.addInterceptor(new CommonInterceptor());
        builder.addNetworkInterceptor(new LoggingInterceptor());
        if (BuildConfig.DEBUG  || UserUtil.isTest()) {
            trustAll(builder);
        } else {
            builder.proxy(Proxy.NO_PROXY);
        }
        return builder;
    }

    public static RequestClient getInstance() {
        if (null == requestClient) {
            synchronized ((RequestClient.class)) {
                if (null == requestClient) {
                    requestClient = new RequestClient();
                }
            }

        }
        return requestClient;
    }


    public Flowable<HttpResult<UserBean>> login(String phone, String code) {
        return mApiServer.login(phone, code, 2, 1);
    }

    public Flowable<HttpResult<UserBean>> my() {
        return mApiServer.my(UserUtil.getUserId()+"");
    }

    public Flowable<HttpResult<Object>> getCode(String phone) {
        return mApiServer.getCode(phone, 2);
    }


    /**
     * 提交click  view 事件
     *
     * @param json
     */
    public void submitJsonEvent(String json) {
        LogUtils.e("submitJsonEvent", json);
        flowableNoHandle(submitEvent(json), "submitJsonEvent");
    }

    private Flowable<ResponseBody> submitEvent(String data) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), data);
        return mApiServer.commitEvent(requestBody);
//        return null;
    }

    private <T> void flowableNoHandle(Flowable<T> flowable, final String type) {
        flowable.compose(Rxutil.<T>rxSchedulerHelper())
                .subscribeWith(new ResourceSubscriber<T>() {
                    @Override
                    public void onNext(T responseBody) {
                        LogUtils.w(type + "事件提交成功");
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(type + "事件提交事件错误");
                    }

                    @Override
                    public void onComplete() {
//                        LogUtil.e(type + "onComplete");
                    }
                });
    }


    public static void trustAll(OkHttpClient.Builder builder) {
        //信任所有服务器地址
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                //设置为true
                return true;
            }
        });
        //创建管理器
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        }};

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            //为OkHttpClient设置sslSocketFactory
            builder.sslSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Flowable<HttpResult<Object>> leaveWord(String content) {

        return mApiServer.leaveWord(content);
    }

    public Flowable<HttpResult<Long>> updateLocation(double lng, double lat, String address) {

        return mApiServer.updateLocation(lng, lat, address);
    }

    public Flowable<HttpResult<List<FriendBean>>> getFriends() {
        return mApiServer.getFriends();
    }


    public Flowable<HttpResult<Object>> addFriend(String userPhone, String nickNmae, String targetUserPhone) {
        return mApiServer.addFriend(userPhone, nickNmae, targetUserPhone);
    }

    public Flowable<HttpResult<Object>> unBinderFriend(long id) {
        return mApiServer.unBinderFriend(id);
    }


    public Flowable<HttpResult<String>> updateIcon(String base64) {
        return mApiServer.updateIcon(base64);
    }

    public Flowable<HttpResult<SplashInfo>> startup(String applicationId, String channel) {
//        return mApiServer.startup(applicationId, channel);
        return mApiServer.startup(URLs.START_IP + "/account/starts/start/up", applicationId, channel);
    }


    public void demo() {
        ResourceSubscriber<Object> resourceSubscriber = mApiServer.demo("http://redis.freemanteam.cn:32768/api/a0pp/getmemberinfo").compose(Rxutil.<HttpResult<Object>>rxSchedulerHelper())
                .subscribeWith(new ResourceSubscriber<Object>() {
                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void upUmengpId(String deviceToken, String applicationId, String channel) {

        mApiServer.upUmengpId(deviceToken, applicationId, channel).compose(Rxutil.<HttpResult<Object>>rxSchedulerHelper())
                .subscribeWith(new ResourceSubscriber<Object>() {
                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public Flowable<HttpResult<Object>> exist() {

        return mApiServer.exist(EventDataUtil.getAndroidId());
    }

    public Flowable<HttpResult<List<FriendBean>>> getNews() {
        return mApiServer.otherApplys();
    }

    public Flowable<HttpResult<Object>> agree(long id) {
        return mApiServer.actionAggre(id);
    }


    public Flowable<HttpResult<Object>> rename(long id, long targetUserId, String targetNickName) {
        return mApiServer.rename(id, targetUserId, targetNickName);
    }


    public Flowable<HttpResult<TestUserBean>> queryTest(String  phone) {
        return mApiServer.queryTest("http://120.78.15.46:19013/report/active/userInfo", phone);
    }

    public Flowable<HttpResult<UpLocateTimeBean>> upLocateTime() {
        return mApiServer.upLocateTime();
    }


    public Flowable<HttpResult<String>> updateName(String userName) {
        return mApiServer.updateName(userName);
    }

    public Flowable<HttpResult<Object>> updateRealName(String userName) {
        return mApiServer.updateRealName(userName);
    }

    public Flowable<HttpResult<List<LocationWarnBean>>> getLocationWarns(long friendId) {

        return mApiServer.getLocationWarns(friendId);
    }

    public Flowable<HttpResult<Object>> addFriendWarn(long friendId, String friendName, String remark, String location, double longitude, double latitude, int remindWay) {

        return mApiServer.addFriendWarn(friendId, friendName, remark, location, longitude, latitude, remindWay);
    }


    public Flowable<HttpResult<Object>> deleteFriendWarn(long id) {
        return mApiServer.deleteFriendWarn(id);
    }

    public Flowable<HttpResult<Object>> addEmergency(String name, String phone, int type) {
        return mApiServer.addEmergency(name, phone, type);
    }

    public Flowable<HttpResult<Object>> deleteContact(int id) {
        return mApiServer.deleteContact(id);
    }


    public Flowable<HttpResult<Object>> sendMsg(String phones, String realname) {
        return mApiServer.sendMsg(phones, realname);
    }

    public Flowable<HttpResult<List<ContactBean>>> emergencyList() {
        return mApiServer.emergencyList(UserUtil.getUserId());
    }


    public Flowable<HttpResult<Object>> prepayment() {
        return mApiServer.prepayment(EventDataUtil.getAndroidId());
    }

    public Flowable<HttpResult<List<LocationWarnBean>>> getFriendSettingWarns() {

        return mApiServer.getFriendSettingWarns(UserUtil.getUserId());

    }

    public Flowable<HttpResult<Object>> aimRemind(long id, long tagetUserId, String friendName, String location, int remindWay, int status) {

        return mApiServer.aimRemind(id, tagetUserId, friendName, location, remindWay, status);
    }


//    public Flowable<HttpResult<Object>> updateName(String userName) {
//        return mApiServer.updateName(userName);
//    }
}
