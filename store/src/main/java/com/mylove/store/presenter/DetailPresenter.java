package com.mylove.store.presenter;

import com.mylove.module_base.base.BasePresenter;
import com.mylove.module_base.net.BaseObserver;
import com.mylove.module_base.net.RxSchedulers;
import com.mylove.module_base.net.down.DownloadRequest;
import com.mylove.module_base.net.down.DownloadUtil;
import com.mylove.store.bean.BaseObject;
import com.mylove.store.bean.DetailData;
import com.mylove.store.contract.DetailContract;
import com.mylove.store.model.StoreApi;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by zhou on 2018/7/14.
 */

public class DetailPresenter extends BasePresenter<DetailContract.View> implements DetailContract.Presenter {

    private StoreApi storeApi;

    @Inject
    public DetailPresenter(StoreApi storeApi) {
        this.storeApi = storeApi;
    }

    public void downStart(DownloadRequest request){
        DownloadUtil.get().enqueue(request);
    }
    public void downResume(DownloadRequest request) {
        DownloadUtil.get().resume(request.getId());
    }
    public void downPause(DownloadRequest request) {
        DownloadUtil.get().pause(request.getId());
    }

    public void getStoreDetail(String lang,String id){
        storeApi.getDetail(lang,id)
                .compose(RxSchedulers.<BaseObject<DetailData>>applySchedulers())
                .subscribe(new BaseObserver<BaseObject<DetailData>>() {
                    @Override
                    public void onSuccess(BaseObject<DetailData> detailData) {
                        if(detailData != null && detailData.getData() != null && mView != null){
                            mView.showDetail(detailData.getData());
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        System.out.println("detailData onFail !"+e.getMessage());
                    }
                });
    }


}
