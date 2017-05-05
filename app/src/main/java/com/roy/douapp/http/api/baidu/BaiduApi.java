package com.roy.douapp.http.api.baidu;

import com.roy.douapp.http.bean.music.billcategory.JsonMusicBillBean;
import com.roy.douapp.http.bean.music.billlist.JsonSongListBean;
import com.roy.douapp.http.bean.music.musicinfo.JsonMusicInfoBean;
import com.roy.douapp.http.bean.music.search.JsonSearchBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by 1vPy(Roy) on 2017/4/13.
 */

public interface BaiduApi {
    @Headers("User-Agent:Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4")
    @GET("ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.billboard.billCategory&kflag=1")
    Observable<JsonMusicBillBean> getMusicBillCategory();

    @Headers("User-Agent:Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4")
    @GET("ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.billboard.billList")
    Observable<JsonSongListBean> getMusicBillList(@Query("type") int type, @Query("offset") int offset, @Query("size") int size);

    @Headers("User-Agent:Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4")
    @GET("ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.search.merge")
    Observable<JsonSearchBean> getMusicSearch(@Query("query")String query, @Query("page_no")int pageNo, @Query("page_size") int pageSize);

    @Headers("User-Agent:Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4")
    @GET
    Observable<JsonMusicInfoBean> getMusicInfo(@Url String url);
}
