package com.jimetec.xunji;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.Toast;
import com.common.baseview.event.EventDataUtil;
import com.common.baseview.event.EventHelp;
import com.common.baseview.event.EventHelpClient;
import com.common.lib.utils.GsonUtil;
import com.common.lib.utils.LogUtils;
import com.common.lib.utils.SpUtil;
import com.common.lib.utils.Utils;
import com.jimetec.basin.event.LoanEventDataUtil;
import com.jimetec.basin.utils.LoanConstant;
import com.jimetec.basin.utils.LoanUtil;
import com.jimetec.xunji.bean.NewsBean;
import com.jimetec.xunji.http.client.RequestClient;
import com.jimetec.xunji.push.NotificationBroadcast;
import com.jimetec.xunji.rx.RxBus;
import com.jimetec.xunji.rx.event.HomeIndexEvent;
import com.jimetec.xunji.ui.MyWebViewActivity;
import com.jimetec.xunji.util.UserUtil;
import com.meituan.android.walle.WalleChannelReader;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;

import org.android.agoo.xiaomi.MiPushRegistar;
import org.litepal.LitePal;

//import com.facebook.stetho.Stetho;

/**
 * 作者:capTain
 * 时间:2019-06-19 10:20
 * 描述:
 */
public class MyApplication extends Application {

    public static boolean hasGloalNews = false;
    public static boolean hasFriendNews = false;
    public static String freeVipDes;

