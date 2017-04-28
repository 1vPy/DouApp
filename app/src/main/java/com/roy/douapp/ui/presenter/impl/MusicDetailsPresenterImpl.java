package com.roy.douapp.ui.presenter.impl;

import android.content.Context;

import com.roy.douapp.http.api.ApiFactory;
import com.roy.douapp.http.api.RequestCallback;
import com.roy.douapp.http.bean.music.musicinfo.JsonMusicInfoBean;
import com.roy.douapp.ui.presenter.Presenter;
import com.roy.douapp.ui.presenter.callback.MusicDetailsCB;

/**
 * Created by Administrator on 2017/4/14.
 */

public class MusicDetailsPresenterImpl implements Presenter{
    private Context mContext;
    private MusicDetailsCB mMusicDetailsCB;


    public MusicDetailsPresenterImpl(Context context,MusicDetailsCB musicDetailsCB){
        this.mContext = context;
        this.mMusicDetailsCB = musicDetailsCB;
    }

    @Override
    public void initialized() {

    }

    public void initialized(String songId){
        ApiFactory.getBaiduApiService().getMusicInfo(songId, new RequestCallback<JsonMusicInfoBean>() {
            @Override
            public void onSuccess(JsonMusicInfoBean jsonMusicInfoBean) {
                mMusicDetailsCB.musicDetails(jsonMusicInfoBean);
            }

            @Override
            public void onFailure(String message) {
                mMusicDetailsCB.showError(message);
            }
        });
    }
}
