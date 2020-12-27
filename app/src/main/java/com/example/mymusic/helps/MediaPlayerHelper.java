package com.example.mymusic.helps;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;
/**
 * 1.直接在activity当中去创建播放音乐，音乐与activity绑定，Activity运行时播放音乐，activity退出时音乐停止播放
 * 2.通过全局单例类与Application进行绑定，Application运行时播放音乐，Application退出时音乐停止播放
 * 3.通过service进行音乐播放
 * */
public class MediaPlayerHelper {
    private static MediaPlayerHelper instance;
    private String mPath;
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private OnMediaPlayerHelperListener onMediaPlayerHelperListener;

    public static MediaPlayerHelper getInstance(Context context){
        if (instance==null) {
            synchronized (MediaPlayerHelper.class){
                if ((instance==null)) {
                    instance=new MediaPlayerHelper(context);
                }
            }
        }
        return instance;
    }
    private MediaPlayerHelper(Context context){
        mContext=context;
        mMediaPlayer = new MediaPlayer();
    }

/* 1.setPath:当前需要播放的音乐
 * 2.start：播放音乐
 * 3.pause:暂停播放
 */
    /*1.setPath:当前需要播放的音乐*/
    public void setPath(String path){
        /** 1.当前音乐正在播放或者切换了音乐，重置音乐播放状态
         * 2.设置音乐播放路径
         * 3.准备播放*/
        /*mPath=path;*/
        /**当音乐处于播放状态时，如果音乐处于播放状态，那么我们就去重置音乐的播放状态，如果音乐没有处于播放状态的话（我们就不会去重置
         * 音乐的状态）
         * 1.当前音乐正在播放或者切换了音乐，重置音乐播放状态*/
        if (mMediaPlayer.isPlaying() || !path.equals(mPath)) {
            mMediaPlayer.reset();
        }
        mPath=path;
        /*2.设置音乐播放路径*/
        try {
            mMediaPlayer.setDataSource(mContext,Uri.parse(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*3.准备播放*/
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (onMediaPlayerHelperListener!=null) {
                    onMediaPlayerHelperListener.onPrepared(mp);
                }
            }
        });
        /**
         * 监听音乐播放完成
         */
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (onMediaPlayerHelperListener!=null) {
                    onMediaPlayerHelperListener.onCompletion(mp);
                }
            }
        });
    }

    /*返回正在播放的音乐路径*/
    public String getPath(){
        return mPath;
    }

    /* 2.start：播放音乐*/
    public void start(){
        if (mMediaPlayer.isPlaying()) {
            return;
        }
        mMediaPlayer.start();
    }
    /*3.pause:暂停播放*/
    public void pause(){
        mMediaPlayer.pause();
    }
    public void setOnMediaPlayerHelperListener(OnMediaPlayerHelperListener onMediaPlayerHelperListener) {
        this.onMediaPlayerHelperListener = onMediaPlayerHelperListener;
    }
    public interface OnMediaPlayerHelperListener{
        void onPrepared(MediaPlayer mp);
        void onCompletion(MediaPlayer mp);
    }
}
