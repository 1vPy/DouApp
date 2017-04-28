package com.roy.douapp.http.api;

import com.roy.douapp.http.api.baidu.BaiduApiService;
import com.roy.douapp.http.api.douban.DoubanApiService;
import com.roy.douapp.http.api.user.UserApiService;

/**
 * Created by Administrator on 2017/3/8.
 */

public class ApiFactory {


    public static synchronized DoubanApiService getDoubanApiService() {
        return DoubanApiService.getInstance();
    }


    public static synchronized UserApiService getUserApiService() {
        return UserApiService.getInstance();
    }

    public static BaiduApiService getBaiduApiService(){
        return BaiduApiService.getInstance();
    }

}
