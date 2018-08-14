package com.mylove.launcher.module;

import com.mylove.launcher.model.LauncherApi;
import com.mylove.launcher.model.LauncherApiSerivce;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/8/14.
 */
@Module
public class LauncherModule {

    @Provides
    LauncherApi provideLauncherApi(OkHttpClient.Builder builder){

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build());

        return LauncherApi.getInstance(retrofitBuilder
                .baseUrl(LauncherApiSerivce.launcherUrl)
                .build().create(LauncherApiSerivce.class));
    }
}
