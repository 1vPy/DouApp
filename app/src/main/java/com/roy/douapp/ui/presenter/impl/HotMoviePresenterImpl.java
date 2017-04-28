package com.roy.douapp.ui.presenter.impl;

import android.content.Context;

import com.roy.douapp.http.api.ApiFactory;
import com.roy.douapp.http.api.RequestCallback;
import com.roy.douapp.http.bean.movie.JsonMovieBean;
import com.roy.douapp.ui.presenter.Presenter;
import com.roy.douapp.ui.presenter.callback.HotMovieCB;

/**
 * Created by Administrator on 2017/4/10.
 */

public class HotMoviePresenterImpl implements Presenter {
    private Context mContext;
    private HotMovieCB mHotMovieCB;

    public HotMoviePresenterImpl(Context context, HotMovieCB hotMovieCB){
        this.mContext = context;
        this.mHotMovieCB = hotMovieCB;
    }

    @Override
    public void initialized() {
        ApiFactory.getDoubanApiService().getHotMovie(0, new RequestCallback<JsonMovieBean>() {
            @Override
            public void onSuccess(JsonMovieBean jsonMovieBean) {
                mHotMovieCB.hotMovie(jsonMovieBean);
            }

            @Override
            public void onFailure(String message) {
                mHotMovieCB.showError(message);
            }
        });
    }
}
