package com.roy.douapp;

import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.roy.douapp.service.MusicService;
import com.yuyh.library.AppUtils;

/**
 * Created by Administrator on 2017/4/10.
 */

public class DouApplication extends Application {

    public static boolean linkSuccess = false;
    private IMusicPlayer mMusicPlayerService;

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicPlayerService = IMusicPlayer.Stub.asInterface(service);
            DouKit.setMusicPlayerService(mMusicPlayerService);
            linkSuccess = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bindService();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        initDouKit();
        initUtils();
        bindService();
    }

    private void initDouKit(){
        DouKit.setContext(getApplicationContext());
    }

    private void initUtils(){
        AppUtils.init(getApplicationContext());
    }

    private void bindService() {
        Intent intent = new Intent(getApplicationContext(), MusicService.class);
        bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
    }


}
