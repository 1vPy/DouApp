package com.roy.douapp.ui.presenter.impl;

import android.content.Context;

import com.roy.douapp.http.api.ApiFactory;
import com.roy.douapp.http.api.RequestCallback;
import com.roy.douapp.http.bean.movie.JsonMovieBean;
import com.roy.douapp.http.bean.movie.details.JsonDetailBean;
import com.roy.douapp.ui.presenter.Presenter;
import com.roy.douapp.ui.presenter.callback.SearchMovieCB;

/**
 * Created by Administrator on 2017/4/13.
 */

public class SearchMoviePresenterImpl implements Presenter {
    private Context mContext;
    private SearchMovieCB mSearchMovieCB;

    public SearchMoviePresenterImpl(Context context, SearchMovieCB searchMovieCB){
        this.mContext = context;
        this.mSearchMovieCB = searchMovieCB;
    }

    @Override
    public void initialized() {

    }

    public void initialized(String query){
        ApiFactory.getDoubanApiService().searchMovie(query, new RequestCallback<JsonMovieBean>() {
            @Override
            public void onSuccess(JsonMovieBean jsonMovieBean) {
                mSearchMovieCB.searchMovie(jsonMovieBean);
            }

            @Override
            public void onFailure(String message) {
                mSearchMovieCB.showError(message);
            }
        });
    }
}
