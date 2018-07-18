package com.mylove.store;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.mylove.module_base.base.BaseActivity;
import com.mylove.module_base.component.ApplicationComponent;
import com.mylove.module_common.RouterURL;

import butterknife.BindView;


@Route(path = RouterURL.StoreSplash)
public class SplashActivity extends BaseActivity{

    @BindView(R2.id.splash_img)
    ImageView splashImg;

    @Override
    public int getContentLayout() {
        return R.layout.module_store_activity_splash;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ARouter.getInstance().build(RouterURL.StoreMain).navigation();
                finish();
            }
        },1500);
    }

    @Override
    public void initData() {

    }
}
