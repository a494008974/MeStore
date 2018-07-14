package com.mylove.store.presenter;

import com.mylove.module_base.base.BasePresenter;
import com.mylove.module_base.net.BaseObserver;
import com.mylove.module_base.net.RxSchedulers;
import com.mylove.store.bean.BannerData;
import com.mylove.store.bean.BaseResponse;
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

    public void getBanner(){
        storeApi.getBanner()
                .compose(RxSchedulers.<BaseResponse<BannerData>>applySchedulers())
                .subscribe(new BaseObserver<BaseResponse<BannerData>>() {
                    @Override
                    public void onSuccess(BaseResponse<BannerData> bannerDataBaseResponse) {
                        mView.showResult(bannerDataBaseResponse);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        System.out.println("Banner onFail !");
                    }
                });
    }
}
