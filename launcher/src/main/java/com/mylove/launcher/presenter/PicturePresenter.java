package com.mylove.launcher.presenter;

import android.content.Context;

import com.mylove.launcher.bean.Bizhi;
import com.mylove.launcher.contract.MainContract;
import com.mylove.launcher.contract.PictureContract;
import com.mylove.launcher.controller.ActionController;
import com.mylove.launcher.model.LauncherApi;
import com.mylove.module_base.base.BasePresenter;
import com.mylove.module_base.helper.ImageLoaderHelper;
import com.mylove.module_base.net.BaseObserver;
import com.mylove.module_base.net.RxSchedulers;

import java.util.List;

import javax.inject.Inject;

import cn.hotapk.fhttpserver.FHttpManager;
import io.reactivex.functions.Function;


/**
 * Created by Administrator on 2018/8/14.
 */

public class PicturePresenter extends BasePresenter<PictureContract.View> implements PictureContract.Presenter {
    private LauncherApi launcherApi;

    @Inject
    PicturePresenter(LauncherApi launcherApi) {
        this.launcherApi = launcherApi;
    }

    public void showBizhi(final Context context) {
        launcherApi.getBizhi()
                .compose(RxSchedulers.<List<Bizhi>>applySchedulers())
                .map(new Function<List<Bizhi>, List<Bizhi>>() {
                    @Override
                    public List<Bizhi> apply(List<Bizhi> bizhis) throws Exception {
                        for (Bizhi bizhi : bizhis){
                            ImageLoaderHelper.getInstance().download(context,bizhi.getImage());
                        }
                        return bizhis;
                    }
                })
                .subscribe(new BaseObserver<List<Bizhi>>() {
                    @Override
                    public void onSuccess(List<Bizhi> bizhis) {
                        if(bizhis != null && bizhis.size() > 0 && mView != null){
                            mView.showBizhi(bizhis);
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                    }
                });

    }

}
