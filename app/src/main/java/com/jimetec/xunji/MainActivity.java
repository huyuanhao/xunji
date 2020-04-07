package com.jimetec.xunji;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.ErrorCode;
import com.amap.api.track.OnTrackLifecycleListener;
import com.amap.api.track.TrackParam;
import com.amap.api.track.query.entity.LocationMode;
import com.common.baseview.base.AbsCommonActivity;
import com.common.baseview.dialog.CommonDialogFragment;
import com.common.lib.utils.AppUtils;
import com.common.lib.utils.LogUtils;
import com.common.lib.utils.SpUtil;
import com.common.lib.utils.ToastUtil;
import com.common.lib.utils.UIUtil;
import com.common.lib.utils.Utils;
import com.jaeger.library.StatusBarUtil;
import com.jimetec.basin.ui.DownloadapkService;
import com.jimetec.xunji.bean.FriendBean;
import com.jimetec.xunji.bean.LocationWarnBean;
import com.jimetec.xunji.bean.TempLocationBean;
import com.jimetec.xunji.bean.UpLocateTimeBean;
import com.jimetec.xunji.map.SimpleOnTrackLifecycleListener;
import com.jimetec.xunji.presenter.MainPresenter;
import com.jimetec.xunji.presenter.contract.MainContract;
import com.jimetec.xunji.rx.RxBus;
import com.jimetec.xunji.rx.event.HomeIndexEvent;
import com.jimetec.xunji.ui.CareFragment;
import com.jimetec.xunji.ui.MyFragment;
import com.jimetec.xunji.ui.MyWebViewActivity;
import com.jimetec.xunji.ui.NewsActivity;
import com.jimetec.xunji.ui.RegisterActivity;
import com.jimetec.xunji.ui.TrackFragment;
import com.jimetec.xunji.util.UpLocateTimeUtil;
import com.jimetec.xunji.util.UserUtil;
import com.jimetec.xunji.util.VersionManager;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//import com.jimetec.xunji.location.MiLocationUtil;

public class MainActivity extends AbsCommonActivity<MainPresenter> implements MainContract.View, AMapLocationListener {

    public static final String TAB_INDEX = "TAB_INDEX";
    @BindView(R.id.flContent)
    FrameLayout mFlContent;
    @BindView(R.id.iv_1)
    ImageView mIv1;
    @BindView(R.id.tv_1)
    TextView mTv1;
    @BindView(R.id.ll_1)
    LinearLayout mLl1;
    @BindView(R.id.iv_2)
    ImageView mIv2;
    @BindView(R.id.tv_2)
    TextView mTv2;
    @BindView(R.id.ll_2)
    RelativeLayout mLl2;
    @BindView(R.id.iv_3)
    ImageView mIv3;
    @BindView(R.id.tv_3)
    TextView mTv3;
    @BindView(R.id.ll_3)
    LinearLayout mLl3;

    private int mIndex = -1;
    private FragmentManager mManager;
    private Fragment mCurrentFragment;
    private Fragment mFragment1;
    private Fragment mFragment2;
    private Fragment mFragment3;
    private AMapTrackClient aMapTrackClient;
    List<LocationWarnBean> mWarnBeanList = new ArrayList<>();

