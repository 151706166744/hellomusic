package com.example.mymusic.views;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.mymusic.R;
import com.example.mymusic.helps.MediaPlayerHelper;
import com.example.mymusic.models.MusicModel;
import com.example.mymusic.services.MusicService;


public class PlayMusicView extends FrameLayout {

    /*private MediaPlayerHelper mMediaPlayerHelper;*/
    private Context mContext;
    private Intent mServiceIntent;
    private MusicService.MusicBind mMusicBind;
    private MusicModel mMusicModel;

    private boolean isPlaying,isBindService;
    /*private String mPath;*/
    private View mView;
    private FrameLayout mFlPlayMusic;
    private ImageView mIvIcon,mIvNeedle,mIvPlay;
    private Animation mPlayMusicAnim;
    private Animation mPlayNeedleAnim;
    private Animation mStopNeedleAnim;

    public PlayMusicView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    private void init(Context context) {
        mContext=context;
        /*生成View*/
        mView = LayoutInflater.from(mContext).inflate(R.layout.play_music, this, false);

        mFlPlayMusic=mView.findViewById(R.id.fl_play_music);
        /*给光盘设置点击事件*/
        mFlPlayMusic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                trigger();
            }
        });
        mIvIcon = mView.findViewById(R.id.iv_icon);
        mIvNeedle=mView.findViewById(R.id.iv_needle);
        mIvPlay=mView.findViewById(R.id.iv_play);


        /*
        * 1.定义所需要执行的动画
        *   1.1光盘转动的动画
        *   1.2指针指向光盘的动画
        *   1.3指针离开光盘时的动画
        * 2.startAnimation
        *
        * */
     mPlayMusicAnim= AnimationUtils.loadAnimation(mContext,R.anim.play_music_anim);
     mPlayNeedleAnim=AnimationUtils.loadAnimation(mContext,R.anim.play_needle_anim);
     mStopNeedleAnim=AnimationUtils.loadAnimation(mContext,R.anim.stop_needle_anim);

     addView(mView);

     /*mMediaPlayerHelper = MediaPlayerHelper.getInstance(mContext);*/
    }
    /*
    * 切换播放状态
    * */
    public void trigger()  {
        if (isPlaying) {
            stopMusic();
        }else{
            /*mPath*/
            playMusic();
        }
    }


    /*播放音乐 String path*/
    public void playMusic() {
        /*mPath=path;*/
        isPlaying=true;
        mIvPlay.setVisibility(GONE);
        mFlPlayMusic.startAnimation(mPlayMusicAnim);
        mIvNeedle.startAnimation(mPlayNeedleAnim);

        startMusicService();
        /*
        * 1.判断当前播放的音乐是否是已经在播放的音乐
        * 2.如果当前的音乐是已经在播放的音乐的话，那么就执行start方法
        * 3.如果说当前播放的音乐不是正在执行的播放的音乐的话，那么就调用setPath的方法
        * */
       /* if (mMediaPlayerHelper.getPath()!=null&& mMediaPlayerHelper.getPath().equals(path)) {
            mMediaPlayerHelper.start();
        }else{
            mMediaPlayerHelper.setPath(path);
            mMediaPlayerHelper.setOnMediaPlayerHelperListener(new MediaPlayerHelper.OnMediaPlayerHelperListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayerHelper.start();
                }
            });
        }*/
    }
    /*停止播放音乐*/
    public void stopMusic(){
        isPlaying=false;
        mFlPlayMusic.clearAnimation();
        mIvNeedle.startAnimation(mStopNeedleAnim);
        mIvPlay.setVisibility(View.VISIBLE);

        if (mMusicBind!=null) {
            mMusicBind.stopMusic();
        }

        mMusicBind.stopMusic();
        /*mMediaPlayerHelper.pause();*/
    }


    /*设置光盘中显示的音乐封面图片  String icon*/
    public void setMusicIcon(){
        Glide.with(mContext)
                .load(mMusicModel.getPoster())
                .into(mIvIcon);
    }
    public void setMusic(MusicModel music){
        mMusicModel=music;
        setMusicIcon();
    }
    /**
     * 启动音乐服务
     */
    private void startMusicService(){
        /**启动service*/
        if (mServiceIntent==null) {
            mServiceIntent=new Intent(mContext, MusicService.class);
            mContext.startService(mServiceIntent);
        }else{
            mMusicBind.playMusic();
        }
        /**绑定service*/
        if(!isBindService){
            isBindService=true;
            mContext.bindService(mServiceIntent,serviceConnection,Context.BIND_AUTO_CREATE);
        }
    }
    /**
     *解除绑定
     */
    public void destroy(){
        /**如果已经绑定了服务，则解除绑定*/
        if(isBindService){
            isBindService=false;
            mContext.unbindService(serviceConnection);
        }
    }
    ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicBind= (MusicService.MusicBind) service;
            mMusicBind.setMusic(mMusicModel);
            mMusicBind.playMusic();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

}
