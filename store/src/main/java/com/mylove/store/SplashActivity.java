package com.mylove.store;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.mylove.module_base.base.BaseActivity;
import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.component.ApplicationComponent;
import com.mylove.module_base.helper.ImageLoaderHelper;
import com.mylove.module_base.helper.LocaleHelper;
import com.mylove.module_base.net.BaseObserver;
import com.mylove.module_base.net.RxSchedulers;
import com.mylove.module_base.net.down.DownloadRequest;
import com.mylove.module_base.net.down.DownloadUtil;
import com.mylove.module_base.utils.FileUtils;
import com.mylove.module_base.utils.Md5;
import com.mylove.module_base.utils.SPUtil;
import com.mylove.module_common.RouterURL;
import com.mylove.store.bean.BaseObject;
import com.mylove.store.bean.SplashData;
import com.mylove.store.model.SplashApiService;
import com.mylove.store.presenter.MainPresenter;
import com.mylove.store.utils.JsonConverterFactory;

import java.io.File;

import butterknife.BindView;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Route(path = RouterURL.StoreSplash)
public class SplashActivity extends BaseActivity{

    @BindView(R2.id.splash_img)
    ImageView splashImg;

    private OkHttpClient.Builder builder;
    @Override
    public int getContentLayout() {
        return R.layout.module_store_activity_splash;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {
        builder = appComponent.getOkHttp();
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {

        if(FileUtils.isFileExists(Constanst.getSplashPath())){
            String md5 = (String) SPUtil.get(BaseApplication.getAppContext(),"SplashUrl","");
            ImageLoaderHelper.getInstance().load(this,new File(Constanst.getSplashPath()),splashImg,md5);
        }else{
            splashImg.setImageResource(R.drawable.splash);
        }
        long delayMillis = (long) SPUtil.get(this,"delayMillis",3L);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ARouter.getInstance().build(RouterURL.StoreMain).navigation();
                finish();
            }
        },delayMillis * 1000);
    }

    private void fetchSplash() {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(JsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build());

        SplashApiService splashApiService = retrofitBuilder
                .baseUrl(SplashApiService.sAnimation)
                .build().create(SplashApiService.class);

        splashApiService.getSplash(LocaleHelper.getLanguage(this).getLanguage())
                .compose(RxSchedulers.<BaseObject<SplashData>>applySchedulers())
                .subscribe(new BaseObserver<BaseObject<SplashData>>() {
                    @Override
                    public void onSuccess(BaseObject<SplashData> splashData) {
                        if (splashData != null && splashData.getData() != null){
                            System.out.println(splashData.getData().getImg());
                            String md5Splash = Md5.MD5Encode(splashData.getData().getImg(),null);
                            String splash = (String) SPUtil.get(BaseApplication.getAppContext(),"SplashUrl","");
                            System.out.println("md5Splash = "+md5Splash+"\n splash = "+splash);
                            if(!splash.equals(md5Splash)){
                                SPUtil.put(BaseApplication.getAppContext(),"SplashUrl", md5Splash);
                                SPUtil.put(BaseApplication.getAppContext(),"delayMillis", Long.valueOf(splashData.getData().getTime()));
                                downSplash(splashData.getData().getImg());
                            }
                        }
                    }
                    @Override
                    public void onFail(Throwable e) {
                        SPUtil.put(BaseApplication.getAppContext(),"SplashUrl", "");
                    }
                });
    }

    public void downSplash(String url){
        DownloadRequest request = DownloadRequest.newBuilder()
                .downloadUrl(url)
                .downloadName(Constanst.SPLASHNAME)
                .downloadDir(BaseApplication.getAppContext().getFilesDir().getPath())
                .build();
        DownloadUtil.get().enqueue(request);
    }

    @Override
    public void initData() {
        fetchSplash();
    }


}