    @Override
    public MainPresenter getPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViewAndData() {
//        mTvText.setText(Pinyin.toPinyin("中国重庆", ""));
        showAgreementDialog();
        initLocation();
//        int color = getResources().getColor(R.color.color_FFFFFF);
//        StatusBarUtil.setColor(this, color, 10);
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        StatusBarUtil.setLightMode(this);

        mFragment1 = new CareFragment();
//        mFragment1 = new HomeTestFragment();
//        mFragment2 = new WebChargeFragment();
        mFragment2 = new TrackFragment();
        mFragment3 = new MyFragment();
        mManager = getSupportFragmentManager();
        selectTab(getIntent().getIntExtra(TAB_INDEX, 1));
//        selectTab(1);


        // 不要使用Activity作为Context传入
        aMapTrackClient = new AMapTrackClient(getApplicationContext());
        UpLocateTimeBean upLocateTimeBean = UpLocateTimeUtil.getInstance();
        aMapTrackClient.setInterval(upLocateTimeBean.collect, upLocateTimeBean.report);
        aMapTrackClient.setLocationMode(LocationMode.HIGHT_ACCURACY);

//        aMapTrackClient.setInterval(1, 5);
        mFlContent.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPermission();
                upDateApp(false);
            }
        }, 400);

    }

    /**
     * 展示服务协议和隐私政策
     */
    boolean isShowAgreement;
    private void showAgreementDialog() {
        isShowAgreement = SpUtil.getBoolean("isShowAgreement");
        if(isShowAgreement){
            return;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_agreement,null);
        TextView tv = (TextView)view.findViewById(R.id.tv_title);
        final SpannableStringBuilder style = new SpannableStringBuilder();
        //设置文字
        style.append("请你务必审慎阅读、充分理解“服务及隐私政策”各条款，包括但不限于:为了向你提供位置等服务,我们需要收集你的设备信息、操作日志等个人信息。你可以在“设置”中查看、变更、删除个人信息并管理你的授权。\n" +
                " 你可阅读《隐私协议》及《用户协议》了解详细信息。如你同意，请点击“同意\"开始接受我们的服务。");
        //设置部分文字点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                MyWebViewActivity.startTo(mActivity, Constants.AGREEMENT_URL_PRIVACY, "隐私协议", "隐私协议");
            }
        };
        //设置部分文字点击事件
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                MyWebViewActivity.startTo(mActivity, Constants.AGREEMENT_URL_USER, "用户协议", "用户协议");
            }
        };
        style.setSpan(clickableSpan, 103, 109, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(clickableSpan2, 110, 116, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        //配置给TextView
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setText(style);

//        //设置部分文字颜色
//        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
//        style.setSpan(foregroundColorSpan, 11, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//

        new CommonDialogFragment.Builder()
                .showTitle(true)
                .setTitleText("服务协议和隐私政策")
                .setContentView(view)
                .setLeftButtonText("暂不使用")
                .setBtLeftColor(R.color.color_title)
                .setRightButtonText("同意")
                .setBtRightColor(R.color.colorPrimary)
                .setLeftButtonClickListener(new CommonDialogFragment.OnClickListener() {
                    @Override
                    public void onClick(CommonDialogFragment dialogFragment, int which, String content) {
                        dialogFragment.dismiss();
                        finish();
                    }
                })
                .setRightButtonClickListener(new CommonDialogFragment.OnClickListener() {
                    @Override
                    public void onClick(CommonDialogFragment dialogFragment, int which, String content) {
                        SpUtil.putBoolean("isShowAgreement",true);
                        dialogFragment.dismiss();
                    }
                })
                .create()
                .show(getSupportFragmentManager());

    }

    @Override
    protected void onResume() {
        super.onResume();
//        ToastUtil.showShort( ""+isTrackServiceRunning());
        updateLoginedData();

    }


    public void updateLoginedData() {
        if (UserUtil.getUserTid() > 0) {
            if (SpUtil.getBoolean(Constants.LOGIN_REFRESH_NEWS,false)){
                mPresenter.getNews();
            }


            mPresenter.upLocateTime();
            if (!isServiceRunning) {
                startTrack();
            }
            if (mTempLocationBean != null) {
                if (mTempLocationBean.isNeedUpload()) {
                    mPresenter.updateLocation(mTempLocationBean.longitude, mTempLocationBean.latitude, mTempLocationBean.address);
                }
                mTempLocationBean = null;
            }
            loadingNetData();
        }

    }

    public long updateFriendSettingWarnsTimes;

    @Override
    public void loadingNetData() {
        super.loadingNetData();
        updateFriendSettingWarnsTimes = System.currentTimeMillis();
        mPresenter.getFriendSettingWarns();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int index = intent.getIntExtra(TAB_INDEX, 1);
        selectTab(index);
    }

    @OnClick({R.id.ll_1, R.id.ll_2, R.id.ll_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_1:
                selectTab(1);
                break;
            case R.id.ll_2:
                selectTab(2);
                break;
            case R.id.ll_3:
                selectTab(3);
                break;
        }
    }


    public void selectTab(int index) {
        if (index < 1 || index > 3) {
            return;
        }

        if (mIndex == index) {
            return;
        }
        mIndex = index;
        switch (index) {

            case 1:
//                EventHelp.submitClickEvent(getEventReferer(),ConstantMode.HOME,ConstantMode.HOME,"");
                mIv1.setImageResource(R.mipmap.teb_s_1);
                mTv1.setTextColor(UIUtil.getColor(R.color.color_theme));
                mIv2.setImageResource(R.mipmap.teb_n_2);
                mTv2.setTextColor(UIUtil.getColor(R.color.teb_text_color_n));
                mIv3.setImageResource(R.mipmap.teb_n_3);
                mTv3.setTextColor(UIUtil.getColor(R.color.teb_text_color_n));
//                if (mHomeFragment.isAdded()) {
//                    transaction.hide(mCurrentFragment).show(mHomeFragment).commit();
//                } else {
//                    transaction.hide(mCurrentFragment).add(R.id.flContent, mHomeFragment).commit();
//                }
//                mCurrentFragment = mHomeFragment;
                changeFrament(mCurrentFragment, mFragment1);

                break;

            case 2:
//                EventHelp.submitClickEvent(getEventReferer(),ConstantMode.CHARGE,ConstantMode.CHARGE,"");

                mIv1.setImageResource(R.mipmap.teb_n_1);
                mTv1.setTextColor(UIUtil.getColor(R.color.teb_text_color_n));
                mIv2.setImageResource(R.mipmap.teb_s_2);
                mTv2.setTextColor(UIUtil.getColor(R.color.color_theme));
                mIv3.setImageResource(R.mipmap.teb_n_3);
                mTv3.setTextColor(UIUtil.getColor(R.color.teb_text_color_n));
//                if (mAskFragment.isAdded()) {
//                    transaction.hide(mCurrentFragment).show(mAskFragment).commit();
//                } else {
//                    transaction.hide(mCurrentFragment).add(R.id.flContent, mAskFragment).commit();
//                }
//                mCurrentFragment = mAskFragment;
                changeFrament(mCurrentFragment, mFragment2);
                break;


            case 3:
//                submitClickEvent(ConstantMode.SCORE,"");
//                EventHelp.submitClickEvent(getEventReferer(),ConstantMode.SCORE,ConstantMode.SCORE,"");

                mIv1.setImageResource(R.mipmap.teb_n_1);
                mTv1.setTextColor(UIUtil.getColor(R.color.teb_text_color_n));
                mIv2.setImageResource(R.mipmap.teb_n_2);
                mTv2.setTextColor(UIUtil.getColor(R.color.teb_text_color_n));
                mIv3.setImageResource(R.mipmap.teb_s_3);
                mTv3.setTextColor(UIUtil.getColor(R.color.color_theme));
//                if (mNewsCenterFragment.isAdded()) {
//                    transaction.hide(mCurrentFragment).show(mNewsCenterFragment).commit();
//                } else {
//                    transaction.hide(mCurrentFragment).add(R.id.flContent, mNewsCenterFragment).commit();
//                }
//                mCurrentFragment = mNewsCenterFragment;
////                startActivity(new Intent(this, NewsActivity.class));
////                mNewsCenterFragment.refresh();
                changeFrament(mCurrentFragment, mFragment3);
                break;
        }
    }


    public void changeFrament(Fragment from, Fragment to) {
        FragmentTransaction transaction = mManager.beginTransaction();
//        transaction.replace(R.id.flContent, to).commit();
        if (from == null) {
            if (to.isAdded()) {
                transaction.show(to).commit();
            } else {
                transaction.add(R.id.flContent, to).commit();
            }
        } else {
            if (to.isAdded()) {
                transaction.hide(from).show(to).commit();
            } else {
                transaction.hide(from).add(R.id.flContent, to).commit();
            }
        }
//        if (to.isAdded()) {
//            transaction.hide(from).show(to).commit();
//        } else {
//            transaction.hide(from).add(R.id.flContent, to).commit();
//        }
//        transaction.replace(R.id.flContent, to).commit();

        mCurrentFragment = to;

        LogUtils.e("to" + to.toString());

    }


    @Override
    public void dealEvent(HomeIndexEvent indexEvent) {
        if (indexEvent.index >= 0) {
            selectTab(indexEvent.index);
        }


    }

    @Override
    public void backLocateTime(UpLocateTimeBean bean) {
        UpLocateTimeUtil.setUpLocateTimeBean(bean);
    }

    @Override
    public void backFriendSettingWarns(List<LocationWarnBean> list) {
        updateFriendSettingWarnsTimes = System.currentTimeMillis();
        mWarnBeanList.clear();
        mWarnBeanList.addAll(list);
    }

    @Override
    public void backRemind() {

    }

    @Override
    public void backNews(List<FriendBean> pageBean) {
        SpUtil.putBoolean(Constants.LOGIN_REFRESH_NEWS,false);
        if(pageBean!=null && pageBean.size()>0){
            mFlContent.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(mActivity, NewsActivity.class);
                    startActivity(intent);
                }
            },1000);
        }
    }


    private boolean isServiceRunning;
    private boolean isGatherRunning;
    public int mSid;
    public int mTid;
    public int mTrid;

    private OnTrackLifecycleListener onTrackListener = new SimpleOnTrackLifecycleListener() {


        @Override
        public void onBindServiceCallback(int status, String msg) {


            Log.w(TAG, "onBindServiceCallback, status: " + status + ", msg: " + msg);
        }

        @Override
        public void onStartTrackCallback(int status, String msg) {

            if (status == ErrorCode.TrackListen.START_TRACK_SUCEE || status == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK) {
                // 成功启动
                LogUtils.e("Track --  启动服务成功");
                LogUtils.print2File("onStartTrackCallback", "Track --  启动服务成功");
                mTrid = UserUtil.getUserTrid();
                aMapTrackClient.setTrackId(mTrid);
                aMapTrackClient.startGather(onTrackListener);
                isServiceRunning = true;

            } else if (status == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED) {
                // 已经启动
                LogUtils.e("Track --  服务已经启动");
                LogUtils.print2File("onStartTrackCallback", "Track --  服务已经启动");

                isServiceRunning = true;

            } else {
                isServiceRunning = false;
                LogUtils.e("Track --  error onStartTrackCallback, status: " + status + ", msg: " + msg);
                LogUtils.print2File("onStartTrackCallback", "Track --  error onStartTrackCallback, status: " + status + ", msg: " + msg);

            }
        }

        @Override
        public void onStopTrackCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.STOP_TRACK_SUCCE) {
                // 成功停止
                isServiceRunning = false;
                LogUtils.e("Track --  停止服务成功");
                LogUtils.print2File("onStopTrackCallback", "Track --  停止服务成功");

            } else {
                isServiceRunning = false;
                LogUtils.print2File("onStopTrackCallback", "Track --  error onStopTrackCallback, status: " + status + ", msg: " + msg);
                LogUtils.e("Track --  error onStopTrackCallback, status: " + status + ", msg: " + msg);
            }
        }

        @Override
        public void onStartGatherCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.START_GATHER_SUCEE) {
                isGatherRunning = true;
                LogUtils.e("Track --  定位采集开启成功");
                LogUtils.print2File("onStartGatherCallback", "Track --  定位采集开启成功");

            } else if (status == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
                isGatherRunning = true;
                LogUtils.e("Track --  定位采集已经开启");
                LogUtils.print2File("onStartGatherCallback", "Track --  定位采集已经开启");


            } else {
                isGatherRunning = false;
                LogUtils.print2File("onStartGatherCallback", "Track --  error onStartGatherCallback, status: " + status + ", msg: " + msg);

                LogUtils.e("Track --  error onStartGatherCallback, status: " + status + ", msg: " + msg);

            }
        }

        @Override
        public void onStopGatherCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.STOP_GATHER_SUCCE) {
                isGatherRunning = false;
                LogUtils.print2File("onStopGatherCallback", "Track --  定位采集停止成功");
                LogUtils.e("Track --  定位采集停止成功");
            } else {
                LogUtils.print2File("onStopGatherCallback", "Track --  error onStopGatherCallback, status: " + status + ", msg: " + msg);
                LogUtils.e("Track --  error onStopGatherCallback, status: " + status + ", msg: " + msg);
            }
        }
    };

    private void startTrack() {

        if (UserUtil.getUserTid() > 0) {
            LogUtils.print2File("startTrack", "开始startTrack");
            mSid = UserUtil.getUserSid();
            mTid = UserUtil.getUserTid();
            // 不指定track id，上报的轨迹点是该终端的散点轨迹
            TrackParam trackParam = new TrackParam(mSid, mTid);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                trackParam.setNotification(buildNotification());
            }
            aMapTrackClient.startTrack(trackParam, onTrackListener);
        }


