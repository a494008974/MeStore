package com.mylove.mmodule;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;


/**
 * Created by Administrator on 2018/7/10.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        ARouter.init(this);
    }
}
