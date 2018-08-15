package com.mylove.launcher.model;

import com.mylove.launcher.bean.Bizhi;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/8/14.
 */

public class LauncherApi {
    public static LauncherApi sInstance;
    public LauncherApiSerivce launcherApiSerivce;

    private LauncherApi(LauncherApiSerivce launcherApiSerivce) {
        this.launcherApiSerivce = launcherApiSerivce;
    }

    public static LauncherApi getInstance(LauncherApiSerivce launcherApiSerivce) {
        if (sInstance == null)
            sInstance = new LauncherApi(launcherApiSerivce);
        return sInstance;
    }


    public Observable<List<Bizhi>> getBizhi() {
        return launcherApiSerivce.getBizhi();
    }
}
