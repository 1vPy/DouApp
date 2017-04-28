package com.roy.douapp.ui.presenter.callback;

import com.roy.douapp.http.bean.music.billcategory.JsonMusicBillBean;

/**
 * Created by Administrator on 2017/4/14.
 */

public interface MusicBillCB extends BaseCB{
    void musicBill(JsonMusicBillBean jsonMusicBillBean);
}
