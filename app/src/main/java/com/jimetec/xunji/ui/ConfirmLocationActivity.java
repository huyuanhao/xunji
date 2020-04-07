package com.jimetec.xunji.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.common.baseview.base.BaseActivity;
import com.common.baseview.progress.ProgressDialogHandler;
import com.common.lib.utils.ToastUtil;
import com.jaeger.library.StatusBarUtil;
import com.jimetec.xunji.R;
import com.jimetec.xunji.bean.LocationWarnBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmLocationActivity extends BaseActivity {

    public static final String TAG = "ConfirmLocationActivity";
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
    //    @BindView(R.id.mapView)
//    TextureMapView mapView;
    @BindView(R.id.tvTop)
    TextView mTvTop;
    @BindView(R.id.tvBottom)
    TextView mTvBottom;
    @BindView(R.id.btSubmit)
    Button mBtSubmit;
    private PoiItem mPoiItem;
    private AMap mMap;
    private GeocodeSearch mGeocodeSearch;
    LocationWarnBean locationWarnBean = new LocationWarnBean();
    private ProgressDialogHandler mProgressDialogHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_location);
        ButterKnife.bind(this);
        mTvTitle.setText("确认位置");
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        StatusBarUtil.setLightMode(this);
        mGeocodeSearch = new GeocodeSearch(this);
        mPoiItem = getIntent().getParcelableExtra(TAG);
        mTvTop.setText(mPoiItem.getTitle());
        String address = mPoiItem.getProvinceName() + mPoiItem.getCityName();
        if (mPoiItem.getAdName().equalsIgnoreCase(mPoiItem.getSnippet())) {
            address += mPoiItem.getAdName();
        } else {
            address += mPoiItem.getAdName() + mPoiItem.getSnippet();
        }
        mTvBottom.setText(address);
//        mMap = mapView.getMap();
//        mMap.
        initMap();
        locationWarnBean.setLocation(mPoiItem.getTitle());
        mGeocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                dismissProgressDialog();
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (result != null && result.getRegeocodeAddress() != null
                            && result.getRegeocodeAddress().getFormatAddress() != null) {
                        String address = result.getRegeocodeAddress().getFormatAddress();
//                        RegeocodeAddress regeocodeAddress = result.getRegeocodeAddress();
//                        String building = regeocodeAddress.getBuilding();
//                        String neighborhood = regeocodeAddress.getNeighborhood();
//                        String township = regeocodeAddress.getTownship();
//                        regeocodeAddress.get
                        if (TextUtils.isEmpty(address)){
                            mTvTop.setText("请选择地址");
                            mTvBottom.setText("");
                            locationWarnBean.setLocation("");
                            return;
                        }
                        address+="附近";
                        LatLonPoint point = result.getRegeocodeQuery().getPoint();
                        locationWarnBean.setLatitude(point.getLatitude());
                        locationWarnBean.setLongitude(point.getLongitude());
                        locationWarnBean.setLocation(address);
                        mTvTop.setText(address);
                        mTvBottom.setText("");
                        mTvBottom.setVisibility(View.GONE);
//                        mTvBottom.setText(building+"-"+neighborhood+"-"+township);
//                        mTvBottom.setVisibility(View.GONE);
//                        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                                AMapUtil.convertToLatLng(latLonPoint), 15));
//                        regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
//                        ToastUtil.show(ReGeocoderActivity.this, addressName);
                    } else {
                        ToastUtil.showShort("该位置无数据展示");
                    }
                } else {
                    ToastUtil.showShort("查询失败");
                }

            }

            @Override
            public void onGeocodeSearched(GeocodeResult result, int rCode) {

            }
        });

    }

    /**
     * 初始化AMap对象
     */
    private void initMap() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
        }
        LatLonPoint latLonPoint = mPoiItem.getLatLonPoint();
        LatLng latLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
        locationWarnBean.setLatitude(latLng.latitude);
        locationWarnBean.setLongitude(latLng.longitude);
        setPosition(latLng);
        mMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                getAddress(latLng);
                setPosition(latLng);
            }
        });
    }


    public void setPosition(LatLng latLng) {
        mMap.clear();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                latLng, 16, 0, 0)), 1000, null);

        MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker_point))
                .position(latLng)
                .draggable(true);
        mMap.addMarker(markerOption);
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(LatLng latLng) {

        if (mProgressDialogHandler == null)
            mProgressDialogHandler = new ProgressDialogHandler(false, null, this, "正在查询地址信息...");
        showProgressDialog();
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 50,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        mGeocodeSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求

    }

    @OnClick({R.id.rlTitleLeft, R.id.btSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlTitleLeft:
                finish();
                break;
            case R.id.btSubmit:
               if(TextUtils.isEmpty(locationWarnBean.location)){
                   ToastUtil.showShort("点击地图,选择地址");
               }else {
                   Intent intent = new Intent( );
                   intent.putExtra(LocationWarnBean.TAG,locationWarnBean);
                   setResult(Activity.RESULT_OK,intent);
                   finish();

               }

                break;
        }
    }


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


    @Override
    public String getEventMode() {
        return "确认位置";
    }
}
