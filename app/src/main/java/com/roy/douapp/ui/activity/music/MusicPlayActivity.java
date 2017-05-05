package com.roy.douapp.ui.activity.music;

import android.animation.ObjectAnimator;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.roy.douapp.DouApplication;
import com.roy.douapp.DouKit;
import com.roy.douapp.IMusicPlayer;
import com.roy.douapp.R;
import com.roy.douapp.base.BaseSwipeBackActivity;
import com.roy.douapp.db.manager.DBManager;
import com.roy.douapp.http.bean.music.playlist.MusicBean;
import com.roy.douapp.service.MusicService;
import com.roy.douapp.ui.fragment.music.PlaylistFragment;
import com.roy.douapp.utils.common.LogUtils;
import com.roy.douapp.utils.common.ScreenUtils;
import com.roy.douapp.utils.image.ImageUtils;
import com.roy.douapp.widget.MusicPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */

public class MusicPlayActivity extends BaseSwipeBackActivity implements View.OnClickListener, MusicPlayer.OnPlayRunningListener {
    private static final String TAG = MusicPlayActivity.class.getSimpleName();

    private TextView tv_music_title;
    private SeekBar sb_vol;


    private SeekBar music_progress;
    private RelativeLayout iv_music_disc;
    private ImageView iv_music_album;
    private ImageView iv_pin;


    private ImageView iv_playing_mode;
    private ImageView iv_playing_pre;
    private ImageView iv_playing_play;
    private ImageView iv_playing_next;
    private ImageView iv_playing_playlist;

    private AudioManager mAudioManager;
    private int mCurrentVol;
    private int mMaxVol;

    private List<MusicBean> mMusicBeanList = new ArrayList<>();

    private ObjectAnimator operatingAnim;
    private LinearInterpolator lin;

    private RotateAnimation animationIn;
    private RotateAnimation animationOut;

    private IMusicPlayer mMusicPlayerService;

