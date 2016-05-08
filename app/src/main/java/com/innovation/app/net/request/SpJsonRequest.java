package com.innovation.app.net.request;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.innovation.app.dialog.DialogInstance;
import com.innovation.app.linstener.RequestListener;
import com.innovation.app.net.CookieManager;
import com.innovation.app.net.entity.BaseEntity;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WangZhengkui on 2015-06-16 10:06
 */
public class SpJsonRequest extends Request {
    public static final int DEFAULT_TIMEOUT_MS = 20000;
    public static final int DEFAULT_MAX_RETRIES = 2;
    public static final float DEFAULT_BACKOFF_MULT = 1.0F;
    RequestListener listener;
    private BaseEntity entity;
    private HashMap<String, String> mBodyMap;
    private Context context;
    private String url;
    private int method;
    private HashMap<String, String> mHeaders = new HashMap<String, String>();
    private Handler handler;

    private String cookie;

    private SpJsonRequest(Context pContext, int method, String url, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.context = pContext;
        this.method = method;
        this.url = url;
        setShouldCache(false);
        if (cookie == null) {
            cookie = CookieManager.getCookie(context);
        }
        handler = new Handler(context.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 0) {
                    DialogInstance.getInstance().showProgressDialogContinue(context, "正在加载");
                }
                return false;
            }
        });
    }

    /**
     *
     * @param url
     * @param entity
     * @param listener
     */
    public SpJsonRequest(Context context, String url, HashMap<String, String> mBodyMap, HashMap<String, String> mHeaders, BaseEntity entity, RequestListener listener) {
        this(context, mBodyMap!=null?Method.POST:Method.GET, url, listener);
        if (mHeaders != null) {
            this.mHeaders = mHeaders;
        }
        this.listener = listener;
        this.entity = entity;
        this.mBodyMap = mBodyMap;
    }

    @Override
    public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {

        return super.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS,
                DEFAULT_MAX_RETRIES,
                DEFAULT_BACKOFF_MULT));
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (mHeaders != null) {
            mHeaders.put("cookie",cookie);
        }
        return mHeaders;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mBodyMap;
    }

    /*@Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        int statusCode = volleyError.networkResponse.statusCode;
        String message = "";
        try {
             message = volleyError.getCause().getMessage();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        //登录失败时会从这里重新登录
        if (statusCode == 404 || message.contains("ServerError")) {
            synchronized (SpRequestQueue.lock) {
                String temoCookie = CookieManager.getCookie(context);
                while (TextUtils.isEmpty(this.cookie) || this.cookie.equals(temoCookie)) {
                    //如果cookie为空，或者缓存的cookie没变，则继续请求
                    //需要重新登录
                    cookie = null;
                    LoginRequest.toLogin(context, Thread.currentThread());
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //重新请求
                handler.sendEmptyMessage(0);
                cookie = temoCookie;
                new SpJsonRequest(context,url,mBodyMap,mHeaders,entity,listener);
            }
        }
        return super.parseNetworkError(volleyError);
    }*/

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            if (entity != null) {
                entity.parserNetResponse(response);
            }
            return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(Object response) {
        if (listener != null) {
            listener.onResponse(entity);
        }
    }
    @Override
    public void deliverError(VolleyError error) {
        Log.i("SpJsonRequest", "error = " + error.toString());
        //在数据返回之后，如果被重定向到登录界面，则数据解析时会抛出JsonException异常
        //则认为需要重新登录
        if (error.getCause() instanceof JSONException) {
            synchronized (SpRequestQueue.lock) {
                String temoCookie = CookieManager.getCookie(context);
                while (TextUtils.isEmpty(this.cookie) || this.cookie.equals(temoCookie)) {
                    //如果cookie为空，或者缓存的cookie没变，则继续请求
                    //需要重新登录
                    cookie = null;
                    LoginRequest.toLogin(context, Thread.currentThread());
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    temoCookie = CookieManager.getCookie(context);
                }
                //重新请求
                handler.sendEmptyMessage(0);
                cookie = temoCookie;
                new SpJsonRequest(context, url, mBodyMap, mHeaders, entity, listener);
            }
        } else {
            super.deliverError(error);
        }
    }
    public void start() {
        SpRequestQueue queue = SpRequestQueue.get();
        queue.addRequest(this, null);
    }

    public void start(String tag) {
        SpRequestQueue queue = SpRequestQueue.get();
        queue.addRequest(this, tag);
    }

}
