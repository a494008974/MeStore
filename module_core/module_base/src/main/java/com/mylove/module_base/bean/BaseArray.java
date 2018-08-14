package com.mylove.module_base.bean;

import java.util.List;

/**
 * Created by zhou on 2018/7/15.
 */

public class BaseArray<T> extends BaseBean{

    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
