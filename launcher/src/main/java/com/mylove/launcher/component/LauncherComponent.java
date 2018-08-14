package com.mylove.launcher.component;

import com.mylove.launcher.MainActivity;
import com.mylove.launcher.model.LauncherApi;
import com.mylove.launcher.module.LauncherModule;
import com.mylove.module_base.module.ApplicationModule;

import dagger.Component;

/**
 * Created by Administrator on 2018/8/14.
 */

@Component(dependencies = {ApplicationModule.class, LauncherModule.class})
public interface LauncherComponent {

    void inject(MainActivity mainActivity);

    LauncherApi getLauncherApi();
}
