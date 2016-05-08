package com.innovation.app.net;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author WangZhengkui on 2016-04-30 20:17
 */
public class CookieManager {
    private static final String COOKIE_FILE_CACHE = "cke";
    private static final String COOKIE_KEY = "cookie_key";
    String cookie;

    public static String getCookie(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(COOKIE_FILE_CACHE,0);
        return sharedPreferences.getString(COOKIE_KEY, "");
    }

    public static boolean putCookie(Context context,String cookie) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(COOKIE_FILE_CACHE, 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(COOKIE_KEY, cookie);
        return edit.commit();
    }
    public static boolean putKey(Context context,String key,String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(COOKIE_FILE_CACHE, 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        return edit.commit();
    }
    public static String getKey(Context context,String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(COOKIE_FILE_CACHE, 0);
        return sharedPreferences.getString(key, "");
    }
}
