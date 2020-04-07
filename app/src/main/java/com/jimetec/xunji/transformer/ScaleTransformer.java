package com.jimetec.xunji.transformer;

import android.view.View;

/**
 * Created by XGJY on 2017/11/3.
 */
public class ScaleTransformer extends BaseTransformer {
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.95f;

    @Override
    public void handleInvisiblePage(View view, float position) {
        view.setAlpha(MIN_ALPHA);
    }

    @Override
    public void handleLeftPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
        float vertMargin = pageHeight * (1 - scaleFactor) / 2;
        float horzMargin = pageWidth * (1 - scaleFactor) / 2;
        view.setTranslationX((horzMargin - vertMargin)*1.3f );
        view.setScaleX(scaleFactor);
        view.setScaleY(scaleFactor);
        view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));


    }

    @Override
    public void handleRightPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
        float vertMargin = pageHeight * (1 - scaleFactor) / 2;
        float horzMargin = pageWidth * (1 - scaleFactor) / 2;
        view.setTranslationX((-horzMargin + vertMargin)*1.3f );
//        ViewCompat.setTranslationX(view, (-horzMargin + vertMargin)*1.3f );

        view.setScaleX(scaleFactor);
        view.setScaleY(scaleFactor);
        view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
    }
}