package com.mylove.store;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.mylove.module_base.base.BaseActivity;
import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.component.ApplicationComponent;
import com.mylove.module_base.module.ApplicationModule;
import com.mylove.module_common.RouterURL;
import com.mylove.store.bean.BannerData;
import com.mylove.store.bean.BaseResponse;
import com.mylove.store.component.DaggerStoreComponent;
import com.mylove.store.contract.MainContract;
import com.mylove.store.module.StoreModule;
import com.mylove.store.presenter.MainPresenter;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Route(path = RouterURL.StoreMain)
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.tv_result)
    TextView tvResult;

    @Override
    public int getContentLayout() {
        return R.layout.module_store_activity_main;
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
        mPresenter.getBanner();
    }


    @Override
    public void showResult(BaseResponse<BannerData> bannerDataBaseResponse) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<bannerDataBaseResponse.getData().size(); i++){
            BannerData bannerData = bannerDataBaseResponse.getData().get(i);
            sb.append("------------------------------\n")
                    .append("id = "+bannerData.getId()+"\n")
                    .append("imagePath = "+bannerData.getImagePath()+"\n")
                    .append("isVisible = "+bannerData.getIsVisible()+"\n")
                    .append("order = "+bannerData.getOrder()+"\n")
                    .append("title = "+bannerData.getTitle()+"\n")
                    .append("type = "+bannerData.getType()+"\n")
                    .append("url = "+bannerData.getUrl()+"\n");
        }
        tvResult.setText(sb.toString());
    }
}
