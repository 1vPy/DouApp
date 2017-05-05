package com.roy.douapp.ui.presenter.impl;

import android.content.Context;

import com.roy.douapp.http.api.ApiFactory;
import com.roy.douapp.http.api.RequestCallback;
import com.roy.douapp.http.bean.music.billlist.JsonSongListBean;
import com.roy.douapp.ui.presenter.MusicBillListPresenter;
import com.roy.douapp.ui.view.MusicBillListView;

/**
 * Created by Administrator on 2017/4/14.
 */

public class MusicBillListPresenterImpl implements MusicBillListPresenter{
    private Context mContext;
    private MusicBillListView mMusicBillListCB;

    public MusicBillListPresenterImpl(Context context,MusicBillListView musicBillListCB){
        this.mContext = context;
        this.mMusicBillListCB = musicBillListCB;
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
