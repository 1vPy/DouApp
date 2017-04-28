package com.roy.douapp.ui.presenter.callback;

import com.roy.douapp.http.bean.music.musicinfo.JsonMusicInfoBean;

/**
 * Created by Administrator on 2017/4/14.
 */

public interface MusicDetailsCB extends BaseCB{
    void musicDetails(JsonMusicInfoBean jsonMusicInfoBean);
}
