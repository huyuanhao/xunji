package com.jimetec.basin.http;


import com.common.lib.utils.LogUtils;
import com.jimetec.basin.bean.LoanInfo;
import com.jimetec.basin.utils.LoanUtil;

import java.net.Proxy;
import java.util.Map;
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
public class LoanClient {


    /**
     * 超时时间(秒)
     */
    public static final int DEFAULT_TIMEOUT = 30;
    public static final int CONNECT_TIMEOUT = 30;

    /**
     * 单例
     */
    private static LoanClient requestClient;

    private Retrofit mApiRetrofit;
    private LoanServer mLoanApi;

    private Retrofit mEventRetrofit;
    private LoanServer  mLoanEvent;


    private LoanClient() {
//
        OkHttpClient okHttpClient = creatBuilder().build();
        mApiRetrofit = new Retrofit.Builder()
                .baseUrl(LoanUrl.getApiBaseUrl()+"/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mLoanApi = mApiRetrofit.create(LoanServer.class);

        mEventRetrofit = new Retrofit.Builder()
                .baseUrl(LoanUrl.getEventBaseUrl()+"/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mLoanEvent = mEventRetrofit.create(LoanServer.class);
    }

    public static OkHttpClient.Builder creatBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
//        builder.retryOnConnectionFailure(false);
        //拦截器－添加公共字段
//        builder.addInterceptor(new CacheInterceptor());
        builder.addInterceptor(new LoanInterceptor());
//        builder.addNetworkInterceptor(new LoggingInterceptor());
        if (LoanUtil.isDebug()) {
            trustAll(builder);
        } else {
            builder.proxy(Proxy.NO_PROXY);
        }
        return builder;
    }

    public static LoanClient getInstance() {
        if (null == requestClient) {
            synchronized ((LoanClient.class)) {
                if (null == requestClient) {
                    requestClient = new LoanClient();
                }
            }

        }
        return requestClient;
    }


    public Flowable<LoanHttpResult<LoanInfo>> queryMarketing(String applicationid) {
        return mLoanApi.queryMarketing(applicationid);
    }


    public void prodStayTime(String data) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), data);
        flowableNoHandle(mLoanApi.prodStayTime(requestBody),"");
    }

    public void feed(Map<String, String> map) {
        flowableNoHandle(mLoanApi.feed(map),"feed");
    }



    public void submitUserAppList(String data) {
        flowableNoHandle(userApplist(data),"submitUserAppList");
    }


    public void submitScreenshot(String data) {
        flowableNoHandle(screenshot(data),"screenshot");
    }



    private Flowable<ResponseBody> userApplist(String data) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), data);
        return mLoanEvent.userApplist(requestBody);

    }

    private Flowable<ResponseBody> screenshot(String data) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), data);
        return mLoanEvent.screenshot(requestBody);

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
        return mLoanEvent.commitEvent(requestBody);
    }




    private <T> void flowableNoHandle(Flowable<T> flowable, final String type) {
        flowable.compose(LoanRxutil.<T>rxSchedulerHelper())
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


}
