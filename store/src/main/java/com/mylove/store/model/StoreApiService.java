package com.mylove.store.model;

import com.mylove.store.bean.AppData;
import com.mylove.store.bean.BaseArray;
import com.mylove.store.bean.BaseObject;
import com.mylove.store.bean.DetailData;
import com.mylove.store.bean.MenuData;
import com.mylove.store.bean.PageData;
import com.mylove.store.bean.SplashData;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhou on 2018/7/14.
 */

public interface StoreApiService {

    int sSize = 30;
    String sStoreApi = "http://sapi.bsw-inc.com/";

//    http://sapi.bsw-inc.com/menu/getMenu?lang=en
    @GET("menu/getMenu")
    Observable<BaseArray<MenuData>> getMenu(@Query("lang") String lang);

//    http://sapi.bsw-inc.com/application/getApplication?lang=en&menu_id=7&sort=1001&page=2&pageSize=3
    @GET("application/getApplication")
    Observable<BaseObject<PageData>> getApp(@Query("lang") String lang,
                                            @Query("menu_id") String id,
                                            @Query("sort") String sort,
                                            @Query("page") String page,
                                            @Query("pageSize") String pageSize);

//    http://sapi.bsw-inc.com/application/detail?lang=en&appid=2
    @GET("application/detail")
    Observable<BaseObject<DetailData>> getDetail(@Query("lang") String lang, @Query("appid") String id);

//    http://sapi.bsw-inc.com/application/searchApp?lang=en
    @GET("application/searchApp")
    Observable<BaseObject<PageData>> getSearchApp(@Query("lang") String lang,
                                                  @Query("page") String page,
                                                  @Query("pageSize") String pageSize,
                                                  @Query("name") String name);


    String sAnimation = "http://sapi.bsw-inc.com/";

    @GET("animation/getAnimation")
    Observable<BaseObject<SplashData>> getSplash(@Query("lang") String lang);
}
