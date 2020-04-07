package com.common.lib.utils;

import android.content.Context;
import android.os.Environment;

/**
 * 作者:zh
 * 时间:1/4/19 2:31 PM
 * 描述:
 */
public class FilePathUtil {


    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }

//        LogUtil.e("cachePath"+cachePath);
        return cachePath;
    }
}
