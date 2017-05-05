package com.roy.douapp.ui.view;

import com.roy.douapp.http.bean.music.billlist.JsonSongListBean;

/**
 * Created by 1vPy(Roy) on 2017/4/14.
 */

public interface MusicBillListView extends BaseView {
    void musicBillList(JsonSongListBean jsonSongListBean);
}
