package com.mylove.module_a;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.mylove.module_common.RouterURL;

@Route(path = RouterURL.ModuleASplash)
public class SplashActivity extends AppCompatActivity {

    static{
        System.loadLibrary("zhou-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    public void doClick(View v){
        if(v.getId() == R.id.btn1 ){
            ARouter.getInstance().build(RouterURL.ModuleAHome).navigation();
        }else if(v.getId() == R.id.btn2 ){
            ARouter.getInstance().build(RouterURL.ModuleBMain).navigation();
        }else if(v.getId() == R.id.btn3 ){
            ARouter.getInstance().build(RouterURL.ModuleAHome).navigation();
        }
    }

    public static native String stringFromJNI();
}
