package com.innovation.app.net.request;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SpRequestQueue {

	private RequestQueue mQueue;

	private static SpRequestQueue _instance;

    public static final Object lock = new Object();
	private SpRequestQueue(Context pContext)
	{
		mQueue = Volley.newRequestQueue(pContext);
	}
	
	public synchronized static void init(Context pContext)
	{
		if(_instance == null) _instance = new SpRequestQueue(pContext);
	}

	public static  SpRequestQueue get()
	{
		return _instance;
	}
    public RequestQueue getQueue() {
        return mQueue;
    }

    public void addRequest(Request<?> request, String pTag)
	{
		if(pTag != null) request.setTag(pTag);
		mQueue.add(request);
	}
	
	public void cancelRequest(String pTag)
	{
        Log.i("SpRequestQueue", "cancle "+pTag);
		mQueue.cancelAll(pTag);
	}
	public void cancelAllRequest()
	{
		mQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
	}


}
