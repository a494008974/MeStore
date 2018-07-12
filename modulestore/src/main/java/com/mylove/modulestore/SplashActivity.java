package com.mylove.modulestore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.mylove.module_common.RouterURL;


@Route(path = RouterURL.StoreSplash)
public class SplashActivity extends AppCompatActivity{

    Button moduleB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        moduleB = findViewById(R.id.tv_moduleb);
        moduleB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("moduleB 跳转到ModuleB!!!!!!!!!!!!!!!!!!!!!!!!!");
                ARouter.getInstance().build(RouterURL.StoreMain).navigation();
            }
        });
    }

}
