package com.mylove.store.presenter;

import com.mylove.module_base.base.BasePresenter;
import com.mylove.module_base.net.BaseObserver;
import com.mylove.module_base.net.RxSchedulers;
import com.mylove.store.bean.AppData;
import com.mylove.store.bean.BaseArray;
import com.mylove.store.bean.MenuData;
import com.mylove.store.contract.MainContract;
import com.mylove.store.model.StoreApi;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/7/13.
 */

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter{

    private StoreApi storeApi;

    @Inject
    MainPresenter(StoreApi storeApi){
        this.storeApi = storeApi;
    }

    public void getStoreTypes(String lang){
        storeApi.getMenu(lang)
                .compose(RxSchedulers.<BaseArray<MenuData>>applySchedulers())
                .subscribe(new BaseObserver<BaseArray<MenuData>>() {
                    @Override
                    public void onSuccess(BaseArray<MenuData> menuDataBaseResponse) {
                        if(menuDataBaseResponse != null && mView != null){
                            mView.showStoreTypes(menuDataBaseResponse.getData());
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        System.out.println("Menu onFail !"+e.getMessage());
                    }
                });

    }

    public void getStoreApps(String lang,String id){
        storeApi.getApp(lang,id)
                .compose(RxSchedulers.<BaseArray<AppData>>applySchedulers())
                .subscribe(new BaseObserver<BaseArray<AppData>>() {
                    @Override
                    public void onSuccess(BaseArray<AppData> appDataBaseResponse) {
                        if(appDataBaseResponse != null && mView != null){
                            mView.showStoreApps(appDataBaseResponse.getData());
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        System.out.println("App onFail !"+e.getMessage());
                    }
                });
    }

}
