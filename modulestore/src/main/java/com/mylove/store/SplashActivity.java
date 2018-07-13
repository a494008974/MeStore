package com.mylove.store;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.mylove.module_common.RouterURL;


@Route(path = RouterURL.StoreSplash)
public class SplashActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_store_activity_splash);

    }

    public void doClick(View v){
        if(v.getId() == R.id.tv_modulea){
            ARouter.getInstance().build(RouterURL.StoreMain).navigation();
        }else if(v.getId() == R.id.tv_moduleb){
            ARouter.getInstance().build(RouterURL.StoreMain).navigation();
        }else if(v.getId() == R.id.tv_modulec){
            ARouter.getInstance().build(RouterURL.StoreMain).navigation();
        }
    }

}
