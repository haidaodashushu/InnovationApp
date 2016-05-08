package com.innovation.app.net.entity;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * @author WangZhengkui on 2016-04-30 21:01
 */
public class MonitorDateEntity extends BaseEntity{

    String[] x ;
    String[] y;
    String[] temp;
    String[] humidity;

    public String[] getX() {
        return x;
    }

    public MonitorDateEntity setX(String[] x) {
        this.x = x;
        return this;
    }

    public String[] getY() {
        return y;
    }

    public MonitorDateEntity setY(String[] y) {
        this.y = y;
        return this;
    }

    public String[] getTemp() {
        return temp;
    }

    public MonitorDateEntity setTemp(String[] temp) {
        this.temp = temp;
        return this;
    }

    public String[] getHumidity() {
        return humidity;
    }

    public MonitorDateEntity setHumidity(String[] humidity) {
        this.humidity = humidity;
        return this;
    }

    /**
     * 如果数据解析失败，会抛出JsonException异常
     * @param jsonStr
     * @throws JSONException
     */
    @Override
    public void parser(String jsonStr) throws JSONException {
        JSONObject object = new JSONObject(jsonStr);
        JSONArray array = object.getJSONArray("X");
        x = new String[array.length()];
        for (int i = 0; i < array.length(); i++) {
            x[i] = array.getString(i);
        }
        array = object.getJSONArray("Temp");
        temp = new String[array.length()];
        for (int i = 0; i < array.length(); i++) {
            temp[i] = array.getString(i);
        }
        array = object.getJSONArray("Humidity");
        humidity = new String[array.length()];
        for (int i = 0; i < array.length(); i++) {
            humidity[i] = array.getString(i);
        }

        //找到温度和湿度的最大值
        float max = 0,min= 0;
        String[] tempValue = new String[temp.length+humidity.length];
        for (int i = 0; i < tempValue.length; i++) {
            if (i < temp.length) {
                tempValue[i] = temp[i];
            } else {
                tempValue[i] = humidity[i - temp.length];
            }
        }
        if (tempValue.length != 0) {
            y = new String[6];
            max = Float.parseFloat(temp[0]);
            min = Float.parseFloat(temp[0]);
            for (int i = 0; i < tempValue.length; i++) {
                float value = Float.parseFloat(tempValue[i]);
                if ( value> max) {
                    max = value;
                }
                if (value < min) {
                    min = value;
                }
            }

        }

        float last = 0;
        float start = 0;
        int i = 0;
        while (last<=max) {
            i++;
            start = min-min%(5*i);
            last = start+(y.length-1)*(5*i);
        }

        for (int j = 0; j<y.length; j++) {
            y[j] = start+j*i*5+"";
        }
        Log.i("MonitorDateEntity", "y = "+ Arrays.toString(y));
    }



}
