package com.roy.douapp.ui.presenter.impl;

import android.content.Context;

import com.roy.douapp.http.api.ApiFactory;
import com.roy.douapp.http.api.RequestCallback;
import com.roy.douapp.http.bean.music.billcategory.JsonMusicBillBean;
import com.roy.douapp.ui.presenter.MusicBillPresenter;
import com.roy.douapp.ui.view.MusicBillView;

/**
 * Created by 1vPy(Roy) on 2017/4/14.
 */

public class MusicBillPresenterImpl implements MusicBillPresenter {
    private Context mContext;
    private MusicBillView mMusicBillCB;

    public MusicBillPresenterImpl(Context context, MusicBillView musicBillCB){
        this.mContext = context;
        this.mMusicBillCB = musicBillCB;
    }

    @Override
    public void initialized() {
        ApiFactory.getBaiduApiService().getMusicBillCategory(new RequestCallback<JsonMusicBillBean>() {
            @Override
            public void onSuccess(JsonMusicBillBean jsonMusicBillBean) {
                mMusicBillCB.musicBill(jsonMusicBillBean);
            }

            @Override
            public void onFailure(String message) {
                mMusicBillCB.showError(message);
            }
        });
    }
}
