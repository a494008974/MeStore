package com.mylove.store.model;

import com.mylove.store.bean.AppData;
import com.mylove.store.bean.BaseArray;
import com.mylove.store.bean.BaseObject;
import com.mylove.store.bean.DetailData;
import com.mylove.store.bean.MenuData;
import com.mylove.store.bean.SplashData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhou on 2018/7/14.
 */

public interface SplashApiService {

    String sAnimation = "http://sapi.bsw-inc.com/";

    @GET("animation/getAnimation")
    Observable<BaseObject<SplashData>> getSplash(@Query("lang") String lang);
}
