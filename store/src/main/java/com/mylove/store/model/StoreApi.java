package com.mylove.store.model;

import com.mylove.store.bean.AppData;
import com.mylove.store.bean.BaseArray;
import com.mylove.store.bean.BaseObject;
import com.mylove.store.bean.DetailData;
import com.mylove.store.bean.MenuData;
import com.mylove.store.bean.PageData;

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


    public Observable<BaseObject<PageData>> getApp(String lang, String id, String sort, String page, String pageSize){
        return storeApiService.getApp(lang,id,sort,page,pageSize);
    }

    public Observable<BaseArray<MenuData>> getMenu(String lang){
        return storeApiService.getMenu(lang);
    }

    public Observable<BaseObject<DetailData>> getDetail(String lang, String id){
        return storeApiService.getDetail(lang,id);
    }

    public Observable<BaseObject<PageData>> getSearchApp(String lang, String page, String pageSize, String name){
        return storeApiService.getSearchApp(lang,page,pageSize,name);
    }
}
