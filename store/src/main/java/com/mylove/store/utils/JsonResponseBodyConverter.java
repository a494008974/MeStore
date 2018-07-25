package com.mylove.store.utils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Administrator on 2018/7/24.
 */

public class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson mGson;//gson对象
    private final TypeAdapter<T> adapter;

    /**
     * 构造器
     */
    public JsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.mGson = gson;
        this.adapter = adapter;
    }

    /**
     * 转换
     *
     * @param responseBody
     * @return
     * @throws IOException
     */
    @Override
    public T convert(ResponseBody responseBody) throws IOException {
        String response = responseBody.string();
        response = AesHelper.AES_Decrypt("1234567890123456",response);
        try{
            return adapter.fromJson(response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            responseBody.close();
        }
        return  null;
    }

}
