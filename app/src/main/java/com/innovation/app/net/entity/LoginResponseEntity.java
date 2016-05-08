package com.innovation.app.net.entity;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author WangZhengkui on 2016-04-30 11:27
 */
public class LoginResponseEntity extends BaseEntity {
    public boolean isLogin;
    public String cookie;
    @Override
    public void parser(String pJsonStr) throws JSONException {
        if (TextUtils.isEmpty(pJsonStr)) {
            return;
        }
        try {
            //使用正则表达式从reponse的头中提取cookie内容的子串
            Pattern pattern = Pattern.compile("Set-Cookie.*?;");
            Matcher m = pattern.matcher(header.toString());
            if (m.find()) {
                cookie = m.group();
                Log.w("LOG", "cookie from server " + cookie);
            }
            //去掉cookie末尾的分号
            cookie = cookie.substring(11, cookie.length() - 1);
            Log.w("LOG", "cookie substring " + cookie);

            if ("success".equals(pJsonStr)) {
                isLogin = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
