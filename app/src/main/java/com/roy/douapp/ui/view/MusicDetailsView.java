package com.roy.douapp.ui.view;

import com.roy.douapp.http.bean.music.musicinfo.JsonMusicInfoBean;

/**
 * Created by Administrator on 2017/4/14.
 */

public interface MusicDetailsView extends BaseView {
    void musicDetails(JsonMusicInfoBean jsonMusicInfoBean);
}
