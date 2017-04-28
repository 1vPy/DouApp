package com.roy.douapp.ui.presenter.impl;

import android.content.Context;

import com.roy.douapp.AppConfig;
import com.roy.douapp.http.api.ApiFactory;
import com.roy.douapp.http.api.RequestCallback;
import com.roy.douapp.http.bean.movie.JsonMovieBean;
import com.roy.douapp.ui.presenter.Presenter;
import com.roy.douapp.ui.presenter.callback.ComingMovieCB;

/**
 * Created by Administrator on 2017/4/10.
 */

public class ComingMoviePresenterImpl implements Presenter {
    private Context mContext;
    private ComingMovieCB mComingMovieCB;

    public ComingMoviePresenterImpl(Context context,ComingMovieCB comingMovieCB){
        this.mContext = context;
        this.mComingMovieCB = comingMovieCB;
    }


    @Override
    public void initialized() {
        ApiFactory.getDoubanApiService().getComingMovie(0, AppConfig.PAGE_SIZE, new RequestCallback<JsonMovieBean>() {
            @Override
            public void onSuccess(JsonMovieBean jsonMovieBean) {
                mComingMovieCB.comingMovie(jsonMovieBean);
            }

            @Override
            public void onFailure(String message) {
                mComingMovieCB.showError(message);
            }
        });
    }

    public void loadMore(int start,int count){
        ApiFactory.getDoubanApiService().getComingMovie(start, count, new RequestCallback<JsonMovieBean>() {
            @Override
            public void onSuccess(JsonMovieBean jsonMovieBean) {
                mComingMovieCB.comingMovie(jsonMovieBean);
            }

            @Override
            public void onFailure(String message) {
                mComingMovieCB.showError(message);
            }
        });
    }
}
