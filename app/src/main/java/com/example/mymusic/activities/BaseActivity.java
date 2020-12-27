package com.example.mymusic.activities;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymusic.R;
public class BaseActivity extends AppCompatActivity {

    private ImageView mIvBack, mIvMe;
    private TextView mTvTitle;

    /**
     * findViewById
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T fd (@IdRes int id) {
        return findViewById(id);
    }

    /**
     * 初始化NavigationBar
     * @param isShowBack
     * @param title
     * @param isShowMe
     */
    protected void initNavBar (boolean isShowBack, String title, boolean isShowMe) {

        mIvBack = fd(R.id.iv_back);
        mTvTitle = fd(R.id.tv_title);
        mIvMe = fd(R.id.iv_me);

        mIvBack.setVisibility(isShowBack ? View.VISIBLE : View.GONE);
        mIvMe.setVisibility(isShowMe ? View.VISIBLE : View.GONE);
        mTvTitle.setText(title);


        mIvBack.setOnClickListener(v -> onBackPressed());

        mIvMe.setOnClickListener(v -> startActivity(new Intent(BaseActivity.this, MeActivity.class)));
    }

}

/*
public class BaseActivity extends AppCompatActivity {
    ImageView mIvBack;
    TextView mTvTitle;
    ImageView mIvMe;
    protected void initNavBar(boolean isShowBack,String title,boolean isShowMe){
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvMe = (ImageView) findViewById(R.id.iv_me);

        mIvBack.setVisibility(isShowBack ? View.VISIBLE:View.GONE);
        mIvMe.setVisibility(isShowMe ? View.VISIBLE:View.GONE);
        mTvTitle.setText(title);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mIvMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseActivity.this,MeActivity.class));
            }
        });
    }
*/

   /* @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_me:
                startActivity(new Intent(BaseActivity.this,MeActivity.class));
                break;
            default:
                break;
        }
    }*/

