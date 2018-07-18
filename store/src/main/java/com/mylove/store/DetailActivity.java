package com.mylove.store;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.mylove.module_base.base.BaseActivity;
import com.mylove.module_base.component.ApplicationComponent;
import com.mylove.module_base.module.ApplicationModule;
import com.mylove.module_common.RouterURL;
import com.mylove.store.component.DaggerStoreComponent;
import com.mylove.store.contract.DetailContract;
import com.mylove.store.module.StoreModule;
import com.mylove.store.presenter.DetailPresenter;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = RouterURL.StoreDetail)
public class DetailActivity extends BaseActivity<DetailPresenter> implements DetailContract.View{

    @BindView(R2.id.tv_result_detail)
    TextView tvResultDetail;

    @BindView(R2.id.app_title)
    TextView tvAppTitle;


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
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String appid = bundle.getString("appid");
            tvAppTitle.setText(appid);
        }
    }

    @Override
    public void initData() {

    }

    @OnClick(R2.id.store_down)
    public void downClick(View view){
        mPresenter.downApp("http://down.znds.com/getdownurl/?s=L2Rvd24vMjAxODA3MTYveTIyMnNrc18yLjIuNV9kYW5nYmVpLmFwaw==");
    }

    @Override
    public void showProgress(int progress) {
        System.out.println("down ........................ = " + progress);
    }

    @Override
    public void downPause() {

    }

    @Override
    public void downStart() {

    }
}
