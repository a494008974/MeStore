package com.mylove.store.model;

import com.mylove.store.bean.BannerData;
import com.mylove.store.bean.BaseResponse;

import io.reactivex.Observable;

/**
 * Created by zhou on 2018/7/14.
 *
 * 可看作MVP中model层
 */

public class StoreApi {



    public static StoreApi sInstance;
    private StoreApiService storeApiService;

    public static StoreApi getInstance(StoreApiService storeApiService) {
        if (sInstance == null)
            sInstance = new StoreApi(storeApiService);
        return sInstance;
    }

    public StoreApi(StoreApiService storeApiService) {
        this.storeApiService = storeApiService;
    }


    public Observable<BaseResponse<BannerData>> getBanner(){
        return storeApiService.getBanner();
    }


}
