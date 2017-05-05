package com.roy.douapp.ui.view;

import com.roy.douapp.http.bean.movie.details.JsonDetailBean;

/**
 * Created by Administrator on 2017/4/12.
 */

public interface MovieDetailsView extends BaseView {
    void movieDetails(JsonDetailBean jsonDetailBean);
}
