package com.mylove.store.model;

import com.mylove.store.bean.AppData;
import com.mylove.store.bean.BaseArray;
import com.mylove.store.bean.BaseObject;
import com.mylove.store.bean.DetailData;
import com.mylove.store.bean.MenuData;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhou on 2018/7/14.
 */

public interface StoreApiService {

    String sStoreApi = "http://sapi.bsw-inc.com/";

//    http://sapi.bsw-inc.com/menu/getMenu?lang=en
    @GET("menu/getMenu")
    Observable<BaseArray<MenuData>> getMenu(@Query("lang") String lang);

//    http://sapi.bsw-inc.com/application/getApplication?lang=en&menu_id=7
    @GET("application/getApplication")
    Observable<BaseArray<AppData>> getApp(@Query("lang") String lang, @Query("menu_id") String id);

//    http://sapi.bsw-inc.com/api/application/detail?lang=en&appid=2
    @GET("application/detail")
    Observable<BaseObject<DetailData>> getDetail(@Query("lang") String lang, @Query("appid") String id);
}
