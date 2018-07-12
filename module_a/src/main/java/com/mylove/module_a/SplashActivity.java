package com.mylove.module_a;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(RouterURL.ModuleAHome).navigation();
            }
        });

        Button btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(RouterURL.ModuleBMain).navigation();
            }
        });

        System.out.println("uuuuuuuuuuuuuuuuuuuuuuuuuu::"+stringFromJNI());
    }

    public static native String stringFromJNI();
}
