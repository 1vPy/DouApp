package com.roy.douapp.widget;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;

import com.roy.douapp.utils.common.LogUtils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/4/14.
 */

public class Player implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private static final String TAG = Player.class.getSimpleName();
    private static Player instance;
    private OnPlayerRunListener mOnPlayerRunListener;

    public MediaPlayer mediaPlayer;
    private Timer mTimer = new Timer();

    private Player(OnPlayerRunListener onPlayerRunListener) {
        mOnPlayerRunListener = onPlayerRunListener;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e) {
            LogUtils.log(TAG, "error:" + e, LogUtils.DEBUG);
        }

        mTimer.schedule(mTimerTask, 0, 1000);
    }

    public static Player getInstance(OnPlayerRunListener onPlayerRunListener) {
        if (instance == null) {
            synchronized (Player.class) {
                if (instance == null) {
                    instance = new Player(onPlayerRunListener);
                }
            }
        }
        return instance;
    }

    /*******************************************************
     * 通过定时器和Handler来更新进度条
     ******************************************************/
    TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (mediaPlayer == null)
                return;
            if (mediaPlayer.isPlaying()) {
                handleProgress.sendEmptyMessage(0);
            }
        }
    };

    Handler handleProgress = new Handler() {
        public void handleMessage(Message msg) {

            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();

            if (duration > 0) {
/*                long pos = skbProgress.getMax() * position / duration;
                skbProgress.setProgress((int) pos);*/
                if (mOnPlayerRunListener != null) {
                    mOnPlayerRunListener.onProgress(position, duration);
                }
            }
        }

        ;
    };
    //*****************************************************

    public void start() {
        mediaPlayer.start();
    }

    public void playUrl(String videoUrl) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.prepare();//prepare之后自动播放
            //mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    /**
     * 通过onPrepared播放
     */
    public void onPrepared(MediaPlayer arg0) {
        arg0.start();
        LogUtils.log(TAG, "onPrepared", LogUtils.DEBUG);
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        if (mOnPlayerRunListener != null) {
            mOnPlayerRunListener.onCompletion(arg0);
        }
        LogUtils.log(TAG, "onCompletion", LogUtils.DEBUG);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
        if (mOnPlayerRunListener != null) {
            mOnPlayerRunListener.onBufferingUpdate(arg0, bufferingProgress);
        }
/*        skbProgress.setSecondaryProgress(bufferingProgress);
        int currentProgress = skbProgress.getMax() * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
        LogUtils.log(TAG, currentProgress + "% play/" + bufferingProgress + "% buffer", LogUtils.DEBUG);*/
    }

    public void setOnPlayerRunListener(OnPlayerRunListener onPlayerRunListener) {
        mOnPlayerRunListener = onPlayerRunListener;
    }

    public interface OnPlayerRunListener {
        void onCompletion(MediaPlayer arg0);

        void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress);

        void onProgress(int position, int duration);
    }

}