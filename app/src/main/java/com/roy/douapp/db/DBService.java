package com.roy.douapp.db;

import com.roy.douapp.DouKit;
import com.roy.douapp.db.manager.DBManager;
import com.roy.douapp.http.api.RequestCallback;
import com.roy.douapp.http.bean.collection.MovieCollection;
import com.roy.douapp.ui.activity.movie.MovieCollectionActivity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 1vPy(Roy) on 2017/5/5.
 */

public class DBService {

    public static void searchMovieCollection(final RequestCallback<List<MovieCollection>> rc){
        DBManager.getInstance(DouKit.getContext()).searchCollections()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MovieCollection>>() {
                    @Override
                    public void accept(@NonNull List<MovieCollection> movieCollections) throws Exception {
                        rc.onSuccess(movieCollections);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });
    }

}
