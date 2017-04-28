
package com.roy.douapp.http.bean.music.musicinfo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Songurl {

    @SerializedName("url")
    private List<Url> mUrl;

    public List<Url> getUrl() {
        return mUrl;
    }

    public void setUrl(List<Url> url) {
        mUrl = url;
    }

}