    @Override
    public void onCreate() {
        super.onCreate();

//        CnCityDict
//        Stetho.initializeWithDefaults(this);
        Utils.init(this);
        initLog();
        LitePal.initialize(this);
        initEvenData();
        LoanUtil.setLoanHelp(new MyLoanHelp());
        EventHelp.setClient(new EventHelpClient() {
            @Override
            public void submitJsonEvent(String gson) {
                RequestClient.getInstance().submitJsonEvent(gson);
            }
        });

        initUMeng();
        initCrash();

        //推送不能设置 否则 推送接受不到
        if (UserUtil.isTest()) {
            LogUtils.e("test");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }

//
//        //定义前台服务的默认样式。即标题、描述和图标
//        ForegroundNotification foregroundNotification = new ForegroundNotification("测试","描述", R.mipmap.ic_launcher,
//                //定义前台服务的通知点击事件
//                new ForegroundNotificationClickListener() {
//                    @Override
//                    public void foregroundNotificationClick(Context context, Intent intent) {
//                        Log.e("live","foregroundNotificationClick");
//
//                    }
//                });
//        //启动保活服务
//        KeepLive.startWork(this, KeepLive.RunMode.ENERGY, foregroundNotification,
//                //你需要保活的服务，如socket连接、定时任务等，建议不用匿名内部类的方式在这里写
//                new KeepLiveService() {
//                    /**
//                     * 运行中
//                     * 由于服务可能会多次自动启动，该方法可能重复调用
//                     */
//                    @Override
//                    public void onWorking() {
//                        Log.e("live","onWorking");
//                    }
//                    /**
//                     * 服务终止
//                     * 由于服务可能会被多次终止，该方法可能重复调用，需同onWorking配套使用，如注册和注销broadcast
//                     */
//                    @Override
//                    public void onStop() {
//                        Log.e("live","onStop");
//
//                    }
//                }
//        );
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public void initEvenData() {


        String channel = SpUtil.getString(LoanConstant.CHANNEL);
        if (TextUtils.isEmpty(channel)) {
            channel = WalleChannelReader.getChannel(this, BuildConfig.DEf_CHANNEL);
            SpUtil.putString(LoanConstant.CHANNEL, channel);
        }

        String mUMChannel = BuildConfig.DEF_EFROM + "_" + channel;

        String mEventChannel;
        if (channel.contains("c_")) {
            mEventChannel = channel.replace("c_", "");
        } else {
            mEventChannel = channel;
        }
        LoanEventDataUtil.setEfrom(BuildConfig.DEF_EFROM);
        LoanEventDataUtil.setApplicationId(BuildConfig.APPLICATION_ID);
        LoanEventDataUtil.setEfrom(BuildConfig.DEF_EFROM);
        LoanEventDataUtil.setVersionCode(BuildConfig.VERSION_CODE);
        LoanEventDataUtil.setVersionName(BuildConfig.VERSION_NAME);
        LoanEventDataUtil.setReallyChannel(channel);
        LoanEventDataUtil.setmUMChannel(mUMChannel);
//        LoanEventDataUtil.setPhone(UserUtil.getUserPhone());
//        LoanEventDataUtil.setUserId(UserUtil.getUserId());


        EventDataUtil.setApplicationId(BuildConfig.APPLICATION_ID);
        EventDataUtil.setEfrom(BuildConfig.DEF_EFROM);
        EventDataUtil.setVersionCode(BuildConfig.VERSION_CODE);
        EventDataUtil.setVersionName(BuildConfig.VERSION_NAME);
        EventDataUtil.setReallyChannel(channel);
        EventDataUtil.setmUMChannel(mUMChannel);
        EventDataUtil.setPhone(UserUtil.getUserPhone());
        EventDataUtil.setUserId(UserUtil.getUserId());


        String[] testDeviceInfo = UMConfigure.getTestDeviceInfo(this);
        for (int i = 0; i < testDeviceInfo.length; i++) {
            if (i == 0) {
                LoanEventDataUtil.setUmDeviceId(testDeviceInfo[i]);
            }

            if (i == 1) {
                LoanEventDataUtil.setUmMac(testDeviceInfo[i]);
            }
        }


    }

    private void initCrash() {
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setAppChannel(LoanEventDataUtil.getmUMChannel());
//        ...在这里设置strategy的属性，在bugly初始化时传入
        CrashReport.initCrashReport(this, "7c5e8a5bac", false, strategy);

    }


    private void initUMeng() {
//        if (1==1)return;

        UMConfigure.init(this, "5e6c3930895cca3b57000015", LoanEventDataUtil.getmUMChannel(), UMConfigure.DEVICE_TYPE_PHONE, "91ad0453b6222b48d3b93bf99d821679");
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        PlatformConfig.setWeixin("wxa286690826455621", "a3d6cfdd3166a56442b972bfc026c2df");
        PushAgent mPushAgent = PushAgent.getInstance(this);

        MiPushRegistar.register( this,"02882303761518152296", "56718152582960");
        /**
         * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */


        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {

            @Override
            public void launchApp(Context context, UMessage msg) {
                super.launchApp(context, msg);
            }

            @Override
            public void openUrl(Context context, UMessage msg) {
                super.openUrl(context, msg);
                try {
                    MyWebViewActivity.startTo(Utils.getApp(),msg.url,"离线消息",msg.title);
//                    MyWebViewActivity.startTo(context,msg.url,"通知栏",msg.title);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void openActivity(Context context, UMessage msg) {
                super.openActivity(context, msg);
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        };
        //使用自定义的NotificationHandler
        mPushAgent.setNotificationClickHandler(notificationClickHandler);



        UmengMessageHandler messageHandler = new UmengMessageHandler() {



            /**
             * 通知的回调方法（通知送达时会回调）
             */
            @Override
            public void dealWithNotificationMessage(Context context, UMessage msg) {
                LogUtils.e("dealWithNotificationMessage",msg.toString());
                //调用super，会展示通知，不调用super，则不展示通知。
                super.dealWithNotificationMessage(context, msg);
            }

            /**
             * 自定义消息的回调方法
             */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {

                LogUtils.e("dealWithCustomMessage", GsonUtil.toGsonString(msg));
                hasGloalNews = true;

                String json = GsonUtil.toGsonString(msg.extra);
                LogUtils.e("dealWithCustomMessage",msg.extra);

                NewsBean bean = GsonUtil.jsonToBean(json, NewsBean.class);
                bean.times = bean.timestamp;
                bean.msgId = msg.msg_id;
//                bean.targetUserId=0;
                if (bean.targetUserId == 0) {
                    bean.targetUserId = UserUtil.getUserId();
                }

                if (bean.type == 2 || bean.type == 3) {
                    if (bean.type == 3) {
                        hasGloalNews = false;
                    }
                    if (bean.type == 2) {
                        hasFriendNews = true;
                    }
                    RxBus.getDefault().post(new HomeIndexEvent(-1));
                } else {
                    bean.save();
                }

                if (bean.targetUserId == UserUtil.getUserId()) {
                    createNotification(bean);
                }
//
            }
        };

        mPushAgent.setMessageHandler(messageHandler);




        //注册推送服务 每次调用register都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                EventDataUtil.setUmDeviceToken(deviceToken);
                RequestClient.getInstance().upUmengpId(deviceToken, LoanEventDataUtil.getApplicationId(), LoanEventDataUtil.getEventChannel());
                LogUtils.e("deviceToken", "device token: " + deviceToken);
//                sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.e("deviceToken", "register failed: " + s + " " + s1);
//                sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }
        });

//        initnotify();

    }

    public void initnotify() {
        NewsBean bean1 = new NewsBean();
        bean1.id = 3;
        bean1.type = 3;
        bean1.title = "已成为好友通知";
        createNotification(bean1);
//
//
//        NewsBean bean2 = new NewsBean();
//        bean2.id=2;
//
//        bean2.type = 2;
//        bean2.title = "请求加好友";
//        createNotification(bean2);
//
//
//        NewsBean bean3 = new NewsBean();
//        bean3.id=1;
//        bean3.type = 1;
//        bean3.url="";
//        bean3.title = "url为空";
//        createNotification(bean3);
//
//        NewsBean bean4 = new NewsBean();
//        bean4.id=4;
//        bean4.type = 1;
//        bean4.title = "url";
//        createNotification(bean4);

    }


    private void createNotification(NewsBean bean) {


        Notification.Builder builder;
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Constants.NOTIFY_FRIEND, "通知", NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.enableLights(true);
            channel.enableVibration(true);
            long[] pattern = new long[]{0, 200, 200, 500};
            channel.setVibrationPattern(pattern);
//            channel.setSound(null, null);


//            notification.vibrate=pattern;
//            builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);


            nm.createNotificationChannel(channel);
            builder = new Notification.Builder(Utils.getApp(), Constants.NOTIFY_FRIEND);


        } else {
            builder = new Notification.Builder(Utils.getApp());
        }

        //1.文本消息类型  2.请求加好友    3.已成为好友通知
        Intent nfIntent = new Intent(this, NotificationBroadcast.class);
        nfIntent.putExtra(NewsBean.TAG, bean);
//        if (bean.type == 2) {
//            nfIntent = new Intent(this, NewsActivity.class);
//            nfIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        } else if (bean.type == 3) {
//            nfIntent = new Intent(this, TestActivity.class);
//            nfIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            nfIntent.putExtra(TestActivity.TAB_INDEX, 1);
//        } else {
//
//            if (!TextUtils.isEmpty(bean.url)) {
//                nfIntent = new Intent();
//                nfIntent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse(bean.url);
//                nfIntent.setData(content_url);
//            } else {
//                nfIntent = new Intent(this, NewDetailActivity.class);
//                nfIntent.putExtra(NewsBean.TAG, bean);
//            }
//
//        }
        long[] pattern = new long[]{0, 800, 500, 1000};

        builder.setVibrate(pattern);
        builder.setContentIntent(PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(), nfIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.mipmap.app_icon)
                .setAutoCancel(true)
                .setContentTitle(bean.title)
                .setContentText(bean.text);
        Notification notification = builder.build();


//        NotificationManager mNotifyManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        int times = 1111;
        try {
            times = (int) bean.times;
        } catch (Exception e) {
            e.printStackTrace();
        }
        nm.notify(times, notification);
//        return notification;
    }


