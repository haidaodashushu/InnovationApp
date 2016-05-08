package com.innovation.app;

import android.app.Application;

import com.innovation.app.net.request.SpRequestQueue;

/**
 * @author WangZhengkui on 2016-04-30 12:49
 */
public class MainApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        SpRequestQueue.init(getApplicationContext());
    }
}
