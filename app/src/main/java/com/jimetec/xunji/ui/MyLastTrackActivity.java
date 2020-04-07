package com.jimetec.xunji.ui;


import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.MovingPointOverlay;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.query.entity.HistoryTrack;
import com.amap.api.track.query.entity.Point;
import com.amap.api.track.query.model.AddTerminalResponse;
import com.amap.api.track.query.model.AddTrackResponse;
import com.amap.api.track.query.model.DistanceResponse;
import com.amap.api.track.query.model.HistoryTrackRequest;
import com.amap.api.track.query.model.HistoryTrackResponse;
import com.amap.api.track.query.model.LatestPointResponse;
import com.amap.api.track.query.model.OnTrackListener;
import com.amap.api.track.query.model.ParamErrorResponse;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.amap.api.track.query.model.QueryTrackResponse;
import com.common.baseview.base.BaseActivity;
import com.common.baseview.progress.ProgressDialogHandler;
import com.common.lib.utils.LogUtils;
import com.common.lib.utils.TimeUtils;
import com.common.lib.utils.ToastUtil;
import com.jaeger.library.StatusBarUtil;
import com.jimetec.xunji.R;
import com.jimetec.xunji.util.DateTimeEntity;
import com.jimetec.xunji.util.UpLocateTimeUtil;
import com.jimetec.xunji.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;

public class MyLastTrackActivity extends BaseActivity {

    @BindView(R.id.ivTitleLeft)
    ImageView mIvTitleLeft;
    @BindView(R.id.rlTitleLeft)
    RelativeLayout mRlTitleLeft;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.ivTitleRight)
    ImageView mIvTitleRight;
    @BindView(R.id.ivRemind)
    ImageView mIvRemind;
    @BindView(R.id.rlTitleRight)
    RelativeLayout mRlTitleRight;
    @BindView(R.id.activity_track_service_map)
    TextureMapView mapView;
    @BindView(R.id.ivReview)
    ImageView mIvReview;
    @BindView(R.id.tvStartDay)
    TextView mTvStartDay;
    @BindView(R.id.tvStartTime)
    TextView mTvStartTime;
    @BindView(R.id.flStart)
    FrameLayout mFlStart;
    @BindView(R.id.tvEndDay)
    TextView mTvEndDay;
    @BindView(R.id.tvEndTime)
    TextView mTvEndTime;
    @BindView(R.id.flEnd)
    FrameLayout mFlEnd;
    @BindView(R.id.btTrackBack)
    Button mBtTrackBack;
    @BindView(R.id.btPlay)
    Button mBtPlay;
    //    @BindView(R.id.RlSelectTime)
//    RelativeLayout mRlSelectTime;//
    @BindView(R.id.llSelectTime)
    LinearLayout mLlSelectTime;
    @BindView(R.id.tvTime)
    TextView mTvTime;
    @BindView(R.id.tvLast)
    TextView mTvLast;


    private AMap aMap;
    LatLng lastLatLng;

    public int sid;
    public int tid;
    public int trid;
    public long times;
    public String avatar = "";
    private DateTimeEntity mStartTime;
    private DateTimeEntity mEndTime;
    private ProgressDialogHandler mProgressDialogHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_last_track);
        ButterKnife.bind(this);
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        StatusBarUtil.setLightMode(this);
        mStartTime = DateTimeEntity.getSixHourAgo();
        mEndTime = DateTimeEntity.now();
        mTvTitle.setText(getEventMode());
        updateTimeView();
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.getUiSettings().setZoomControlsEnabled(false);
        drawLastPositionCenter();
        mLlSelectTime.setVisibility(View.VISIBLE);
        mIvReview.setVisibility(View.GONE);
        mIvReview.setEnabled(false);
        initInfoData();
