package com.jimetec.basin.http;

import com.jimetec.basin.bean.LoanInfo;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 作者:zh
 * 时间:2019-05-23 10:51
 * 描述:
 */
public interface LoanServer {




    /**
     * 全局信息
     */
    @GET("upgrade/queryAndroidMarketing")
    Flowable<LoanHttpResult<LoanInfo>>  queryMarketing(@Query("name") String  name);



//    /**
//     *
//     *建议
//     */
//    @POST("proposal/save")
//    @FormUrlEncoded
//    Flowable<LoanHttpResult<Object>>  commitIdea(@FieldMap Map<String, String> map);

    //弹窗
//    feed/save
    @POST("feed/save")
    @FormUrlEncoded
    Flowable<LoanHttpResult<Object>>  feed(@FieldMap Map<String, String> map);

    //提交事件
    //eed/save
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("event/record")
    Flowable<ResponseBody> commitEvent(@Body RequestBody route);

    //提交已安装app列表
//    feed/save
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("event/userApplist")
    Flowable<ResponseBody>  userApplist(@Body RequestBody applist);

    //提交截屏时间
//    feed/save
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("event/screenshot")
    Flowable<ResponseBody>  screenshot(@Body RequestBody applist);

    //提交截屏时间
//    feed/save
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("event/stay")
    Flowable<ResponseBody>  prodStayTime(@Body RequestBody prod);




}
