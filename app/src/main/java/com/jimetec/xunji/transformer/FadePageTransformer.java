package com.jimetec.xunji.transformer;

import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * 作者:zh
 * 时间:12/29/18 4:52 PM
 * 描述:
 */
public class FadePageTransformer extends BaseTransformer {
    @Override
    public void handleInvisiblePage(View view, float position) {

    }
    @Override
    public void handleLeftPage(View view, float position) {
        ViewCompat.setTranslationX(view, -view.getWidth() * position);
        ViewCompat.setAlpha(view, 1 + position);
    }

    @Override
    public void handleRightPage(View view, float position) {
        ViewCompat.setTranslationX(view, -view.getWidth() * position);
        ViewCompat.setAlpha(view, 1 - position);
    }
}
