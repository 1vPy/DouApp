
package com.roy.douapp.http.bean.music.search;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SongInfo {

    @SerializedName("song_list")
    private List<SongList> mSongList;
    @SerializedName("total")
    private Long mTotal;

    public List<SongList> getSongList() {
        return mSongList;
    }

    public void setSongList(List<SongList> songList) {
        mSongList = songList;
    }

    public Long getTotal() {
        return mTotal;
    }

    public void setTotal(Long total) {
        mTotal = total;
    }

}
