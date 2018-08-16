package com.mylove.launcher.presenter;

import android.content.Context;

import com.mylove.launcher.bean.Bizhi;
import com.mylove.launcher.contract.MainContract;
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

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {
    private LauncherApi launcherApi;
    private FHttpManager fHttpManager;
    @Inject
    MainPresenter(LauncherApi launcherApi) {
        this.launcherApi = launcherApi;
    }

    public void startServer(Context context){
        System.out.println("startServer ........ ");
        fHttpManager = FHttpManager.init(context, ActionController.class);
        fHttpManager.setPort(9999);
        fHttpManager.setAllowCross(true);
        fHttpManager.startServer();
    }

    public void stopServer(){
        if(fHttpManager != null){
            fHttpManager.stopServer();
        }
    }
}
