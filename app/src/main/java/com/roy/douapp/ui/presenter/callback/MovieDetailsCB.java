package com.roy.douapp.ui.presenter.callback;

import com.roy.douapp.http.bean.movie.details.JsonDetailBean;

/**
 * Created by Administrator on 2017/4/12.
 */

public interface MovieDetailsCB extends BaseCB{
    void movieDetails(JsonDetailBean jsonDetailBean);
}
