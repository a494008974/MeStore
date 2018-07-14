package com.mylove.store.component;

import com.mylove.module_base.module.ApplicationModule;
import com.mylove.store.DetailActivity;
import com.mylove.store.MainActivity;
import com.mylove.store.module.StoreModule;
import com.mylove.store.model.StoreApi;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2018/7/13.
 */
@Singleton
@Component(modules = {ApplicationModule.class,StoreModule.class})
public interface StoreComponent {

    void inject(MainActivity mainActivity);

    void inject(DetailActivity detailActivity);

    StoreApi getStoreApi();
}
