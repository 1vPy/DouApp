package com.roy.douapp;

import com.roy.douapp.IMusicPlayerListener;
import com.roy.douapp.http.bean.music.playlist.MusicBean;


interface IMusicPlayer {
        boolean isPlaying();
        String getCurrentMusicName();
        String getCurrentArtistName();
        String getCurrentAlbumPicUrl();
        void play();
        void pause();
        void stop();
        void next();
        void prev();
        void seekTo(int progress);
        List<MusicBean> getMusicList();
        void registerListener(IMusicPlayerListener listener);
        void unregisterListener(IMusicPlayerListener listener);
}
