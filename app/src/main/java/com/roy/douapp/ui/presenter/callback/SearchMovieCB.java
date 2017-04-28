package com.roy.douapp.ui.presenter.callback;

import com.roy.douapp.http.bean.movie.JsonMovieBean;

/**
 * Created by Administrator on 2017/4/13.
 */

public interface SearchMovieCB extends BaseCB{
    void searchMovie(JsonMovieBean jsonMovieBean);
}
