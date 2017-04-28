
package com.roy.douapp.http.bean.music.search;

import java.util.List;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ArtistInfo {

    @SerializedName("artist_list")
    private List<ArtistList> mArtistList;
    @SerializedName("total")
    private Long mTotal;

    public List<ArtistList> getArtistList() {
        return mArtistList;
    }

    public void setArtistList(List<ArtistList> artistList) {
        mArtistList = artistList;
    }

    public Long getTotal() {
        return mTotal;
    }

    public void setTotal(Long total) {
        mTotal = total;
    }

}
