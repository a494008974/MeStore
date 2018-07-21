package com.mylove.store.bean;

/**
 * Created by Administrator on 2018/7/19.
 */

public class BaseObject<T> extends BaseBean {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
