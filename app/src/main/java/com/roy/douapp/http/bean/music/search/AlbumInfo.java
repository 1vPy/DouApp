
package com.roy.douapp.http.bean.music.search;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class AlbumInfo {

    @SerializedName("album_list")
    private List<AlbumList> mAlbumList;
    @SerializedName("total")
    private Long mTotal;

    public List<AlbumList> getAlbumList() {
        return mAlbumList;
    }

    public void setAlbumList(List<AlbumList> albumList) {
        mAlbumList = albumList;
    }

    public Long getTotal() {
        return mTotal;
    }

    public void setTotal(Long total) {
        mTotal = total;
    }

}
