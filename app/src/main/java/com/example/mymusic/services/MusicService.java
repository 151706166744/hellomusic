package com.example.mymusic.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.example.mymusic.R;
import com.example.mymusic.activities.WelcomeActivity;
import com.example.mymusic.helps.MediaPlayerHelper;
import com.example.mymusic.models.MusicModel;

/**
 * 1、通过Service 连接 PlayMusicView 和 MediaPlayHelper
 * 2、PlayMusicView -- Service：
 *      1、播放音乐、暂停音乐
 *      2、启动Service、绑定Service、解除绑定Service
 * 3、MediaPlayHelper -- Service：
 *      1、播放音乐、暂停音乐
 *      2、监听音乐播放完成，停止 Service
 */
public class MusicService extends Service {
    //不为0
    public static final int NOTIFICATION_ID=1;

    private MediaPlayerHelper mMediaPlayerHelper;
    private MusicModel mMusicModel;


    public MusicService() {
    }
    public class MusicBind extends Binder {
        /**
         * 设置音乐（MusicModel）
         */
        public void setMusic(MusicModel musicModel){
            mMusicModel=musicModel;
            startForeground();
        }
        /**
        * 播放音乐
         */
        public void playMusic(){
            /*
             * 1.判断当前播放的音乐是否是已经在播放的音乐
             * 2.如果当前的音乐是已经在播放的音乐的话，那么就执行start方法
             * 3.如果说当前播放的音乐不是正在执行的播放的音乐的话，那么就调用setPath的方法
             * */
            if (mMediaPlayerHelper.getPath()!=null&& mMediaPlayerHelper.getPath().equals(mMusicModel.getPath())) {
                mMediaPlayerHelper.start();
            }else{
                mMediaPlayerHelper.setPath(mMusicModel.getPath());
                mMediaPlayerHelper.setOnMediaPlayerHelperListener(new MediaPlayerHelper.OnMediaPlayerHelperListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mMediaPlayerHelper.start();
                    }

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopSelf();
                    }
                });
            }
        }

        /**
         * 暂停播放
         * */
        public void stopMusic(){
            mMediaPlayerHelper.pause();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBind();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayerHelper=MediaPlayerHelper.getInstance(this);
    }
    /**
     * 系统默认不允许不可见的后台服务播放音乐，希望服务是可见的
     * Notification
     * */
    /**
     * 设置服务在前台可见
     * */
    private void startForeground(){
        /**通知栏点击跳转的Intent*/
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent
                        (this, WelcomeActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setContentTitle(mMusicModel.getName())
                .setContentText(mMusicModel.getAuthor())
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(pendingIntent)
                .build();
        /**设置notification在前台展示*/
        startForeground(NOTIFICATION_ID,notification);
    }
}