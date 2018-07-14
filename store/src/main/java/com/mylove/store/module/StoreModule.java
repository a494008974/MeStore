package com.mylove.store.module;

import com.mylove.store.model.StoreApi;
import com.mylove.store.model.StoreApiService;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/7/13.
 */
@Module
public class StoreModule {

    @Provides
    StoreApi provideStoreApi(OkHttpClient.Builder builder) {

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build());

        return StoreApi.getInstance(retrofitBuilder
                .baseUrl(StoreApiService.sStoreApi)
                .build().create(StoreApiService.class));
    }

}
