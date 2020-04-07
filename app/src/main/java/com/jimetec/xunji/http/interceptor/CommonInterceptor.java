package com.jimetec.xunji.http.interceptor;



import android.text.TextUtils;

import com.common.baseview.event.EventDataUtil;
import com.common.baseview.event.EventHeadData;
import com.common.lib.sign.MD5Util;
import com.common.lib.utils.GsonUtil;
import com.common.lib.utils.LogUtils;
import com.jimetec.xunji.util.UserUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * @作者 zh
 * @时间 2018/8/6 下午3:47
 * @描述
 */
public class CommonInterceptor implements Interceptor {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Response intercept(Chain chain) throws IOException {
//        //得到原始的请求对象
        Request request = chain.request();
//        //得到用户所使用的请求方式
        String method = request.method();
//        String qTime = System.currentTimeMillis() + "";
//        if (1==1){
////            Request original = chain.request();
//            Request.Builder requestBuilder = request.newBuilder()
//                    .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiI1IiwiVXNlck5hbWUiOiLlsI_psoHnj60iLCJpYXQiOiIxNTY1NjY0MTQ5IiwibmJmIjoiMTU2NTY2NDE0OSIsImV4cCI6MTU2NTcxMjE0OSwiaXNzIjoiRnJtRmlsbUNtcyIsImF1ZCI6WyJDbXMiLCJDbXMiXSwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9yb2xlIjoiQXBwIn0.Uf32ub2j2shwHaLmzv8qLJPsES-Y8IZyPNlraGixroU")
//                    .header("Accept", "application/json")
//                    .method(request.method(), request.body());
//
//            Request newre = requestBuilder.build();
//            return chain.proceed(newre);
//        }

        if ("GET".equals(method)) {
//
            HttpUrl.Builder builder = request.url().newBuilder();

            builder.setQueryParameter("udid", EventDataUtil.getUtid());

            if (UserUtil.getUserId() > 0) {
                builder.setQueryParameter("userId", UserUtil.getUserId() + "");
            }
            if (!TextUtils.isEmpty(UserUtil.getUserPhone())) {
                builder.setQueryParameter("phone", UserUtil.getUserPhone());
            }




            Request.Builder reqBuilder = request.newBuilder();
            addHears(reqBuilder);
            request = reqBuilder.url(builder.build()).build();
            pritlnHeader(request);
            LogUtils.e("RequestClient  GET--", request.url().toString());
        } else if ("POST".equals(method)) {
//
            if (request.body() instanceof FormBody) {
                Map<String, String> params = getParamsPost(request);
                //加入公共参数
                params.put("udid", EventDataUtil.getUtid());


                if (UserUtil.getUserId() > 0) {
                    params.put("userId", UserUtil.getUserId() + "");
                }

                if (!TextUtils.isEmpty(UserUtil.getUserPhone())) {
                    params.put("phone", UserUtil.getUserPhone());
                }


                Request.Builder builder = request.newBuilder();
                addHears(builder);
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    formBodyBuilder.add(entry.getKey(), entry.getValue());
                }
                //重新构建请求体
                request = builder.post(formBodyBuilder.build()).build();
                pritlnHeader(request);
                String postBodyString = bodyToString(request.body());
                String fullUrl = request.url() + "?" + postBodyString;
                LogUtils.e("RequestClient POST--FormBody--", fullUrl);
            } else {
                Request.Builder builder = request.newBuilder();
                addHears(builder);
                pritlnHeader(request);
                request = builder.build();
                LogUtils.e("RequestClient POST--", request.url().toString());
            }
        }
//        //重新发送请求
       return chain.proceed(request);
    }


    private Map<String, String> getParamsPost(Request request) {
        Map<String, String> map = new HashMap<>();
        if (request.body() instanceof FormBody) {
            FormBody body = (FormBody) request.body();
            for (int i = 0; i < body.size(); i++) {
//                map.put(body.encodedName(i), body.encodedValue(i));
                map.put(body.name(i), body.value(i));
            }
        }
        return map;
    }


    private Map<String, String> getParamsMultipart(Request request) {
        Map<String, String> map = new HashMap<>();
        if (request.body() instanceof MultipartBody) {
            MultipartBody body = (MultipartBody) request.body();
            List<MultipartBody.Part> parts = body.parts();

            for (int i = 0; i < body.size(); i++) {

//                map.put(body.encodedName(i), body.encodedValue(i));
//                map.put(body.name(i), body.value(i));
            }
        }
        return map;
    }


    private Map<String, String> getParamsGet(Request request) {
        Map<String, String> map = new HashMap<>();
        HttpUrl httpUrl = request.url();
        for (int i = 0; i < httpUrl.querySize(); i++) {
            //            map.put(httpUrl.queryParameterName(i), URLEncoder.encode(httpUrl.queryParameterValue(i)));
            map.put(httpUrl.queryParameterName(i), httpUrl.queryParameterValue(i));
        }
        return map;
    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null) {
                copy.writeTo(buffer);
                return buffer.readUtf8();
            } else {
                return "";
            }
        } catch (final IOException e) {
            return "did not work";
        }
    }

//
//    public String getSign(long timestamp) {
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("udid").append("=").append(LoanEventDataUtil.getUtid())
//                .append("&").append("timestamp").append("=").append(timestamp)
//                .append("&").append("token").append("=").append(UserUtil.getSignToken())
//                .append("&").append(MD5Util.MD5_SALT);
//        try {
//            String before = sb.toString();
//            LogUtils.e("sign before",before);
//            String after = MD5Util.encrypt32(before);
//            LogUtils.e("sign after",after);
//
//
//            return after;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//
    public void addHears(Request.Builder builder) {
        long timestamp = System.currentTimeMillis();
        builder.addHeader("u_udid", EventDataUtil.getUtid() + "");
        builder.addHeader("u_timestamp", timestamp+"");
        builder.addHeader("u_sign", getSign(timestamp));
//        builder.addHeader("u_token", UserUtil.getSignToken());
        builder.addHeader("u_device", GsonUtil.toGsonString(new EventHeadData()));

        if (UserUtil.getUserId() > 0) {
            builder.addHeader("u_userId", UserUtil.getUserId() + "");
        }

        if (!TextUtils.isEmpty(UserUtil.getUserPhone())) {
            builder.addHeader("u_phone", UserUtil.getUserPhone());
        }
    }




    public String getSign(long timestamp) {

        StringBuilder sb = new StringBuilder();
        sb.append("udid").append("=").append(EventDataUtil.getUtid())
                .append("&").append("timestamp").append("=").append(timestamp)
//                .append("&").append("token").append("=").append(UserUtil.getSignToken())
                .append("&").append(MD5Util.MD5_SALT);
        try {
            String before = sb.toString();
            LogUtils.e("sign before",before);
            String after = MD5Util.encrypt32(before);
            LogUtils.e("sign after",after);
            return after;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }



    //
    public void pritlnHeader(Request request) {
        StringBuilder sb = new StringBuilder();

        Headers requestHeaders = request.headers();
        int requestHeadersLength = requestHeaders.size();
        for (int i = 0; i < requestHeadersLength; i++) {
            String headerName = requestHeaders.name(i);
            String headerValue = requestHeaders.get(headerName);
            sb.append(headerName + ":" + headerValue + "\n");
        }
        LogUtils.e("RequestClient ---  head start ---\n" + sb.toString()
                + "\n---  head end  ---");
    }


}
