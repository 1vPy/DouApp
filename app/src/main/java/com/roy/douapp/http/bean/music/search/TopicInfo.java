
package com.roy.douapp.http.bean.music.search;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class TopicInfo {

    @SerializedName("total")
    private Long mTotal;

    public Long getTotal() {
        return mTotal;
    }

    public void setTotal(Long total) {
        mTotal = total;
    }

}
