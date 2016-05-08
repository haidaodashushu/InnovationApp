package com.innovation.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.innovation.app.dialog.DialogInstance;
import com.innovation.app.linstener.RequestListener;
import com.innovation.app.net.Constants;
import com.innovation.app.net.CookieManager;
import com.innovation.app.net.entity.LoginResponseEntity;
import com.innovation.app.net.request.SpJsonRequest;

import java.util.HashMap;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        loginBtn = (Button) findViewById(R.id.login);
        checkLastLogin();

        if (loginBtn != null) {
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginBtn.setEnabled(false);
                    toLogin();
                }
            });
        }
    }

    private void checkLastLogin() {
        DialogInstance.getInstance().showProgressDialog(this, "正在检查cookie");
        try {
            long lastTime = Long.parseLong(CookieManager.getKey(this,"loginTime"));
            //上次登录超过30分钟
            if (System.currentTimeMillis() - lastTime < 30 * 60 * 1000) {
                DialogInstance.getInstance().cancleProgressDialog();
                startMainAty();
            } else {
                DialogInstance.getInstance().cancleProgressDialog();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            DialogInstance.getInstance().cancleProgressDialog();
        }
    }

    public void toLogin() {
        HashMap<String, String> params = new HashMap<String, String>(0);
        params.put("username", mEmailView.getText().toString());
        params.put("passwd", mPasswordView.getText().toString());
        DialogInstance.getInstance().showProgressDialog(this, "正在登录", "login");
        new SpJsonRequest(this, Constants.LOGIN_URL, params, null, new LoginResponseEntity(), new RequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("LoginRequest", "toLoginError");
                Toast.makeText(LoginActivity.this,"账号密码错误，请重新登录",Toast.LENGTH_SHORT).show();
                loginBtn.setEnabled(true);
                DialogInstance.getInstance().cancleProgressDialog();
            }

            @Override
            public void onResponse(Object response) {
                Log.i("LoginRequest", "toLoginSuccess");
                DialogInstance.getInstance().cancleProgressDialog();
                loginBtn.setEnabled(true);
                //缓存cookie
                CookieManager.putCookie(LoginActivity.this, ((LoginResponseEntity) response).cookie);
                CookieManager.putKey(LoginActivity.this,"loginTime",System.currentTimeMillis()+"");
                startMainAty();
            }
        }).start();
    }

    public void startMainAty() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}

