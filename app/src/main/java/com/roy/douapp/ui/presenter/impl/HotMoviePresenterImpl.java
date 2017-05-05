package com.roy.douapp.ui.presenter.impl;

import android.content.Context;

import com.roy.douapp.http.api.ApiFactory;
import com.roy.douapp.http.api.RequestCallback;
import com.roy.douapp.http.bean.movie.JsonMovieBean;
import com.roy.douapp.ui.presenter.HotMoviePresenter;
import com.roy.douapp.ui.view.HotMovieView;

/**
 * Created by 1vPy(Roy) on 2017/4/10.
 */

public class HotMoviePresenterImpl implements HotMoviePresenter {
    private Context mContext;
    private HotMovieView mHotMovieCB;

    public HotMoviePresenterImpl(Context context, HotMovieView hotMovieCB) {
        this.mContext = context;
        this.mHotMovieCB = hotMovieCB;
    }

    @Override
    public void initialized() {
        ApiFactory.getDoubanApiService().getHotMovie(0, 20, null, new RequestCallback<JsonMovieBean>() {
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

    public void initialized(String city) {
        ApiFactory.getDoubanApiService().getHotMovie(0, 20, city, new RequestCallback<JsonMovieBean>() {
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
