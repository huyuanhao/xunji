package com.jimetec.xunji.ui;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
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
import com.common.baseview.base.AbsCommonActivity;
import com.common.baseview.progress.ProgressDialogHandler;
import com.common.lib.utils.LogUtils;
import com.common.lib.utils.TimeUtils;
import com.common.lib.utils.ToastUtil;
import com.jaeger.library.StatusBarUtil;
import com.jimetec.xunji.R;
import com.jimetec.xunji.bean.FriendBean;
import com.jimetec.xunji.bean.TestUserBean;
import com.jimetec.xunji.presenter.QueryTestPresenter;
import com.jimetec.xunji.presenter.contract.QueryTestContract;
import com.jimetec.xunji.util.DateTimeEntity;
import com.jimetec.xunji.util.UpLocateTimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;

public class QueryTestActivity extends AbsCommonActivity<QueryTestPresenter> implements QueryTestContract.View {

    @BindView(R.id.ivTitleLeft)
    ImageView mIvTitleLeft;
    @BindView(R.id.rlTitleLeft)
    RelativeLayout mRlTitleLeft;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.activity_track_service_map)
    TextureMapView mapView;
    @BindView(R.id.ivAvatar)
    ImageView mIvAvatar;
    @BindView(R.id.tvPhone)
    TextView mTvPhone;
    @BindView(R.id.tvTime)
    TextView mTvTime;
    @BindView(R.id.tvLast)
    TextView mTvLast;
    @BindView(R.id.tvDeal)
    TextView mTvDeal;
    @BindView(R.id.ivReview)
    ImageView mIvReview;
    @BindView(R.id.RlContent)
    RelativeLayout mRlContent;
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
    @BindView(R.id.RlSelectTime)
    RelativeLayout mRlSelectTime;
    @BindView(R.id.tvCardTime)
    TextView mTvCardTime;
    @BindView(R.id.tvCardLast)
    TextView mTvCardLast;
    @BindView(R.id.llSelectTime)
    LinearLayout mLlSelectTime;
    @BindView(R.id.etQuery)
    EditText mEtQuery;
    @BindView(R.id.btQuery)
    Button mBtQuery;
    private FriendBean mBean;
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
        mBean = (FriendBean) getIntent().getSerializableExtra(FriendBean.TAG);
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.getUiSettings().setZoomControlsEnabled(false);
//        drawLastPositionCenter();

    }

    @Override
    public QueryTestPresenter getPresenter() {
        return new QueryTestPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_query_test;
    }


    //    boolean isFirst = true;
    @Override
    public void initViewAndData() {
        initBeanData();

        mStartTime = DateTimeEntity.getSixHourAgo();
        mEndTime = DateTimeEntity.now();
        updateTimeView();
//        mTvTitle.setText(mBean.getFriendNickName() + "的轨迹");
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        StatusBarUtil.setLightMode(this);
//        StatusBarUtil.setLightMode(this);
        mLlSelectTime.setVisibility(View.GONE);
        mRlContent.setVisibility(View.VISIBLE);
        mIvReview.setVisibility(View.VISIBLE);
        mIvReview.setEnabled(true);
//        queryHistory();
    }

    public void initBeanData() {
        mTvTitle.setText(getEventMode());

        if (mBean != null) {
//            sid = mBean.sid;
//            tid = mBean.tid;
//            trid = mBean.trid;
//            avatar = mBean.headImage;
//            times = mBean.lastLocationTimes;
//            mTvLast.setText("最后的位置: " + mBean.lastLocation);
//            mTvCardLast.setText("最后的位置: " + mBean.lastLocation);
//            mTvPhone.setText(mBean.getFriendNickName());
//            mRlContent.setVisibility(View.VISIBLE);
        } else {
//            mRlContent.setVisibility(View.GONE);
//            sid = UserUtil.getUser().sid;
//            tid = UserUtil.getUser().tid;
//            trid = UserUtil.getUserTrid();
//            avatar = UserUtil.getUser().headImage;
//            times = UserUtil.getUser().lastLocationTimes;
//            mTvPhone.setText("自己");
//            mTvLast.setText("最后的位置: " + UserUtil.getUser().lastLocation);

        }

//        ImageManager.loadAvatar(this, avatar, mIvAvatar);
        if (times > 0) {
            mTvTime.setVisibility(View.VISIBLE);
            mTvCardTime.setVisibility(View.VISIBLE);
            mTvTime.setText(TimeUtils.millis2String(times));
            mTvCardTime.setText(TimeUtils.millis2String(times));
        } else {
            mTvTime.setVisibility(View.GONE);
            mTvCardTime.setVisibility(View.GONE);
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

        if (mProgressDialogHandler == null)
            mProgressDialogHandler = new ProgressDialogHandler(false, null, this, "正在查询轨迹...");

        if (mEndTime.getCurrentTimeMillis() - mStartTime.getCurrentTimeMillis() >= 1000 * 60 * 60 * 24) {
            ToastUtil.showShort("查询时间不能大于一天");
            return;
        }
        showProgressDialog();
        LogUtils.e("time  -- start " + mStartTime.getCurrentTimeMillis());
        LogUtils.e("time  -- end" + mEndTime.getCurrentTimeMillis());
        // 搜索最近12小时以内上报的轨迹
        HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest(
//                42998,
//                212140030,
                sid,
                tid,

                mStartTime.getCurrentTimeMillis(),
                mEndTime.getCurrentTimeMillis(),
                0,      // 不绑路
                0,      // 不做距离补偿
                5000,   // 距离补偿阈值，只有超过5km的点才启用距离补偿
                0,  //由旧到新排序
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
                    LogUtils.e("count", count);
                    List<Point> pagePoints = historyTrack.getPoints();
                    if (pagePoints != null && pagePoints.size() > 0) {
                        mAllPoints.addAll(pagePoints);
                    }

                    if (count > pageNum * pageSize) {
                        pageNum += 1;
                        queryHistory();
                    } else {
                        aMap.clear();
                        pageNum = 1;
                        dealPoints(mAllPoints);
                    }
//                    List<Point> pagePoints = historyTrack.getPoints();
//                    Collections.reverse(points);

//                    Point point = points.get(0);


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


//
//
//        // 搜索最近12小时以内上报的属于某个轨迹的轨迹点信息，散点上报不会包含在该查询结果中
//        QueryTrackRequest queryTrackRequest = new QueryTrackRequest(
//                sid,
//                tid,
//                trid,	// 轨迹id，传-1表示查询所有轨迹
//                times - 6 * 60 * 60 * 1000,
//                times,
//                0,      // 不启用去噪
//                0,   // 绑路
//                0,      // 不进行精度过滤
//                DriveMode.DRIVING,  // 当前仅支持驾车模式
//                0,     // 距离补偿
//                5000,   // 距离补偿，只有超过5km的点才启用距离补偿
//                1,  // 结果应该包含轨迹点信息
//                1,  // 返回第1页数据，由于未指定轨迹，分页将失效
//                100    // 一页不超过100条
//        );


//        QueryTrackRequest queryTrackRequest = new QueryTrackRequest(
//                serviceId,
//                terminalId,
//                -1,	// 轨迹id，传-1表示查询所有轨迹
//                System.currentTimeMillis() - 12 * 60 * 60 * 1000,
//                System.currentTimeMillis(),
//                0,      // 不启用去噪
//                bindRoadCheckBox.isChecked() ? 1 : 0,   // 绑路
//                0,      // 不进行精度过滤
//                DriveMode.DRIVING,  // 当前仅支持驾车模式
//                recoupCheckBox.isChecked() ? 1 : 0,     // 距离补偿
//                5000,   // 距离补偿，只有超过5km的点才启用距离补偿
//                1,  // 结果应该包含轨迹点信息
//                1,  // 返回第1页数据，由于未指定轨迹，分页将失效
//                100    // 一页不超过100条
//        );

//
//        aMapTrackClient.queryTerminalTrack(queryTrackRequest, new OnTrackListener() {
//            @Override
//            public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
//
//            }
//
//            @Override
//            public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {
//
//            }
//
//            @Override
//            public void onDistanceCallback(DistanceResponse distanceResponse) {
//
//            }
//
//            @Override
//            public void onLatestPointCallback(LatestPointResponse latestPointResponse) {
//
//            }
//
//            @Override
//            public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {
//
//                if (historyTrackResponse.isSuccess()) {
//                    HistoryTrack historyTrack = historyTrackResponse.getHistoryTrack();
//                    // historyTrack中包含终端轨迹信息
//                    List<Point> points = historyTrack.getPoints();
////                    Point point = points.get(0);
//                    if (points==null)return;
//                    LogUtils.e(points.size());
//                    dealPoints(points);
//
//                } else {
//                    // 查询失败
//                    String errorMsg = historyTrackResponse.getErrorMsg();
//                    LogUtils.e(errorMsg);
//
//                }
//            }
//
//            @Override
//            public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {
//                if (queryTrackResponse.isSuccess()) {
//                    List<Track> tracks =  queryTrackResponse.getTracks();
////                    // 查询成功，tracks包含所有轨迹及相关轨迹点信息
////                    Track track = tracks.get(0);
////                    ArrayList<Point> points = track.getPoints();
////                    Point point = points.get(0);
////                    Point point1 = points.get(points.size()-1);
////
////                    String f = TimeUtils.millis2String(point.getTime());
////                    String last = TimeUtils.millis2String(point1.getTime());
////                    LogUtils.e(f);
////                    LogUtils.e(last);
//                } else {
//
//                }
//            }
//
//            @Override
//            public void onAddTrackCallback(AddTrackResponse addTrackResponse) {
//
//            }
//
//            @Override
//            public void onParamErrorCallback(ParamErrorResponse paramErrorResponse) {
//
//            }
//        });
//


    }

    public void drawLastPositionCenter() {
//        lastLatLng = new LatLng(mBean.latitude, mBean.longitude);
//
//        aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
//                getLastPosition(), 16, 0, 0)), 1000, null);
//        aMap.animateCamera(CameraUpdateFactory.zoomTo(5));
//        setGeniusIcon();
    }


    public LatLng getLastPosition() {
//        double lat = 22.551518;
//        double lng = 113.962783;
//        LatLng latLng = new LatLng(lat, lng);
        lastLatLng = new LatLng(mBean.latitude, mBean.longitude);
        return lastLatLng;
    }

    List<LatLng> latLngs;


    Marker mStartMarker;
    Marker mEndMarker;

    public void dealPoints(List<Point> points) {

        latLngs = new ArrayList<>();

        for (Point p : points) {
            LatLng latLng = new LatLng(p.getLat(), p.getLng());
            latLngs.add(latLng);
        }
        drawLastPositionCenter();
        dismissProgressDialog();
        if (points.size() > 0) {
            // 终点
//            Point p = points.get(points.size() - 1);
//            lastLatLng = new LatLng(p.getLat(), p.getLng());
//            drawLastPositionCenter();


            if (points.size() > 3) {

                Point startPoint = points.get(0);
                LatLng startLatLng = new LatLng(startPoint.getLat(), startPoint.getLng());
                long startTime = startPoint.getTime();
                addMarker(startLatLng, startTime, R.mipmap.icon_start_marker, "开始时间");

                for (int i = 1; i <points.size()-1; i++) {
                    Point midPoint = points.get(i);
                    LatLng midLatLng = new LatLng(midPoint.getLat(), midPoint.getLng());
                    long midTime = midPoint.getTime();
                    addMarker(midLatLng, midTime, R.mipmap.icon_marker_point, "中间点");


                }


                Point endPoint = points.get(points.size() - 1);
                LatLng endLatLng = new LatLng(endPoint.getLat(), endPoint.getLng());
                long endTime = endPoint.getTime();
                addMarker(endLatLng, endTime, R.mipmap.icon_end_marker, "结束时间");


                addPolylineInPlayGround();
//                mIvReview.setVisibility(View.VISIBLE);
            } else {
//                mIvReview.setVisibility(View.GONE);
            }

        } else {
//            lastLatLng = new LatLng(mBean.latitude, mBean.longitude);

        }


        startMove();

//        if (isFirst) {
//
//        } else {
//            startMove();
//        }

    }


    /**
     * 设置网络图片
     */
    public void setGeniusIcon() {

        MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker_point))
                .position(getLastPosition())
                .draggable(true);
        marker = aMap.addMarker(markerOption);
//        RequestOptions options = new RequestOptions()
//                .circleCrop()
//                .priority(Priority.HIGH)
//                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
//        Glide.with(this)
//                .asBitmap()
//                .load(mBean.avatar)
//                .apply(options)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//
////                        MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker_point))
////                                .position(latLng)
////                                .draggable(true);
////                        aMap.addMarker(markerOption);
////                            图标下载成功重新生成自定义大小的Bitmap
//                        Bitmap newBmp = Bitmap.createScaledBitmap(resource, 65, 65, true);
//                        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(newBmp);
//                        drawAvater(bitmapDescriptor);
//
//                    }
//
//                    @Override
//                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                        super.onLoadFailed(errorDrawable);
//                        Bitmap newBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_avatar_deafult), 65, 65, true);
//                        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(newBmp);
//                        drawAvater(bitmapDescriptor);
//
//                    }
//                });
    }

    Marker marker;

    public void drawAvater(BitmapDescriptor bitmapDescriptor) {
        //添加覆盖物
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(getLastPosition());
//                        markerOption.title("小小");
        markerOption.draggable(false);//设置Marker可拖动
        markerOption.icon(
                bitmapDescriptor);
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        marker = mapView.getMap().addMarker(markerOption);
    }


    public void addMarker(LatLng latLng, long time, int resId, String des) {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(resId);
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.title(des + ":");
        markerOption.snippet(TimeUtils.millis2String(time));
        markerOption.draggable(false);//设置Marker可拖动
        markerOption.icon(
                bitmapDescriptor);
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        Marker marker = mapView.getMap().addMarker(markerOption);
//            marker.showInfoWindow();
    }


    public List<LatLng> getLatLngs() {
        return latLngs;
    }

    /**
     * 添加轨迹线
     */
    private void addPolylineInPlayGround() {


        List<LatLng> list = getLatLngs();
//        List<Integer> colorList = new ArrayList<Integer>();
//        List<BitmapDescriptor> bitmapDescriptors = new ArrayList<BitmapDescriptor>();
//
//        int[] colors = new int[]{Color.argb(255, 0, 255, 0), Color.argb(255, 255, 255, 0), Color.argb(255, 255, 0, 0)};
//
//        //用一个数组来存放纹理
//        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
//        textureList.add(BitmapDescriptorFactory.fromResource(R.drawable.custtexture));
//
//        List<Integer> texIndexList = new ArrayList<Integer>();
//        texIndexList.add(0);//对应上面的第0个纹理
//        texIndexList.add(1);
//        texIndexList.add(2);
//
//        Random random = new Random();
//        for (int i = 0; i < list.size(); i++) {
//            colorList.add(colors[random.nextInt(3)]);
//            bitmapDescriptors.add(textureList.get(0));
//
//        }
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

//        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),600));

        if (mIvReview != null)
            mIvReview.setVisibility(View.VISIBLE);
    }


    private MovingPointOverlay smoothMarker;


    /**
     * 开始移动
     */
    public void startMove() {

//
//        if (mPolyline == null) {
//            ToastUtil.showShortToast(this, "请先设置路线");
//            return;
//        }


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

//        aMap.animateCamera(CameraUpdateFactory.newLatLng(points.get(0)), 100, null);
//        aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
//              , 16, 0, 0)), 100, null);
//        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200));


//        Marker marker = null;
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
//                                mIvReview.setVisibility(View.VISIBLE);
//                                mIvReview.setEnabled(true);
                                mIvReview.setVisibility(View.GONE);
                                mBtTrackBack.setEnabled(true);
                                updatePlayStatus(TYPE_NOTHING);
                            }

