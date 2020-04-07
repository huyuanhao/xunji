package com.jimetec.xunji.map;

import android.content.Context;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;

/**
 * 作者:capTain
 * 时间:2019-07-16 16:45
 * 描述:
 */
public class MyInfoWindowAdapter implements AMap.InfoWindowAdapter {

    Context mContext;

    public MyInfoWindowAdapter(Context context) {
        mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {


        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        return null;
    }
}
