package com.common.lib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Set;

/**
 * 存储xml键值对
 * Created by pc on 2017/12/26.
 */
public class SpUtil {

    private static final String SHAREDNAME 	= "prefrences";


    /**
     * put一个string键值对
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean putString(String key, String value) {

        SharedPreferences prefence = Utils.getApp().getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor eidtor = prefence.edit();
        eidtor.putString(key, value);
        return eidtor.commit();
    }

    /**
     * put一个set键值对
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean putStringSet(String key, Set<String> value) {

        SharedPreferences prefence = Utils.getApp().getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor eidtor = prefence.edit();
        eidtor.putStringSet(key, value);
        return eidtor.commit();
    }

    /**
     * put一个string键值对
     *
     * @param key
     * @return
     */
    public static boolean removeKey(String key) {

        SharedPreferences prefence = Utils.getApp().getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor eidtor = prefence.edit();
        eidtor.remove(key);
        return eidtor.commit();
    }

    /**
     * put一个string键值对
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean putBoolean(String key, boolean value) {

        SharedPreferences prefence = Utils.getApp().getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor eidtor = prefence.edit();
        eidtor.putBoolean(key, value);
        return eidtor.commit();
    }

    /**
     * 获取string 值
     * @param key
     * @return
     */
    public static String getString(String key) {
        SharedPreferences prefence = Utils.getApp().getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);
        return prefence.getString(key, "");
    }

    /**
     * 获取string Set
     * @param key
     * @return
     */
    public static Set<String> getStringSet(String key) {
        SharedPreferences prefence = Utils.getApp().getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);
        return prefence.getStringSet(key, null);
    }

    /**
     * 获取string 值
     * @param key
     * @return
     */
    public static String getString(String key, String defaultValue) {
        SharedPreferences prefence = Utils.getApp().getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);
        return prefence.getString(key, defaultValue);
    }

    /**
     * 获取int 值
     * @param key
     * @return
     */
    public static int getInt(String key, int defaultValue) {
        SharedPreferences prefence = Utils.getApp().getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);
        return prefence.getInt(key, defaultValue);
    }
    /**
     * 获取long 值
     * @param key
     * @return
     */
    public static long getLong(String key, long defaultValue) {
        SharedPreferences prefence = Utils.getApp().getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);
        return prefence.getLong(key, defaultValue);
    }

    /**
     * put一个Int键值对
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean putInt(String key, int value) {
        SharedPreferences prefence = Utils.getApp().getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor eidtor = prefence.edit();
        eidtor.putInt(key, value);
        return eidtor.commit();
    }

    /**
     *  put一个Long键值对
     */
    public static boolean putLong(String key, long value) {
        SharedPreferences prefence = Utils.getApp().getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor eidtor = prefence.edit();
        eidtor.putLong(key, value);
        return eidtor.commit();
    }

    /**
     * 获取string 值
     * @param key
     * @return
     */
    public static boolean getBoolean(String key) {
        SharedPreferences prefence = Utils.getApp().getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);
        return prefence.getBoolean(key, false);
    }
    /**
     * 获取string 值
     * @param key
     * @return
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences prefence = Utils.getApp().getSharedPreferences(SHAREDNAME, Context.MODE_PRIVATE);
        return prefence.getBoolean(key, defaultValue);
    }

    /**
     * 字符串累加
     * @param key
     * @param value
     * @param split
     */
    public static void appendString(String key, String value, String split) {
        String val = getString(key);
        if(TextUtils.isEmpty(val)){

            //首次插入值
            putString(key, value);

        }else{

            //累加值
            val = (val + split + value);
            putString(key, val);

        }

    }

}
