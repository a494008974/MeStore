package com.mylove.store.model;

import com.mylove.store.bean.BaseObject;
import com.mylove.store.bean.SplashData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by zhou on 2018/7/14.
 */

public interface UpdateApiService {

//    String sUpdate = "http://api.bsw-inc.com/";

    @GET
    Observable<String> getUpdate(@Url String url);
}
