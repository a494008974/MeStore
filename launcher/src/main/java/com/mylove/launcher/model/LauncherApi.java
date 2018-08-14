package com.mylove.launcher.model;

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


}
