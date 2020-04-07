package com.jimetec.xunji.ui;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.MaskFilterSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.Projection;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.common.baseview.base.AbsCommonFragment;
import com.common.lib.utils.TimeUtils;
import com.jimetec.xunji.R;
import com.jimetec.xunji.bean.FriendBean;
import com.jimetec.xunji.bean.UserBean;
import com.jimetec.xunji.presenter.TrackPresenter;
import com.jimetec.xunji.presenter.contract.TrackContract;
import com.jimetec.xunji.util.ImageManager;
import com.jimetec.xunji.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackFragment extends AbsCommonFragment<TrackPresenter> implements TrackContract.View, AMap.OnMarkerClickListener {


    @BindView(R.id.activity_track_service_map)
    TextureMapView mapView;
    @BindView(R.id.ivLocation)
    ImageView mIvLocation;
//    @BindView(R.id.rlTitleLeft)
//    RelativeLayout mRlTitleLeft;
//    @BindView(R.id.tvTitle)
//    TextView mTvTitle;


    @BindView(R.id.ivAvatar)
    ImageView mIvAvatar;
    @BindView(R.id.tvPhone)
    TextView mTvPhone;
    @BindView(R.id.tvTime)
    TextView mTvTime;
    @BindView(R.id.tvLast)
    TextView mTvLast;
    @BindView(R.id.rlCardInfo)
    RelativeLayout mRlCardInfo;
    @BindView(R.id.tvDeal)
    TextView mTvDeal;
    private AMap aMap;

    public TrackFragment() {

    }


    boolean isFirst = true;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.getMap().moveCamera(CameraUpdateFactory.zoomTo(18));
        mapView.onCreate(savedInstanceState);
        // 启用地图内置定位
        aMap = mapView.getMap();
        mapView.getMap().setMyLocationEnabled(true);
        mapView.getMap().setOnMarkerClickListener(this);

    }


    @Override
    protected void onFragmentResume(boolean isFirst, boolean isViewDestroyed) {
        super.onFragmentResume(isFirst, isViewDestroyed);
        if (mapView != null) {
            mapView.onResume();
        }
        if (UserUtil.isLogined()) {
            mPresenter.getFriends();
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_track;
    }

    @Override
    public TrackPresenter getPresenter() {
        return new TrackPresenter(getActivity());
    }

    @Override
    public void initViewAndData() {
//        mRlTitleLeft.setVisibility(View.GONE);
//        mTvTitle.setText("轨迹");
        MyLocationStyle myLocationStyle = new MyLocationStyle()
                .interval(2000)
                .myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
//                .myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_pop_close))
//                .anchor(0.5f,0.5f)
                ;
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        mapView.getMap().setMyLocationStyle(myLocationStyle);
        mapView.getMap().setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (isFirst) {
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();

                    mapView.getMap().animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                            new LatLng(lat, lng), 18, 0, 0)), 1000, null);
                    isFirst = false;
                }

            }
        });

      /*  mapView.getMap().setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {

                View inflate = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_marker_title, null);
                TextView textView = inflate.findViewById(R.id.tvMarkerName);
                textView.setText(marker.getTitle());
                if (TextUtils.isEmpty(marker.getTitle())) inflate.setVisibility(View.GONE);
                return inflate;
            }

            @Override
            public View getInfoContents(Marker marker) {
//                View inflate = LayoutInflater.from(getActivity()).inflate(
//                        R.layout.item_marker_title, null);
//                TextView textView = inflate.findViewById(R.id.tvMarkerName);

                return null;
            }
        });*/

//        setGeniusIcon();
//        make();
        mapView.getMap().getUiSettings().setZoomControlsEnabled(false);
        mapView.getMap().getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
