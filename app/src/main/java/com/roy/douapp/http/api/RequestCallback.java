package com.roy.douapp.http.api;

/**
 * Created by Administrator on 2017/4/10.
 */

public interface RequestCallback<T> {
    void onSuccess(T t);

    void onFailure(String message);
}
