package com.innovation.innovationapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button monitorBtn,controlBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        monitorBtn = new Button(this);
        monitorBtn.setText("监控");
        monitorBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        monitorBtn.setTag("monitor");
        monitorBtn.setOnClickListener(this);

        controlBtn = new Button(this);
        controlBtn.setText("控制");
        controlBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        controlBtn.setTag("control");
        controlBtn.setOnClickListener(this);


        ViewGroup contentView = (ViewGroup) findViewById(R.id.contentView);
        if (contentView != null) {
            contentView.addView(monitorBtn,-2,-2);
            contentView.addView(controlBtn,-2,-2);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) controlBtn.getLayoutParams();
            params.topMargin =
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

    }
}