//        NotificationManager mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        mNotifyManager.notify(1,createNotification());

    }


//    private static final String CHANNEL_ID_SERVICE_RUNNING = "CHANNEL_ID_SERVICE_RUNNING";

//    /**
//     * 在8.0以上手机，如果app切到后台，系统会限制定位相关接口调用频率
//     * 可以在启动轨迹上报服务时提供一个通知，这样Service启动时会使用该通知成为前台Service，可以避免此限制
//     */
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    private Notification createNotification() {
//        Notification.Builder builder;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_SERVICE_RUNNING, "app service", NotificationManager.IMPORTANCE_LOW);
//            nm.createNotificationChannel(channel);
//            builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID_SERVICE_RUNNING);
//        } else {
//            builder = new Notification.Builder(getApplicationContext());
//        }
//        Intent nfIntent = new Intent(TestActivity.this, TestActivity.class);
//        nfIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        builder.setContentIntent(PendingIntent.getActivity(TestActivity.this, 0, nfIntent, 0))
//                .setSmallIcon(R.mipmap.app_icon)
//                .setContentTitle(getResources().getString(R.string.app_name))
//                .setContentText("运行中...");
//        Notification notification = builder.build();
//        return notification;
//    }


    private int GPS_REQUEST_CODE = 10;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE) {
            //做需要做的事情，比如再次检测是否打开GPS了 或者定位
//
            if (checkGPSIsOpen()) {

            } else {
                showPermissionFail(true);
//                ToastUtil.showShort("GPS");
//                openGPSSettings();
            }
        }
    }

    public void checkGps() {
        if (checkGPSIsOpen()) {

        } else {
            if (this==Utils.getActivityLifecycle().getTopActivity()){
                new CommonDialogFragment.Builder()
                        .showTitle(false)
                        .showSingleButton(true)
                        .setSingleButtonText("去设置")
                        .setContentText("定位失败,请先打开GPS定位" + "")
                        .setCanceledOnTouchOutside(false)
                        .setCancelable(false)
                        .setSingleButtonClickListener(new CommonDialogFragment.OnClickListener() {
                            @Override
                            public void onClick(CommonDialogFragment dialogFragment, int which, String content) {
                                openGPSSettings();
                                dialogFragment.dismiss();
                            }

                        })
                        .create()
                        .show(getSupportFragmentManager());
            }
        }
    }


    /**
     * 检测GPS是否打开
     *
     * @return
     */
    private boolean checkGPSIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        return isOpen;
    }


    /**
     * 跳转GPS设置
     */
    private void openGPSSettings() {
        //跳转GPS设置界面
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, GPS_REQUEST_CODE);
    }


    @Override
    protected void onDestroy() {
        AppUtils.unregisterAppStatusChangedListener(TAG);
        LogUtils.print2File("onDestroy", "onDestroy");
        try {
            if (mlocationClient != null) {
                mlocationClient.stopLocation();
                mlocationClient.onDestroy();
            }
            mlocationClient = null;
            if (aMapTrackClient != null) {
                aMapTrackClient.stopTrack(new TrackParam(mSid, mTid), new SimpleOnTrackLifecycleListener());
                LogUtils.print2File("onDestroy", "stopTrack 开始停止");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    public void initLocation() {

//        MiLocationUtil.initOnServiceStarted(getApplicationContext());
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(UpLocateTimeUtil.getInstance().lastReport * 1000);
//        mLocationOption.setInterval(5 * 1000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
        AppUtils.registerAppStatusChangedListener(TAG, new Utils.OnAppStatusChangedListener() {
            @Override
            public void onForeground() {
                if (mlocationClient != null)
                    mlocationClient.disableBackgroundLocation(true);
            }

            @Override
            public void onBackground() {
                if (mlocationClient != null)
                    mlocationClient.enableBackgroundLocation(2001, buildNotification());
            }
        });


    }


    private static final String NOTIFICATION_CHANNEL_NAME = "BackgroundLocation";
//    private NotificationManager notificationManager = null;
//    boolean isCreateChannel = false;

    @SuppressLint("NewApi")
    private Notification buildNotification() {
        Notification notification = null;
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_NAME, "后台定位", NotificationManager.IMPORTANCE_LOW);
            channel.enableVibration(false);
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setVibrationPattern(new long[]{0});
            channel.setSound(null, null);
            nm.createNotificationChannel(channel);
            builder = new Notification.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_NAME);
        } else {
            builder = new Notification.Builder(getApplicationContext());
            builder.setVibrate(new long[0]);
            builder.setSound(null, null);
        }

        Intent nfIntent = new Intent(MainActivity.this, MainActivity.class);
        nfIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        builder.setContentIntent(PendingIntent.getActivity(MainActivity.this, 0, nfIntent, 0))
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("正在后台运行...")
                .setWhen(System.currentTimeMillis());

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notification = builder.build();
        } else {
            return builder.getNotification();
        }
        return notification;
    }

    TempLocationBean mTempLocationBean;
