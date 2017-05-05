package com.roy.douapp.http.api.douban;

import com.roy.douapp.AppConfig;
import com.roy.douapp.DouKit;
import com.roy.douapp.R;
import com.roy.douapp.http.api.RequestCallback;
import com.roy.douapp.http.api.baidu.BaiduApiService;
import com.roy.douapp.http.bean.movie.JsonMovieBean;
import com.roy.douapp.http.bean.movie.details.JsonDetailBean;
import com.roy.douapp.http.bean.movie.star.JsonStarBean;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/4/10.
 */

public class DoubanApiService {
    private static final String API_BASE_URL = "https://api.douban.com/";
    private static DoubanApiService instance;
    private DoubanApi mDoubanApi;


    private DoubanApiService() {
        File httpCacheDirectory = new File(DouKit.getContext().getCacheDir(), "okhttpcache");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB

        Cache mCache = new Cache(httpCacheDirectory, cacheSize);
        OkHttpClient mClient = new OkHttpClient.Builder()
                .connectTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                .cache(mCache)
                .build();

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.DOUBAN_API_SERVER)
                .client(mClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mDoubanApi = mRetrofit.create(DoubanApi.class);
    }

    public static DoubanApiService getInstance() {
        if (instance == null) {
            synchronized (BaiduApiService.class) {
                if (instance == null) {
                    instance = new DoubanApiService();
                }
            }
        }
        return instance;
    }

    public void getHotMovie(int start, int count, String city, final RequestCallback<JsonMovieBean> rc) {
        mDoubanApi.getHotMovie(start, count, city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonMovieBean>() {
                    @Override
                    public void accept(JsonMovieBean jsonMovieBean) throws Exception {
                        if (jsonMovieBean != null) {
                            rc.onSuccess(jsonMovieBean);
                        } else {
                            rc.onFailure(DouKit.getContext().getString(R.string.no_data));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });

    }


    public void getComingMovie(int start, int count, final RequestCallback<JsonMovieBean> rc) {
        mDoubanApi.getComingMovie(start, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonMovieBean>() {
                    @Override
                    public void accept(JsonMovieBean jsonMovieBean) throws Exception {
                        if (jsonMovieBean != null) {
                            rc.onSuccess(jsonMovieBean);
                        } else {
                            rc.onFailure(DouKit.getContext().getString(R.string.no_data));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });

    }

    public void getMovieDetail(String id, final RequestCallback<JsonDetailBean> rc) {
        mDoubanApi.getMovieDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonDetailBean>() {
                    @Override
                    public void accept(JsonDetailBean jsonDetailBean) throws Exception {
                        if (jsonDetailBean != null) {
                            rc.onSuccess(jsonDetailBean);
                        } else {
                            rc.onFailure(DouKit.getContext().getString(R.string.no_data));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });
    }


    public void getStarDetail(String id, final RequestCallback<JsonStarBean> rc) {
        mDoubanApi.getStarDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonStarBean>() {
                    @Override
                    public void accept(JsonStarBean jsonStarBean) throws Exception {
                        if (jsonStarBean != null) {
                            rc.onSuccess(jsonStarBean);
                        } else {
                            rc.onFailure(DouKit.getContext().getString(R.string.no_data));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });
    }

    public void searchMovie(String query, final RequestCallback<JsonMovieBean> rc) {
        mDoubanApi.searchMovie(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonMovieBean>() {
                    @Override
                    public void accept(JsonMovieBean jsonMovieBean) throws Exception {
                        if (jsonMovieBean != null) {
                            rc.onSuccess(jsonMovieBean);
                        } else {
                            rc.onFailure(DouKit.getContext().getString(R.string.no_data));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });
    }

}
