package com.mylove.launcher;

import android.os.Bundle;
import android.view.View;

import com.mylove.launcher.contract.MainContract;
import com.mylove.launcher.presenter.MainPresenter;
import com.mylove.module_base.base.BaseActivity;
import com.mylove.module_base.component.ApplicationComponent;
import com.mylove.module_base.helper.ImageLoaderHelper;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {
//    http://zmapi.dangbei.net/thirdpart/bizhi/

    String[] urls = {"http://imgzm.qun7.com/uploads/20180808/5b6a7e0c27a0a.jpg",
            "http://imgzm.qun7.com/uploads/20180808/5b6a7e0c27a0a.jpg" ,
            "http://imgzm.qun7.com/uploads/20180803/5b63fe9305a81.jpg" ,
            "http://imgzm.qun7.com/uploads/20180801/5b613e26ee229.jpg" ,
            "http://imgzm.qun7.com/uploads/20180720/5b5177d60a9d3.jpg" ,
            "http://imgzm.qun7.com/uploads/20180630/5b373de0d36ca.jpg" ,
            "http://imgzm.qun7.com/uploads/20180720/5b517fd80007a.jpg" ,
            "http://imgzm.qun7.com/uploads/20180720/5b5182fbdf608.jpg" ,
            "http://imgzm.qun7.com/uploads/20180803/5b63febdd6b24.jpg" ,
            "http://imgzm.qun7.com/uploads/20180725/5b5815f39aea7.jpg" ,
            "http://imgzm.qun7.com/uploads/20180630/5b373ec817be9.jpg" ,
            "http://imgzm.qun7.com/uploads/20180702/5b39fd216f58f.jpg" ,
            "http://imgzm.qun7.com/uploads/20180712/5b470374642cd.jpg" ,
            "http://imgzm.qun7.com/uploads/20180710/5b441bfccac67.jpg"
    };

    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                for (String s : urls){
                    System.out.println("s ===== "+s);
                    ImageLoaderHelper.getInstance().download(MainActivity.this,s);

                }
            }
        }.start();
    }
}