//    long curTime = 0;

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0 && UserUtil.isLogined()) {
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                double latitude = amapLocation.getLatitude();//获取纬度
                double longitude = amapLocation.getLongitude();//获取经度
                String address = amapLocation.getAddress();
                if (TextUtils.isEmpty(address)) return;
                mTempLocationBean = new TempLocationBean(longitude, latitude, address);
                if (UserUtil.isLogined()) {
                    //定位成功回调信息，设置相关消息
                    String lat = SpUtil.getString(Constants.latitude, "");
                    String lng = SpUtil.getString(Constants.longitude, "");
                    String locationAddress = SpUtil.getString(Constants.locationAddress, "");


                    try {
                        dealWarnDistance(new LatLng(latitude, longitude), address);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    LogUtils.e("我要发送数据了");
                    //更新 好友位置  不更新tab
                    RxBus.getDefault().post(new HomeIndexEvent(0));

//                    if (!TextUtils.isEmpty(address) && !address.equalsIgnoreCase(locationAddress)) {
//                        LogUtils.print2File("onLocationChanged", "--longitude--" + longitude + "--latitude--" + latitude + "--address--" + address);
//                    } else {
////                        if (System.currentTimeMillis() - curTime > 1000 * 60 * 5) {
//                        if (System.currentTimeMillis() - curTime > 60 * 5) {
//                            curTime = System.currentTimeMillis();
//                            LogUtils.print2File("onLocationChanged", "--longitude--" + longitude + "--latitude--" + latitude + "--address--" + address);
//                        }
//                    }

                    if ((latitude + "").equalsIgnoreCase(lat) && (lng + "").equalsIgnoreCase(lng)) {
                        LogUtils.e("不更新" + address);
                        mPresenter.updateLocation(longitude, latitude, address);

                    } else {
                        LogUtils.e("更新" + address);
                        mPresenter.updateLocation(longitude, latitude, address);
                    }

                    if (System.currentTimeMillis() - updateFriendSettingWarnsTimes > UpLocateTimeUtil.getInstance().collect * 1000) {
                        mPresenter.getFriendSettingWarns();
                        if (!isTrackServiceRunning()) {
                            startTrack();
                        }
                    }

                }


//                MiLocationUtil.onLocateSuccess(getApplicationContext(), amapLocation.getErrorCode());

            } else {
                LogUtils.print2File("onLocationChanged", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());

                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());

                try {
//                    MiLocationUtil.onLocateFail(getApplicationContext(), amapLocation.getErrorCode());
//                    MiLocationUtil.onLocateFail(getApplicationContext(), 4);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }


    public void dealWarnDistance(LatLng cur, String address) {
        LatLng other = null;
        for (int i = 0; i < mWarnBeanList.size(); i++) {
            LocationWarnBean bean = mWarnBeanList.get(i);
            other = new LatLng(bean.latitude, bean.longitude);
            float distance = AMapUtils.calculateLineDistance(cur, other);
            int curStatus = 0;
            long curtime = System.currentTimeMillis();
//
            if (distance < UpLocateTimeUtil.getInstance().locationRemind) {
                curStatus = 2;
//                ToastUtil.showShort("distance"+distance);
            } else {
                curStatus = 1;
            }

//            curStatus = 2;
            if (bean.status != curStatus && curtime - bean.lastUpdateTimes > 1000 * 60) {
                bean.lastUpdateTimes = System.currentTimeMillis();
                mPresenter.aimRemind(bean, bean.id, bean.targetUserId, bean.friendName, bean.location, 1, curStatus);
                bean.status = curStatus;
            }
//            if (BuildConfig.DEBUG){
//                if (bean.status==1){
//                    curStatus=2;
//                }else {
//                    curStatus=1;
//                }
//                if ( curtime-bean.lastUpdateTimes >1000*10){
//                    mPresenter.aimRemind(bean,bean.id,bean.targetUserId,bean.friendName,bean.location,1,curStatus);
//                    bean.status=curStatus;
//                    bean.lastUpdateTimes=curTime;
//                }
//
//            }else {
//                if (bean.status != curStatus  &&curtime -  bean.lastUpdateTimes >1000*60){
//                    mPresenter.aimRemind(bean,bean.id,bean.targetUserId,bean.friendName,bean.location,1,curStatus);
//                    bean.status=curStatus;
//                }
//            }


        }
    }

    /**
     * 检查更新
     *
     * @param isToast
     */
    public void upDateApp(boolean isToast) {
//        if (BuildConfig.appUpdateEnable) {
        if (VersionManager.getInstance().isUpdateApp()) {
            try {
                mFlContent.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showUpdateDialog(VersionManager.getInstance().isMustUpdate());
                    }
                }, 2000);


            } catch (Exception e) {
                e.printStackTrace();
//                CrashReport.postCatchedException(new Exception("检查更新--" + e.getMessage()));
                downloadApk();
            }
        } else {
            if (isToast)
                ToastUtil.showShort("已经是最新版本");
        }
    }


    //    private NumberProgressBar mProgress;
    private Dialog mDialog;

    /**
     * 展示更新对话框
     *
     * @param isMustUpdate
     */
    public void showUpdateDialog(boolean isMustUpdate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 设置参数
//        mDialog = builder.create();
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(com.jimetec.basin.R.layout.dialog_update, null);
        View leftbt = v.findViewById(com.jimetec.basin.R.id.dialog_left_btn);
//        mProgress = v.findViewById(R.id.progress_bar);
        final TextView title = v.findViewById(com.jimetec.basin.R.id.dialog_title);
        View rightbt = v.findViewById(com.jimetec.basin.R.id.dialog_right_btn);
        final View content = v.findViewById(com.jimetec.basin.R.id.dialog_content);
        final View bottom = v.findViewById(com.jimetec.basin.R.id.bottom_button_container);
        final View bottom_button_line = v.findViewById(com.jimetec.basin.R.id.bottom_button_line);
        if (isMustUpdate == true) {
            leftbt.setVisibility(View.GONE);
        }
        leftbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                subimtClick("app_取消升级", AppData.getInstance().getNewVersion());
                mDialog.cancel();
            }
        });

        rightbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                subimtClick("app_确认升级", AppData.getInstance().getNewVersion());
