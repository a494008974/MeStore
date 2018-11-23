package com.mylove.store.module;

import android.content.Context;

import com.mylove.module_base.base.BaseApplication;
import com.mylove.store.R;
import com.mylove.store.model.StoreApi;
import com.mylove.store.model.StoreApiService;
import com.mylove.store.utils.JsonConverterFactory;

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
    StoreApi provideStoreApi(OkHttpClient.Builder builder, Context mContext) {

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(JsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build());

        String storeUrl = mContext.getString(R.string.module_store_base_url);

        return StoreApi.getInstance(retrofitBuilder
                .baseUrl(storeUrl)
                .build().create(StoreApiService.class));
    }

}
