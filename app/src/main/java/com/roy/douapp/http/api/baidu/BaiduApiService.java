package com.roy.douapp.http.api.baidu;

import com.roy.douapp.DouKit;
import com.roy.douapp.R;
import com.roy.douapp.http.api.RequestCallback;
import com.roy.douapp.http.bean.music.billcategory.JsonMusicBillBean;
import com.roy.douapp.http.bean.music.billlist.JsonSongListBean;
import com.roy.douapp.http.bean.music.musicinfo.JsonMusicInfoBean;
import com.roy.douapp.http.bean.music.search.JsonSearchBean;
import com.roy.douapp.utils.common.AESTools;
import com.roy.douapp.utils.common.LogUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/4/13.
 */

public class BaiduApiService {
    private static final String TAG = BaiduApiService.class.getSimpleName();
    private static final String API_BASE_URL = "http://tingapi.ting.baidu.com/v1/restserver/";
    private static BaiduApiService instance;

    private BaiduApi mBaiduApi;

    private BaiduApiService() {
        File httpCacheDirectory = new File(DouKit.getContext().getCacheDir(), "okhttpcache");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB

        Cache mCache = new Cache(httpCacheDirectory, cacheSize);
        OkHttpClient mClient = new OkHttpClient.Builder()
                .connectTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                .cache(mCache)
                .build();

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(mClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mBaiduApi = mRetrofit.create(BaiduApi.class);
    }

    public static BaiduApiService getInstance() {
        if (instance == null) {
            synchronized (BaiduApiService.class) {
                if (instance == null) {
                    instance = new BaiduApiService();
                }
            }
        }
        return instance;
    }

    public void getMusicBillCategory(final RequestCallback<JsonMusicBillBean> rc) {
        mBaiduApi.getMusicBillCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonMusicBillBean>() {
                    @Override
                    public void accept(@NonNull JsonMusicBillBean jsonMusicBillBean) throws Exception {
                        if (jsonMusicBillBean != null) {
                            rc.onSuccess(jsonMusicBillBean);
                        } else {
                            rc.onFailure(DouKit.getContext().getString(R.string.no_data));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });
    }

    public void getMusicBillList(int type, int offset, int size, final RequestCallback<JsonSongListBean> rc) {
        mBaiduApi.getMusicBillList(type, offset, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonSongListBean>() {
                    @Override
                    public void accept(@NonNull JsonSongListBean jsonSongListBean) throws Exception {
                        if (jsonSongListBean != null) {
                            rc.onSuccess(jsonSongListBean);
                        } else {
                            rc.onFailure(DouKit.getContext().getString(R.string.no_data));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });
    }

    public void getMusicSearch(String query, int pageNo, int pageSize, final RequestCallback<JsonSearchBean> rc) {
        mBaiduApi.getMusicSearch(query, pageNo, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonSearchBean>() {
                    @Override
                    public void accept(@NonNull JsonSearchBean jsonSearchBean) throws Exception {
                        if (jsonSearchBean != null) {
                            rc.onSuccess(jsonSearchBean);
                        } else {
                            rc.onFailure(DouKit.getContext().getString(R.string.no_data));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });
    }

    public void getMusicInfo(String songid, final RequestCallback<JsonMusicInfoBean> rc) {
        mBaiduApi.getMusicInfo(songInfo(songid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonMusicInfoBean>() {
                    @Override
                    public void accept(@NonNull JsonMusicInfoBean jsonMusicInfoBean) throws Exception {
                        if (jsonMusicInfoBean != null) {
                            rc.onSuccess(jsonMusicInfoBean);
                        } else {
                            rc.onFailure(DouKit.getContext().getString(R.string.no_data));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });
    }

    public static String songInfo(String songid) {
        StringBuffer sb = new StringBuffer("http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json");
        String str = "songid=" + songid + "&ts=" + System.currentTimeMillis();
        String e = AESTools.encrpty(str);
        sb.append("&method=").append("baidu.ting.song.getInfos")
                .append("&").append(str)
                .append("&e=").append(e);
        LogUtils.log(TAG, sb.toString(), LogUtils.DEBUG);
        return sb.toString();
    }

}
