package com.roy.douapp.ui.presenter.impl;

import android.content.Context;

import com.roy.douapp.http.api.ApiFactory;
import com.roy.douapp.http.api.RequestCallback;
import com.roy.douapp.http.bean.music.musicinfo.JsonMusicInfoBean;
import com.roy.douapp.ui.presenter.MusicDetailsPresenter;
import com.roy.douapp.ui.view.MusicDetailsView;

/**
 * Created by 1vPy(Roy) on 2017/4/14.
 */

public class MusicDetailsPresenterImpl implements MusicDetailsPresenter{
    private Context mContext;
    private MusicDetailsView mMusicDetailsCB;


    public MusicDetailsPresenterImpl(Context context,MusicDetailsView musicDetailsCB){
        this.mContext = context;
        this.mMusicDetailsCB = musicDetailsCB;
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
