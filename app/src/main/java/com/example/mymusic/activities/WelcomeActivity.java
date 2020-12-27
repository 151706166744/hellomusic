package com.example.mymusic.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.mymusic.R;
import com.example.mymusic.activities.BaseActivity;
import com.example.mymusic.activities.LoginActivity;
import com.example.mymusic.activities.MainActivity;
import com.example.mymusic.utils.UserUtils;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends BaseActivity {
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }

    private void init() {
        boolean isLogin = UserUtils.validateUserLogin(this);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                /*Log.d("WelcomeActivity","当前线程为："+Thread.currentThread());*/

                if (isLogin) {
                    toMain();
                }else {
                    toLogin();
                }
            }
        },1*1000);
    }
    /*跳转到MainActivity*/
    private void toMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    /*跳转到LoginActivity*/
    private void toLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}