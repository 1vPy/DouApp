package com.roy.douapp.ui.view;

        import com.roy.douapp.http.bean.movie.JsonMovieBean;

/**
 * Created by 1vPy(Roy) on 2017/4/10.
 */

public interface HotMovieView extends BaseView {

    void hotMovie(JsonMovieBean jsonMovieBean);
}