//        queryHistory();

    }


    public void initInfoData() {
        mTvTitle.setText(getEventMode());

        sid = UserUtil.getUser().sid;
        tid = UserUtil.getUser().tid;
        trid = UserUtil.getUserTrid();
        avatar = UserUtil.getUser().headImage;
        times = UserUtil.getUser().lastLocationTimes;
//            mTvPhone.setText("自己");
        mTvLast.setText("最后的位置: " + UserUtil.getUser().lastLocation);
//        ImageManager.loadAvatar(this, avatar, mIvAvatar);
        if (times > 0) {
            mTvTime.setVisibility(View.VISIBLE);
            mTvTime.setText(TimeUtils.millis2String(times));
        } else {
            mTvTime.setVisibility(View.GONE);
        }
    }

    int pageNum = 1;
    int pageSize = 999;
    List<Point> mAllPoints = new ArrayList<>();


    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }


    public void queryHistory() {

        if (pageNum == 1) mAllPoints.clear();
        if (mEndTime.getCurrentTimeMillis() - mStartTime.getCurrentTimeMillis() <= 0) {
            ToastUtil.showShort("结束时间不能少于开始时间");
            return;
        }

        if (mProgressDialogHandler == null) mProgressDialogHandler = new ProgressDialogHandler(false, null, this, "正在查询轨迹...");

        if (mEndTime.getCurrentTimeMillis() - mStartTime.getCurrentTimeMillis() >= 1000 * 60 * 60 * 24) {
            ToastUtil.showShort("查询时间不能大于一天");
            return;
        }
        showProgressDialog();
        // 搜索最近12小时以内上报的轨迹
        HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest(
                sid,
                tid,
                mStartTime.getCurrentTimeMillis(),
                mEndTime.getCurrentTimeMillis(),
                0,      // 不绑路
                0,      // 不做距离补偿
                5000,   // 距离补偿阈值，只有超过5km的点才启用距离补偿
                0,  // 由旧到新排序
                pageNum,  // 返回第1页数据
                pageSize,    // 一页不超过100条
                ""  // 暂未实现，该参数无意义，请留空
        );
        AMapTrackClient aMapTrackClient = new AMapTrackClient(getApplicationContext());


        aMapTrackClient.queryHistoryTrack(historyTrackRequest, new OnTrackListener() {
            @Override
            public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
            }

            @Override
            public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {
            }

            @Override
            public void onDistanceCallback(DistanceResponse distanceResponse) {
            }

            @Override
            public void onLatestPointCallback(LatestPointResponse latestPointResponse) {
            }

            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {
                if (isDestroy) return;
                if (historyTrackResponse.isSuccess()) {
                    HistoryTrack historyTrack = historyTrackResponse.getHistoryTrack();
                    // historyTrack中包含终端轨迹信息
                    int count = historyTrack.getCount();
                    List<Point> pagePoints = historyTrack.getPoints();
                    if (pagePoints != null && pagePoints.size() > 0) {
                        mAllPoints.addAll(pagePoints);
                    }

                    if (isDestroy) return;

                    if (count > pageNum * pageSize) {
                        pageNum += 1;
                        queryHistory();
                    } else {
                        aMap.clear();
                        pageNum = 1;
                        dealPoints(mAllPoints);
                    }
                } else {
                    // 查询失败
                    mBtTrackBack.setEnabled(true);

                }
            }

            @Override
            public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {
            }

            @Override
            public void onAddTrackCallback(AddTrackResponse addTrackResponse) {
            }

            @Override
            public void onParamErrorCallback(ParamErrorResponse paramErrorResponse) {
            }
        });

    }

    List<LatLng> latLngs;

    public List<LatLng> getLatLngs() {
        return latLngs;
    }

    public void dealPoints(List<Point> points) {
        latLngs = new ArrayList<>();
        for (Point p : points) {
            LatLng latLng = new LatLng(p.getLat(), p.getLng());
            latLngs.add(latLng);
        }
        dismissProgressDialog();
        drawLastPositionCenter();
        if (points.size() > 0) {
            // 终点
//            Point p = points.get(points.size() - 1);
//            lastLatLng = new LatLng(p.getLat(), p.getLng());


            if (points.size() > 3) {
                Point startPoint = points.get(0);
                LatLng startLatLng = new LatLng(startPoint.getLat(), startPoint.getLng());
                long startTime = startPoint.getTime();
                addMarker(startLatLng,startTime,R.mipmap.icon_start_marker,"开始时间");



                Point endPoint = points.get(points.size()-1);
                LatLng endLatLng = new LatLng(endPoint.getLat(), endPoint.getLng());
                long endTime = endPoint.getTime();
                addMarker(endLatLng,endTime,R.mipmap.icon_end_marker,"结束时间");


                addPolylineInPlayGround();
//                mIvReview.setVisibility(View.VISIBLE);
            } else {
//                mIvReview.setVisibility(View.GONE);
            }

        } else {
//            lastLatLng = new LatLng(UserUtil.getUser().latitude, UserUtil.getUser().longitude);
//            drawLastPositionCenter();
        }
        startMove();
//        if (isFirst) {
//
//        } else {
//            startMove();
//        }
    }



    public void addMarker(LatLng latLng, long time, int resId, String des) {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(resId);
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.title(des+":");
        markerOption.snippet(TimeUtils.millis2String(time));
        markerOption.draggable(false);//设置Marker可拖动
        markerOption.icon(
                bitmapDescriptor);
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        Marker marker = mapView.getMap().addMarker(markerOption);
//            marker.showInfoWindow();
    }



    public void drawLastPositionCenter() {
        aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                getLastPosition(), 16, 0, 0)), 1000, null);
