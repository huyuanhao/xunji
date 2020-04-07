package com.jimetec.xunji.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.common.baseview.base.AbsLoadActivity;
import com.common.baseview.dialog.CommonDialogFragment;
import com.common.lib.utils.ToastUtil;
import com.jaeger.library.StatusBarUtil;
import com.jimetec.xunji.R;
import com.jimetec.xunji.adapter.LocationWarnAdapter;
import com.jimetec.xunji.bean.FriendBean;
import com.jimetec.xunji.bean.LocationWarnBean;
import com.jimetec.xunji.presenter.LocationWarnPresenter;
import com.jimetec.xunji.presenter.contract.LocationWarnContract;
import com.jimetec.xunji.util.UpLocateTimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LocationWarnActivity extends AbsLoadActivity<LocationWarnPresenter> implements LocationWarnContract.View, AMap.OnMarkerClickListener {

    @BindView(R.id.rlTitleLeft)
    RelativeLayout mRlTitleLeft;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    TextView mTvDistanceWarn;
    @BindView(R.id.ivTitleRight)
    ImageView mIvTitleRight;
    @BindView(R.id.ivRemind)
    ImageView mIvRemind;
    @BindView(R.id.rlTitleRight)
    RelativeLayout mRlTitleRight;
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.btNewWarn)
    Button mBtNewWarn;
    private FriendBean mFriendBean;
    private LocationWarnAdapter mAdapter;
    private AMap mMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public LocationWarnPresenter getPresenter() {

        return new LocationWarnPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_location_warn;
    }

    @Override
    public void initViewAndData() {
        initMap();
        mTvTitle.setText("位置提醒");
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        StatusBarUtil.setLightMode(this);
        View emptyClick = findViewById(R.id.emptyClick);
        if (emptyClick != null) {
            emptyClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addLocationWarn();
                }
            });
        }

        mFriendBean = (FriendBean) getIntent().getSerializableExtra(FriendBean.TAG);
        if (mFriendBean ==null){
            finish();
            return;
        }

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        mAdapter = new LocationWarnAdapter(this);
        mRv.setAdapter(mAdapter);
        mAdapter.setListener(new LocationWarnAdapter.OnLocationWarnSelectedListener() {


            @Override
            public void onLocationWarnSelected(LocationWarnBean bean, int position) {
                mAdapter.setIndex(position);
                if (mMarkers.size() > position) {
//                    moveMarkerCenter(new LatLng(bean.latitude,bean.longitude));
                    moveMarkerCenter(mMarkers.get(position));

                }
            }

            @Override
            public void onLocationWarnDelete(LocationWarnBean bean) {
//                mPresenter.deleteFriendWarn(bean.id);
                popDeleteTip(bean);
            }
        });


        loadingNetData();
    }

    /**
     * 初始化AMap对象
     */
    private void initMap() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
        }
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
        mMap.setOnMarkerClickListener(this);
    }


    @Override
    public void loadingNetData() {
//        super.loadingNetData();
        mPresenter.getLocationWarns(mFriendBean.getFriendUserId());
    }

    List<LocationWarnBean> pageBeans;
    List<Marker> mMarkers = new ArrayList<>();
    TextView tvPopAddress;

    public void addLocationWarn() {
        mLocationWarnBean = null;
        View playView = View.inflate(this, R.layout.pop_add_location_warn, null);
        final EditText etNickName = (EditText) playView.findViewById(R.id.etNickName);
        tvPopAddress = (TextView) playView.findViewById(R.id.tvAddress);
        Button btSubmit = (Button) playView.findViewById(R.id.btSubmit);
        ImageView ivPopClose = (ImageView) playView.findViewById(R.id.ivPopClose);
//        etNickName.setText(UserUtil.getNickName());
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
                hideInput(etNickName);
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.tvAddress:
                        startActivityForResult(new Intent(mActivity, SearchLocationActivity.class), 1);
                        break;
                    case R.id.btSubmit:
//                        String phone = etPhone.getText().toString();
                        String nickName = etNickName.getText().toString();
                        if (TextUtils.isEmpty(nickName)) {
                            ToastUtil.showShort("请输入自定义地点名称");
                            return;
                        }
                        if (mLocationWarnBean == null) {
                            ToastUtil.showShort("提醒位置为空,请去搜索位置");
                            return;
                        }
                        mLocationWarnBean.remark = nickName;
                        addWarn();
                        if (mPopupWindow != null) {
                            mPopupWindow.dismiss();
                        }
                        break;
                    case R.id.ivPopClose:
                        if (mPopupWindow != null) {
                            mPopupWindow.dismiss();
                        }
                        break;
                }
            }
        };

        tvPopAddress.setOnClickListener(onClickListener);
        btSubmit.setOnClickListener(onClickListener);
        ivPopClose.setOnClickListener(onClickListener);
        etNickName.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etNickName, InputMethodManager.SHOW_FORCED);
            }
        }, 50);
    }


    public void addWarn() {
        mPresenter.addFriendWarn(mFriendBean.getFriendUserId(), mFriendBean.getFriendNickName(), mLocationWarnBean.remark, mLocationWarnBean.location, mLocationWarnBean.longitude, mLocationWarnBean.latitude, 1);
    }

    LocationWarnBean mLocationWarnBean;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            mLocationWarnBean = (LocationWarnBean) data.getSerializableExtra(LocationWarnBean.TAG);
            if (tvPopAddress != null) tvPopAddress.setText(mLocationWarnBean.location);
        }
    }


    @Override
    public int getEmptyResId() {
        return R.layout.layout_empty_location_warn;
    }

    @Override
    public boolean isOnlyFirst() {
        return false;
    }

    @OnClick({R.id.rlTitleLeft, R.id.btNewWarn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlTitleLeft:
                finish();
                break;
            case R.id.btNewWarn:
                addLocationWarn();
                break;
        }
    }

    @Override
    public void backLocationWarns(List<LocationWarnBean> pageBean) {
        pageBeans = pageBean;
        if (pageBean.size() == 0) {
            showEmptyPage();
            mTvDistanceWarn = findViewById(R.id.tvDistanceWarn);
            if (mTvDistanceWarn!=null){
                String sAgeFormat = getResources().getString(R.string.distance_warn);
                String sFinalAge = String.format(sAgeFormat, UpLocateTimeUtil.getInstance().locationRemind);
                mTvDistanceWarn.setText(sFinalAge);
            }
        } else {
            showSuccessPage();
            mAdapter.setData(pageBean);
            addMarker(pageBean);
            if (pageBean.size() > 2) {
                mBtNewWarn.setVisibility(View.GONE);
            } else {
                mBtNewWarn.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void backAddWarn(Object object) {
        ToastUtil.showShort("位置添加成功");
        loadingNetData();
    }

    @Override
    public void backDeleteWarn(Object object) {
        ToastUtil.showShort("位置提醒删除成功");
        loadingNetData();
    }

    public void popDeleteTip(final LocationWarnBean bean) {
        new CommonDialogFragment.Builder()
                .showTitle(true)
                .setTitleText("是否删除位置")
                .setBtLeftColor(R.color.colorPrimary)
                .setBtRightColor(R.color.color_F5445C)
                .setContentText("删除后TA到达或离开该位置您将不再收到提示。")
                .setRightButtonText("确认删除")
                .setRightButtonClickListener(new CommonDialogFragment.OnClickListener() {
                    @Override
                    public void onClick(CommonDialogFragment dialogFragment, int which, String content) {
                        dialogFragment.dismiss();
                        mPresenter.deleteFriendWarn(bean.id);

                    }


                })
                .create()
                .show(getSupportFragmentManager());
    }

    public void addMarker(List<LocationWarnBean> beans) {
        mMap.clear();
        mMarkers.clear();
        LatLng latLng = null;
        for (int i = 0; i < beans.size(); i++) {
            LocationWarnBean bean = beans.get(i);
            latLng = new LatLng(bean.latitude, bean.longitude);
            if (i == mAdapter.getIndex()) {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        latLng, 16, 0, 0)), 1500, null);
            }
            MarkerOptions markerOption = new MarkerOptions().anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker_point))
                    .position(latLng)
                    .draggable(false);
            mMap.addCircle(new CircleOptions().center(latLng)
                    .radius(200).strokeColor(Color.argb(50, 1, 1, 1))
                    .fillColor(Color.argb(50, 66, 135, 255)).strokeWidth(0));
            Marker marker = mMap.addMarker(markerOption);
            markerOption.setFlat(true);
            mMarkers.add(marker);
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
//        jumpPoint(marker);

//        moveMarkerCenter(marker.getPosition());
        startGrowAnimation(marker);
        return true;
    }


    public void moveMarkerCenter(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                latLng, 16, 0, 0)), 500, null);
    }


    /**
     * 地上生长的Marker
     */
    private void startGrowAnimation(Marker growMarker) {
        if (growMarker != null) {
            Animation animation = new ScaleAnimation(0, 1, 0, 1);
            animation.setInterpolator(new LinearInterpolator());
            //整个移动所需要的时间
            animation.setDuration(1000);
            //设置动画
            growMarker.setAnimation(animation);
            //开始动画
            growMarker.startAnimation();
        }
    }

    public void moveMarkerCenter(Marker growMarker) {
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                growMarker.getPosition(), 16, 0, 0)), 500, null);
        startGrowAnimation(growMarker);
    }

    @Override
    public String getEventMode() {
        return "位置提醒";
    }

}
