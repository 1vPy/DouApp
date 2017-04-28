package com.roy.douapp.ui.presenter.callback;

import com.roy.douapp.http.bean.music.billlist.JsonSongListBean;

/**
 * Created by Administrator on 2017/4/14.
 */

public interface MusicBillListCB extends BaseCB{
    void musicBillList(JsonSongListBean jsonSongListBean);
}