//                            title.setText("距离终点还有： " + (int) distance + "米");

//                            if (infoWindowLayout != null && title != null) {
//
//                                title.setText("距离终点还有： " + (int) distance + "米");
//                            }
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


    public void popRename() {

        View playView = View.inflate(this, R.layout.pop_item_rename, null);
        final EditText etPhone = (EditText) playView.findViewById(R.id.etPhone);
        Button dialogLeftBtn = (Button) playView.findViewById(R.id.dialog_left_btn);
        Button dialogRightBtn = (Button) playView.findViewById(R.id.dialog_right_btn);


        final PopupWindow mPopupWindow = new PopupWindow(playView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // PopupWindow popupWindow = new PopupWindow(popuView, 100, 100);
        // 设置点击外部区域, 自动隐藏
        mPopupWindow.setOutsideTouchable(true); // 外部可触摸
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
                hideInput(etPhone);
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.dialog_left_btn:
                        if (mPopupWindow != null) {
                            mPopupWindow.dismiss();
                        }

                        break;
                    case R.id.dialog_right_btn:

                        String string = etPhone.getText().toString();
                        if (TextUtils.isEmpty(string)) {
                            ToastUtil.showShort("好友备注不能为空");
                            return;
                        }
                        if (string.length() > 6) {
                            ToastUtil.showShort("好友备注不能超过6位");
                            return;
                        }
                        if (mPopupWindow != null) {
                            mPopupWindow.dismiss();
                        }

                        mPresenter.rename(mBean.id, mBean.getFriendUserId(), string);

                        break;
                }
            }
        };
        if (!TextUtils.isEmpty(mBean.getFriendNickName())) {
            etPhone.setText(mBean.getFriendNickName());
//            etPhone.setSelection(mBean.getFriendNickName().length());
        }

        dialogLeftBtn.setOnClickListener(onClickListener);
        dialogRightBtn.setOnClickListener(onClickListener);
        etPhone.postDelayed(new Runnable() {
            @Override
            public void run() {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etPhone, InputMethodManager.SHOW_FORCED);
            }
        }, 50);
    }


    /**
     * 隐藏键盘
     */
    protected void hideInput(View view) {

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null == imm)
                return;
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//            if (view.getCurrentFocus() != null) {
//                //有焦点关闭
//            } else {x
//                //无焦点关闭
//                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void backName(String info) {
        mBean.setFriendNickName(info);
        mTvTitle.setText(mBean.getFriendNickName() + "的轨迹");
        mTvPhone.setText(mBean.getFriendNickName());

    }

    @Override
    public void backTestUser(TestUserBean bean) {
        mRlContent.setVisibility(View.GONE);
        mLlSelectTime.setVisibility(View.VISIBLE);
        mIvReview.setVisibility(View.GONE);
        mIvReview.setEnabled(false);

        sid = bean.sid;
        tid = bean.tid;
        trid = bean.trid;
    }


    @Override
    public String getEventMode() {
        if (mBean == null) {
            return "我的轨迹";
        }

        return "好友轨迹";
    }

    @OnClick({R.id.rlTitleLeft, R.id.ivReview, R.id.tvDeal, R.id.flStart, R.id.flEnd, R.id.btTrackBack,
            R.id.btPlay,  R.id.btQuery})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlTitleLeft:
                finish();
