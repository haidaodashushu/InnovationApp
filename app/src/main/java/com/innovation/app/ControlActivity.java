package com.innovation.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.innovation.app.dialog.DialogInstance;
import com.innovation.app.linstener.RequestListener;
import com.innovation.app.net.Constants;
import com.innovation.app.net.entity.StringEntity;
import com.innovation.app.net.request.SpJsonRequest;
import com.innovation.app.net.tools.NetTools;

import java.util.HashMap;

public class ControlActivity extends AppCompatActivity implements View.OnClickListener{

    Button motorBtn,relayBtn;
    EditText motorEv,relayEv;
    Handler handler ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        motorBtn = (Button)findViewById(R.id.motorSureBtn);
        relayBtn = (Button)findViewById(R.id.relaySureBtn);
        motorEv = (EditText)findViewById(R.id.motorEv);
        relayEv = (EditText)findViewById(R.id.relayEv);

        motorBtn.setOnClickListener(this);
        relayBtn.setOnClickListener(this);

        handler = new Handler();
        toMotor();
        toRelay();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.motorSureBtn:
//                toMotor();
                toDoController("motor",motorEv.getText().toString());
                break;
            case R.id.relaySureBtn:
                toDoController("relay",motorEv.getText().toString());
                toRelay();
                break;
        }
    }

    private void toDoController(final String name, String value) {
        HashMap<String,String> params = new HashMap<String,String>(0);
        params.put("name", name);
        params.put("value",value);
        new SpJsonRequest(this, NetTools.encodeUrl(Constants.DO_CONTROLLER_URL, params), null, null, new StringEntity(),new RequestListener(){

            @Override
            public void onResponse(Object response) {
                DialogInstance.getInstance().cancleProgressDialog();
                StringEntity entity = (StringEntity)response;
                if ("success".equals(entity.getString())) {
                    checkResult(name);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                DialogInstance.getInstance().cancleProgressDialog();
                Toast.makeText(ControlActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        }).start();
    }

    private void checkResult(String name) {
        //正常
        if ("relay".equals(name)) {
            toRelay();
        }else if ("motor".equals(name)) {
            toMotor();
        }
    }

    private void toRelay() {
        relayBtn.setEnabled(false);
        relayBtn.setText("等待浇水结束");
        new SpJsonRequest(this, Constants.STATE_CHECK_URL, null, null, new StringEntity(),new RequestListener(){

            @Override
            public void onResponse(Object response) {
                DialogInstance.getInstance().cancleProgressDialog();
                StringEntity entity = (StringEntity)response;
                if ("\"doing\"".equals(entity.getString())) {
                    relayBtn.setEnabled(false);
                    relayBtn.setText("等待浇水结束");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toRelay();
                        }
                    },2000);
                } else {
                    relayBtn.setEnabled(true);
                    relayBtn.setText("浇水");
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                DialogInstance.getInstance().cancleProgressDialog();
                Toast.makeText(ControlActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        }).start();
    }

    private void toMotor() {
        motorBtn.setEnabled(false);
        motorBtn.setText("等待响应结束");
        new SpJsonRequest(this, Constants.CHECK_MOTOR_URL, null, null, new StringEntity(),new RequestListener(){

            @Override
            public void onResponse(Object response) {
                DialogInstance.getInstance().cancleProgressDialog();
                StringEntity entity = (StringEntity)response;
                if ("\"waiting\"".equals(entity.getString())) {
                    motorBtn.setEnabled(false);
                    motorBtn.setText("等待响应结束");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toMotor();
                        }
                    }, 2000);
                } else {
                    motorBtn.setEnabled(true);
                    motorBtn.setText("打开");
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                DialogInstance.getInstance().cancleProgressDialog();
                Toast.makeText(ControlActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        }).start();

    }
}
