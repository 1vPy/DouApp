package com.roy.douapp.ui.presenter.callback;

import com.roy.douapp.http.bean.movie.JsonMovieBean;

/**
 * Created by Administrator on 2017/4/10.
 */

public interface ComingMovieCB extends BaseCB{

    void comingMovie(JsonMovieBean jsonMovieBean);
}
