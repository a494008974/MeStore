package com.mylove.store.bean;

import java.util.List;

/**
 * Created by zhou on 2018/7/15.
 */

public class BaseResponse<T> {
    private int errorCode;
    private String errorMsg;
    private List<T> data;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
