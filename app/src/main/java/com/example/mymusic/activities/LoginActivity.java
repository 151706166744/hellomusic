package com.example.mymusic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mymusic.R;
import com.example.mymusic.utils.UserUtils;
import com.example.mymusic.views.InputView;

public class LoginActivity extends BaseActivity {
    private InputView mInputPhone,mInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initNavBar();
        mInputPhone = findViewById(R.id.input_phone);
        mInputPassword = findViewById(R.id.input_password);
    }
    /*初始化view*/
    private void initNavBar() {
        initNavBar(false,"登录",false);
    }
    /*跳转注册页面点击事件*/
    public void onRegisterClick(View view) {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    public void onCommitClick(View view) {
        String phone = mInputPhone.getInputStr();
        String password = mInputPassword.getInputStr();
        /*验证用户输入是否合法*/
        if (!UserUtils.validateLogin(this,phone,password)) {
            return;
        }
        /*跳转到Mainactivity*/
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        /*finish();*/
    }

}