//                if (isMoving){
//                    isMoving=false;
//                    smoothMarker.stopMove();
//                }else {
//                    isMoving=true;
//                    smoothMarker.startSmoothMove();
//                }


                break;
            case R.id.ivReview:
//                startMove();
                mRlContent.setVisibility(View.GONE);
                mLlSelectTime.setVisibility(View.VISIBLE);
                mIvReview.setVisibility(View.GONE);
                mIvReview.setEnabled(false);
                break;
            case R.id.tvDeal:
                popRename();
                break;
            case R.id.flStart:
                selectTime(mStartTime);
                break;
            case R.id.flEnd:
                selectTime(mEndTime);
                break;
            case R.id.btPlay:
                if (playType == TYPE_PLAYING) {
                    updatePlayStatus(TYPE_PAUSE);
                    if (smoothMarker != null) smoothMarker.stopMove();
                } else if (playType == TYPE_PAUSE) {
                    updatePlayStatus(TYPE_PLAYING);
                    if (smoothMarker != null) smoothMarker.startSmoothMove();
                }
                break;
            case R.id.btQuery:
                String phone = mEtQuery.getText().toString();
                if (TextUtils.isEmpty(phone) || phone.length()<10){

                    return;
                }
                hideInput(mEtQuery);
                mPresenter.queryTest(phone);
                break;
            case R.id.btTrackBack:
                mBtTrackBack.setEnabled(false);
                queryHistory();