//        mapView.getMap().getUiSettings().setMyLocationButtonEnabled(true);


    }


    public void make() {
        double lat = 22.541518;
        double lng = 113.952783;
        ArrayList<MarkerOptions> markerOptionlst = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            lat += 0.01;
            lng += 0.01;
            LatLng latLng = new LatLng(lat, lng);
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.position(latLng);
            markerOption.title("小小");
            markerOption.draggable(false);//设置Marker可拖动
            markerOption.icon(
//                    BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.icon_marker_point)));
                    BitmapDescriptorFactory.fromPath(""));
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            markerOption.setFlat(false);//设置marker平贴地图效果
            Marker marker = mapView.getMap().addMarker(markerOption);
//            marker.showInfoWindow();
//            markerOptionlst.add(markerOption);
        }
//        mapView.getMap().addMarkers(markerOptionlst, true);

    }


    @Override
    protected void onFragmentPause() {
        super.onFragmentPause();
        if (mapView != null)
            mapView.onPause();
    }


    @Override
    public void onDestroyView() {
        if (mapView != null) {
            mapView.onDestroy();
        }
        super.onDestroyView();
    }


    @Override
    public void backFriends(List<FriendBean> friendBeans) {
        mapView.getMap().clear(true);
        hasInfoWindow = false;
        for (FriendBean bean : friendBeans) {
            if (bean.status == 1)
                setFriendPoint(bean);
        }
        mCurBean = null;
        updateCurInfo();
    }


    /**
     * 设置网络图片
     *
     * @param bean
     */
    public void setFriendPoint(final FriendBean bean) {

        RequestOptions options = new RequestOptions()
                .circleCrop()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);


        Glide.with(getActivity())
                .asBitmap()
                .load(bean.headImage)
                .apply(options)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                            图标下载成功重新生成自定义大小的Bitmap
                        Bitmap newBmp = Bitmap.createScaledBitmap(resource, 65, 65, true);
                        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(newBmp);
                        //添加覆盖物
                        addMarker(bitmapDescriptor, bean);
                    }

//                        @Override
//                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                            super.onLoadFailed(e, errorDrawable);
//                            //图片加载失败显示本地默认图标
////                            Bitmap newBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.homepage_qeebike_icon), DisplayUtil.dip2px(CtxHelper.getApp(), 50f), DisplayUtil.dip2px(CtxHelper.getApp(), 50f), true);
////                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(newBmp);
////                            addMarker(bike, bitmapDescriptor);
//                        }
//
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//
//                        }
                });
    }


    boolean hasInfoWindow;

    public void addMarker(BitmapDescriptor bitmapDescriptor, FriendBean bean) {
        double lat = bean.latitude;
        double lng = bean.longitude;
        LatLng latLng = new LatLng(lat, lng);
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.title(bean.getFriendNickName());
        markerOption.draggable(false);//设置Marker可拖动
        markerOption.icon(
                bitmapDescriptor);
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        Marker marker = mapView.getMap().addMarker(markerOption);
        marker.setObject(bean);
/*//        if (!hasInfoWindow) {
//            marker.showInfoWindow();
//            hasInfoWindow = true;
//        }*/


//        ArrayList<MarkerOptions> markerOptionlst = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            lat += 0.01;
//            lng += 0.01;

//            markerOptionlst.add(markerOption);
//        }
//        mapView.getMap().addMarkers(markerOptionlst, true);
    }


    @Override
    public String getEventMode() {

        return "轨迹";
    }


    @OnClick({R.id.ivLocation, R.id.tvDeal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvDeal:
                if (mCurBean == null){
                    if (UserUtil.getUser().lastLocationTimes > 0) {
                        Intent last = new Intent(getActivity(), MyLastTrackActivity.class);
                        startActivity(last);
                    }
                }else {
                    if (UserUtil.isVip()){
                        Intent intent = new Intent(getActivity(), LastTrackActivity.class);
                        intent.putExtra(FriendBean.TAG, mCurBean);
                        startActivity(intent);
                    }else {
                        popNoVip();
                    }
                }
                break;
            case R.id.ivLocation:
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_location);
                mIvLocation.startAnimation(animation);
                if (!isFirst)
                    isFirst = true;
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (aMap != null) {
//            jumpPoint(marker);
            moveMarkerCenter(marker);


        }
        mCurBean = (FriendBean) marker.getObject();

//        if (object == null) {
//            ToastUtil.showShort(marker.toString());
//        } else {
//            ToastUtil.showShort(object.getFriendNickName());
//        }

        updateCurInfo();
        return true;
    }


