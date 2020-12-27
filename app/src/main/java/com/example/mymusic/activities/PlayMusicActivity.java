package com.example.mymusic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymusic.R;
import com.example.mymusic.adapters.MusicListAdapter;
import com.example.mymusic.helps.RealmHelper;
import com.example.mymusic.models.MusicModel;
import com.example.mymusic.views.PlayMusicView;

import java.io.IOException;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class PlayMusicActivity extends BaseActivity {
     public static final String MUSIC_ID="musicId";
     private ImageView mIvBg;
     private TextView mTvName,mTvAuthor;
     private PlayMusicView mPlayMusicView;
     private String mMusicId;
     private RealmHelper mRealmHelper;
     private MusicModel mMusicModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        /*隐藏statusBar*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initData();
        initView();
    }

    private void initData() {
       mMusicId= getIntent().getStringExtra(MUSIC_ID);
       mRealmHelper=new RealmHelper();
       mMusicModel = mRealmHelper.getMusic(mMusicId);
    }


    private void initView()  {
        mIvBg=findViewById(R.id.iv_bg);
        mTvName = findViewById(R.id.tv_name);
        mTvAuthor = findViewById(R.id.tv_author);
        /*glide-transformations*/
        Glide.with(this)
                /*.load("http://img.mp.sohu.com/upload/20170606/9579a5d9545d447e9a8497158e819635_th.png")*/
                .load(mMusicModel.getPoster())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,10)))
                .into(mIvBg);
        mTvName.setText(mMusicModel.getName());
        mTvAuthor.setText(mMusicModel.getAuthor());

        mPlayMusicView=findViewById(R.id.play_music_view);
        /*mPlayMusicView.setMusicIcon("http://img.mp.sohu.com/upload/20170606/9579a5d9545d447e9a8497158e819635_th.png");*/
        /*mPlayMusicView.setMusicIcon(mMusicModel.getPoster());*/
        mPlayMusicView.setMusic(mMusicModel);
        mPlayMusicView.playMusic();
        /*开始直接播放*/
        /*mPlayMusicView.playMusic("http://res.lgdsunday.club/Nostalgic%20Piano.mp3");*/
        /*mPlayMusicView.playMusic(mMusicModel.getPath());*/
    }

    /*后退按钮点击事件*/
    public void onBackClick(View view) {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayMusicView.destroy();
        mRealmHelper.close();
    }
}