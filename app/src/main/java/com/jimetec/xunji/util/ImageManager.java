package com.jimetec.xunji.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.jimetec.xunji.R;


/**
 * 图片获取统一管理类
 */
public class ImageManager {
    static RequestOptions defaultOption = new RequestOptions()
            .centerCrop()

            .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

    static DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();


    /**
     * 加载图片
     *
     * @param imgUrl
     * @param imageView
     */
    public static void loadAvatar(Context context ,String imgUrl, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .circleCrop()
                .placeholder(R.mipmap.icon_avatar_deafult)
                .error(R.mipmap.icon_avatar_deafult)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(imgUrl).apply(options)

                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                .into(imageView);
    }


    /**
     * 加载图片
     * @param imageView
     */
    public static void loadAvatar(Context context ,int resId, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .circleCrop()
                .placeholder(R.mipmap.icon_avatar_deafult)
                .error(R.mipmap.icon_avatar_deafult)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(resId).apply(options)
                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                .into(imageView);
    }


    /**
     * 加载图片
     *
     * @param imgUrl
     * @param imageView
     */
    public static void loadDefault(Context context ,String imgUrl, ImageView imageView) {
        Glide.with(context).load(imgUrl).apply(defaultOption)

                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                .into(imageView);
    }


    /**
     * 加载图片
     *
     * @param resId
     * @param imageView
     */
    public static void loadDefault(Context context ,int resId, ImageView imageView) {
        Glide.with(context).load(resId).apply(defaultOption)
                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                .into(imageView);
    }
        /**
     * 加载图片
     *
     * @param imgUrl
     * @param imageView
     */
    public static void load(Context context ,String imgUrl, ImageView imageView) {
        Glide.with(context).load(imgUrl).apply(new RequestOptions()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                .into(imageView);
    }





}