//    /**
//     * 地上生长的Marker
//     */
//    private void startGrowAnimation(Marker growMarker) {
//        if (growMarker != null) {
//            com.amap.api.maps.model.animation.Animation animation = new ScaleAnimation(0, 1, 0, 1);
//            animation.setInterpolator(new LinearInterpolator());
//            //整个移动所需要的时间
//            animation.setDuration(1000);
//            //设置动画
//            growMarker.setAnimation(animation);
//            //开始动画
//            growMarker.startAnimation();
//        }
//    }

    public void moveMarkerCenter(Marker growMarker) {
        aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                growMarker.getPosition(), 18, 0, 0)), 500, null);
        jumpPoint(growMarker);
    }


    FriendBean mCurBean;

    public void updateCurInfo() {
        if (mCurBean == null) {
            mTvPhone.setText("我自己");
            UserBean user = UserUtil.getUser();
            if (user != null && user.lastLocationTimes > 0) {
                mRlCardInfo.setVisibility(View.VISIBLE);
                ImageManager.loadAvatar(mContext, user.headImage, mIvAvatar);
                mTvTime.setVisibility(View.VISIBLE);
                mTvTime.setText(TimeUtils.millis2String(user.lastLocationTimes));
                mTvLast.setVisibility(View.VISIBLE);
                mTvLast.setText("最后的位置: " + user.lastLocation);
            } else {
                mRlCardInfo.setVisibility(View.GONE);
            }
        } else {
            mTvPhone.setText(mCurBean.getFriendNickName());
            if (mCurBean.status == 1) {
                if (mCurBean.lastLocationTimes > 0) {
                    String time = TimeUtils.millis2String(mCurBean.lastLocationTimes);
                    if (UserUtil.isVip()) {
                        mTvTime.setText(TimeUtils.millis2String(mCurBean.lastLocationTimes));
                        mTvLast.setText("最后的位置: " + mCurBean.lastLocation);
                    } else {
                        blurText(mTvLast, "最后的位置: " + mCurBean.lastLocation, 7);
                        blurText(mTvTime, time, 0);
                    }
                    mTvTime.setVisibility(View.VISIBLE);
                    mTvLast.setVisibility(View.VISIBLE);
                } else {
                    mTvTime.setVisibility(View.GONE);
//                    mTvLast.setText("暂未获取到位置");
                    blurText(mTvLast, "最后的位置: 暂未获取到位置", 7);

                }
                ImageManager.loadAvatar(mContext, mCurBean.headImage, mIvAvatar);
            }

        }
    }

    public void blurText(TextView tv, String text, int startIndex) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //View从API Level 11才加入setLayerType方法 //设置View以软件渲染模式绘图   
            tv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        SpannableString stringBuilder = new SpannableString(text);
        stringBuilder.setSpan(new MaskFilterSpan(new BlurMaskFilter(12f, BlurMaskFilter.Blur.NORMAL)), startIndex, stringBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setText(stringBuilder);

    }


    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * markerLatlng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }




    public void popNoVip() {


        View playView = View.inflate(getActivity(), R.layout.pop_no_vip, null);
        TextView tvContent = (TextView) playView.findViewById(R.id.tvContent);

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
        mPopupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        final WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.5f;
        getActivity().getWindow().setAttributes(params);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getActivity().getWindow().setAttributes(params);

            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.dialog_left_btn:

                        break;
                    case R.id.dialog_right_btn:
                        MyWebViewActivity.startToAfterVip(mContext);

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


}

