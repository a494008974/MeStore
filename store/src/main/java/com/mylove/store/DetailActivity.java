package com.mylove.store;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.mylove.module_base.base.BaseActivity;
import com.mylove.module_base.component.ApplicationComponent;
import com.mylove.module_base.module.ApplicationModule;
import com.mylove.module_common.RouterURL;
import com.mylove.store.component.DaggerStoreComponent;
import com.mylove.store.contract.DetailContract;
import com.mylove.store.module.StoreModule;
import com.mylove.store.presenter.DetailPresenter;

@Route(path = RouterURL.StoreDetail)
public class DetailActivity extends BaseActivity<DetailPresenter> implements DetailContract.View{

    @Override
    public int getContentLayout() {
        return R.layout.module_store_activity_detail;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {
        DaggerStoreComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .storeModule(new StoreModule())
                .build()
                .inject(this);
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initData() {

    }
}
