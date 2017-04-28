package com.roy.douapp.ui.presenter.impl;

import android.content.Context;

import com.roy.douapp.http.api.ApiFactory;
import com.roy.douapp.http.api.RequestCallback;
import com.roy.douapp.http.bean.music.billlist.JsonSongListBean;
import com.roy.douapp.ui.presenter.Presenter;
import com.roy.douapp.ui.presenter.callback.MusicBillListCB;

/**
 * Created by Administrator on 2017/4/14.
 */

public class MusicBillListPresenterImpl implements Presenter{
    private Context mContext;
    private MusicBillListCB mMusicBillListCB;

    public MusicBillListPresenterImpl(Context context,MusicBillListCB musicBillListCB){
        this.mContext = context;
        this.mMusicBillListCB = musicBillListCB;
    }

    @Override
    public void initialized() {

    }
    public void initialized(int type){
        ApiFactory.getBaiduApiService().getMusicBillList(type, 0, 100, new RequestCallback<JsonSongListBean>() {
            @Override
            public void onSuccess(JsonSongListBean jsonSongListBean) {
                mMusicBillListCB.musicBillList(jsonSongListBean);
            }

            @Override
            public void onFailure(String message) {
                mMusicBillListCB.showError(message);
            }
        });
    }
}