//                title.setText(UIUtil.getString(R.string.app_name)+"下载进度");

//                content.setVisibility(View.GONE);
//                mProgress.setVisibility(View.VISIBLE);
//                bottom_button_line.setVisibility(View.GONE);
//                bottom.setVisibility(View.GONE);
                downloadApk();
                mDialog.cancel();
            }
        });

        builder.setTitle("应用更新")
//                .setIcon(R.mipmap.aic_launcher)
                .setView(v);
        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(!isMustUpdate);
        mDialog.setCancelable(!isMustUpdate);
        mDialog.show();
//        mDialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {


            moveTaskToBack(true);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    private void downloadApk() {


        Intent intent = new Intent(this, DownloadapkService.class);
        intent.putExtra(DownloadapkService.TAG, VersionManager.getInstance().getUpgradeUrl());
        startService(intent);
    }


    public void checkPermission() {
        if (AndPermission.hasPermissions(this, Permission.Group.LOCATION)) {
            checkGpsDelayed();
        } else {
            if (this==Utils.getActivityLifecycle().getTopActivity()) {
                new CommonDialogFragment.Builder()
                        .showTitle(false)
                        .showSingleButton(true)
                        .setSingleButtonText("去授权")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .setBtRightColor(R.color.color_F5445C)
                        .setContentText(getResources().getString(R.string.app_name) + "需要定位权限,为你提供轨迹服务" + "?")
                        .setSingleButtonClickListener(new CommonDialogFragment.OnClickListener() {
                            @Override
                            public void onClick(CommonDialogFragment dialogFragment, int which, String content) {
                                dialogFragment.dismiss();
                                requestPermission(Permission.Group.LOCATION);
                            }


                        })
                        .create()
                        .show(getSupportFragmentManager());
            }



        }
    }

    public void checkGpsDelayed() {

        mFlContent.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkGps();
            }
        }, 1000);
    }


    private void requestPermission(String... permissions) {
        AndPermission.with(this)
                .runtime()
                .permission(permissions)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        checkGpsDelayed();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                            showSettingDialog(MainActivity.this, permissions);
                        } else {
                            showPermissionFail(false);
                        }
                    }
                })
                .start();
    }


    /**
     * Display setting dialog.
     */
    public void showSettingDialog(Context context, final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = "您已禁止授予" + getResources().getString(R.string.app_name) + permissionNames.toString() + "权限,可能会造成功能不可用,如需使用前往设置";
//        String message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));

        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermission();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showPermissionFail(false);
                    }
                })
                .show();
    }


    /**
     * Set permissions.
     */
    private void setPermission() {
        AndPermission.with(this)
                .runtime()
                .setting()
                .onComeback(new Setting.Action() {
                    @Override
                    public void onAction() {
                        if (AndPermission.hasPermissions(MainActivity.this, Permission.Group.LOCATION)) {
                            checkGpsDelayed();
                        } else {
                            showPermissionFail(false);
                        }
//                        Toast.makeText(StartActivity.this, R.string.message_setting_comeback, Toast.LENGTH_SHORT).show();
                    }


                })
                .start();
    }


    public void showPermissionFail(final boolean isGsp) {

        View playView = View.inflate(this, R.layout.pop_permission_fail, null);
        TextView tvContent = (TextView) playView.findViewById(R.id.tvContent);
        if (isGsp) {
            tvContent.setText("定位失败,GPS尚未打开,无法使用完整的应用服务~");
        }
        Button dialogLeftBtn = (Button) playView.findViewById(R.id.dialog_left_btn);
        Button dialogRightBtn = (Button) playView.findViewById(R.id.dialog_right_btn);
        final PopupWindow mPopupWindow = new PopupWindow(playView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // PopupWindow popupWindow = new PopupWindow(popuView, 100, 100);
        // 设置点击外部区域, 自动隐藏
        mPopupWindow.setOutsideTouchable(false); // 外部可触摸
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable()); // 设置空的背景, 响应点击事件
        mPopupWindow.setFocusable(true); //设置可获取焦点
        mPopupWindow.setAnimationStyle(R.style.addCareFriendAnim);  //添加动画
//        mPopupWindow.setAnimationStyle(R.style.popslide_style);
        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.dialog_left_btn:

                        break;
                    case R.id.dialog_right_btn:
                        if (isGsp) {
                            openGPSSettings();
                        } else {
                            requestPermission(Permission.Group.LOCATION);
                        }

                        break;
                }
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        };

        dialogLeftBtn.setOnClickListener(onClickListener);
        dialogRightBtn.setOnClickListener(onClickListener);
    }


    private boolean isTrackServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.amap.api.track.AMapTrackService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getEventMode() {
        return "主页";
    }
}

