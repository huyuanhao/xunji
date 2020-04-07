package com.common.lib.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * 作者:zh
 * 时间:12/19/18 12:31 PM
 * 描述;
 */
public class GsonUtil {

    static {
         sGson = new GsonBuilder().disableHtmlEscaping().create();
    }

    private static Gson sGson;

    public static  String toGsonString(Object  obj){
        try {
            return sGson.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static<T> T jsonToBean(String json,Class<T> clazz){
        try {
            T t = sGson.fromJson(json, clazz);
            return t;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }





}
