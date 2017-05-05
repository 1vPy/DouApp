package com.roy.douapp.http.api.user;

import com.roy.douapp.AppConfig;
import com.roy.douapp.DouKit;
import com.roy.douapp.http.api.baidu.BaiduApiService;
import com.roy.douapp.http.api.douban.DoubanApiService;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 1vPy(Roy) on 2017/4/10.
 */

public class UserApiService {
    private static final String API_BASE_URL = "http://192.168.2.250:8080/DouProjectServer/";
    private static UserApiService instance;
    private UserApi mUserApi;

    private UserApiService() {
        File httpCacheDirectory = new File(DouKit.getContext().getCacheDir(), "okhttpcache");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB

        Cache mCache = new Cache(httpCacheDirectory, cacheSize);
        OkHttpClient mClient = new OkHttpClient.Builder()
                .connectTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                .cache(mCache)
                .build();

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.USER_PAI_SERVER)
                .client(mClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mUserApi = mRetrofit.create(UserApi.class);
    }

    public static UserApiService getInstance(){
        if(instance == null){
            synchronized (BaiduApiService.class){
                if(instance == null){
                    instance = new UserApiService();
                }
            }
        }
        return instance;
    }

}
