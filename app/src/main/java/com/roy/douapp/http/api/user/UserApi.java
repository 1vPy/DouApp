package com.roy.douapp.http.api.user;

import com.roy.douapp.http.bean.user.Results;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 1vPy(Roy) on 2017/4/10.
 */

public interface UserApi {

    /**
     * 账号查询
     *
     * @param username
     * @return
     */
    @GET("check")
    Observable<Results> checkUser(@Query("username") String username);

    /**
     * 用户注册
     *
     * @param username
     * @param password
     * @return
     */
    @GET("register")
    Observable<Results> register(@Query("username") String username, @Query("password") String password);


    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @GET("login")
    Observable<Results> login(@Query("username") String username, @Query("password") String password);
}
