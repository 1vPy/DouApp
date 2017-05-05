package com.roy.douapp.ui.presenter.impl;

import android.content.Context;

import com.roy.douapp.db.DBService;
import com.roy.douapp.db.manager.DBManager;
import com.roy.douapp.http.api.RequestCallback;
import com.roy.douapp.http.bean.collection.MovieCollection;
import com.roy.douapp.ui.presenter.MovieCollectionPresenter;
import com.roy.douapp.ui.view.MovieCollectionView;

import java.util.List;

/**
 * Created by 1vPy(Roy) on 2017/5/5.
 */

public class MovieCollectionPresenterImpl implements MovieCollectionPresenter{
    private Context mContext;
    private MovieCollectionView mMovieCollectionView;

    public MovieCollectionPresenterImpl(Context context, MovieCollectionView movieCollectionView){
        mContext = context;
        mMovieCollectionView = movieCollectionView;
    }

    @Override
    public void searchCollection() {
        DBService.searchMovieCollection(new RequestCallback<List<MovieCollection>>() {
            @Override
            public void onSuccess(List<MovieCollection> movieCollections) {
                mMovieCollectionView.MovieCollection(movieCollections);
            }

            @Override
            public void onFailure(String message) {
                mMovieCollectionView.showError(message);
            }
        });
    }
}