//                if (isFirst){
//                    startMove();
//                    isFirst = false;
//                }else {
//
//                }


//                Calendar calendar = Calendar.getInstance();
//                int year = calendar.get(Calendar.YEAR);
//                int month = calendar.get(Calendar.MONTH) + 1;
//                int day = calendar.get(Calendar.DAY_OF_MONTH);
////                int hour = calendar.get(Calendar.HOUR_OF_DAY);
////                int minute = calendar.get(Calendar.MINUTE);


                break;
        }
    }


    int playType = TYPE_NOTHING;  // 0  没什么事做  1  播放状态  2 暂停状态
    public static final int TYPE_NOTHING = 0;
    public static final int TYPE_PLAYING = 1;
    public static final int TYPE_PAUSE = 2;

    public void updatePlayStatus(int type) {
        playType = type;
        if (playType == TYPE_NOTHING) {
            mBtTrackBack.setEnabled(true);
            mBtTrackBack.setVisibility(View.VISIBLE);
            mBtPlay.setVisibility(View.GONE);

        } else if (playType == TYPE_PLAYING) {
            mBtTrackBack.setVisibility(View.GONE);
            mBtPlay.setText("暂停回放");
            mBtTrackBack.setEnabled(false);
            mBtPlay.setVisibility(View.VISIBLE);
        } else if (playType == TYPE_PAUSE) {
            mBtTrackBack.setVisibility(View.GONE);
            mBtPlay.setText("继续回放");
            mBtTrackBack.setEnabled(false);
            mBtPlay.setVisibility(View.VISIBLE);
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


    public void updateTimeView() {
        mTvStartDay.setText(mStartTime.getYMD());
        mTvStartTime.setText(mStartTime.getHM());

        mTvEndDay.setText(mEndTime.getYMD());
        mTvEndTime.setText(mEndTime.getHM());
        mIvReview.setVisibility(View.GONE);
        updatePlayStatus(TYPE_NOTHING);

//        // 读取轨迹点
//        List<LatLng> points = getLatLngs();
//        if (points == null || points.size() < 3) {
//
//            return;
//        }
    }


}
