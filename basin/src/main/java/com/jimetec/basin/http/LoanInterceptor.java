package com.jimetec.basin.http;

import com.common.lib.sign.SignCore;
import com.common.lib.utils.LogUtils;
import com.jimetec.basin.event.LoanEventDataUtil;
import com.jimetec.basin.utils.LoanAppUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * @作者 zh
 * @时间 2018/8/6 下午3:47
 * @描述
 */
public class LoanInterceptor implements Interceptor {

    private SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public Response intercept(Chain chain) throws IOException {
        //得到原始的请求对象
        Request request = chain.request();
        //得到用户所使用的请求方式
        String method = request.method();
        String qTime=System.currentTimeMillis()+"";
        if("GET".equals(method)){
            HttpUrl.Builder builder = request.url().newBuilder();

//            String qTime="20180912112723";
            //加入公共参数
            Map<String, String> params=getParamsGet(request);
            params.put("deviceId", LoanAppUtils.getUtid());
            params.put("timestamp",qTime);
//            params.put("appkey", SecretConstains.APP_KEY);
            builder.addQueryParameter("deviceId", LoanEventDataUtil.getUtid());//
            builder.addQueryParameter("timestamp",qTime);
//            builder.addQueryParameter("appkey", SecretConstains.APP_KEY);
            builder.addQueryParameter("sign", SignCore.getSignString(params));
            //重新构建请求体
            request = request.newBuilder().url(builder.build()).build();
            LogUtils.e("RequestClient", request.url().toString());
        }else if("POST".equals(method)){

            if (request.body() instanceof FormBody) {
                Map<String, String> params=getParamsPost(request);
                //加入公共参数
                params.put("deviceId", LoanAppUtils.getUtid());//
                params.put("timestamp",qTime);
//            params.put("appkey", SecretConstains.APP_KEY);
                Request.Builder builder = request.newBuilder();
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    formBodyBuilder.add(entry.getKey(),entry.getValue());
                }
                //加入公共参数sign
                formBodyBuilder.add("sign",SignCore.getSignString(params));
                //重新构建请求体
                request = builder.post(formBodyBuilder.build()).build();
                String postBodyString=bodyToString(request.body());
                String fullUrl = request.url()+"?"+postBodyString;
                LogUtils.e("RequestClient", fullUrl);
            }

        }
        //重新发送请求
        return chain.proceed(request);

    }


    private Map<String, String> getParamsPost(Request request){
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

    private Map<String, String> getParamsGet(Request request){
        Map<String, String> map = new HashMap<>();
        HttpUrl httpUrl=request.url();
        for (int i=0;i<httpUrl.querySize();i++){
            //            map.put(httpUrl.queryParameterName(i), URLEncoder.encode(httpUrl.queryParameterValue(i)));
            map.put(httpUrl.queryParameterName(i),httpUrl.queryParameterValue(i));
        }
        return map;
    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null){
                copy.writeTo(buffer);
                return buffer.readUtf8();
            } else{
                return "";
            }
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
