package com.roy.douapp.ui.view;

import com.roy.douapp.http.bean.movie.JsonMovieBean;

/**
 * Created by 1vPy(Roy) on 2017/4/13.
 */

public interface SearchMovieView extends BaseView {
    void searchMovie(JsonMovieBean jsonMovieBean);
}
