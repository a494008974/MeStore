package com.mylove.store.presenter;

import com.mylove.module_base.base.BasePresenter;
import com.mylove.module_base.net.BaseObserver;
import com.mylove.module_base.net.RxSchedulers;
import com.mylove.store.bean.BaseObject;
import com.mylove.store.bean.PageData;
import com.mylove.store.contract.SearchContract;
import com.mylove.store.model.StoreApi;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/7/24.
 */

public class SearchPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter {
    private StoreApi storeApi;
    @Inject
    public SearchPresenter(StoreApi storeApi){
        this.storeApi = storeApi;
    }

    public void getSearchApps(String lang,String page,String pageSize,String name){
        storeApi.getSearchApp(lang,page,pageSize,name)
                .compose(RxSchedulers.<BaseObject<PageData>>applySchedulers())
                .subscribe(new BaseObserver<BaseObject<PageData>>() {
                    @Override
                    public void onSuccess(BaseObject<PageData> pageData) {
                        if(pageData != null && mView != null){
                            mView.showSearchApps(pageData.getData());
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        System.out.println("Search onFail !"+e.getMessage());
                    }
                });
    }
}
