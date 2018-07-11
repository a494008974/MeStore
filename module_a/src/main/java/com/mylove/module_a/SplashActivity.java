package com.mylove.module_a;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;

@Route(path = "/module/SplashActivity")
public class SplashActivity extends AppCompatActivity {

    static{
        System.loadLibrary("zhou-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("uuuuuuuuuuuuuuuuuuuuuuuuuu::"+stringFromJNI());
    }

    public static native String stringFromJNI();
}
