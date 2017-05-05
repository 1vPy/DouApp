package com.roy.douapp;

import android.content.Context;

/**
 * Created by Administrator on 2017/4/10.
 */

public class DouKit {
    private static Context mContext;

    private static String mNowCity;

    private static IMusicPlayer mMusicPlayerService;

    public static void setContext(Context context){
        mContext = context;
    }

    public static Context getContext(){
        return mContext;
    }

    public static void setMusicPlayerService(IMusicPlayer musicPlayerService){
        mMusicPlayerService = musicPlayerService;
    }

    public static IMusicPlayer getMusicPlayerService() {
        return mMusicPlayerService;
    }

    public static void setNowCity(String city){
        mNowCity = city;
    }

    public static String getNowCity(){
        return mNowCity;
    }
}
