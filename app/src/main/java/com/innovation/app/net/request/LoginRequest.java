package com.innovation.app.net.request;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.VolleyError;
import com.innovation.app.dialog.DialogInstance;
import com.innovation.app.linstener.RequestListener;
import com.innovation.app.net.Constants;
import com.innovation.app.net.CookieManager;
import com.innovation.app.net.entity.LoginResponseEntity;

import java.util.HashMap;

/**
 * @author WangZhengkui on 2016-04-30 14:05
 */
public class LoginRequest {
    public static void toLogin(final Context context, final Thread thread) {
        Handler handler = new Handler(context.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 0) {
                    DialogInstance.getInstance().showProgressDialogContinue(context, "正在登录");
                }
                return false;
            }
        });

        Log.i("LoginRequest", "toLogin1");
        HashMap<String, String> params = new HashMap<String, String>(0);
        params.put("username", "root");
        params.put("passwd", "Qwert");
        handler.sendEmptyMessage(0);
        new SpJsonRequest(context, Constants.LOGIN_URL, params, null, new LoginResponseEntity(), new RequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("LoginRequest", "toLoginError");
                SpRequestQueue.lock.notify();
            }

            @Override
            public void onResponse(Object response) {
            Log.i("LoginRequest", "toLoginSuccess");
            //缓存cookie
            CookieManager.putCookie(context, ((LoginResponseEntity) response).cookie);
//          打断线程睡眠
            thread.interrupt();

            }
        }).start();
    }

}
