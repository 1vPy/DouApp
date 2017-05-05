package com.roy.douapp.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.roy.douapp.IMusicPlayer;
import com.roy.douapp.IMusicPlayerListener;
import com.roy.douapp.db.manager.DBManager;
import com.roy.douapp.http.bean.music.musicinfo.JsonMusicInfoBean;
import com.roy.douapp.http.bean.music.playlist.MusicBean;
import com.roy.douapp.ui.view.MusicDetailsView;
import com.roy.douapp.ui.presenter.impl.MusicDetailsPresenterImpl;
import com.roy.douapp.utils.common.LogUtils;
import com.roy.douapp.widget.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public class MusicService extends Service implements MusicDetailsView, Player.OnPlayerRunListener {
    private static final String TAG = MusicService.class.getSimpleName();

    //control
    public static final int STOP_MUSIC = 199;
    public static final int PLAY_MUSIC = 200;
    public static final int PAUSE_MUSIC = 201;
    public static final int PREV_MUSIC = 202;
    public static final int NEXT_MUSIC = 203;
    public static final int SEEK_MUSIC = 204;

    //play mode
    public static final int MODE_RANDOM_MUSIC = 300;
    public static final int MODE_SINGLE_MUSIC = 301;
    public static final int MODE_REPEAT_MUSIC = 302;

    //action
    public static final int PLAYING = 400;
    public static final int PAUSED = 401;
    public static final int STOPPED = 402;

    //return type
    public static final int PLAY_PROGRESS = 500;
    public static final int PLAY_CHANGE = 501;
    public static final int PLAY_BUFFER = 502;
    public static final int PLAY_ACTION = 503;
    public static final int PLAY_POSITION = 504;
    public static final int PLAY_MODE = 505;

    private RemoteCallbackList<IMusicPlayerListener> mRemoteCallbackList = new RemoteCallbackList<>();

    private List<MusicBean> mMusicBeanList = new ArrayList<>();

    private MusicDetailsPresenterImpl mPresenter;

    private Player mPlayer;

    public static int mCurrent_mode = MODE_REPEAT_MUSIC;

    public static int mCurrent_action = STOPPED;

    public static int mCurrent_position = 0;

    public String mCurrent_albumPicUrl;


    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = Player.getInstance(this);
        mMusicBeanList = DBManager.getInstance(this).searchMusic();
        mPresenter = new MusicDetailsPresenterImpl(this, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private IMusicPlayer.Stub mBinder = new IMusicPlayer.Stub() {

        @Override
        public boolean isPlaying() throws RemoteException {
            return mPlayer.mediaPlayer.isPlaying();
        }

        @Override
        public String getCurrentMusicName() throws RemoteException {
            return mMusicBeanList.get(mCurrent_position).getMusicName();
        }

        @Override
        public String getCurrentArtistName() throws RemoteException {
            return mMusicBeanList.get(mCurrent_position).getMusicName();
        }

        @Override
        public String getCurrentAlbumPicUrl() throws RemoteException {
            return mCurrent_albumPicUrl;
        }

        @Override
        public void play() throws RemoteException {
            LogUtils.log(TAG,"play",LogUtils.DEBUG);
            playSong();
        }

        @Override
        public void pause() throws RemoteException {
            LogUtils.log(TAG,"pause",LogUtils.DEBUG);
            pauseSong();
        }

        @Override
        public void stop() throws RemoteException {
            stopSong();
        }

        @Override
        public void next() throws RemoteException {
            nextSong();
        }

        @Override
        public void prev() throws RemoteException {
            prevSong();
        }

        @Override
        public void seekTo(int progress) throws RemoteException {
            seekToSong(progress);
        }

        @Override
        public List<MusicBean> getMusicList() throws RemoteException {
            return mMusicBeanList;
        }

        @Override
        public void registerListener(IMusicPlayerListener listener) throws RemoteException {
            mRemoteCallbackList.register(listener);
        }

        @Override
        public void unregisterListener(IMusicPlayerListener listener) throws RemoteException {
            mRemoteCallbackList.unregister(listener);
        }

    };

    private void playMode(int action) {
        switch (action) {
            case MODE_RANDOM_MUSIC:
                mCurrent_mode = MODE_RANDOM_MUSIC;
                break;
            case MODE_REPEAT_MUSIC:
                mCurrent_mode = MODE_REPEAT_MUSIC;
                break;
            case MODE_SINGLE_MUSIC:
                mCurrent_mode = MODE_SINGLE_MUSIC;
                break;
        }
    }

    private void stopSong() {
        mPlayer.stop();
        mCurrent_action = STOPPED;
    }

    private void playSong() {
        mMusicBeanList = DBManager.getInstance(this).searchMusic();
        switch (mCurrent_action) {
            case STOPPED:
                mPresenter.initialized(mMusicBeanList.get(mCurrent_position).getMusicId());
                break;
            case PAUSED:
                mPlayer.start();
                break;
        }
        mCurrent_action = PLAYING;
    }

    private void pauseSong() {
        if (mPlayer.mediaPlayer != null && mPlayer.mediaPlayer.isPlaying()) {
            mPlayer.pause();
            mCurrent_action = PAUSED;
        }
    }

    private void prevSong() {
        mMusicBeanList = DBManager.getInstance(this).searchMusic();
        prevPosition();
        mPresenter.initialized(mMusicBeanList.get(mCurrent_position).getMusicId());
    }

    private void nextSong() {
        mMusicBeanList = DBManager.getInstance(this).searchMusic();
        nextPosition();
        mPresenter.initialized(mMusicBeanList.get(mCurrent_position).getMusicId());
    }

    private void seekToSong(int progress) {
        if (mPlayer.mediaPlayer != null && mPlayer.mediaPlayer.isPlaying()) {
            mPlayer.mediaPlayer.seekTo(progress);
        }
    }

    private void nextPosition() {
        switch (mCurrent_mode) {
            case MODE_RANDOM_MUSIC:
                mCurrent_position = randomMusic();
                break;
            case MODE_REPEAT_MUSIC:
                if (mCurrent_position >= mMusicBeanList.size() - 1) {
                    mCurrent_position = 0;
                } else {
                    mCurrent_position++;
                }
                break;
            case MODE_SINGLE_MUSIC:
                break;
        }
    }

    private void prevPosition() {
        switch (mCurrent_mode) {
            case MODE_RANDOM_MUSIC:
                mCurrent_position = randomMusic();
                break;
            case MODE_REPEAT_MUSIC:
                if (mCurrent_position <= 0) {
                    mCurrent_position = mMusicBeanList.size() - 1;
                } else {
                    mCurrent_position--;
                }
                break;
            case MODE_SINGLE_MUSIC:
                break;
        }
    }

    private int randomMusic() {
        return (int) (Math.random() * mMusicBeanList.size());
    }

    @Override
    public void musicDetails(JsonMusicInfoBean jsonMusicInfoBean) {
        mCurrent_action = PLAYING;
        mPlayer.playUrl(jsonMusicInfoBean.getSongurl().getUrl().get(0).getShowLink());
        mCurrent_albumPicUrl = jsonMusicInfoBean.getSonginfo().getAlbum500500();
        sendMusicChange(jsonMusicInfoBean.getSonginfo().getTitle(),jsonMusicInfoBean.getSonginfo().getAuthor(),jsonMusicInfoBean.getSonginfo().getAlbum500500());
    }

    @Override
    public void showError(String msg) {

    }


    @Override
    public void onCompletion(MediaPlayer arg0) {
        nextSong();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
        sendBufferingUpdate(bufferingProgress);
    }

    @Override
    public void onProgress(int position, int duration) {
        sendProgress(position, duration);
    }

    private void sendBufferingUpdate(int bufferingProgress) {
        try {
            final int N = mRemoteCallbackList.beginBroadcast();
            for (int i = 0; i < N; i++) {
                IMusicPlayerListener broadcastItem = mRemoteCallbackList.getBroadcastItem(i);
                if (broadcastItem != null) {
                    broadcastItem.onBufferingUpdate(bufferingProgress);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            mRemoteCallbackList.finishBroadcast();
        }
    }

    private void sendProgress(int position, int duration) {
        try {
            final int N = mRemoteCallbackList.beginBroadcast();
            for (int i = 0; i < N; i++) {
                IMusicPlayerListener broadcastItem = mRemoteCallbackList.getBroadcastItem(i);
                if (broadcastItem != null) {
                    broadcastItem.onProgressUpdate(position, duration);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            mRemoteCallbackList.finishBroadcast();
        }
    }

    private void sendMusicChange(String musicName, String singerName, String albumPicUrl) {
        try {
            final int N = mRemoteCallbackList.beginBroadcast();
            for (int i = 0; i < N; i++) {
                IMusicPlayerListener broadcastItem = mRemoteCallbackList.getBroadcastItem(i);
                if (broadcastItem != null) {
                    broadcastItem.onMusicChange(musicName, singerName, albumPicUrl);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            mRemoteCallbackList.finishBroadcast();
        }
    }
}
