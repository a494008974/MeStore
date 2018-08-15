package com.mylove.launcher.bean;

/**
 * Created by Administrator on 2018/8/15.
 */

public class HttpEvent {
    public static final int UPLOAD_EVENT = 0x901;
    public static final int DOWNLOAD_EVENT = 0x902;
    public static final int CHANGE_STYLE = 0x903;
    public static final int SUBMIT_EVENT = 0x904;

    private int event;
    private Object obj;

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
