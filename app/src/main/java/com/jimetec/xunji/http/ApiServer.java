package com.jimetec.xunji.http;


import com.jimetec.xunji.bean.ContactBean;
import com.jimetec.xunji.bean.FriendBean;
import com.jimetec.xunji.bean.HttpResult;
import com.jimetec.xunji.bean.LocationWarnBean;
import com.jimetec.xunji.bean.SplashInfo;
import com.jimetec.xunji.bean.TestUserBean;
import com.jimetec.xunji.bean.UpLocateTimeBean;
import com.jimetec.xunji.bean.UserBean;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiServer {

    @GET
    Flowable<HttpResult<Object>> demo(@Url String path);


    @POST("account/user/exist")
    @FormUrlEncoded
    Flowable<HttpResult<Object>> exist(@Field("udid") String udid);


    @POST("account/starts/start/up")
    @FormUrlEncoded
    Flowable<HttpResult<Object>> upUmengpId(@Field("umengPid") String umengPid, @Field("applicationId") String applicationId, @Field("channel") String channel);

    @POST("account/user/prepayment")
    @FormUrlEncoded
    Flowable<HttpResult<Object>> prepayment(@Field("udid") String udid);


    @POST
    @FormUrlEncoded
    Flowable<HttpResult<SplashInfo>> startup(@Url String path, @Field("applicationId") String applicationId, @Field("channel") String channel);


    //提交事件
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("account/event/info/create")
    Flowable<ResponseBody> commitEvent(@Body RequestBody route);


    @POST("account/user/starts/code")
    @FormUrlEncoded
    Flowable<HttpResult<Object>> getCode(@Field("phone") String phone, @Field("type") int type);


    @POST("account/user/authc/login")
    @FormUrlEncoded
    Flowable<HttpResult<UserBean>> login(@Field("phone") String phone, @Field("code") String code, @Field("type") int type, @Field("isTerminal") int isTerminal);

    @POST("account/user/my")
    @FormUrlEncoded
    Flowable<HttpResult<UserBean>> my(@Field("userId") String userId);


    @POST("account/lmessage/leave/message")
    @FormUrlEncoded
    Flowable<HttpResult<Object>> leaveWord(@Field("content") String content);
//    好友申请 POST
///friend/apply
//            userId
//    userPhone
//    targetUserPhone  目标好友手机

    //    我的好友申请（别人请求我添加为好友） GET
    @POST("account/user/updateLocation")
    @FormUrlEncoded
    Flowable<HttpResult<Long>> updateLocation(@Field("longitude") double longitude,
                                              @Field("latitude") double latitude,
                                              @Field("lastLocation") String lastLocation);

    //    我的好友申请（别人请求我添加为好友） GET
    @GET("friend/otherApplys")
    Flowable<HttpResult<List<FriendBean>>> otherApplys();


    //    好友申请（同意操作） POST
///friend/actionAggre
    //    我的好友申请（别人请求我添加为好友） GET
    @POST("friend/actionAggre")
    @FormUrlEncoded
    Flowable<HttpResult<Object>> actionAggre(@Field("id") long id);


    //解绑接口
    @POST("account/friends/info/rename")
    @FormUrlEncoded
    Flowable<HttpResult<Object>> rename(@Field("id") long userPhone, @Field("targetUserId") long targetUserId, @Field("targetNickName") String targetNickName);



    @GET
    Flowable<HttpResult<TestUserBean>> queryTest(@Url String  url,@Query("phonesss") String  phone);


    @GET("account/friends/info/list")
    Flowable<HttpResult<List<FriendBean>>> getFriends();


    @POST("friend/apply")
    @FormUrlEncoded
    Flowable<HttpResult<Object>> addFriend(@Field("userPhone") String userPhone, @Field("userName") String nickNmae, @Field("targetUserPhone") String targetUserPhone);

    @POST("account/friends/info/remove")
    @FormUrlEncoded
    Flowable<HttpResult<Object>> unBinderFriend(@Field("id") long id);


    @GET("account/starts/upLocateTime")
    Flowable<HttpResult<UpLocateTimeBean>> upLocateTime();


    @POST("account/user/updateIcon")
    @FormUrlEncoded
    Flowable<HttpResult<String>> updateIcon(@Field("base64Str") String base64Str);

    @POST("account/user/updateName")
    @FormUrlEncoded
    Flowable<HttpResult<String>> updateName(@Field("userName") String userName);


    @POST("account/user/updateName")
    @FormUrlEncoded
    Flowable<HttpResult<Object>> updateRealName(@Field("realName") String userName);

    /**
     * 拉取设置的好友地点提醒列表
     *
     * @param friendId
     * @return
     */
    @POST("locationRemind/getReminds")
    @FormUrlEncoded
    Flowable<HttpResult<List<LocationWarnBean>>> getLocationWarns(@Field("friendId") long friendId);


    //long friendId, String friendName, String location, double longitude, double latitude, int remindWay
    //设置地点提醒 /locationRemind/add post
    @POST("locationRemind/add")
    @FormUrlEncoded
    Flowable<HttpResult<Object>> addFriendWarn(@Field("friendId") long friendId, @Field("friendName") String friendName, @Field("remark") String remark,
                                               @Field("location") String location, @Field("longitude") double longitude,
                                               @Field("latitude") double latitude, @Field("remindWay") int remindWay
    );
//    /locationRemind/del?id=1


    //删除 /locationRemind/add post
    @POST("locationRemind/del")
    @FormUrlEncoded
    Flowable<HttpResult<Object>> deleteFriendWarn(@Field("id") long id);


    /**
     * @param name
     * @param phone
     * @param type  1 表示从好友列表选择， 2 通讯录
     * @return
     */
    //添加紧急联系人
    @POST("emergency/add")
    @FormUrlEncoded
    Flowable<HttpResult<Object>> addEmergency(@Field("emergencyName") String name, @Field("emergencyPhone") String phone, @Field("type") int type);


    @POST("emergency/emergencyList")
    @FormUrlEncoded
    Flowable<HttpResult<List<ContactBean>>> emergencyList(@Field("userId") long userId);

    @POST("emergency/remove")
    @FormUrlEncoded
    Flowable<HttpResult<Object>> deleteContact(@Field("id") long id);

    @POST("emergency/sendMess")
    @FormUrlEncoded
    Flowable<HttpResult<Object>> sendMsg(@Field("phones") String phones,@Field("realName") String realName);


    /**
     * 拉取对userId设置了地点提醒列表  Post
     *
     * @return
     */
    @POST("locationRemind/otherReminds")
    @FormUrlEncoded
    Flowable<HttpResult<List<LocationWarnBean>>> getFriendSettingWarns(@Field("userId") long id);


    /**
     * 拉取对userId设置了地点提醒列表  Post
     *
     * @returnlocationRemind
     */

//    @Headers({"xiao:xiao1;xiao2"})
    @POST("locationRemind/arrivePush")
    @FormUrlEncoded
    Flowable<HttpResult<Object>> aimRemind(@Field("id") long id,
                                           @Field("targetUserId") long targetUserId, @Field("friendName") String friendName,
                                           @Field("location") String location, @Field("remindWay") int remindWay, @Field("status") int status);

}
