package com.roy.douapp.ui.view;

import com.roy.douapp.http.bean.music.billcategory.JsonMusicBillBean;

/**
 * Created by Administrator on 2017/4/14.
 */

public interface MusicBillView extends BaseView {
    void musicBill(JsonMusicBillBean jsonMusicBillBean);
}
