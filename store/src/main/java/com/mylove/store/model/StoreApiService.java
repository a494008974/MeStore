package com.mylove.store.model;

import com.mylove.store.bean.BannerData;
import com.mylove.store.bean.BaseResponse;


import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by zhou on 2018/7/14.
 */

public interface StoreApiService {

    String sStoreApi = "http://www.wanandroid.com/";

    @GET("banner/json")
    Observable<BaseResponse<BannerData>> getBanner();
}
