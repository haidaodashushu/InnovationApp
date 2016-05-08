package com.innovation.app;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.innovation.app.dialog.DialogInstance;
import com.innovation.app.linstener.RequestListener;
import com.innovation.app.net.Constants;
import com.innovation.app.net.entity.MonitorDateEntity;
import com.innovation.app.net.request.SpJsonRequest;
import com.innovation.app.net.tools.NetTools;
import com.innovation.app.view.ChartLineView;
import com.innovation.app.view.TimePickerView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class MonitorActivity extends AppCompatActivity implements OnClickListener,TimePickerView.OnTimeSelectListener{

    TextView timePickerTv;
    ChartLineView chartLineView;
    String data = "{\"X\":[\" 0:0\\n\",\" 0:0\\n\",\" 1:0\\n\",\" 2:0\\n\",\" 3:0\\n\",\" 4:0\\n\",\" 5:0\\n\",\" 6:0\\n\",\" 7:0\\n\",\" 8:0\\n\",\" 9:0\\n\",\" 10:0\\n\",\" 11:0\\n\",\" 12:0\\n\",\" 13:0\\n\",\" 14:0\\n\",\" 15:0\\n\",\" 16:0\\n\",\" 17:0\\n\"],\"Temp\":[\"21.0\",\"19.0\",\"21.0\",\"20.0\",\"21.0\",\"21.0\",\"22.0\",\"22.0\",\"22.0\",\"23.0\",\"23.0\",\"23.0\",\"23.0\",\"23.0\",\"23.0\",\"22.0\",\"22.0\",\"22.0\",\"22.0\"],\"Humidity\":[\"36.0\",\"38.0\",\"37.0\",\"38.0\",\"37.0\",\"37.0\",\"37.0\",\"37.0\",\"37.0\",\"36.0\",\"36.0\",\"36.0\",\"36.0\",\"37.0\",\"37.0\",\"38.0\",\"38.0\",\"38.0\",\"38.0\"]}";
    MonitorDateEntity dateEntity = new MonitorDateEntity();
    java.util.Date currentDate;
    TimePickerView timePickerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        timePickerTv = (TextView)findViewById(R.id.timePickerTv);
        timePickerTv.setOnClickListener(this);

        timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setOnTimeSelectListener(this);
        timePickerView.setCyclic(true);

        chartLineView = (ChartLineView)findViewById(R.id.charLineView);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i("MonitorActivity","onConfigureationChanged = " + newConfig.orientation);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.timePickerTv:
                if (currentDate == null) {
                    currentDate  = new Date(System.currentTimeMillis());
                }
                timePickerView.setTime(currentDate);
                timePickerView.show();

                break;
        }
    }
    @Override
    public void onTimeSelect(java.util.Date date) {
        currentDate = date;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/M/d");
        String dateStr = dateFormat.format(date);
        getMonitor(dateStr);
        timePickerTv.setText("时间选择："+dateStr);
    }
    public void getMonitor(String date) {
        HashMap<String,String> params = new HashMap<String,String>(0);
        params.put("time", date);
        DialogInstance.getInstance().showProgressDialog(this, "正在加载", "getDate");
        new SpJsonRequest(this, NetTools.encodeUrl(Constants.GET_DATA_URL, params), null, null, new MonitorDateEntity(),new RequestListener(){

            @Override
            public void onResponse(Object response) {
                DialogInstance.getInstance().cancleProgressDialog();
                dateEntity = (MonitorDateEntity)response;
                chartLineView.setData(dateEntity.getX(),dateEntity.getY(),dateEntity.getTemp(),dateEntity.getHumidity());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                DialogInstance.getInstance().cancleProgressDialog();
                Toast.makeText(MonitorActivity.this,"请求失败",Toast.LENGTH_SHORT).show();
            }
        }).start();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK :
                if (timePickerView.isShowing()) {
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
