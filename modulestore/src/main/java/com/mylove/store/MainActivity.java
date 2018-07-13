package com.mylove.store;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.mylove.module_base.base.BaseActivity;
import com.mylove.module_common.RouterURL;
import com.mylove.store.contract.MainContract;
import com.mylove.store.presenter.MainPresenter;

@Route(path = RouterURL.StoreMain)
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {


    @Override
    public int getContentLayout() {
        return R.layout.module_store_activity_main;
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initData() {

    }
}