    private void initLog() {
        LogUtils.Config config = LogUtils.getConfig()
                .setLogSwitch(isLogging())// 设置 log 总开关，包括输出到控制台和文件，默认开
                .setConsoleSwitch(isLogging())// 设置是否输出到控制台开关，默认开
                .setGlobalTag("xunji")// 设置 log 全局标签，默认为空
                // 当全局标签不为空时，我们输出的 log 全部为该 tag，
                // 为空时，如果传入的 tag 为空那就显示类名，否则显示 tag
                .setLogHeadSwitch(true)// 设置 log 头信息开关，默认为开
                .setLog2FileSwitch(isLogging())// 打印 log 时是否存到文件的开关，默认关
                .setDir("")// 当自定义路径为空时，写入应用的/cache/log/目录中
                .setFilePrefix("xunji")// 当文件前缀为空时，默认为"util"，即写入文件为"util-yyyy-MM-dd.txt"
                .setBorderSwitch(true)// 输出日志是否带边框开关，默认开
                .setSingleTagSwitch(true)// 一条日志仅输出一条，默认开，为美化 AS 3.1 的 Logcat
                .setConsoleFilter(LogUtils.V)// log 的控制台过滤器，和 logcat 过滤器同理，默认 Verbose
                .setFileFilter(LogUtils.V)// log 文件过滤器，和 logcat 过滤器同理，默认 Verbose
                .setStackDeep(1)// log 栈深度，默认为 1
                .setStackOffset(0)// 设置栈偏移，比如二次封装的话就需要设置，默认为 0
                .setSaveDays(3);// 设置日志可保留天数，默认为 -1 表示无限时长
        // 新增 ArrayList 格式化器，默认已支持 Array, Throwable, Bundle, Intent 的格式化输出
        LogUtils.i(config.toString());
//        LogUtilss.i(config.toString())
//        LogUtilss.e(ProcessUtils.getCurrentProcessName())
    }


    public boolean isLogging() {
//        return false;
        return BuildConfig.DEBUG || UserUtil.isTest();
    }


    public void initDouyin() {

//        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
//        TTAdSdk.init(this,
//                new TTAdConfig.Builder()
//                        .appId("5039628")
////                        .appId("5001121")
//                        .useTextureView(false) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
//                        .appName(getResources().getString(R.string.app_name))
//                        .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
//                        .allowShowNotify(true) //是否允许sdk展示通知栏提示
//                        .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
////                        .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
//                        .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_MOBILE) //允许直接下载的网络状态集合
//                        .supportMultiProcess(true) //是否支持多进程，true支持
////                        .httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
//                        .build());

    }

}

