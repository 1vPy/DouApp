package com.roy.douapp.ui.presenter.impl;

import android.content.Context;

import com.roy.douapp.http.api.ApiFactory;
import com.roy.douapp.http.api.RequestCallback;
import com.roy.douapp.http.bean.movie.details.JsonDetailBean;
import com.roy.douapp.ui.presenter.Presenter;
import com.roy.douapp.ui.presenter.callback.MovieDetailsCB;

/**
 * Created by Administrator on 2017/4/12.
 */

public class MovieDetailsPresenterImpl implements Presenter{
    private Context mContext;
    private MovieDetailsCB mMovieDetailsCB;

    public MovieDetailsPresenterImpl(Context context,MovieDetailsCB movieDetailsCB){
        this.mContext = context;
        this.mMovieDetailsCB = movieDetailsCB;
    }


    @Override
    public void initialized() {

    }

    public void initialized(String id){
        ApiFactory.getDoubanApiService().getMovieDetail(id, new RequestCallback<JsonDetailBean>() {
            @Override
            public void onSuccess(JsonDetailBean jsonDetailBean) {
                mMovieDetailsCB.movieDetails(jsonDetailBean);
            }

            @Override
            public void onFailure(String message) {
                mMovieDetailsCB.showError(message);
            }
        });
    }
}