//        aMap.animateCamera(CameraUpdateFactory.zoomTo(5));
        setGeniusIcon();
    }


    public LatLng getLastPosition() {
//        double lat = 22.551518;
//        double lng = 113.962783;
//        LatLng latLng = new LatLng(lat, lng);
        lastLatLng = new LatLng(UserUtil.getUser().latitude, UserUtil.getUser().longitude);
        return lastLatLng;
    }

    /**
     * 设置网络图片
     */

    Marker marker;

    public void setGeniusIcon() {

        MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker_point))
                .position(getLastPosition())
                .draggable(false);
        marker = aMap.addMarker(markerOption);
    }

    /**
     * 添加轨迹线
     */
    private void addPolylineInPlayGround() {


        List<LatLng> list = getLatLngs();
        Polyline mPolyline = aMap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.custtexture)) //setCustomTextureList(bitmapDescriptors)
//				.setCustomTextureIndex(texIndexList)
                .addAll(list)
                .useGradient(true)
                .width(18));


        // 构建 轨迹的显示区域
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(list.get(0));
        builder.include(list.get(list.size() - 1));
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200));
//        mIvReview.setVisibility(View.VISIBLE);
    }


    private MovingPointOverlay smoothMarker;


    /**
     * 开始移动
     */
    public void startMove() {

        // 读取轨迹点
        List<LatLng> points = getLatLngs();

        if (points == null || points.size() <= 3) {
            ToastUtil.showShort("未找到该时间段的轨迹");
            updatePlayStatus(TYPE_NOTHING);
            return;
        }
        mBtTrackBack.setEnabled(false);
        mIvReview.setEnabled(false);
        mIvReview.setVisibility(View.VISIBLE);
        // 构建 轨迹的显示区域
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        builder.include(points.get(0));
//        builder.include(points.get(points.size() - 1));
//
////        aMap.animateCamera(CameraUpdateFactory.newLatLng(points.get(0)), 100, null);
//        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200));


        // 实例 MovingPointOverlay 对象
//        if (smoothMarker == null) {
        // 设置 平滑移动的 图标
//            marker = aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker_point)).anchor(0.5f, 0.5f));
        smoothMarker = new MovingPointOverlay(aMap, marker);
//        }

        // 取轨迹点的第一个点 作为 平滑移动的启动
        LatLng drivePoint = points.get(0);
        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
        points.set(pair.first, drivePoint);
        List<LatLng> subList = points.subList(pair.first, points.size());

        // 设置轨迹点
        smoothMarker.setPoints(subList);
        // 设置平滑移动的总时间  单位  秒
        int dur = 10;
        if (points.size() > 500) {
            dur = UpLocateTimeUtil.getInstance().playback;
        }
        smoothMarker.setTotalDuration(dur);

        // 设置  自定义的InfoWindow 适配器
//        aMap.setInfoWindowAdapter(infoWindowAdapter);
        // 显示 infowindow
//        marker.showInfoWindow();

        // 设置移动的监听事件  返回 距终点的距离  单位 米
        smoothMarker.setMoveListener(new MovingPointOverlay.MoveListener() {
            @Override
            public void move(final double distance) {

                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.e("距离终点还有： " + (int) distance + "米");
                            if (distance < 1 && mIvReview != null && !mBtTrackBack.isEnabled()) {
                                mIvReview.setVisibility(View.GONE);
                                mBtTrackBack.setEnabled(true);
                                updatePlayStatus(TYPE_NOTHING);

                            }
                        }
                    });

                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });


        // 开始移动
        smoothMarker.startSmoothMove();
        updatePlayStatus(TYPE_PLAYING);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    boolean isDestroy = false;

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        isDestroy = true;
        if (smoothMarker != null) {
            smoothMarker.destroy();
        }
        mapView.onDestroy();
        super.onDestroy();
    }


    @OnClick({R.id.rlTitleLeft, R.id.ivReview, R.id.flStart, R.id.flEnd, R.id.btTrackBack,R.id.btPlay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlTitleLeft:
                finish();
                break;
            case R.id.ivReview:

                break;
            case R.id.flStart:
                selectTime(mStartTime);
                break;
            case R.id.flEnd:
                selectTime(mEndTime);
                break;

            case R.id.btPlay:
                if (playType == TYPE_PLAYING){
                    updatePlayStatus(TYPE_PAUSE);
                    if (smoothMarker!=null)smoothMarker.stopMove();
                }else  if (playType == TYPE_PAUSE){
                    updatePlayStatus(TYPE_PLAYING);
                    if (smoothMarker!=null)smoothMarker.startSmoothMove();
                }
                break;


            case R.id.btTrackBack:
                mBtTrackBack.setEnabled(false);
                queryHistory();
//                if (isFirst) {
//                    startMove();
//                    isFirst = false;
//                } else {
//                    queryHistory();
//                }
                break;
        }
    }


    public void selectTime(final DateTimeEntity date) {

        DateTimeEntity now = DateTimeEntity.now();
        DateTimePicker picker = new DateTimePicker(this, DateTimePicker.HOUR_24);
        picker.setDateRangeStart(2016, 8, 1);
        picker.setDateRangeEnd(now.getYear(), now.getMonth(), now.getDay());
        picker.setSelectedItem(date.getYear(), date.getMonth(), date.getDay(), date.getHour(), date.getMinute());
        picker.setTimeRangeStart(0, 0);
        picker.setTimeRangeEnd(23, 59);
//                picker.setTopLineColor(0x99FF0000);
        picker.setCancelTextColor(getResources().getColor(R.color.color_F5445C));
        picker.setSubmitTextColor(getResources().getColor(R.color.colorPrimary));
        picker.setPressedTextColor(getResources().getColor(R.color.colorPrimary));
        picker.setTextColor(getResources().getColor(R.color.colorPrimary));
        picker.setTopLineColor(getResources().getColor(R.color.colorPrimary));
        picker.setLabelTextColor(getResources().getColor(R.color.colorPrimary));
        picker.setDividerColor(getResources().getColor(R.color.colorPrimary));
//                picker.setLabelTextColor(0xFFFF0000);
//                picker.setDividerColor(0xFFFF0000);
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
//                        ToastUtil.showShort(year + "-" + month + "-" + day + " " + hour + ":" + minute);
//                DateTimeEntity dateTimeEntity = new DateTimeEntity(
                date.updateDateTime(year, month, day, hour, minute);
                updateTimeView();
            }
        });
        picker.show();

    }

    @Override
    public String getEventMode() {
        return "我的轨迹";
    }


    public void updateTimeView() {
        mTvStartDay.setText(mStartTime.getYMD());
        mTvStartTime.setText(mStartTime.getHM());

        mTvEndDay.setText(mEndTime.getYMD());
        mTvEndTime.setText(mEndTime.getHM());

        mIvReview.setVisibility(View.GONE);
        updatePlayStatus(TYPE_NOTHING);

    }




    int  playType =TYPE_NOTHING;  // 0  没什么事做  1  播放状态  2 暂停状态
    public static final int TYPE_NOTHING = 0;
    public static final int TYPE_PLAYING = 1;
    public static final int TYPE_PAUSE = 2;
    public void updatePlayStatus(int  type){
        playType = type;
        if (playType ==TYPE_NOTHING ){
            mBtTrackBack.setEnabled(true);
            mBtTrackBack.setVisibility(View.VISIBLE);
            mBtPlay.setVisibility(View.GONE);

        }else  if (playType==TYPE_PLAYING) {
            mBtTrackBack.setVisibility(View.GONE);
            mBtPlay.setText("暂停回放");
            mBtTrackBack.setEnabled(false);
            mBtPlay.setVisibility(View.VISIBLE);
        }else if (playType==TYPE_PAUSE){
            mBtTrackBack.setVisibility(View.GONE);
            mBtPlay.setText("继续回放");
            mBtTrackBack.setEnabled(false);
            mBtPlay.setVisibility(View.VISIBLE);
        }
    }

}