    private int duration = 0;
    private boolean isPause = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplay);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!DouApplication.linkSuccess) {
                    SystemClock.sleep(300);
                }

            }

        }).start();
        MusicPlayer.registerListener(DouKit.getMusicPlayerService(), this);
        tv_music_title.setText(MusicPlayer.getCurrentMusicName());
        ImageUtils.displayCircleImage(MusicPlayActivity.this, MusicPlayer.getCurrentAlbumPicUrl(), ScreenUtils.dipToPx(MusicPlayActivity.this, 150), ScreenUtils.dipToPx(MusicPlayActivity.this, 150), iv_music_album, R.drawable.default_album);
        if (MusicPlayer.isPlaying()) {
            iv_playing_play.setImageResource(R.drawable.play_btn_pause);

            if (operatingAnim != null) {
                operatingAnim.start();
            }
            iv_pin.startAnimation(animationIn);
        } else {
            iv_playing_play.setImageResource(R.drawable.play_btn_play);

            if (operatingAnim != null && operatingAnim.isRunning()) {
                operatingAnim.pause();
            }
            iv_pin.startAnimation(animationOut);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicPlayer.unRegisterListener();
    }

    private void init() {
        findView();
        initView();
        initData();
        initEvent();
    }

    private void findView() {
        tv_music_title = (TextView) findViewById(R.id.tv_music_title);
        sb_vol = (SeekBar) findViewById(R.id.sb_vol);

        music_progress = (SeekBar) findViewById(R.id.music_progress);
        iv_music_disc = (RelativeLayout) findViewById(R.id.iv_music_disc);
        iv_music_album = (ImageView) findViewById(R.id.iv_music_album);
        iv_pin = (ImageView) findViewById(R.id.iv_pin);

        iv_playing_mode = (ImageView) findViewById(R.id.iv_playing_mode);
        iv_playing_pre = (ImageView) findViewById(R.id.iv_playing_pre);
        iv_playing_play = (ImageView) findViewById(R.id.iv_playing_play);
        iv_playing_next = (ImageView) findViewById(R.id.iv_playing_next);
        iv_playing_playlist = (ImageView) findViewById(R.id.iv_playing_playlist);
    }

    private void initView() {
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mCurrentVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mMaxVol = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sb_vol.setMax(mMaxVol);
        sb_vol.setProgress(mCurrentVol);

        operatingAnim = ObjectAnimator.ofFloat(iv_music_disc, "rotation", 0.0f, 360.0f)
                .setDuration(50000);
        lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        operatingAnim.setRepeatCount(-1);
        animationIn = new RotateAnimation(0f, 30f, Animation.RELATIVE_TO_SELF,
                0.0689f, Animation.RELATIVE_TO_SELF, 0.0887f);
        animationIn.setDuration(300);
        animationIn.setFillAfter(true);
        animationOut = new RotateAnimation(30f, 0f, Animation.RELATIVE_TO_SELF,
                0.0689f, Animation.RELATIVE_TO_SELF, 0.0887f);
        animationOut.setDuration(300);
        animationOut.setFillAfter(true);
    }

    private void initData() {
        if (DBManager.getInstance(this).searchMusic() != null) {
            mMusicBeanList = DBManager.getInstance(this).searchMusic();
        }
        for (MusicBean musicBean : mMusicBeanList) {
            LogUtils.log(TAG, "musicName:" + musicBean.getMusicName(), LogUtils.DEBUG);
        }
        LogUtils.log(TAG, "position:" + MusicService.mCurrent_position, LogUtils.DEBUG);
    }

    private void initEvent() {
        music_progress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        iv_playing_mode.setOnClickListener(this);
        iv_playing_pre.setOnClickListener(this);
        iv_playing_play.setOnClickListener(this);
        iv_playing_next.setOnClickListener(this);
        iv_playing_playlist.setOnClickListener(this);
        sb_vol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAudioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC, progress, AudioManager.ADJUST_SAME);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_playing_mode:
                break;
            case R.id.iv_playing_pre:
                MusicPlayer.prev();
                break;
            case R.id.iv_playing_play:
                if (MusicPlayer.isPlaying()) {
                    iv_playing_play.setImageResource(R.drawable.play_btn_play);

                    if (operatingAnim != null && operatingAnim.isRunning()) {
                        operatingAnim.pause();
                    }
                    iv_pin.startAnimation(animationOut);
                    isPause = true;
                } else {
                    iv_playing_play.setImageResource(R.drawable.play_btn_pause);
                    if (operatingAnim != null) {
                        if (isPause) {
                            operatingAnim.resume();
                        } else {
                            operatingAnim.start();
                        }
                    }
                    iv_pin.startAnimation(animationIn);
                    isPause = false;
                }
                MusicPlayer.playOrPause();
                break;
            case R.id.iv_playing_next:
                MusicPlayer.next();
                break;
            case R.id.iv_playing_playlist:
                PlaylistFragment.newInstance().show(getSupportFragmentManager(), "playlistframent");
                break;
        }
    }

    @Override
    public void onBufferingUpdate(int bufferingProgress) {
        Message msg = Message.obtain();
        msg.what = 0;
        msg.arg1 = bufferingProgress;
        mHandle.sendMessage(msg);
    }

    @Override
    public void onProgressUpdate(int position, int duration) {
        this.duration = duration;
        Message msg = Message.obtain();
        msg.what = 1;
        msg.arg1 = position;
        msg.arg2 = duration;
        mHandle.sendMessage(msg);
    }

    @Override
    public void onMusicChange(String musicName, String singerName, String albumPicUrl) {
        LogUtils.log(TAG, "musicName:" + musicName + ",singerName:" + singerName + ",albumPicUrl" + albumPicUrl, LogUtils.DEBUG);
        String musicInfo = musicName + "|" + singerName + "|" + albumPicUrl;
        Message msg = Message.obtain();
        msg.what = 2;
        msg.obj = musicInfo;
        mHandle.sendMessage(msg);
    }

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    music_progress.setSecondaryProgress(msg.arg1);
                    break;
                case 1:
                    music_progress.setProgress(music_progress.getMax() * msg.arg1 / msg.arg2);
                    break;
                case 2:
                    String[] musicInfo = ((String) msg.obj).split("\\|");
                    tv_music_title.setText(musicInfo[0]);
                    ImageUtils.displayCircleImage(DouKit.getContext(), musicInfo[2], ScreenUtils.dipToPx(DouKit.getContext(), 150), ScreenUtils.dipToPx(DouKit.getContext(), 150), iv_music_album, R.drawable.default_album);
                    break;
            }
        }
    };

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
            this.progress = progress * duration
                    / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            MusicPlayer.seekTo(progress);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER, 0);
            mCurrentVol = mAudioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC);
            sb_vol.setProgress(mCurrentVol);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE, 0);
            mCurrentVol = mAudioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC);
            sb_vol.setProgress(mCurrentVol);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
