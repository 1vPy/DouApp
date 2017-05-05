package com.roy.douapp.widget;

import android.os.Message;
import android.os.RemoteException;

import com.roy.douapp.IMusicPlayer;
import com.roy.douapp.IMusicPlayerListener;
import com.roy.douapp.http.bean.music.playlist.MusicBean;
import com.roy.douapp.service.MusicService;
import com.roy.douapp.utils.common.LogUtils;

import java.util.List;

/**
 * Created by 1vPy(Roy) on 2017/4/20.
 */

public class MusicPlayer {
    private static final String TAG = MusicPlayer.class.getSimpleName();
    private static IMusicPlayer mMusicPlayerService;
    private static OnPlayRunningListener mOnPlayRunningListener;

    public static void registerListener(IMusicPlayer musicPlayerService, OnPlayRunningListener onPlayRunningListener) {
        mMusicPlayerService = musicPlayerService;
        mOnPlayRunningListener = onPlayRunningListener;
        try {
            mMusicPlayerService.registerListener(mPlayerListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void unRegisterListener() {
        try {
            mMusicPlayerService.unregisterListener(mPlayerListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static IMusicPlayerListener mPlayerListener = new IMusicPlayerListener.Stub() {

        @Override
        public void onBufferingUpdate(int bufferingProgress) throws RemoteException {
            mOnPlayRunningListener.onBufferingUpdate(bufferingProgress);
        }

        @Override
        public void onProgressUpdate(int position, int duration) throws RemoteException {
            mOnPlayRunningListener.onProgressUpdate(position, duration);
        }

        @Override
        public void onMusicChange(String musicName, String singerName, String albumPicUrl) throws RemoteException {
            LogUtils.log(TAG, "musicName:" + musicName + ",singerName:" + singerName + ",albumPicUrl" + albumPicUrl, LogUtils.DEBUG);
            mOnPlayRunningListener.onMusicChange(musicName, singerName, albumPicUrl);
        }
    };

    public static boolean isPlaying() {
        try {
            if (mMusicPlayerService != null) {
                return mMusicPlayerService.isPlaying();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void playOrPause() {
        LogUtils.log(TAG, "mMusicPlayerService:" + mMusicPlayerService, LogUtils.DEBUG);
        try {
            if (mMusicPlayerService != null) {
                if (mMusicPlayerService.isPlaying()) {
                    mMusicPlayerService.pause();

                } else {
                    mMusicPlayerService.play();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void next() {
        try {
            if (mMusicPlayerService != null) {
                mMusicPlayerService.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void prev() {
        try {
            if (mMusicPlayerService != null) {
                mMusicPlayerService.prev();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void seekTo(int progress) {
        try {
            if (mMusicPlayerService != null) {
                mMusicPlayerService.seekTo(progress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentMusicName() {
        try {
            if (mMusicPlayerService != null) {
                return mMusicPlayerService.getCurrentMusicName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrentArtistName() {
        try {
            if (mMusicPlayerService != null) {
                return mMusicPlayerService.getCurrentArtistName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrentAlbumPicUrl() {
        try {
            if (mMusicPlayerService != null) {
                return mMusicPlayerService.getCurrentAlbumPicUrl();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<MusicBean> getMusicList() {
        try {
            if (mMusicPlayerService != null) {
                return mMusicPlayerService.getMusicList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface OnPlayRunningListener {
        void onBufferingUpdate(int bufferingProgress);

        void onProgressUpdate(int position, int duration);

        void onMusicChange(String musicName, String singerName, String albumPicUrl);
    }

}
