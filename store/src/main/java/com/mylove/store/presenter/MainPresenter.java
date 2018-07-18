package com.mylove.store.presenter;

import android.os.Handler;

import com.mylove.module_base.base.BasePresenter;
import com.mylove.module_base.net.BaseObserver;
import com.mylove.module_base.net.RxSchedulers;
import com.mylove.store.ItemDatas;
import com.mylove.store.bean.BannerData;
import com.mylove.store.bean.BaseResponse;
import com.mylove.store.contract.MainContract;
import com.mylove.store.model.StoreApi;

import java.util.Arrays;
import java.util.List;

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

    public void getBanner(){
        storeApi.getBanner()
                .compose(RxSchedulers.<BaseResponse<BannerData>>applySchedulers())
                .subscribe(new BaseObserver<BaseResponse<BannerData>>() {
                    @Override
                    public void onSuccess(BaseResponse<BannerData> bannerDataBaseResponse) {
//                        mView.showResult(bannerDataBaseResponse);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        System.out.println("Banner onFail !");
                    }
                });
    }

    public void getStoreTypes(){
        mView.showStoreTypes(Arrays.asList(ItemDatas.types));
    }

    public void getStoreApps(final String type, final int count){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> apps = ItemDatas.getDatas(type,count);
                mView.showStoreApps(apps);
            }
        },500);

    }
}
