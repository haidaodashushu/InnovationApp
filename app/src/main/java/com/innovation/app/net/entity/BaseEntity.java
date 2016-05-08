package com.innovation.app.net.entity;

import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author WangZhengkui on 2015-06-16 11:01
 */
public abstract class BaseEntity implements Serializable{
    public String jsonStr;
    public Map<String,String> header;
    public void parserNetResponse(NetworkResponse response) throws JSONException, UnsupportedEncodingException {
        jsonStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        header = response.headers;
        parser(jsonStr);
    };

    /**
     * 数据解析失败，抛出json异常，
     * 如果捕获该异常，则意味着此时网络请求成功,会回调Response.Listener.onResponse(),
     * 如果没有捕获该异常，则意味着此时网络请求失败，会回调Response.ErrorListener.onErrorResponse()
     * @param jsonStr
     * @throws JSONException
     */
    public abstract void parser(String jsonStr) throws JSONException;

    protected int GetInt(String key, JSONObject jsonObj) throws JSONException {
        if (!jsonObj.isNull(key)) {
            return jsonObj.getInt(key);
        }
        return 0;
    }
    protected float GetFloat(String key,JSONObject jsonObject) throws JSONException{
        if (!jsonObject.isNull(key)){
            return (float)jsonObject.getDouble(key);
        }
        return 0;
    }
    protected long GetLong(String key,JSONObject jsonObject) throws JSONException{
        if (!jsonObject.isNull(key)){
            return jsonObject.getLong(key);
        }
        return 0;
    }

    protected double GetDouble(String key, JSONObject jsonObj)
            throws JSONException {
        if (!jsonObj.isNull(key)) {
            return jsonObj.getDouble(key);
        }
        return 0;
    }

    protected String GetString(String key, JSONObject jsonObj)
            throws JSONException {
        if (!jsonObj.isNull(key)) {
            return jsonObj.getString(key);
        }
        return "";
    }

    protected boolean GetBoolean(String key, JSONObject jsonObj)
            throws JSONException {
        return !jsonObj.isNull(key) && jsonObj.getBoolean(key);
    }

    protected void GetInt(String key, JSONObject jsonObj,BaseEntity entity)
            throws JSONException {

        if (!jsonObj.isNull(key)) {
            int value = jsonObj.getInt(key);
            try {
                Field field = entity.getClass().getDeclaredField(key);
                field.set(entity,value);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    protected void GetLong(String key, JSONObject jsonObj,BaseEntity entity)
            throws JSONException {

        if (!jsonObj.isNull(key)) {
            long value = jsonObj.getLong(key);
            try {
                Field field = entity.getClass().getDeclaredField(key);
                field.set(entity,value);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    protected void GetDouble(String key, JSONObject jsonObj,BaseEntity entity)
            throws JSONException {

        if (!jsonObj.isNull(key)) {
            double value = jsonObj.getDouble(key);
            try {
                Field field = entity.getClass().getDeclaredField(key);
                field.set(entity,value);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    protected void GetString(String key, JSONObject jsonObj,BaseEntity entity)
            throws JSONException {

        if (!jsonObj.isNull(key)) {
            String value = jsonObj.getString(key);
            try {
                Field field = entity.getClass().getDeclaredField(key);
                field.set(entity,value);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
