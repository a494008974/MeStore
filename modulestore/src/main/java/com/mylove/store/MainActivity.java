package com.mylove.store;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.mylove.module_base.base.BaseActivity;
import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.component.ApplicationComponent;
import com.mylove.module_common.RouterURL;
import com.mylove.store.contract.MainContract;
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
    public void bindView(View view, Bundle savedInstanceState) {
        System.out.println(".............bindView.................");
        ApplicationComponent applicationComponent = BaseApplication.getAppContext().getApplicationComponent();
        OkHttpClient.Builder builder = applicationComponent.getOkHttp();
        OkHttpClient okHttpClient = builder.build();

        String url = "http://www.wanandroid.com/banner/json";
        Request request = new Request.Builder().url(url).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(".................onFailure................"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println(result);
            }
        });

    }

    @Override
    public void initData() {

    }
}
