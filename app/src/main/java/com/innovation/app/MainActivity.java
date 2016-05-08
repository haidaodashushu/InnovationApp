package com.innovation.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button monitorBtn,controlBtn;
    private ViewGroup contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        monitorBtn = new Button(this);
        monitorBtn.setText(getString(R.string.monitorName));
        monitorBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        monitorBtn.setTag("monitor");
        monitorBtn.setPadding(dp2px(30), dp2px(10), dp2px(30), dp2px(10));
        monitorBtn.setOnClickListener(this);

        controlBtn = new Button(this);
        controlBtn.setText(getString(R.string.controlName));
        controlBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        controlBtn.setTag("control");
        controlBtn.setPadding(dp2px(30), dp2px(10), dp2px(30), dp2px(10));
        controlBtn.setOnClickListener(this);


        contentView = (ViewGroup) findViewById(R.id.contentView);
        if (contentView != null) {
            contentView.addView(monitorBtn, -2, -2);
            contentView.addView(controlBtn, -2, -2);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) controlBtn.getLayoutParams();
            params.setMargins(0,dp2px(20),0,0);
        }
    }

    public int dp2px(int dp) {
        DisplayMetrics  metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float density = metrics.density;
        return (int) (dp*density+0.5f);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        if ("monitor".equals(v.getTag().toString())) {
            intent.setClass(this,MonitorActivity.class);
        }else if ("control".equals(v.getTag().toString())) {
            intent.setClass(this,ControlActivity.class);
        }
        startActivity(intent);
    }


    boolean flag;
    long stopMis;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            if (!flag) {
                Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                stopMis = System.currentTimeMillis();
                flag = true;
            } else {
                if ((System.currentTimeMillis() - stopMis) < 2000) {
                    // 退出
                    this.finish();
                    // System.exit(0);
                    flag = false;
                } else {
                    flag = false;
                    stopMis = System.currentTimeMillis();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
