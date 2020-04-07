package com.jimetec.xunji.transformer;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 作者:zh
 * 时间:12/29/18 4:50 PM
 * 描述:
 */
public abstract class BaseTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(@NonNull View view, float position) {
        if (position < -1.0f) {
            // [-Infinity,-1)
            // This page is way off-screen to the left.
            handleInvisiblePage(view, position);
        } else if (position <= 0.0f) {
            // [-1,0]
            // Use the default slide transition when moving to the left page
            handleLeftPage(view, position);
        } else if (position <= 1.0f) {
            // (0,1]
            handleRightPage(view, position);
        } else {
            // (1,+Infinity]
            // This page is way off-screen to the right.
            handleInvisiblePage(view, position);
        }
    }



    public abstract void handleInvisiblePage(View view, float position);

    public abstract void handleLeftPage(View view, float position);

    public abstract void handleRightPage(View view, float position);
}
